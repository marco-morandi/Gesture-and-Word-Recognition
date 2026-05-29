package com.marco.iot.gesture_word_recognition.accessManager;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.DTW;
import com.marco.iot.gesture_word_recognition.data.GestureData;

public class GestureChecker {
    private final String TAG = "GestureChecker";
    private DTW dtw;

    private static final double THRESHOLD_X = 0.02;
    private static final double THRESHOLD_Y = 0.02;
    private static final double THRESHOLD_Z = 0.02;

    public GestureChecker() {
        dtw = new DTW();
    }

    public boolean match(
            GestureData template,
            GestureData sample
    ) {

        float[] tx = normalizeAmplitude(toArray(template.getXValues()));
        float[] sx = normalizeAmplitude(toArray(sample.getXValues()));

        float[] ty = normalizeAmplitude(toArray(template.getYValues()));
        float[] sy = normalizeAmplitude(toArray(sample.getYValues()));

        float[] tz = normalizeAmplitude(toArray(template.getZValues()));
        float[] sz = normalizeAmplitude(toArray(sample.getZValues()));

        double dx = dtw.compute(tx, sx).getDistance() / (tx.length + sx.length);
        Log.i(TAG, "NORM_DISTANCE_X = " + dx);

        double dy = dtw.compute(ty, sy).getDistance() / (ty.length + sy.length);
        Log.i(TAG, "NORM_DISTANCE_Y = " + dy);

        double dz = dtw.compute(tz, sz).getDistance() / (tz.length + sz.length);
        Log.i(TAG, "NORM_DISTANCE_Z = " + dz);


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

    // Z-normalization: mean = 0, variance = 1
    private float[] normalizeAmplitude(float[] data) {
        if (data == null || data.length == 0) return data;

        float sum = 0;
        for (float v : data) sum += v;
        float mean = sum / data.length;

        float sqSum = 0;
        for (float v : data) sqSum += (float)Math.pow(v - mean, 2);
        float stdDev = (float) Math.sqrt(sqSum / data.length);

        float[] normalized = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            // Se stdDev è quasi zero (segnale piatto), restituiamo un array di zeri
            normalized[i] = (stdDev > 0.00001f) ? (data[i] - mean) / stdDev : 0f;
        }
        return normalized;
    }
}
