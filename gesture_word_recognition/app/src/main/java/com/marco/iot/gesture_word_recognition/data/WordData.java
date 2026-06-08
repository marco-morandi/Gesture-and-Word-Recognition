package com.marco.iot.gesture_word_recognition.data;

public class WordData {
    private float[] data;

    public WordData(float[] data) {
        this.data = data;
    }

    public float[] getSamples() {
        return data;
    }
}
