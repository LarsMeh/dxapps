package de.hhu.bsinfo.dxapp;


import java.io.File;

import de.hhu.bsinfo.dxram.datastructure.HashMap;

public class ByteArrayBenchmark extends Benchmark {

    static void startSinglecore(final File p_file, final TimeFormat p_timeFormat, HashMap p_hashMap, final int p_entries, final int p_from, final int p_to) {
        throughput(p_file, p_timeFormat, new NonPrimRunner("ByteArrayRunner", p_hashMap, p_entries, p_from, p_to));
    }


}
