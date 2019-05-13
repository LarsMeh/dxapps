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

    private Runner[] m_runner;

    public PerformanceBenchmark(File p_file, TimeFormat p_timeFormat, int p_entries, HashMap<byte[], byte[]> p_hashMap, final int p_numberOfThreads) {
        super(p_file, p_timeFormat, p_entries, p_hashMap, p_numberOfThreads);
    }

    void setPrim(){
        m_runner = new PrimRunner[m_numberOfThreads];
    }

    void setNonPrim(final int p_from, final int p_to){
        m_runner = new NonPrimRunner[m_numberOfThreads];
        m_from = p_from;
        m_to = p_to;
    }

    void start(){
        int countOperations = 0;
        long time_elapsed;
        int timer = 0;
        AtomicInteger atomicInteger = new AtomicInteger(0);
        final java.util.HashMap<Integer, Integer> map = new java.util.HashMap<>();

        final ByteArrayPool pool = new ByteArrayPool(m_from, m_to);
        NonPrimRunner[] threads = new NonPrimRunner[1];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new NonPrimRunner(("NonPrimRunner-" + i), m_hashMap, m_entries, pool, atomicInteger);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(m_file))) {

            bw.write("Benchmark csv\n");

            startThreads(threads);

            while (!threads[0].isDone()) {

                time_elapsed = System.nanoTime();
                do {
                    Thread.sleep(1);
                } while (System.nanoTime() - time_elapsed < m_timeFormat.getTime());

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

    private void startThreads(NonPrimRunner[] p_threads) {
        log.debug("Start Threads");
        for (NonPrimRunner thread : p_threads) {
            thread.start();
        }
    }
}
