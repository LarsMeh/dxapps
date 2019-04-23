package de.hhu.bsinfo.dxapp;

public class TimeConverter {

    public static long convertToMikros(long p_nanos) {
        return p_nanos / 1000;
    }

    public static long convertToMillis(long p_nanos) {
        return p_nanos / 1000000;
    }

    public static long convertToSec(long p_nanos) {
        return p_nanos / 1000000000;
    }

}
