package de.hhu.bsinfo.dxram.datastructure;

import de.hhu.bsinfo.dxapp.HashMapBenchmark;
import de.hhu.bsinfo.dxmem.DXMem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class HashtableBenchmark {

    private final Logger log = LogManager.getFormatterLogger(HashMapBenchmark.class);

    private final File m_file;
    private final DXMem m_memory;
    private final long heapSize;

    public HashtableBenchmark(File p_file) {
        m_file = p_file;
        heapSize = (long) Math.pow(2, 31);
        m_memory = new DXMem((short) 0xAE3C, heapSize);
    }

    public void start() {

        final int iteration = 100;
        final int start = 7;
        final int end = 27;
        short depth = start;
        int position;

        final long[] arr = {0xB1BD};

        long cid, adr, time;

        long[][] times = new long[end - start + 1][iteration];

        log.warn("Start Benchmark");

        for (int j = 0; j < iteration; j++) {

            log.warn("Iteration: %d", j);

            depth = start;

            for (int i = 0; i < times.length; i++) {

                cid = m_memory.create().create(Hashtable.getInitialMemorySize(depth));
                adr = m_memory.pinning().pin(cid).getAddress();
                position = (int) Math.pow(2, depth) / 2;

                Hashtable.initialize(m_memory.rawWrite(), adr, m_memory.size().size(cid), i, arr);

                time = System.nanoTime();
                Hashtable.splitForEntry(m_memory, cid, adr, 0xB1BD, position, 0xA65C);
                time = System.nanoTime() - time;

                times[i][j] = time;

                m_memory.pinning().unpinCID(cid);
                m_memory.remove().remove(cid);

                depth++;
            }

        }

        log.warn("Finish Benchmark by writing to file " + m_file.getAbsolutePath());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(m_file))) {

            depth = start;

            for (int i = 0; i <= end; i++) {
                bw.write(String.format("%d, %f\n", depth, Arrays.stream(times[i]).average().getAsDouble()));
                depth++;
            }

        } catch (IOException p_e) {
            p_e.printStackTrace();
        }
    }

}
