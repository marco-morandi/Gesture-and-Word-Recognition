package com.marco.iot.gesture_word_recognition;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.marco.iot.gesture_word_recognition.interfaces.ISensor;
import com.marco.iot.gesture_word_recognition.recorder.Recorder;

public class TrainingFragment extends Fragment {
    private final String TAG = "TrainingFragment";
    private Button bttTrainFragWordRec, bttTrainFragGestureRec;
    private TextView tvWordRecordDone, tvGestureRecordDone;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        return inflater.inflate(R.layout.training_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated");

        bttTrainFragWordRec = view.findViewById(R.id.bttTrainFragWordRec);
        bttTrainFragGestureRec = view.findViewById(R.id.bttTrainFragGestureRec);
        tvWordRecordDone = view.findViewById(R.id.tvWordRecordDone);
        tvGestureRecordDone = view.findViewById(R.id.tvGestureRecordDone);


        bttTrainFragGestureRec.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString("cmd", "start_accelerometer");

            getParentFragmentManager().setFragmentResult("record_gesture_cmd", result);
            tvGestureRecordDone.setText("Recording...");
        });

        bttTrainFragWordRec.setOnClickListener( v -> {
            Bundle result = new Bundle();
            result.putString("cmd", "start_word_recording");

            getParentFragmentManager().setFragmentResult("training_word_cmd", result);
            tvWordRecordDone.setText("Recording...");
        });

        getParentFragmentManager().setFragmentResultListener("training_word_feedback", this, (requestKey, bundle) -> {
            String status = bundle.getString("status");
            if ("Recording finished".equals(status)) {
                tvWordRecordDone.setText("Word recorded!");
            }
        });

        getParentFragmentManager().setFragmentResultListener("training_gesture_feedback", this, (key, bundle) -> {
            String status = bundle.getString("status");
            Log.i(TAG, "Status: " + status);
            if ("gesture_done".equals(status))
                tvGestureRecordDone.setText("Gesture recorded!");
        });



    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
