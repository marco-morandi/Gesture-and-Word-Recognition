package com.marco.iot.gesture_word_recognition.accessManager;

import com.marco.iot.gesture_word_recognition.data.GestureData;
import com.marco.iot.gesture_word_recognition.data.WordData;

public class AccessChecker {

    private WordChecker wordChecker;
    private GestureChecker gestureChecker;

    public AccessChecker() {

        wordChecker = new WordChecker();
        gestureChecker = new GestureChecker();
    }

    public boolean authenticate(
            WordData templateWord,
            WordData sampleWord,
            GestureData templateGesture,
            GestureData sampleGesture
    ) {

        boolean audioOk =
                wordChecker.match(
                        templateWord,
                        sampleWord
                );

        boolean gestureOk =
                gestureChecker.match(
                        templateGesture,
                        sampleGesture
                );

        return audioOk && gestureOk;
    }
}
