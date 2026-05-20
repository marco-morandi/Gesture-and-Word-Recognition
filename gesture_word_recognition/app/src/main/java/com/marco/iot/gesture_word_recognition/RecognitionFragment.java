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

        });
    }


}
