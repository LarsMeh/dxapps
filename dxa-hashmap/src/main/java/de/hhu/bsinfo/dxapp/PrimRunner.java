package de.hhu.bsinfo.dxapp;


import de.hhu.bsinfo.dxram.datastructure.HashMap;

import java.util.concurrent.atomic.AtomicInteger;

public class PrimRunner extends Runner {

    public PrimRunner(String p_name, AtomicInteger p_atomicInteger, int p_maxIterations) {
        super(p_name, p_atomicInteger, p_maxIterations);
    }

    @Override
    public void run() {
        System.out.println("Start PrimRunner");

        System.out.println("End PrimRunner");
    }

}