package com.marco.iot.gesture_word_recognition.recorder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.marco.iot.gesture_word_recognition.data.WordData;
import com.marco.iot.gesture_word_recognition.interfaces.IAccelerometer;
import com.marco.iot.gesture_word_recognition.interfaces.IRecorder;
import com.marco.iot.gesture_word_recognition.interfaces.ISensor;

public class Recorder implements ISensor {
    private final String TAG = "Recorder";

    private int fsInHz;
    private int nSamples;
    private int recordingLengthInSec;

    private short[] audioData;

    private AudioRecord audioRecord = null;

    private Context context;
    private IRecorder iRecorder;


    public Recorder(Context context, int fsInHz, int recordingLengthInSec) {
        Log.i(TAG, "Recorder()");

        this.fsInHz = fsInHz;
        this.recordingLengthInSec = recordingLengthInSec;

        this.nSamples = fsInHz * this.recordingLengthInSec;

        this.context = context;
        iRecorder = (IRecorder) context;
    }

    public void start() {
        new Thread( () -> {

            initRecorder();
            doRecording();
            releaseRecorder();
            float[] audioDataToFloat = audioSamplesConvertionToFloat(audioData);
            WordData wordData = new WordData(audioDataToFloat, fsInHz);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> iRecorder.onRecordingDone(wordData));

        }).start();
    }

    public void stop() {
        audioRecord.stop();
        releaseRecorder();
    }

    private void initRecorder() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, fsInHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, 2 * nSamples);
        audioData = new short[nSamples];
    }

    private void releaseRecorder() {
        audioRecord.release();
        audioRecord = null;
    }



    private void doRecording() {
        Log.i(TAG, "Start recording...");

        audioRecord.startRecording();
        audioRecord.read(audioData, 0, nSamples);

        Log.i(TAG, "Recording done!");
    }

    private float[] audioSamplesConvertionToFloat(short[] input) {
        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (float) input[i] / 32768.0f;
        }
        return output;
    }

}
