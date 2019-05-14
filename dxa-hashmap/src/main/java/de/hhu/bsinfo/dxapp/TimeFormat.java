package de.hhu.bsinfo.dxapp;

public enum TimeFormat {
    SECOND, MILLISECOND, MICROSECOND;

    public long getCorrectTime() {
        switch (this) {
            case SECOND:
                return 1000000000;
            case MILLISECOND:
                return 1000000;
            default:
                return -1;
        }
    }

    public long getTimeMilli() {
        switch (this) {
            case SECOND:
                return 999;
            case MILLISECOND:
                return 1;
            default:
                return -1;
        }
    }
}
