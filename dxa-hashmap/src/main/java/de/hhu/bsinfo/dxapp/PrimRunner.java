package de.hhu.bsinfo.dxapp;


import de.hhu.bsinfo.dxram.datastructure.HashMap;

import java.util.concurrent.atomic.AtomicInteger;

public class PrimRunner extends Runner {

    private HashMap<Integer, Integer> m_map;

    PrimRunner(final String p_name, HashMap<Integer, Integer> p_map, final int p_maxIterations, AtomicInteger p_atomicInteger) {
        super(p_name, p_atomicInteger, p_maxIterations);
        m_map = p_map;
    }

    @Override
    public void run() {
        int var;
        while ((var = m_atomicInteger.incrementAndGet()) < m_maxIterations) {
            m_map.put(var, var + 1);
        }
    }

}