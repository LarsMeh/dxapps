package de.hhu.bsinfo.dxapp;

import java.util.ArrayList;
import java.util.Arrays;

class ByteArrayPool {

    private ArrayList<ByteArray> m_pool;
    private int m_next;

    ByteArrayPool(final int lowestSize, final int highestSize) {
        assert lowestSize > 0 && highestSize > 0;

        final int poolSize = highestSize - lowestSize + 1;
        assert poolSize > 0;

        m_pool = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            m_pool.add(i, new ByteArray(lowestSize + i));
        }
        m_next = -1;
    }

    synchronized byte[] next() {
        increment();
        return m_pool.get(m_next).next();
    }

    private synchronized void increment() {
        if (m_next == m_pool.size() - 1)
            m_next = 0;
        else
            m_next++;
    }

    @Override
    public String toString() {
        return m_pool.get(m_next).toString();
    }

    private class ByteArray {

        private byte[] m_arr;
        private final int length;

        private ByteArray(final int p_size) {
            m_arr = new byte[p_size];
            m_arr[0] = Byte.MIN_VALUE;
            length = m_arr.length;
        }

        private byte[] next() {
            for (int i = 0; i < length; i++) {
                if (m_arr[i] != Byte.MAX_VALUE) {
                    m_arr[i]++;
                    break;
                } else
                    m_arr[i] = Byte.MIN_VALUE;
            }
            return m_arr;
        }

        @Override
        public String toString() {
            return Arrays.toString(m_arr);
        }
    }

}
