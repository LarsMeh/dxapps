package de.hhu.bsinfo.dxapp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import de.hhu.bsinfo.dxram.datastructure.DataStructureService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class MemoryBenchmark extends Benchmark {

    private final Logger log = LogManager.getFormatterLogger(MemoryBenchmark.class);

    private final DataStructureService m_service;

    MemoryBenchmark(File p_file, TimeFormat p_timeFormat, int p_entries, final int p_numberOfThreads, final DataStructureService p_service) {
        super(p_file, p_timeFormat, p_entries, p_numberOfThreads);
        m_service = p_service;
    }

    void start() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);

        initThreads(atomicInteger);

        startThreads();

        try {
            joinThreads();
        } catch (InterruptedException p_e) {
            p_e.printStackTrace();
        }

        finishedCorrectly(atomicInteger);

        log.debug("Starting with extraction");

        try {
            m_service.extractMemoryInformation(getMap(), m_file);
        } catch (IOException p_e) {
            p_e.printStackTrace();
        }

        log.info("Benchmark is finished");

    }
}
