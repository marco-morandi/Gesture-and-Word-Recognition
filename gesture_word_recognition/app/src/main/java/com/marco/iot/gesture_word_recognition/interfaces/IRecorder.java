package com.marco.iot.gesture_word_recognition.interfaces;

import com.marco.iot.gesture_word_recognition.data.WordData;

public interface IRecorder {
    public void onRecordingDone(WordData data);
}
