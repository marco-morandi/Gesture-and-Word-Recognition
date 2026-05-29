package com.marco.iot.gesture_word_recognition.accessManager;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.DTW;
import com.marco.iot.gesture_word_recognition.data.WordData;

public class WordChecker {
    private final String TAG = "WordChecker";
    private DTW dtw;

    private static final double THRESHOLD = 4.5e-5;

    public WordChecker() {
        dtw = new DTW();
    }

    public boolean match(
            WordData template,
            WordData sample
    ) {

        float[] tAudio = normalizeAmplitude(template.getSamples());
        float[] sAudio = normalizeAmplitude(sample.getSamples());

        double rawDistance = dtw.compute(tAudio, sAudio).getDistance();

        double normalizedDistance = rawDistance / (tAudio.length + sAudio.length);

        Log.i(TAG, "AUDIO_NORM_DISTANCE = " + normalizedDistance);

        return normalizedDistance < THRESHOLD;
    }

    private float[] shortToFloat(short[] data) {

        float[] result = new float[data.length];

        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }

        return result;
    }

    private float[] normalizeAmplitude(float[] data) {
        if (data == null || data.length == 0) return data;

        float sum = 0;
        for (float v : data) sum += v;
        float mean = sum / data.length;

        float sqSum = 0;
        for (float v : data) sqSum += (float) Math.pow(v - mean, 2);
        float stdDev = (float) Math.sqrt(sqSum / data.length);

        float[] normalized = new float[data.length];
        // remove silence bias and normalize amplitude
        for (int i = 0; i < data.length; i++) {
            normalized[i] = (stdDev > 0.00001f) ? (data[i] - mean) / stdDev : 0f;
        }
        return normalized;
    }
}
