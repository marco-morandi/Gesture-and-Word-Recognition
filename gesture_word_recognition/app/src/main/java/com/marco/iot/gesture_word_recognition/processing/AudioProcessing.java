package com.marco.iot.gesture_word_recognition.processing;

import java.util.Arrays;

public final class AudioProcessing {

    private AudioProcessing() { }

    public static float[] preProcess(float[] signal) {

        float[] processedSignal = new float[signal.length];

        processedSignal = removeMean(signal);
        processedSignal = trimSilence(processedSignal);
        processedSignal = normalizeByMax(processedSignal);
        processedSignal = downsample(processedSignal, 8);

        return processedSignal;
    }

    public static float[] downsample(float[] data, int factor) {
        if (data == null) return null;
        float[] result = new float[data.length / factor];
        for (int i = 0; i < result.length; i++) {
            result[i] = data[i * factor];
        }
        return result;
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

    public static float[] trimSilence(
            float[] signal
    ) {

        if (signal == null || signal.length == 0) {
            return signal;
        }

        float maxAbs = 0f;

        for (float v : signal) {
            maxAbs = Math.max(
                    maxAbs,
                    Math.abs(v)
            );
        }

        float threshold = maxAbs * 0.15f;

        int start = 0;

        while (start < signal.length &&
                Math.abs(signal[start]) < threshold) {
            start++;
        }

        int end = signal.length - 1;

        while (end > start &&
                Math.abs(signal[end]) < threshold) {
            end--;
        }

        return Arrays.copyOfRange(
                signal,
                start,
                end + 1
        );
    }

    public static float[] convertToFloat(short[] input) {
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) input[i] / 32768.0f;
        }
        return output;
    }

}
