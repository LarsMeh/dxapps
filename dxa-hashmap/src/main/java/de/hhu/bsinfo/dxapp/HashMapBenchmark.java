package de.hhu.bsinfo.dxapp;


import de.hhu.bsinfo.dxram.app.Application;
import de.hhu.bsinfo.dxram.datastructure.DataStructureService;
import de.hhu.bsinfo.dxram.datastructure.util.HashFunctions;
import de.hhu.bsinfo.dxram.engine.DXRAMVersion;
import de.hhu.bsinfo.dxram.generated.BuildConfig;
import de.hhu.bsinfo.dxram.datastructure.HashMap;


public class HashMapBenchmark extends Application {

    @Override
    public DXRAMVersion getBuiltAgainstVersion() {
        return BuildConfig.DXRAM_VERSION;
    }

    @Override
    public String getApplicationName() {
        return "HashMapBenchmark";
    }

    @Override
    public void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Only one argument for prim(1) or non prim(0) testing");
            return;
        }

        DataStructureService service = getService(DataStructureService.class);

        TimeFormat timeFormat = TimeFormat.SECOND;
        final int entries = 5000000;

        if (Integer.parseInt(args[0]) == 0) {

            String fileName = "byteArrayBenchmark";
            final int from = 2;
            final int to = 4;
            HashMap<byte[], byte[]> map = service.createHashMap("a", entries, -1, to, to, HashFunctions.MURMUR3_32);

            ByteArrayBenchmark.startSinglecore(fileName, timeFormat, map, entries, from, to);

        } else {

            String fileName = "integerBenchmark";
            HashMap<Integer, Integer> map = service.createHashMap("a", entries, -1, Integer.BYTES, Integer.BYTES, HashFunctions.MURMUR3_32);

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
