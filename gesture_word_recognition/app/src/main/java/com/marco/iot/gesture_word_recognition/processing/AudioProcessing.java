package com.marco.iot.gesture_word_recognition.processing;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.Constants;

import java.util.Arrays;

public final class AudioProcessing {

    private static final String TAG = "AudioProcessing";


    private AudioProcessing() { }

    public static float[] preProcess(float[] signal) {

        float[] processedSignal;
        float[] envelope;

        // Log.i(TAG, "Signal length = " + signal.length);
        processedSignal = removeMean(signal);
        // Log.i(TAG, "Signal length after mean removed = " + processedSignal.length);
        processedSignal = trimSilence(processedSignal);
        Log.i(TAG, "Signal length after silence removed = " + processedSignal.length);
        envelope = getEnvelope(processedSignal, Constants.WINDOW_SIZE);
        Log.i(TAG, "Envelope length = " + envelope.length);
        envelope = zNormalize(envelope);
        // Log.i(TAG, "Signal length after normalization = " + processedSignal.length);


        return envelope;
    }

    public static float[] getEnvelope(float[] signal, int windowSize) {
        if (signal == null || signal.length < windowSize) return signal;

        int numWindows = signal.length / windowSize;
        float[] envelope = new float[numWindows];

        for (int i = 0; i < numWindows; i++) {
            float sum = 0f;
            for (int j = 0; j < windowSize; j++) {
                float sample = signal[i * windowSize + j];
                sum += sample * sample; // Elevamento al quadrato (Energia)
            }
            // Radice della media (Volume efficace nella finestra)
            envelope[i] = (float) Math.sqrt(sum / windowSize);
        }
        return envelope;
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

        // padding di 200 campioni -> a 8kHz ca 25ms -> per recuperare eventuali attacchi di parola tagliati
        if (start > 200) {
            start -= 200;
        }
        if (end < signal.length - 200) {
            end += 200;
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
