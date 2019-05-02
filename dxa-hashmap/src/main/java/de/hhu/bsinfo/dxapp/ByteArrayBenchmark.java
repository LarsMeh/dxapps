package de.hhu.bsinfo.dxapp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import de.hhu.bsinfo.dxram.datastructure.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ByteArrayBenchmark extends Benchmark {

    private final Logger log = LogManager.getFormatterLogger(ByteArrayBenchmark.class);

    public ByteArrayBenchmark(File p_file, TimeFormat p_timeFormat, int p_entries, HashMap<byte[], byte[]> p_hashMap, int p_from, int p_to) {
        super(p_file, p_timeFormat, p_entries, p_hashMap, p_from, p_to);
    }

    private void startSinglecore() {
        startMulticore(1);
    }

    private void startMulticore(final int p_countThreads) {
        log.debug("Initialize Benchmark for " + p_countThreads + " Threads");
        int countOperations = 0;
        long time_elapsed;
        int timer = 0;
        AtomicInteger atomicInteger = new AtomicInteger(0);
        final java.util.HashMap<Integer, Integer> map = new java.util.HashMap<>();

        final ByteArrayPool pool = new ByteArrayPool(m_from, m_to);
        NonPrimRunner[] threads = new NonPrimRunner[p_countThreads];

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

            log.debug("Benchmark is done\nResults will be written to File: " + m_file.getAbsolutePath());

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

    @Override
    public void startNonPrimitivePerformance(int p_cores) {
        if (p_cores > Runtime.getRuntime().availableProcessors()) {
            p_cores = Runtime.getRuntime().availableProcessors();
            log.warn("Threads was set down to: " + p_cores + ", because no more supported");
        } else if (p_cores == 1)
            startSinglecore();
        else
            startMulticore(p_cores);
    }

    @Override
    public void startPrimitivePerformance() {
        System.err.println("In a non-primitive Benchmark a primitive method is called");
    }
}
