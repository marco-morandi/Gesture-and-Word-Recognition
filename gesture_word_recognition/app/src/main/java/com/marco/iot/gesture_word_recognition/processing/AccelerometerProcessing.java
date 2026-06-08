package com.marco.iot.gesture_word_recognition.processing;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.accelerometer.Accelerometer;

import java.util.ArrayList;
import java.util.List;

public final class AccelerometerProcessing {

    private static final String TAG = "AccelerometerProcessing";

    private AccelerometerProcessing() { }

    public static float[] preProcess(float[] signal) {
        float[] processedSignal = new float[signal.length];

        //Log.i(TAG, "Signal = " + signal.toString());
        processedSignal = removeMean(signal);
        //Log.i(TAG, "Signal after mean removed = " + processedSignal.toString());
        processedSignal = zNormalize(processedSignal);
        //Log.i(TAG, "Signal after normalization = " + processedSignal.toString());

        return processedSignal;
    }

    public static float[] zNormalize(float[] signal) {
        if (signal == null || signal.length == 0) return signal;

        float sum = 0f;
        for (float v : signal) sum += v;
        float mean = sum / signal.length;

        float sqSum = 0f;
        for (float v : signal) sqSum += (float) Math.pow(v - mean, 2);
        float stdDev = (float) Math.sqrt(sqSum / signal.length);

        float[] normalized = new float[signal.length];
        for (int i = 0; i < signal.length; i++) {
            // Se stdDev è quasi zero (silenzio totale), restituiamo 0
            normalized[i] = (stdDev > 1e-6f) ? (signal[i] - mean) / stdDev : 0f;
        }
        return normalized;
    }

    public static float[] removeMean(float[] signal) {

        if (signal == null || signal.length == 0) {
            return signal;
        }

        float sum = 0f;

        for (float v : signal) {
            sum += v;
        }

        float mean = sum / signal.length;

        float[] result = new float[signal.length];

        for (int i = 0; i < signal.length; i++) {
            result[i] = signal[i] - mean;
        }

        return result;
    }

    public static float[] convertFloatListToArray(List<Float> list) {

            float[] array = new float[list.size()];

            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }

            return array;
    }
}

