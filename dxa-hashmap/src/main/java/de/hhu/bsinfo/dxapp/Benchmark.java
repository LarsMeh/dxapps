package de.hhu.bsinfo.dxapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Benchmark {

    protected static void throughput(final File p_output, final TimeFormat p_timeFormat, final Runner p_workingThread) {
        int counter = 0;
        long timestamp = p_timeFormat.getTime();
        long time_elapsed;
        int countSec = 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(p_output))) {

            p_workingThread.start();

            while (!p_workingThread.isDone()) {

                time_elapsed = System.nanoTime();
                do {
                    Thread.sleep(1);
                } while (System.nanoTime() - time_elapsed < timestamp);

                // get number of operations
                int tmp = p_workingThread.m_counter;
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

    protected static void throughtputMulticore(final File p_output, final TimeFormat p_timeFormat, final Runner p_workingThread, final int p_countThreads){

    }

}
