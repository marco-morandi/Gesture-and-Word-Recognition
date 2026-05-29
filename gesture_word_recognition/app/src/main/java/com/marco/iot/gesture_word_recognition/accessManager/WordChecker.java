package com.marco.iot.gesture_word_recognition.accessManager;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.DTW;
import com.marco.iot.gesture_word_recognition.data.WordData;

public class WordChecker {
    private final String TAG = "WordChecker";
    private DTW dtw;

    private static final double THRESHOLD = 0.0012;

    public WordChecker() {
        dtw = new DTW();
    }

    public boolean match(
            WordData template,
            WordData sample
    ) {

        double distance =
                dtw.compute(template.getSamples(), sample.getSamples()).getDistance();
        Log.i(TAG, "DISTANCE_WORD = " + distance);

        return distance < THRESHOLD;
    }

    private float[] shortToFloat(short[] data) {

        float[] result = new float[data.length];

        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }

        return result;
    }
}
