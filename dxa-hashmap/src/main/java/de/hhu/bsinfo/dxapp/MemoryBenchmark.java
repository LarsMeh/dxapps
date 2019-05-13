package de.hhu.bsinfo.dxapp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import de.hhu.bsinfo.dxram.datastructure.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MemoryBenchmark extends Benchmark {

    private final Logger log = LogManager.getFormatterLogger(MemoryBenchmark.class);

    public MemoryBenchmark(File p_file, TimeFormat p_timeFormat, int p_entries, final int p_numberOfThreads, final DataStructureService p_service) {
        super(p_file, p_timeFormat, p_entries, p_hashMap, p_numberOfThreads);
    }

    void start(){
        int countOperations = 0;
        AtomicInteger atomicInteger = new AtomicInteger(0);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(m_file))) {

            bw.write("Memory Benchmark csv\n");

            startThreads();

            joinThreads();

            log.debug("Starting with extraction")

            p_service.extractMemoryInformation(m_map, m_file);

        } catch (IOException | InterruptedException p_e) {
            p_e.printStackTrace();
        }
    }
}
