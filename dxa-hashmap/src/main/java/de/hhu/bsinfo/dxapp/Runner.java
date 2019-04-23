package de.hhu.bsinfo.dxapp;


import de.hhu.bsinfo.dxram.datastructure.HashMap;

public abstract class Runner extends Thread {

    HashMap m_map; // TODO: Import my HashMap
    int m_counter;
    int m_maxIterations;

    Runner(final String p_name, HashMap p_map, final int p_maxIterations) {
        super(p_name);
        m_maxIterations = p_maxIterations;
        m_map = p_map;
    }

    @Override
    public abstract void run();

    boolean isDone() {
        return m_counter == m_maxIterations;
    }

}
