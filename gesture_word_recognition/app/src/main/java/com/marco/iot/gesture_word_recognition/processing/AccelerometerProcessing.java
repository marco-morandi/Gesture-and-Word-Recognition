package com.marco.iot.gesture_word_recognition.processing;

import com.marco.iot.gesture_word_recognition.accelerometer.Accelerometer;

import java.util.ArrayList;
import java.util.List;

public final class AccelerometerProcessing {

    private AccelerometerProcessing() { }

    public static List<Float> preProcess(List<Float> signal) {
        List<Float> processedSignal = new ArrayList<>();

        return processedSignal;
    }

    public static float[] normalizeByMax(float[] signal) {

        if (signal == null || signal.length == 0) {
            return signal;
        }

        float maxAbs = 0f;

        for (float v : signal) {
            maxAbs = Math.max(maxAbs, Math.abs(v));
        }

        if (maxAbs < 1e-6f) {
            return signal.clone();
        }

        float[] normalized = new float[signal.length];

        for (int i = 0; i < signal.length; i++) {
            normalized[i] = signal[i] / maxAbs;
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
}
