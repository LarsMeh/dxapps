package de.hhu.bsinfo.dxapp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.hhu.bsinfo.dxram.datastructure.HashMap;

public class ByteArrayBenchmark extends Benchmark {

    static void startSinglecore(final File p_file, final TimeFormat p_timeFormat, HashMap p_hashMap, final int p_entries, final int p_from, final int p_to) {
        throughput(p_file, p_timeFormat, new NonPrimRunner("ByteArrayRunner", p_hashMap, p_entries, new ByteArrayPool(p_from, p_to)));
    }

    static void startMulticore(final File p_output, final TimeFormat p_timeFormat, HashMap p_hashMap, int p_entries, final int p_from, final int p_to, final int p_countThreads) {
        int counter = 0;
        long timestamp = p_timeFormat.getTime();
        long time_elapsed;
        int countSec = 0;

        final ByteArrayPool pool = new ByteArrayPool(p_from, p_to);
        NonPrimRunner[] threads = new NonPrimRunner[p_countThreads];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new NonPrimRunner(("NonPrimRunner-" + i), p_hashMap, p_entries, pool);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(p_output))) {

            startThreads(threads);

            while (!threads[0].isDone()) {

                time_elapsed = System.nanoTime();
                do {
                    Thread.sleep(1);
                } while (System.nanoTime() - time_elapsed < timestamp);

                // get number of operations
                int tmp = threads[0].m_counter;
                int operations = tmp - counter;
                counter = tmp;
                countSec++;

                // write into file
                bw.write(String.format("%d,%d\n", countSec, operations));
            }

        } catch (IOException | InterruptedException p_e) {
            p_e.printStackTrace();
        }
    }

    private static void startThreads(NonPrimRunner[] p_threads) {
        for (NonPrimRunner thread : p_threads) {
            thread.start();
        }
    }

}
