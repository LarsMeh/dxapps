package de.hhu.bsinfo.dxapp;

import de.hhu.bsinfo.dxram.datastructure.HashMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Benchmark {

    public enum Mode {
        PRIM, NON_PRIM
    }

    protected final File m_file;
    protected final TimeFormat m_timeFormat;
    protected final int m_entries;
    protected final HashMap<byte[], byte[]> m_hashMap;
    protected final HashMap<Integer, Integer> m_hashMapPrim;
    protected final int m_numberOfThreads;
    protected int m_from;
    protected int m_to;
    protected Mode m_mode
    protected Runner[] m_runner;


    public Benchmark(final File p_file, final TimeFormat p_timeFormat, final int p_entries, final int p_numberOfThreads) {
        m_file = p_file;
        m_timeFormat = p_timeFormat;
        m_entries = p_entries;
        m_numberOfThreads = p_numberOfThreads;
    }

    public final void setPrim(final HashMap<Integer, Integer> p_hashMapPrim) {
        m_mode = Mode.PRIM;
        m_hashMapPrim = p_hashMapPrim;
        m_runner = new PrimRunner[m_numberOfThreads];
        initThreads();
    }

    public final void setNonPrim(final HashMap<byte[], byte[]> p_hashMap, final int p_from, final int p_to) {
        m_mode = Mode.NON_PRIM;
        m_hashMap = p_hashMap;
        m_runner = new NonPrimRunner[m_numberOfThreads];
        m_from = p_from;
        m_to = p_to;
        initThreads();
    }

    private final void initThreads(){
        final ByteArrayPool pool = new ByteArrayPool(m_from, m_to);

        for (int i = 0; i < threads.length; i++) {
            switch (m_mode) {
                case Mode.PRIM:
                    threads[i] = new PrimRunner(("NonPrimRunner-" + i), m_hashMapPrim, m_entries, atomicInteger);
                    break;
                case Mode.NON_PRIM:
                    threads[i] = new NonPrimRunner(("NonPrimRunner-" + i), m_hashMap, m_entries, pool, atomicInteger);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    abstract void start();

    protected final void startThreads() {
        log.debug("Start Threads");
        for (Runner thread : m_runner) {
            thread.start();
        }
    }

    protected final void joinThreads() {
        log.debug("Join Threads");
        for (Runner thread : m_runner) {
            thread.join();
        }
    }

}
