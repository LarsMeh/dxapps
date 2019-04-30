package de.hhu.bsinfo.dxapp;


import de.hhu.bsinfo.dxram.app.Application;
import de.hhu.bsinfo.dxram.datastructure.DataStructureService;
import de.hhu.bsinfo.dxram.datastructure.util.HashFunctions;
import de.hhu.bsinfo.dxram.engine.DXRAMVersion;
import de.hhu.bsinfo.dxram.generated.BuildConfig;
import de.hhu.bsinfo.dxram.datastructure.HashMap;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class HashMapBenchmark extends Application {

    private final Logger log = LogManager.getFormatterLogger(HashMapBenchmark.class);

    private static final String ARG_TYPE;
    private static final String ARG_DATATYPE;
    private static final String ARG_ENTRIES;

    static {
        ARG_TYPE = "type";
        ARG_DATATYPE = "datatype";
        ARG_ENTRIES = "entries";
    }


    @Override
    public DXRAMVersion getBuiltAgainstVersion() {
        return BuildConfig.DXRAM_VERSION;
    }

    @Override
    public String getApplicationName() {
        return "HashMapBenchmark";
    }

    @Override
    public void main(String[] p_args) {
        Options options = new Options();
        getOptions().forEach(options::addOption);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, p_args);
        } catch (ParseException e) {
            log.error("Application options could not be parsed", e);
            return;
        }

        DataStructureService dataStructureService = getService(DataStructureService.class);

        String typeValue = cmd.getOptionValue(ARG_TYPE);
        String datatypeValue = cmd.getOptionValue(ARG_DATATYPE);
        String entriesValue = cmd.getOptionValue(ARG_ENTRIES);

        int numberOfEntries = Integer.parseInt(entriesValue);
        TimeFormat timeFormat = TimeFormat.SECOND;

        String user = System.getProperty("user.name");
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("/home/%s/dxlogs/hashmap/", user));

        if (typeValue.equals("perf")) {

            if (datatypeValue.equals("non-prim")) {
                builder.append("byteArrayBenchmark.csv");

                final int from = 2;
                final int to = 4;
                HashMap<byte[], byte[]> map = dataStructureService.createHashMap("a", numberOfEntries, -1, to, to, HashFunctions.MURMUR3_32);

                ByteArrayBenchmark.startSinglecore(new File(builder.toString()), timeFormat, map, numberOfEntries, from, to);

            } else if (datatypeValue.equals("prim")) {

                builder.append("integerBenchmark.csv");
                HashMap<Integer, Integer> map = dataStructureService.createHashMap("b", numberOfEntries, -1, Integer.BYTES, Integer.BYTES, HashFunctions.MURMUR3_32);

                IntegerBenchmark.startSinglecore(new File(builder.toString()), timeFormat, map, numberOfEntries);

            } else {
                System.err.println("Invalid value for parameter " + ARG_DATATYPE);
                return;
            }

        } else {

            if (datatypeValue.equals("non-prim")) {
                builder.append("byteArrayMulticoreBenchmark.csv");

                final int from = 2;
                final int to = 4;
                HashMap<byte[], byte[]> map = dataStructureService.createHashMap("c", numberOfEntries, -1, to, to, HashFunctions.MURMUR3_32);

                ByteArrayBenchmark.startMulticore(new File(builder.toString()), timeFormat, map, numberOfEntries, from, to, 4);

            } else if (datatypeValue.equals("prim")) {

                builder.append("integerMulticoreBenchmark.csv");
                HashMap<Integer, Integer> map = dataStructureService.createHashMap("d", numberOfEntries, -1, Integer.BYTES, Integer.BYTES, HashFunctions.MURMUR3_32);

            } else {
                System.err.println("Invalid value for parameter " + ARG_DATATYPE);
                return;
            }

        }
    }

    @Override
    public void signalShutdown() {
        // Interrupt any flow of your application and make sure it shuts down.
        // Do not block here or wait for something to shut down. Shutting down of your application
        // must be execute asynchronously
    }

    private static List<Option> getOptions() {
        return Arrays.asList(
                Option.builder(ARG_TYPE).argName(ARG_TYPE)
                        .hasArg()
                        .desc("The type of the benchmark test. Memory or performance [mem,perf]")
                        .required(true)
                        .type(String.class)
                        .build(),
                Option.builder(ARG_DATATYPE).argName(ARG_DATATYPE)
                        .hasArg()
                        .desc("The datatype which should used for the HashMap. primitive or non primitive [prim,non-prim]")
                        .required(true)
                        .type(String.class)
                        .build(),
                Option.builder(ARG_ENTRIES).argName(ARG_ENTRIES)
                        .hasArg()
                        .desc("The number of entries which should be generated")
                        .required(true)
                        .type(Integer.class)
                        .build()
        );
    }
}
