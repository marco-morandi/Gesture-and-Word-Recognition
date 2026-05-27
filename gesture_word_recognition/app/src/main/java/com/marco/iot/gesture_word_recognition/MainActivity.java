package com.marco.iot.gesture_word_recognition;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private MaterialButtonToggleGroup bttChooseButton;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttChooseButton = findViewById(R.id.bttChooseButton);

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

    }
}