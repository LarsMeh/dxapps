package de.hhu.bsinfo.dxapp;

import de.hhu.bsinfo.dxram.datastructure.HashMap;

import java.util.concurrent.atomic.AtomicInteger;

public class NonPrimRunner extends Runner {
    private final ByteArrayPool m_pool;
    private final byte[][] m_valueSet = {{1, 2, 3}, {1}, {1, 2}};
    private int m_index;
    private AtomicInteger m_atomicInteger;

    NonPrimRunner(final String p_name, HashMap<byte[], byte[]> p_map, final int p_maxIterations, final ByteArrayPool p_pool, AtomicInteger p_atomicInteger) {
        super(p_name, p_map, p_maxIterations);
        m_pool = p_pool;
        m_index = -1;
        m_atomicInteger = p_atomicInteger;
    }

    private byte[] nextValue() {
        if (m_index == m_valueSet.length - 1)
            m_index = 0;
        m_index++;
        return m_valueSet[m_index];
    }

    @Override
    public void run() {
        System.out.println("Start PrimRunner");

        for (m_counter = 0; m_counter < m_maxIterations; m_counter++)
            m_map.put(m_pool.next(), nextValue());

        System.out.println("End PrimRunner");
    }


}
