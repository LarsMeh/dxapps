package de.hhu.bsinfo.dxapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.hhu.bsinfo.dxmem.data.ChunkByteArray;
import de.hhu.bsinfo.dxram.app.AbstractApplication;
import de.hhu.bsinfo.dxram.boot.BootService;
import de.hhu.bsinfo.dxram.chunk.ChunkLocalService;
import de.hhu.bsinfo.dxram.chunk.ChunkService;
import de.hhu.bsinfo.dxram.engine.DXRAMVersion;
import de.hhu.bsinfo.dxram.generated.BuildConfig;
import de.hhu.bsinfo.dxram.migration.MigrationService;
import de.hhu.bsinfo.dxram.migration.MigrationStatus;
import de.hhu.bsinfo.dxram.migration.MigrationTicket;
import de.hhu.bsinfo.dxram.util.NodeCapabilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MigrationApp extends AbstractApplication {

    private final Logger log = LogManager.getFormatterLogger(MigrationApp.class);

    private static final String DEFAULT_TOTAL_SIZE = String.valueOf(1024 * 1024 * 32);

    private static final String DEFAULT_CHUNK_SIZE = String.valueOf(64);

    private static final String DEFAULT_ITERATION_COUNT = String.valueOf(1);

    private Random m_random = new Random();

    private static final String ARG_TOTAL = "total";

    private static final String ARG_CHUNK = "chunk";

    private static final String ARG_ITERATIONS = "iterations";

    @Override
    public DXRAMVersion getBuiltAgainstVersion() {
        return BuildConfig.DXRAM_VERSION;
    }

    @Override
    public String getApplicationName() {
        return "MigrationApp";
    }

    @Override
    public void main(final String[] p_args) {
        Options options = new Options();
        getOptions().forEach(options::addOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, p_args);
        } catch (ParseException e) {
            log.error("Application options could not be parsed", e);
            return;
        }

        ChunkService chunkService = getService(ChunkService.class);
        ChunkLocalService chunkLocalService = getService(ChunkLocalService.class);
        MigrationService migrationService = getService(MigrationService.class);
        BootService bootService = getService(BootService.class);

        String totalSizeValue = cmd.getOptionValue(ARG_TOTAL, DEFAULT_TOTAL_SIZE);
        String chunkSizeValue = cmd.getOptionValue(ARG_CHUNK, DEFAULT_CHUNK_SIZE);
        String iterationsValue = cmd.getOptionValue(ARG_ITERATIONS, DEFAULT_ITERATION_COUNT);

        int iterations = Integer.parseInt(iterationsValue);

        int totalSize = Integer.parseInt(totalSizeValue);
        int chunkSize = Integer.parseInt(chunkSizeValue);
        int numChunks = totalSize / chunkSize;

        long[] creationTimes = new long[iterations];
        long[] migrationTimes = new long[iterations];

        Optional<Short> targetOptional = bootService.getSupportingNodes(NodeCapabilities.STORAGE).stream()
                .filter(id -> id != bootService.getNodeID())
                .findFirst();

        log.info("Waiting for other peer");

        while (!targetOptional.isPresent()) {
            targetOptional = bootService.getSupportingNodes(NodeCapabilities.STORAGE).stream()
                    .filter(id -> id != bootService.getNodeID())
                    .findFirst();
        }

        short source = bootService.getNodeID();
        short target = targetOptional.get();

        log.info("Starting migration to %X", target);

        for (int i = 0; i < iterations; i++) {
            long firstChunk = 1L;
            long lastChunk = numChunks;

            long then = System.currentTimeMillis();
            for (int j = 0; j < numChunks; j++) {
                byte[] bytes = new byte[chunkSize];
                m_random.nextBytes(bytes);

                ChunkByteArray byteArray = new ChunkByteArray(bytes);
                chunkLocalService.createLocal().create(byteArray);
                // TODO(krakowski)
                //  Replace with putLocal once it is implemented
                chunkService.put().put(byteArray);

                if (j == 0) {
                    firstChunk = byteArray.getID();
                }

                if (j == numChunks - 1) {
                    lastChunk = byteArray.getID();
                }
            }

            if (firstChunk == 0 || lastChunk == 0) {
                throw new IllegalStateException("Creation of one or more chunks failed");
            }

            creationTimes[i] = System.currentTimeMillis() - then;

            log.info("Migrating chunk range [%X , %X]", firstChunk, lastChunk + 1);

            MigrationStatus status;
            MigrationTicket ticket;

            then = System.currentTimeMillis();
            ticket = migrationService.migrateRange(firstChunk, lastChunk + 1, target);
            status = migrationService.await(ticket);
            migrationTimes[i] = System.currentTimeMillis() - then;

            if (status == MigrationStatus.ERROR) {
                log.warn("Iteration %d was not successful", i);
            }
        }

        int workerCount = migrationService.getWorkerCount();

        String user = System.getProperty("user.name");

        File logDir = new File(
                String.format("/home/%s/dxlogs/migration/migration_%d_%d_%d-%d.csv", user, workerCount, iterations,
                        numChunks, System.currentTimeMillis()));
        logDir.getParentFile().mkdirs();

        try {
            FileWriter writer = new FileWriter(logDir);

            writer.append("iteration,creation_time,migration_time,chunk_count\n");
            for (int i = 0; i < iterations; i++) {
                writer.append(String.format("%d,%d,%d,%d\n", i, creationTimes[i], migrationTimes[i], numChunks));
            }

            writer.flush();
            writer.close();

            log.info("Results written to %s", logDir.getAbsolutePath());
        } catch (IOException e) {
            log.error("Couldn't write results to file");
        }
    }

    @Override
    public void signalShutdown() {

    }

    private static List<Option> getOptions() {
        return Arrays.asList(
                Option.builder(ARG_TOTAL).argName(ARG_TOTAL)
                        .hasArg()
                        .desc("The total amount of data to send in bytes")
                        .required(false)
                        .type(Integer.class)
                        .build(),
                Option.builder(ARG_CHUNK).argName(ARG_CHUNK)
                        .hasArg()
                        .desc("The size of a single chunk in bytes")
                        .required(false)
                        .type(Integer.class)
                        .build(),
                Option.builder(ARG_ITERATIONS).argName(ARG_ITERATIONS)
                        .hasArg()
                        .desc("The iteration count")
                        .required(false)
                        .type(Integer.class)
                        .build()
        );
    }
}
