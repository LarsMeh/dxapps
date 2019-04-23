package de.hhu.bsinfo.dxapp;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NonPrimRunner extends Runner {

    private final ByteArrayPool m_pool;
    private final byte[][] m_valueSet = {{1}, {1}, {1}};
    private int m_index;

    NonPrimRunner(final String p_name, HashMap<byte[], byte[]> p_map, final int p_maxIterations, final int p_from, final int p_to) {
        super(p_name, p_map, p_maxIterations);
        m_pool = new ByteArrayPool(p_from, p_to);
    }

    private byte[] nextValue() {
        return m_valueSet[m_index];
    }

    @Override
    public void run() {
        System.out.println("Start PrimRunner");

        for (m_counter = 0; m_counter < m_maxIterations; m_counter++)
            m_map.put(m_pool.next(), nextValue());

        System.out.println("End PrimRunner");
    }

    boolean isDone() {
        return m_counter == m_maxIterations;
    }

    private class ByteArrayPool {

        private ArrayList<ByteArray> m_pool;
        private int m_next;

        public ByteArrayPool(final int lowestSize, final int highestSize) {
            assert lowestSize > 0 && highestSize > 0;

            final int poolSize = highestSize - lowestSize + 1;
            assert poolSize > 0;

            m_pool = new ArrayList<>(poolSize);
            for (int i = 0; i < poolSize; i++) {
                m_pool.add(i, new ByteArray(lowestSize + i));
            }
            m_next = -1;
        }

        private byte[] next() {
            increment();
            return m_pool.get(m_next).next();
        }

        private void increment() {
            if (m_next == m_pool.size() - 1)
                m_next = 0;
            else
                m_next++;
        }

        private class ByteArray {

            private byte[] m_arr;
            private final int length;

            private ByteArray(final int p_size) {
                m_arr = new byte[p_size];
                m_arr[0] = -1;
                length = m_arr.length;
            }

            private byte[] next() {
                for (int i = 0; i < length; i++) {
                    if (m_arr[i] != Byte.MAX_VALUE) {
                        m_arr[i]++;
                        break;
                    } else
                        m_arr[i] = 0;
                }
                return m_arr;
            }

            public String toString() {
                return Arrays.toString(m_arr);
            }
        }

    }

}
