package com.marco.iot.gesture_word_recognition.data;

import java.util.List;

public class GestureData {
    private List<Float> xValues;
    private List<Float> yValues;
    private List<Float> zValues;

    public GestureData(
            List<Float> xValues,
            List<Float> yValues,
            List<Float> zValues
    ) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.zValues = zValues;
    }

    public List<Float> getXValues() {
        return xValues;
    }

    public List<Float> getYValues() {
        return yValues;
    }

    public List<Float> getZValues() {
        return zValues;
    }
}
