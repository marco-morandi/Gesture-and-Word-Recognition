package com.marco.iot.gesture_word_recognition.data;

public class WordData {
    private float[] data;
    private int sampleRate;

    public WordData(float[] data, int sampleRate) {
        this.data = data;
        this.sampleRate = sampleRate;
    }

    public float[] getSamples() {
        return data;
    }

    public int getSampleRate() {
        return sampleRate;
    }
}
