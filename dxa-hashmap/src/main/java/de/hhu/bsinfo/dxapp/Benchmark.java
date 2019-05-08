package de.hhu.bsinfo.dxapp;

import de.hhu.bsinfo.dxram.datastructure.HashMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Benchmark {

    protected final File m_file;
    protected final TimeFormat m_timeFormat;
    protected final int m_entries;
    protected final HashMap<byte[], byte[]> m_hashMap;
    protected final int m_numberOfThreads;
    protected int m_from;
    protected int m_to;

    public Benchmark(final File p_file, final TimeFormat p_timeFormat, final int p_entries, final HashMap<byte[], byte[]> p_hashMap, final int p_numberOfThreads) {
        m_file = p_file;
        m_timeFormat = p_timeFormat;
        m_entries = p_entries;
        m_hashMap = p_hashMap;
        m_numberOfThreads = p_numberOfThreads;
    }

    protected void throughput(final Runner p_workingThread) {
        int counter = 0;
        long timestamp = m_timeFormat.getTime();
        long time_elapsed;
        int countSec = 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(m_file))) {

            p_workingThread.start();

            while (!p_workingThread.isDone()) {

                time_elapsed = System.nanoTime();
                do {
                    Thread.sleep(1);
                } while (System.nanoTime() - time_elapsed < timestamp);

                // get number of operations
                int tmp = p_workingThread.m_atomicInteger.get();
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

    abstract void start();

}
