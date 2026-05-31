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

import com.marco.iot.gesture_word_recognition.processing.AudioProcessing;
import com.marco.iot.gesture_word_recognition.recorder.Recorder;

import java.util.List;


public class MainActivity extends AppCompatActivity implements IAccelerometer, IRecorder {

    private final String TAG = "MainActivity";

    private String mode;

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
    private Handler recHandler = new Handler(Looper.getMainLooper());


    private final int FS = 8000;
    private final int RECORDING_LENGTH_IN_SEC = 2;

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

            tvWordRecDone.setText("");
            tvGestureRecDone.setText("");
            tvResult.setText("");

            if (checkedId == R.id.bttTraining) {
                mode = "training";

            } else if (checkedId == R.id.bttRecognition) {
                mode = "recognition";
            }
        });

        // better to automatically check one of the button (this trigger the event listener)
        bttChooseButton.check(R.id.bttTraining);

        bttStartGestureRec.setOnClickListener(v->{
            tvGestureRecDone.setText("");
            tvGestureRecDone.setText("Recording gesture...");
            tvResult.setText("");
            startGestureRecording();
        });

        bttStartWordRec.setOnClickListener(v->{
            tvWordRecDone.setText("");
            tvWordRecDone.setText("Recording word...");
            tvResult.setText("");
            startWordRecording();
        });

        accessChecker = new AccessChecker();

        bttAuthenticate.setOnClickListener(v->{
            Log.i(TAG, "Authenticate button clicked");
            new Thread( () -> {
                Log.i(TAG, "Thread started...");

                if (templateWord == null || sampleWord == null || templateGesture == null || sampleGesture == null) {
                    runOnUiThread(() -> tvResult.setText("Error: DATA MISSING!"));
                    return;
                }

                // audio pre processing
                float[] templateAudio = templateWord.getSamples();
                float[] sampleAudio = sampleWord.getSamples();

                templateAudio = AudioProcessing.preProcess(templateAudio);
                sampleAudio = AudioProcessing.preProcess(sampleAudio);

                WordData templateWordPreprocessed = new WordData(templateAudio, templateWord.getSampleRate());
                WordData sampleWordPreprocessed = new WordData(sampleAudio, sampleWord.getSampleRate());

                // gesture pre processing
                List<Float> templateX = templateGesture.getXValues();
                List<Float> templateY = templateGesture.getYValues();
                List<Float> templateZ = templateGesture.getZValues();

                List<Float> sampleX = templateGesture.getXValues();
                List<Float> sampleY = templateGesture.getYValues();
                List<Float> sampleZ = templateGesture.getZValues();

                boolean authenticate = accessChecker.authenticate(templateWordPreprocessed, sampleWordPreprocessed, templateGesture, sampleGesture);
                runOnUiThread(() -> {
                    if(authenticate){
                        tvResult.setText("Access granted!");
                    }
                    else {
                        tvResult.setText("Access denied!");
                    }
                });
            }).start();
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

        accHandler.postDelayed(() -> {
            tvGestureRecDone.setText("");
        }, 3000);
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

        recHandler.postDelayed(() -> {
            tvWordRecDone.setText("");
        }, 3000);
    }


}
