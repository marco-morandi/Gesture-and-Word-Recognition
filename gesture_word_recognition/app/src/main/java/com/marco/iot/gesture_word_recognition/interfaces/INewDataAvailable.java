package com.marco.iot.gesture_word_recognition.interfaces;

public interface INewDataAvailable {

    public void onNewAccelerometerDataAvailable(float x, float y, float z);
    public void onAccelerometerCollectionDone();

    public void onRecordingDone(short[] audioData);

}
