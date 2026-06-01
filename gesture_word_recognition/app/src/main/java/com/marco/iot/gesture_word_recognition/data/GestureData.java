package com.marco.iot.gesture_word_recognition.data;

import com.marco.iot.gesture_word_recognition.processing.AccelerometerProcessing;

import java.util.List;

public class GestureData {
    private float[] xValues;
    private float[] yValues;
    private float[] zValues;

    public GestureData(
            float[] xValues,
            float[] yValues,
            float[] zValues
    ) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.zValues = zValues;
    }

    public float[] getXValues() {
        return xValues;
    }

    public float[] getYValues() {
        return yValues;
    }

    public float[] getZValues() {
        return zValues;
    }
}
