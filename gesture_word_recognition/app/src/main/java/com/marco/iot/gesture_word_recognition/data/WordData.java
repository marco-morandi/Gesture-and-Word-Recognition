package com.marco.iot.gesture_word_recognition.data;

public class WordData {
    private float[] samples;
    private int sampleRate;

    public WordData(float[] samples, int sampleRate) {
        this.samples = samples;
        this.sampleRate = sampleRate;
    }

    public float[] getSamples() {
        return samples;
    }

    public int getSampleRate() {
        return sampleRate;
    }
}
