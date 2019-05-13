package de.hhu.bsinfo.dxapp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import de.hhu.bsinfo.dxram.datastructure.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PerformanceBenchmark extends Benchmark {

    private final Logger log = LogManager.getFormatterLogger(PerformanceBenchmark.class);

    public PerformanceBenchmark(File p_file, TimeFormat p_timeFormat, int p_entries, final int p_numberOfThreads) {
        super(p_file, p_timeFormat, p_entries, p_hashMap, p_numberOfThreads);
    }

    void start() {
        int countOperations = 0;
        int timer = 0;
        AtomicInteger atomicInteger = new AtomicInteger(0);
        final java.util.HashMap<Integer, Integer> map = new java.util.HashMap<>();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(m_file))) {

            bw.write("Performance Benchmark csv\n");

            startThreads();

            while (!m_runner[0].isDone()) {

                Thread.sleep(999);

                // get number of operations
                int tmp = atomicInteger.get();
                int operations = tmp - countOperations;
                countOperations = tmp;
                timer++;

                map.put(timer, operations);
            }

            log.info("Benchmark is done\nResults will be written to File: " + m_file.getAbsolutePath());

            // write into file
            for (int key : map.keySet()) {
                bw.write(String.format("%d,%d\n", key, map.get(key)));
            }

        } catch (IOException | InterruptedException p_e) {
            p_e.printStackTrace();
        }
    }
}
