package com.marco.iot.gesture_word_recognition;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class RecognitionFragment extends Fragment {

    private final String TAG = "RecognitionFragment";
    private Button bttFragWordRec, bttFragGestureRec;
    private TextView tvWordRecogDone, tvGestureRecogDone;

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, TAG + "onCreate");
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, TAG + "onCreateView");
        return inflater.inflate(R.layout.recognition_fragment, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, TAG + "onViewCreated");

        bttFragWordRec = view.findViewById(R.id.bttFragWordRec);
        bttFragGestureRec = view.findViewById(R.id.bttFragGestureRec);
        tvWordRecogDone = view.findViewById(R.id.tvWordRecogDone);
        tvGestureRecogDone = view.findViewById(R.id.tvGestureRecogDone);

        bttFragWordRec.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString("cmd", "start_word_recording");

            getParentFragmentManager().setFragmentResult("record_word_cmd", result);
            tvWordRecogDone.setText("Recording...");
        });

        getParentFragmentManager().setFragmentResultListener("recognition_word_feedback", this, (requestKey, bundle) -> {
            String status = bundle.getString("status");
            Log.i(TAG, "Status: " + status);
            if ("Recording finished".equals(status)) {
                tvWordRecogDone.setText("Word recorded!");
            }
        });



        bttFragGestureRec.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putString("cmd", "start_accelerometer");

            getParentFragmentManager().setFragmentResult("recognition_acc_cmd", result);
            tvGestureRecogDone.setText("Recording...");
        });

        getParentFragmentManager().setFragmentResultListener("recognition_gesture_feedback", this, (key, bundle) -> {
            String status = bundle.getString("status");
            Log.i(TAG, "Status: " + status);
            if ("gesture_done".equals(status))
                tvGestureRecogDone.setText("Gesture recorded!");
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
