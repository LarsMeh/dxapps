package de.hhu.bsinfo.dxram.datastructure;

import de.hhu.bsinfo.dxmem.DXMem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HashtableBenchmark {

    private final File m_file;
    private final DXMem m_memory;
    private final long heapSize;

    public HashtableBenchmark(File p_file) {
        m_file = p_file;
        heapSize = (long) Math.pow(2, 27);
        m_memory = new DXMem((short) 0xAE3C, heapSize);
    }

    public void start() {

        final int start = 2;
        final int end = 24;

        final long[] arr = {0xB1BD};

        long cid, adr, time;

        ArrayListy<Long> times = new ArrayList<>();

        for (int i = start; i < end; i++) {

            cid = m_memory.create().create(Hashtable.getInitialMemorySize(i));
            adr = m_memory.pinning().pin(cid);

            Hashtable.initialize(m_memory.rawWrite(), adr, m_memory.size().size(cid), i, arr);

            time = System.nanoTime();

            Hashtable.splitForEntry(m_memory.rawRead(), m_memory.rawWrite(), m_memory.size(), cid, adr, 0xB1BD);

            time = System.nanoTime() - time;

            times.add(time);

            m_memory.pinning().unpinCID(cid);
            m_memory.remove().remove(cid);

        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(m_file))) {

            int depth = start;

            for (long time : times) {
                bw.write(String.format("%d, %d", depth, time));
                depth++;
            }

        } catch (IOException p_e) {
            p_e.printStackTrace();
        }
    }

}
