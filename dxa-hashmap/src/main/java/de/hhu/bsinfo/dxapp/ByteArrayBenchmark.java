package de.hhu.bsinfo.dxapp;


import java.io.File;
import de.hhu.bsinfo.dxram.datastructure.HashMap;

public class ByteArrayBenchmark {

    static void startSinglecore(final String p_fileName, final TimeFormat p_timeFormat, HashMap p_hashMap, final int p_entries, final int p_from, final int p_to) {
        Benchmark.throughput(new File(p_fileName + ".csv"), p_timeFormat, new NonPrimRunner("ByteArrayRunner", p_hashMap, p_entries, p_from, p_to));
    }


}
