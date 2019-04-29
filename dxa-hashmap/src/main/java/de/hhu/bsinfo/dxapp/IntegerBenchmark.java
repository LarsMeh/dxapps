package de.hhu.bsinfo.dxapp;

import java.io.File;

import de.hhu.bsinfo.dxram.datastructure.HashMap;

public class IntegerBenchmark extends Benchmark {


    static void startSinglecore(final File p_file, final TimeFormat p_timeFormat, HashMap p_hashMap, final int p_entries) {
        throughput(p_file, p_timeFormat, new PrimRunner("IntegerRunner", p_hashMap, p_entries));
    }

    static void startMulticore(final File p_file, final TimeFormat p_timeFormat, HashMap p_hashMap, final int p_entries) {
        int countThreads = Runtime.getRuntime().availableProcessors() * 2 - 3;

    }

}