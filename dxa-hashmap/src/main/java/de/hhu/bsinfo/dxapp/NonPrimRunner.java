package de.hhu.bsinfo.dxapp;

import de.hhu.bsinfo.dxram.datastructure.HashMap;

import java.util.concurrent.atomic.AtomicInteger;

public class NonPrimRunner extends Runner {
    private final ByteArrayPool m_pool;
    private final byte[][] m_valueSet = {{1, 2, 3}, {1}, {1, 2}};
    private int m_index;
    private HashMap<byte[], byte[]> m_map;

    NonPrimRunner(final String p_name, HashMap<byte[], byte[]> p_map, final int p_maxIterations, final ByteArrayPool p_pool, AtomicInteger p_atomicInteger) {
        super(p_name, p_atomicInteger, p_maxIterations);
        m_pool = p_pool;
        m_index = -1;
        m_map = p_map;
    }

    private byte[] nextValue() {
        if (m_index == m_valueSet.length - 1)
            m_index = 0;
        m_index++;
        return m_valueSet[m_index];
    }

    @Override
    public void run() {
        while (m_atomicInteger.get() < m_maxIterations) {
            put();
        }
    }

    /**
     * 
     */
    private synchronized void put() {
        m_map.put(m_pool.next(), nextValue());
        m_atomicInteger.incrementAndGet();
    }


}
