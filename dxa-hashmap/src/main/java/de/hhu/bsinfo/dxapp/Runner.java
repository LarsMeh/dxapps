package de.hhu.bsinfo.dxapp;


import java.util.concurrent.atomic.AtomicInteger;

public abstract class Runner extends Thread {

    final int m_maxIterations;
    final AtomicInteger m_atomicInteger;

    Runner(final String p_name, final AtomicInteger p_atomicInteger, final int p_maxIterations) {
        super(p_name);
        m_maxIterations = p_maxIterations;
        m_atomicInteger = p_atomicInteger;
    }

    @Override
    public abstract void run();

    final boolean isDone() {
        return m_maxIterations <= m_atomicInteger.get();
    }

}
