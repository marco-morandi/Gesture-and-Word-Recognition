package com.marco.iot.gesture_word_recognition.accessManager;

import com.marco.iot.gesture_word_recognition.DTW;
import com.marco.iot.gesture_word_recognition.data.GestureData;

public class GestureChecker {

    private DTW dtw;

    private static final double THRESHOLD = 0.1;

    public GestureChecker() {
        dtw = new DTW();
    }

    public boolean match(
            GestureData template,
            GestureData sample
    ) {

        double dx =
                dtw.compute(toArray(template.getXValues()), toArray(sample.getXValues())).getDistance();

        double dy =
                dtw.compute(toArray(template.getYValues()), toArray(sample.getYValues())).getDistance();

        double dz =
                dtw.compute(toArray(template.getZValues()), toArray(sample.getZValues())).getDistance();

        return dx < THRESHOLD
                && dy < THRESHOLD
                && dz < THRESHOLD;
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
