package de.hhu.bsinfo.dxapp;


import de.hhu.bsinfo.dxram.datastructure.HashMap;

import java.util.concurrent.atomic.AtomicInteger;

public class PrimRunner extends Runner {

    private private HashMap<Integer, Integer> m_map;

    public PrimRunner(final String p_name, HashMap<Integer, Integer> p_map, final int p_maxIterations, AtomicInteger p_atomicInteger) {
        super(p_name, p_atomicInteger, p_maxIterations);
    }

    @Override
    public void run() {
        System.out.println("Start PrimRunner");
        int var;
        while ((var = atomic.incrementAndGet()) < limit) {
            m_map.put(var, var + 1);
        }
        System.out.println("End PrimRunner");
    }

}