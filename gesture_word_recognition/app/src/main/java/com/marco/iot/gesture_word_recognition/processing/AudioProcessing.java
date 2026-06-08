package com.marco.iot.gesture_word_recognition.processing;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.Constants;

import java.util.Arrays;

public final class AudioProcessing {

    private static final String TAG = "AudioProcessing";


    private AudioProcessing() { }

    public static float[] preProcess(float[] signal) {

        float[] processedSignal = new float[signal.length];

        // Log.i(TAG, "Signal length = " + signal.length);
        processedSignal = removeMean(signal);
        // Log.i(TAG, "Signal length after mean removed = " + processedSignal.length);
        processedSignal = trimSilence(processedSignal);
        // Log.i(TAG, "Signal length after silence removed = " + processedSignal.length);
        processedSignal = zNormalize(processedSignal);
        // Log.i(TAG, "Signal length after normalization = " + processedSignal.length);
        processedSignal = downsample(processedSignal, Constants.DOWNSAMPLING_FACTOR);
        // Log.i(TAG, "Signal length after downsampling = " + processedSignal.length);

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

        float threshold = maxAbs * 0.10f;

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

        // padding di 500 campioni -> a 8kHz ca 50ms -> per recuperare eventuali attacchi di parola tagliati
        if (start > 500) {
            start -= 500;
        }
        if (end < signal.length - 500) {
            end += 500;
        }

        // Log.i(TAG, "start = " + start);
        // Log.i(TAG, "end = " + end);



        return Arrays.copyOfRange(
                signal,
                start,
                end + 1
        );
    }

    public static float[] convertShortToFloat(short[] input) {
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) input[i] / 32768.0f;
        }
        return output;
    }

}
