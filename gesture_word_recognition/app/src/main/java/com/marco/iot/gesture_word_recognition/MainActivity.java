package com.marco.iot.gesture_word_recognition;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButtonToggleGroup;

import com.marco.iot.gesture_word_recognition.accelerometer.Accelerometer;
import com.marco.iot.gesture_word_recognition.interfaces.INewDataAvailable;
import com.marco.iot.gesture_word_recognition.interfaces.ISensor;

import com.marco.iot.gesture_word_recognition.interfaces.INewDataAvailable;
import com.marco.iot.gesture_word_recognition.interfaces.ISensor;
import com.marco.iot.gesture_word_recognition.recorder.Recorder;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements INewDataAvailable {

    private final String TAG = "MainActivity";

    private MaterialButtonToggleGroup bttChooseButton;
    private TextView tvResult;

    private float[] wordTemplate;
    private ISensor accelerometer;

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
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new TrainingFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (checkedId == R.id.bttRecognition) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new RecognitionFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });


        getSupportFragmentManager().setFragmentResultListener("training_acc_cmd", this,
                (requestKey, bundle) -> {
                    String cmd = bundle.getString("cmd");
                    if ("start_accelerometer".equals(cmd)) {
                        Log.i(TAG, "Command: " + cmd);
                        // accelerometer.start();
                    }
                });

        getSupportFragmentManager().setFragmentResultListener("start_word_recording", this, (requestKey, bundle) -> {
            String cmd = bundle.getString("cmd");

            if ("start_word_recording".equals(cmd)) {
                recorder.start();
            }
        });


    }

    private float[] audioSampplesConvertiontoFloat(short[] input) {
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) input[i] / 32768.0f;
        }
        return output;
    }

    @Override
    public void onNewAccelerometerDataAvailable(float x, float y, float z) {

    }

    @Override
    public void onRecordingDone(short[] audioData) {
        wordTemplate = audioSampplesConvertiontoFloat(audioData);

        Bundle result = new Bundle();
        result.putString("status", "Recording finished");
        getSupportFragmentManager().setFragmentResult("training_request", result);
    }
}
