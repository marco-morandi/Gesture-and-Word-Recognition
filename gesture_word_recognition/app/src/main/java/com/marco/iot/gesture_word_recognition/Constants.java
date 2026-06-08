package com.marco.iot.gesture_word_recognition;

public final class Constants {

    private Constants() {}

    public static final int FS = 8000;
    public static final int RECORDING_LENGTH_IN_SEC = 2;

    public static final int WINDOW_SIZE = 120; // a 8kHz sono ca 15ms

    public static final double GESTURE_THRESHOLD = 8.5e-4;
    public static final double WORD_THRESHOLD = 1e-3;

}
