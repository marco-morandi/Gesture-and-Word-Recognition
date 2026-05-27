package com.marco.iot.gesture_word_recognition.interfaces;

import com.marco.iot.gesture_word_recognition.data.GestureData;


public interface IAccelerometer {

    public void onRecordingDone(GestureData data);

}
