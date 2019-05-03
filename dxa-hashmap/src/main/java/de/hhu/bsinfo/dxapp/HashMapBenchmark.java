package de.hhu.bsinfo.dxapp;


import de.hhu.bsinfo.dxram.app.Application;
import de.hhu.bsinfo.dxram.datastructure.DataStructureService;
import de.hhu.bsinfo.dxram.datastructure.util.HashFunctions;
import de.hhu.bsinfo.dxram.engine.DXRAMVersion;
import de.hhu.bsinfo.dxram.generated.BuildConfig;
import de.hhu.bsinfo.dxram.datastructure.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class HashMapBenchmark extends Application {

    private final Logger log = LogManager.getFormatterLogger(HashMapBenchmark.class);

    // type=[performance,memory]
    // data=[prim,non-prim]
    // entries=[Integer]
    // cores=[Integer]

    private static final String ARG_TYPE;
    private static final String ARG_DATA;
    private static final String ARG_ENTRIES;
    private static final String ARG_CORES;
    private static final String ARG_HELP;

    private static final String TYPE_PERF;
    private static final String TYPE_MEM;
    private static final String DATA_PRIM;
    private static final String DATA_NON_PRIM;

    private static final String SPLITTER;
    private static final TimeFormat TIME_FORMAT;

    static {
        ARG_TYPE = "type";
        ARG_DATA = "data";
        ARG_ENTRIES = "entries";
        ARG_CORES = "cores";
        ARG_HELP = "help";

        TYPE_PERF = "performance";
        TYPE_MEM = "memory";
        DATA_PRIM = "primitive";
        DATA_NON_PRIM = "non-primitive";

        SPLITTER = "=";
        TIME_FORMAT = TimeFormat.SECOND;
    }


    @Override
    public DXRAMVersion getBuiltAgainstVersion() {
        return BuildConfig.DXRAM_VERSION;
    }

    @Override
    public String getApplicationName() {
        return "HashMapBenchmark";
    }

    @Override
    public void main(String[] p_args) {

        // local var
        final int from = 2;
        final int to = 4;
        final DataStructureService service = getService(DataStructureService.class);
        String path = "/tmp/";
        Benchmark benchmark;

        // Arguments
        final int entries, cores;
        final String data, type;


        // Parse
        String[] current_args;
        switch (p_args.length) {
            case 1:
                if (p_args[0].startsWith(ARG_HELP)) {
                    System.out.println("Use " + SPLITTER + " to split argument and value of argument\n\nArguments which are required in the following order:\n\t" + ARG_TYPE + ": [" + TYPE_PERF + "," + TYPE_MEM + "]\n\t" + ARG_DATA + ": [" + DATA_PRIM + "," + DATA_NON_PRIM + "]\n\t" + ARG_ENTRIES + ": [positive Integer]\n\n " +
                            "Optional Argument:\n\t" + ARG_CORES + ": [positive Integer]");
                } else
                    log.error("If you use 1 argument it must be help");
                return;
            case 3:
                current_args = p_args[0].split(SPLITTER);
                if (checkArgument(current_args, ARG_TYPE))
                    if (checkType(current_args[1]))
                        type = current_args[1];
                    else
                        return;
                else
                    return;

                current_args = p_args[1].split(SPLITTER);
                if (checkArgument(current_args, ARG_DATA))
                    if (checkData(current_args[1]))
                        data = current_args[1];
                    else
                        return;
                else
                    return;

                current_args = p_args[2].split(SPLITTER);
                if (checkArgument(current_args, ARG_ENTRIES))
                    try {
                        entries = Integer.parseInt(current_args[1]);
                    } catch (NumberFormatException p_e) {
                        log.error("Value " + current_args[1] + " for argument " + ARG_ENTRIES + " could not be parsed");
                        return;
                    }
                else
                    return;

                cores = 1;
                break;
            case 4:
                current_args = p_args[0].split(SPLITTER);
                if (checkArgument(current_args, ARG_TYPE))
                    if (checkType(current_args[1]))
                        type = current_args[1];
                    else
                        return;
                else
                    return;

                current_args = p_args[1].split(SPLITTER);
                if (checkArgument(current_args, ARG_DATA))
                    if (checkData(current_args[1]))
                        data = current_args[1];
                    else
                        return;
                else
                    return;

                current_args = p_args[2].split(SPLITTER);
                if (checkArgument(current_args, ARG_ENTRIES))
                    try {
                        entries = Integer.parseInt(current_args[1]);
                    } catch (NumberFormatException p_e) {
                        log.error("Value " + current_args[1] + " for argument " + ARG_ENTRIES + " could not be parsed");
                        return;
                    }
                else
                    return;

                current_args = p_args[3].split(SPLITTER);
                if (checkArgument(current_args, ARG_CORES))
                    try {
                        cores = Integer.parseInt(current_args[1]);
                    } catch (NumberFormatException p_e) {
                        log.error("Value " + current_args[1] + " for argument " + ARG_ENTRIES + " could not be parsed");
                        return;
                    }
                else
                    return;
                break;
            default:
                log.error("3 Arguments required or use argument help for more information");
                return;
        }

        log.debug("Arguments correct. The Benchmark will be selected.");

        // Call correct method
        if (data.equals(DATA_NON_PRIM)) {

            HashMap<byte[], byte[]> map = service.createHashMap("a", entries, -1, to, to, HashFunctions.MURMUR3_32);
            benchmark = new ByteArrayBenchmark(new File("/home/mehnert/benchmarkLogs/byteArrayBenchmark.csv"), TIME_FORMAT, entries, map, from, to);

            if (type.equals(TYPE_PERF))
                benchmark.startNonPrimitivePerformance(cores);

            else if (type.equals(TYPE_MEM))
                log.error("Call performance for non-primitive");
            else
                throw new RuntimeException();

        } else if (data.equals(DATA_PRIM)) {

            HashMap<Integer, Integer> map = service.createHashMap("b", entries, -1, Integer.BYTES, Integer.BYTES, HashFunctions.MURMUR3_32);

            if (type.equals(TYPE_PERF))
                log.error("Call performance for primitive");
            else if (type.equals(TYPE_MEM))
                log.error("Call memory for primitive");
            else
                throw new RuntimeException();

        } else
            throw new RuntimeException();


    }

    /**
     * Checks if the given argument has length 2 and equals the expected argument.
     * If not it will print an error to System.err.
     *
     * @param p_argument argument which was split
     * @param p_expected argument which is expected
     * @return true if argument has length 2 and equals the expected
     */
    private static boolean checkArgument(final String[] p_argument, final String p_expected) {
        if (p_argument.length != 2) {
            System.err.println("Argument have to be splitted by '" + SPLITTER + "'");
            return false;
        } else if (p_argument[0].equals(p_expected)) {
            return true;
        } else {
            System.err.println("Argument was: " + p_argument[0] + " but expected: " + p_expected);
            return false;
        }
    }

    /**
     * Checks the type.
     *
     * @param p_type to be checked
     * @return true if type is valid
     */
    private static boolean checkType(String p_type) {
        if (p_type.equals(TYPE_MEM) || p_type.equals(TYPE_PERF))
            return true;
        else {
            System.err.println("Value " + p_type + " for argument " + ARG_TYPE + " is invalid");
            return false;
        }
    }

    /**
     * Checks the data.
     *
     * @param p_data to be checked
     * @return true if data is valid
     */
    private static boolean checkData(String p_data) {
        if (p_data.equals(DATA_PRIM) || p_data.equals(DATA_NON_PRIM))
            return true;
        else {
            System.err.println("Value " + p_data + " for argument " + ARG_DATA + " is invalid");
            return false;
        }
    }

    @Override
    public void signalShutdown() {
        // Interrupt any flow of your application and make sure it shuts down.
        // Do not block here or wait for something to shut down. Shutting down of your application
        // must be execute asynchronously
    }

}
