package com.marco.iot.gesture_word_recognition;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButtonToggleGroup;

import com.marco.iot.gesture_word_recognition.accelerometer.Accelerometer;
import com.marco.iot.gesture_word_recognition.interfaces.INewDataAvailable;
import com.marco.iot.gesture_word_recognition.interfaces.ISensor;

import com.marco.iot.gesture_word_recognition.interfaces.INewDataAvailable;
import com.marco.iot.gesture_word_recognition.interfaces.ISensor;
import com.marco.iot.gesture_word_recognition.recorder.Recorder;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements INewDataAvailable {

    private final String TAG = "MainActivity";

    private String mode = "training";

    private MaterialButtonToggleGroup bttChooseButton;
    private TextView tvResult;

    private float[] wordTemplate;
    private ISensor accelerometer;
    private List<Float> gestureTemplateX = new ArrayList<>();
    private List<Float> gestureTemplateY = new ArrayList<>();
    private List<Float> gestureTemplateZ = new ArrayList<>();
    private List<Float> gestureSampleX = new ArrayList<>();
    private List<Float> gestureSampleY = new ArrayList<>();
    private List<Float> gestureSampleZ = new ArrayList<>();
    private boolean isRecordingGesture = false;


    private Handler accHandler = new Handler(Looper.getMainLooper());

    private ISensor recorder;
    private final int FS = 8000;



    private final int RECORDING_LENGTH_IN_SEC = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttChooseButton = findViewById(R.id.bttChooseButton);
        tvResult = findViewById(R.id.tv_result);

        accelerometer = new Accelerometer(this);

        recorder = new Recorder(this, FS, RECORDING_LENGTH_IN_SEC);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new TrainingFragment())
                    .commit();
        }

        bttChooseButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) { return; }

            if (checkedId == R.id.bttTraining) {
                mode = "training";
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new TrainingFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (checkedId == R.id.bttRecognition) {
                mode = "recognition";
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new RecognitionFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });


        getSupportFragmentManager().setFragmentResultListener("record_gesture_cmd", this,
                (requestKey, bundle) -> {
                    String cmd = bundle.getString("cmd");
                    Log.i(TAG, "Command: " + cmd);
                    if ("start_accelerometer".equals(cmd)) {
                        startAccelerometerDataCollection();
                    }
                });

        getSupportFragmentManager().setFragmentResultListener("record_word_cmd", this,
                (requestKey, bundle) -> {
                    String cmd = bundle.getString("cmd");
                    Log.i(TAG, "Command: "+ cmd);
                    if ("start_word_recording".equals(cmd)) {
                        recorder.start();
                    }
                });

    }

    private void startAccelerometerDataCollection() {
        gestureTemplateX.clear();
        gestureTemplateY.clear();
        gestureTemplateZ.clear();
        gestureSampleX.clear();
        gestureSampleY.clear();
        gestureSampleZ.clear();

        isRecordingGesture = true;
        accelerometer.start();

        accHandler.postDelayed(() -> {
            accelerometer.stop();
            isRecordingGesture = false;
            onAccelerometerCollectionDone();
        }, RECORDING_LENGTH_IN_SEC * 1000L);

    }

    private float[] audioSamplesConvertionToFloat(short[] input) {
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) input[i] / 32768.0f;
        }
        return output;
    }

    @Override
    public void onNewAccelerometerDataAvailable(float x, float y, float z) {
        if(!isRecordingGesture) { return; }

        if (mode.equals("training")) {
            gestureTemplateX.add(x);
            gestureTemplateY.add(y);
            gestureTemplateZ.add(z);
        }
        else if (mode.equals("recognition")) {
            gestureSampleX.add(x);
            gestureSampleY.add(y);
            gestureSampleZ.add(z);
        }

    }

    public void onAccelerometerCollectionDone() {
        Log.i(TAG, "onAccelerometerCollectionDone");

        Bundle result = new Bundle();
        result.putString("status", "gesture_done");
        if (mode.equals("training"))
            getSupportFragmentManager().setFragmentResult("training_gesture_feedback", result);
        else if (mode.equals("recognition"))
            getSupportFragmentManager().setFragmentResult("recognition_gesture_feedback", result);
    }

    @Override
    public void onRecordingDone(short[] audioData) {
        wordTemplate = audioSamplesConvertionToFloat(audioData);

        Bundle result = new Bundle();
        result.putString("status", "Recording finished");
        getSupportFragmentManager().setFragmentResult("training_word_feedback", result);
    }
}
