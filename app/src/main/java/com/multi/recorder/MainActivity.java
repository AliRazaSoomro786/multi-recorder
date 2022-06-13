package com.multi.recorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final static int PERMISSION_CODE = 7566;

    private MediaRecorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File root = new File(getExternalFilesDir("") + "/multiRecorder");

        findViewById(R.id.actionStartRecording).setOnClickListener(v -> {
            if (!granted()) {
                Log.d(TAG, "Please allow permissions");
                requestPermissions(PERMISSIONS, PERMISSION_CODE);
                return;
            }

            if (!root.exists() && !root.mkdirs()) {
                Log.d(TAG, "Folder creating failed.");
                return;
            }

            File mFilePath = new File(root.getAbsolutePath() + "/test.3gp");

            if (mFilePath.exists()) Log.d(TAG, "file Delete Status : " + mFilePath.delete());

            try {
                Log.d(TAG, "FileCreated Status : " + mFilePath.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            Log.d(TAG, "Assigned AudioSource");

            try {
                Log.d(TAG, "onCreate: " + mRecorder.getActiveMicrophones().size());
            } catch (IOException e) {
                e.printStackTrace();
            }

            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mFilePath.getAbsolutePath());
            Log.d(TAG, "FilePath Assigned to recorder " + mFilePath.getAbsolutePath());

            try {
                mRecorder.prepare();
                mRecorder.start();
                Log.d(TAG, "Starting media recorder");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Failed to start media recorder", e);
            }


        });

        findViewById(R.id.actionStopRecording).setOnClickListener(v -> {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                Log.d(TAG, "Recorder stop");
            } else {
                Log.w(TAG, "Trying to stop null media recorder");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!granted()) requestPermissions(PERMISSIONS, 12030);
    }

    public boolean granted() {
        for (String per : PERMISSIONS)
            if (checkSelfPermission(per) == PackageManager.PERMISSION_GRANTED) return true;

        return false;
    }
}