package de.hhu.bsinfo.dxapp;

import java.io.File;

import de.hhu.bsinfo.dxram.datastructure.HashMap;

public class IntegerBenchmark extends Benchmark {

    public IntegerBenchmark(File p_file, TimeFormat p_timeFormat, int p_entries, HashMap<byte[], byte[]> p_hashMap, int p_from, int p_to) {
        super(p_file, p_timeFormat, p_entries, p_hashMap, p_from, p_to);
    }

    @Override
    public void startNonPrimitivePerformance(int p_cores) {

    }

    @Override
    public void startPrimitivePerformance() {

    }
}