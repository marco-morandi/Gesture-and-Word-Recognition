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

<<<<<<< HEAD
        bttTrainFragGestureRec.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString("cmd", "start_accelerometer");

            getParentFragmentManager().setFragmentResult("training_acc_cmd", result);
        });
=======
        bttTrainFragWordRec.setOnClickListener( v -> {
            Bundle result = new Bundle();
            result.putString("cmd", "start_word_recording");

            getParentFragmentManager().setFragmentResult("start_word_recording", result);
        });

>>>>>>> c8df9a0574db58c1f09475a57253668b7ab82379

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
