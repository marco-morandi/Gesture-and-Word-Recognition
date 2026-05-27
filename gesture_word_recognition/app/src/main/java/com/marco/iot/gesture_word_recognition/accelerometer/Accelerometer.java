package com.marco.iot.gesture_word_recognition.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.marco.iot.gesture_word_recognition.interfaces.INewDataAvailable;
import com.marco.iot.gesture_word_recognition.interfaces.ISensor;

public class Accelerometer implements ISensor, SensorEventListener {
    private final String TAG = "MyAccelerometer";

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    private INewDataAvailable newDataAvailable = null;


    public Accelerometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        else
            Log.e(TAG, "Sensor not found");

        this.newDataAvailable = (INewDataAvailable) context;
    }

    public void start() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void stop() {
        sensorManager.unregisterListener(this);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event) { // implements callback method when sensor data changes
        Log.i(TAG, "onSensorChanged");

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        Log.i(TAG, "X: " + x + " Y: " + y + " Z: " + z);

        newDataAvailable.onNewAccelerometerDataAvailable(x, y, z);
    }
}
