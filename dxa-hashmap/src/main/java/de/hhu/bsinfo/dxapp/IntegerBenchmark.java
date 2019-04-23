package de.hhu.bsinfo.dxapp;

import java.io.File;
import java.util.HashMap;

public class IntegerBenchmark {


    static void startSinglecore(final String p_fileName, final TimeFormat p_timeFormat, HashMap p_hashMap, final int p_entries) {
        Benchmark.throughput(new File(p_fileName + ".csv"), p_timeFormat, new PrimRunner("IntegerRunner", p_hashMap, p_entries));
    }


}