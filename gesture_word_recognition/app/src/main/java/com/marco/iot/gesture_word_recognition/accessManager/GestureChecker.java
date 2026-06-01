package com.marco.iot.gesture_word_recognition.accessManager;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.Constants;
import com.marco.iot.gesture_word_recognition.DTW;
import com.marco.iot.gesture_word_recognition.data.GestureData;

public class GestureChecker {
    private final String TAG = "GestureChecker";
    private DTW dtw;

    public GestureChecker() {
        dtw = new DTW();
    }

    public boolean match(
            GestureData template,
            GestureData sample
    ) {

        float[] tx = template.getXValues();
        float[] sx = sample.getXValues();

        float[] ty = template.getYValues();
        float[] sy = sample.getYValues();

        float[] tz = template.getZValues();
        float[] sz = sample.getZValues();

        double dx = dtw.compute(tx, sx).getDistance() / (tx.length + sx.length);
        Log.i(TAG, "NORM_DISTANCE_X = " + dx);

        double dy = dtw.compute(ty, sy).getDistance() / (ty.length + sy.length);
        Log.i(TAG, "NORM_DISTANCE_Y = " + dy);

        double dz = dtw.compute(tz, sz).getDistance() / (tz.length + sz.length);
        Log.i(TAG, "NORM_DISTANCE_Z = " + dz);

        double distance_sum = dx + dy + dz;

        Log.i(TAG, "Sum of distances: " + distance_sum);

        return distance_sum < Constants.GESTURE_THRESHOLD;
    }

}
