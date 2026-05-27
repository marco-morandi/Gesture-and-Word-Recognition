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

import com.marco.iot.gesture_word_recognition.interfaces.INewDataAvailable;

public class Recorder {
    private final String TAG = "Recorder";

    private int fsInHz;
    private int nSamples;
    private int recordingLengthInSec;

    private short[] audioData;

    private AudioRecord audioRecord = null;

    private Context context;
    private INewDataAvailable iRecording;


    public Recorder(Context context, int fsInHz, int recordingLengthInSec) {
        Log.i(TAG, "Recorder()");

        this.fsInHz = fsInHz;
        this.recordingLengthInSec = recordingLengthInSec;

        this.nSamples = fsInHz * this.recordingLengthInSec;

        this.context = context;
        iRecording = (INewDataAvailable) context;
    }

    public void start() {
        new Thread( () -> {

            initRecorder();
            doRecording();
            releaseRecorder();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> iRecording.onRecordingDone(audioData));

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

}
