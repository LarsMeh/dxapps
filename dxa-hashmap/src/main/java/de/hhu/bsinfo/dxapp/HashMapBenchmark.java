package de.hhu.bsinfo.dxapp;


import de.hhu.bsinfo.dxram.app.Application;
import de.hhu.bsinfo.dxram.engine.DXRAMVersion;
import de.hhu.bsinfo.dxram.generated.BuildConfig;

import java.util.HashMap;

public class HashMapBenchmark extends Application {
    @Override
    public DXRAMVersion getBuiltAgainstVersion() {
        return BuildConfig.DXRAM_VERSION;
    }

    @Override
    public String getApplicationName() {
        return "HelloApplication";
    }

    @Override
    public void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Only one argument for prim(1) or non prim(0) testing");
            return;
        }

        TimeFormat timeFormat = TimeFormat.SECOND;
        final int entries = 5000000;

        if (Integer.parseInt(args[0]) == 0) {

            String fileName = "byteArrayBenchmark";
            final int from = 2;
            final int to = 4;
            HashMap<byte[], byte[]> map = new HashMap<>();

            ByteArrayBenchmark.startSinglecore(fileName, timeFormat, map, entries, from, to);

        } else {

            String fileName = "integerBenchmark";
            HashMap<Integer, Integer> map = new HashMap<>();

            IntegerBenchmark.startSinglecore(fileName, timeFormat, map, entries);


        }
    }

    @Override
    public void signalShutdown() {
        // Interrupt any flow of your application and make sure it shuts down.
        // Do not block here or wait for something to shut down. Shutting down of your application
        // must be execute asynchronously
    }
}
