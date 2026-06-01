package com.marco.iot.gesture_word_recognition.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.marco.iot.gesture_word_recognition.data.GestureData;
import com.marco.iot.gesture_word_recognition.interfaces.IAccelerometer;
import com.marco.iot.gesture_word_recognition.interfaces.ISensor;
import com.marco.iot.gesture_word_recognition.processing.AccelerometerProcessing;

import java.util.ArrayList;
import java.util.List;

public class Accelerometer implements ISensor, SensorEventListener {
    private final String TAG = "Accelerometer";

    private SensorManager sensorManager = null;
    private Sensor sensor = null;

    private IAccelerometer iAccelerometer = null;

    private List<Float> xValues, yValues, zValues;


    public Accelerometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        else
            Log.e(TAG, "Sensor not found");

        this.iAccelerometer = (IAccelerometer) context;
    }

    public void start() {
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        zValues = new ArrayList<>();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
        GestureData gestureData = new GestureData(
                AccelerometerProcessing.convertFloatListToArray(xValues),
                AccelerometerProcessing.convertFloatListToArray(yValues),
                AccelerometerProcessing.convertFloatListToArray(zValues));
        iAccelerometer.onRecordingDone(gestureData);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "onSensorChanged");

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        Log.i(TAG, "X: " + x + " Y: " + y + " Z: " + z);
        xValues.add(x);
        yValues.add(y);
        zValues.add(z);
    }
}
