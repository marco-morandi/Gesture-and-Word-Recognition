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

public class MainActivity extends AppCompatActivity implements INewDataAvailable {

    private final String TAG = "MainActivity";

    private MaterialButtonToggleGroup bttChooseButton;
    private TextView tvResult;

    private ISensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttChooseButton = findViewById(R.id.bttChooseButton);
        tvResult = findViewById(R.id.tv_result);

        accelerometer = new Accelerometer(this);

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

    }

    @Override
    public void onNewAccelerometerDataAvailable(float x, float y, float z) {

    }

    @Override
    public void onRecordingDone(short[] audioData) {

    }
}
