package de.hhu.bsinfo.dxapp;

public enum TimeFormat {
    SECOND, MILLISECOND, MICROSECOND;

    public long getTime() {
        switch (this) {
            case SECOND:
                return 1000000000;
            case MILLISECOND:
                return 1000000;
            case MICROSECOND:
                return 1000;
            default:
                return -1;
        }
    }
}
