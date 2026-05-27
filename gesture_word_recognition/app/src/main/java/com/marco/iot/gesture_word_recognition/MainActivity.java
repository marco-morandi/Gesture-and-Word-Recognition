package com.marco.iot.gesture_word_recognition;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButtonToggleGroup;

import com.marco.iot.gesture_word_recognition.accelerometer.Accelerometer;
import com.marco.iot.gesture_word_recognition.accessManager.AccessChecker;
import com.marco.iot.gesture_word_recognition.data.GestureData;
import com.marco.iot.gesture_word_recognition.data.WordData;
import com.marco.iot.gesture_word_recognition.interfaces.IAccelerometer;
import com.marco.iot.gesture_word_recognition.interfaces.IRecorder;
import com.marco.iot.gesture_word_recognition.interfaces.ISensor;

import com.marco.iot.gesture_word_recognition.recorder.Recorder;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IAccelerometer, IRecorder {

    private final String TAG = "MainActivity";

    private String mode = "training";

    private AccessChecker accessChecker;

    private Button bttStartWordRec, bttStartGestureRec, bttAuthenticate;
    private MaterialButtonToggleGroup bttChooseButton;
    private TextView tvResult, tvWordRecDone, tvGestureRecDone;

    private GestureData sampleGesture;
    private GestureData templateGesture;

    private WordData sampleWord;
    private WordData templateWord;

    private ISensor accelerometer;
    private ISensor recorder;

    private boolean isRecordingGesture = false;

    private Handler accHandler = new Handler(Looper.getMainLooper());


    private final int FS = 6000;
    private final int RECORDING_LENGTH_IN_SEC = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttChooseButton = findViewById(R.id.bttChooseButton);
        tvResult = findViewById(R.id.tv_result);

        bttStartGestureRec = findViewById(R.id.bttStartGestureRec);
        bttStartWordRec = findViewById(R.id.bttStartWordRec);
        bttAuthenticate = findViewById(R.id.bttAuthenticate);

        tvWordRecDone = findViewById(R.id.tvWordRecDone);
        tvGestureRecDone = findViewById(R.id.tvGestureRecDone);

        accelerometer = new Accelerometer(this);
        recorder = new Recorder(this, FS, RECORDING_LENGTH_IN_SEC);


        bttChooseButton.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) { return; }

            if (checkedId == R.id.bttTraining) {
                tvWordRecDone.setText("");
                tvGestureRecDone.setText("");
                mode = "training";

            } else if (checkedId == R.id.bttRecognition) {
                tvWordRecDone.setText("");
                tvGestureRecDone.setText("");
                mode = "recognition";
            }
        });
        bttStartGestureRec.setOnClickListener(v->{
            startGestureRecording();
        });

        bttStartWordRec.setOnClickListener(v->{
            startWordRecording();
        });

        accessChecker = new AccessChecker();

        bttAuthenticate.setOnClickListener(v->{
            boolean authenticate = accessChecker.authenticate(templateWord, sampleWord, templateGesture, sampleGesture);
            if(authenticate){
                tvResult.setText("Access granted!");
            }
            else{
                tvResult.setText("Access denied!");
            }
        });
    }


    private void startGestureRecording() {
        isRecordingGesture = true;
        accelerometer.start();

        accHandler.postDelayed(() -> {
            accelerometer.stop();
            isRecordingGesture = false;
        }, RECORDING_LENGTH_IN_SEC * 1000L);

    }

    private void startWordRecording(){
        recorder.start();
    }

    @Override
    public void onRecordingDone(GestureData data) {
        if (mode.equals("training")) {
            templateGesture = data;
        }
        if (mode.equals("recognition")) {
            sampleGesture = data;
        }
        tvGestureRecDone.setText("Gesture recorded!");
    }

    @Override
    public void onRecordingDone(WordData data) {
        if (mode.equals("training")) {
            templateWord = data;
        }
        if (mode.equals("recognition")) {
            sampleWord = data;
        }
        tvWordRecDone.setText("Word recorded!");
    }
}
