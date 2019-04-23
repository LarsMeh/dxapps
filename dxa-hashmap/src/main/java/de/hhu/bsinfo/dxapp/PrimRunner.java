package de.hhu.bsinfo.dxapp;


import java.util.HashMap;

public class PrimRunner extends Runner {

    PrimRunner(final String p_name, HashMap<Integer, Integer> p_map, final int p_maxIterations) {
        super(p_name, p_map, p_maxIterations);
    }

    @Override
    public void run() {
        System.out.println("Start PrimRunner");

        for (m_counter = 0; m_counter < m_maxIterations; m_counter++)
            m_map.put(m_counter, m_counter);

        System.out.println("End PrimRunner");
    }

    boolean isDone() {
        return m_counter == m_maxIterations;
    }

}