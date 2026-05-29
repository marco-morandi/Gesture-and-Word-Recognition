package com.marco.iot.gesture_word_recognition.accessManager;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.DTW;
import com.marco.iot.gesture_word_recognition.data.GestureData;

public class GestureChecker {
    private final String TAG = "GestureChecker";
    private DTW dtw;

    private static final double THRESHOLD_X = 1.5;
    private static final double THRESHOLD_Y = 1.5;
    private static final double THRESHOLD_Z = 1.5;

    public GestureChecker() {
        dtw = new DTW();
    }

    public boolean match(
            GestureData template,
            GestureData sample
    ) {

        double dx =
                dtw.compute(toArray(template.getXValues()), toArray(sample.getXValues())).getDistance();
        Log.i(TAG, "DISTANCE_GESTURE_X = " + dx);


        double dy =
                dtw.compute(toArray(template.getYValues()), toArray(sample.getYValues())).getDistance();
        Log.i(TAG, "DISTANCE_GESTURE_Y = " + dy);

        double dz =
                dtw.compute(toArray(template.getZValues()), toArray(sample.getZValues())).getDistance();
        Log.i(TAG, "DISTANCE_GESTURE_Z = " + dz);

        return dx < THRESHOLD_X
                && dy < THRESHOLD_Y
                && dz < THRESHOLD_Z;
    }

    private float[] toArray(
            java.util.List<Float> list
    ) {

        float[] array = new float[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }
}
