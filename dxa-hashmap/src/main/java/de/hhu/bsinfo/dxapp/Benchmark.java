package de.hhu.bsinfo.dxapp;

import de.hhu.bsinfo.dxram.datastructure.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Benchmark {

    private final Logger log = LogManager.getFormatterLogger(Benchmark.class);

    public enum Mode {
        PRIM, NON_PRIM
    }

    final File m_file;
    final TimeFormat m_timeFormat;
    private final int m_entries;
    private HashMap<byte[], byte[]> m_hashMap;
    private HashMap<Integer, Integer> m_hashMapPrim;
    private final int m_numberOfThreads;
    private int m_from;
    private int m_to;
    private Mode m_mode;
    Runner[] m_runner;


    Benchmark(final File p_file, final TimeFormat p_timeFormat, final int p_entries, final int p_numberOfThreads) {
        m_file = p_file;
        m_timeFormat = p_timeFormat;
        m_entries = p_entries;
        m_numberOfThreads = p_numberOfThreads;
    }

    final void setPrim(final HashMap<Integer, Integer> p_hashMapPrim) {
        log.debug("Mode PRIM");
        m_mode = Mode.PRIM;
        m_hashMapPrim = p_hashMapPrim;
        m_runner = new PrimRunner[m_numberOfThreads];
    }

    final void setNonPrim(final HashMap<byte[], byte[]> p_hashMap, final int p_from, final int p_to) {
        log.debug("Mode NON PRIM");
        m_mode = Mode.NON_PRIM;
        m_hashMap = p_hashMap;
        m_runner = new NonPrimRunner[m_numberOfThreads];
        m_from = p_from;
        m_to = p_to;
    }

    final void initThreads(AtomicInteger p_atomicInteger) {
        log.debug("Initialize %d Threads", m_numberOfThreads);

        ByteArrayPool pool = null;

        if (m_mode == Mode.NON_PRIM)
            pool = new ByteArrayPool(m_from, m_to);

        for (int i = 0; i < m_runner.length; i++) {
            switch (m_mode) {
                case PRIM:
                    m_runner[i] = new PrimRunner(("NonPrimRunner-" + i), m_hashMapPrim, m_entries, p_atomicInteger);
                    break;
                case NON_PRIM:
                    m_runner[i] = new NonPrimRunner(("NonPrimRunner-" + i), m_hashMap, m_entries, pool, p_atomicInteger);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    final HashMap getMap() {
        switch (m_mode) {
            case PRIM:
                return m_hashMapPrim;
            case NON_PRIM:
                return m_hashMap;
            default:
                throw new RuntimeException();
        }
    }

    abstract void start();

    final void startThreads() {
        log.debug("Start Threads");
        for (Runner thread : m_runner) {
            thread.start();
        }
    }

    final void joinThreads() throws InterruptedException {
        log.debug("Join Threads");
        for (Runner thread : m_runner) {
            thread.join();
        }
    }

    void finishedCorrectly(final AtomicInteger p_atomicInteger) {
        log.warn(getMap().size() + " :: " + p_atomicInteger.get() + " :: " + m_entries);
        if (getMap().size() == p_atomicInteger.get() && p_atomicInteger.get() == m_entries)
            log.debug("Operation ended with %d calls", getMap().size());
        else
            log.fatal("Benchmark failed");
    }

}
