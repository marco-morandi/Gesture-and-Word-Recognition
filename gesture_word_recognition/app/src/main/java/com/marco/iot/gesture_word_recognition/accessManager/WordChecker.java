package com.marco.iot.gesture_word_recognition.accessManager;

import android.util.Log;

import com.marco.iot.gesture_word_recognition.Constants;
import com.marco.iot.gesture_word_recognition.DTW;
import com.marco.iot.gesture_word_recognition.data.WordData;

public class WordChecker {
    private final String TAG = "WordChecker";
    private DTW dtw;

    public WordChecker() {
        dtw = new DTW();
    }

    public boolean match(
            WordData template,
            WordData sample
    ) {

        float[] tAudio = template.getSamples();
        float[] sAudio = sample.getSamples();

        double rawDistance = dtw.compute(tAudio, sAudio).getDistance();

        double normalizedDistance = rawDistance / (tAudio.length + sAudio.length);

        Log.i(TAG, "AUDIO_NORM_DISTANCE = " + normalizedDistance);

        return normalizedDistance < Constants.WORD_THRESHOLD;
    }

}
