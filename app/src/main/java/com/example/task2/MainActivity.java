package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.JobIntentService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyApp";
    private static final String WAS_PLAYED = "was_played";
    private ImageButton playImageButton;
    private ImageButton pauseImageButton;
    private ImageButton stopImageButton;
    private boolean isPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setOnClickListener();
    }

    private void setOnClickListener() {
        playImageButton.setOnClickListener(this);
        pauseImageButton.setOnClickListener(this);
        stopImageButton.setOnClickListener(this);
    }

    private void init() {
        playImageButton = findViewById(R.id.playImageButton);
        pauseImageButton = findViewById(R.id.pauseImageButton);
        stopImageButton = findViewById(R.id.stopImageButton);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playImageButton:
                if (!isPlay) {
                    Log.d(TAG, "onClick: play button");
                    startService(new Intent(this, MusicService.class)
                            .setAction(MusicService.ACTION_PLAY));
                    isPlay = true;
                }
                break;
            case R.id.pauseImageButton:
                if (isPlay) {
                    Log.d(TAG, "onClick: pause button");
                    startService(new Intent(this, MusicService.class)
                            .setAction(MusicService.ACTION_PAUSE));
                    isPlay = false;
                }
                break;
            case R.id.stopImageButton:
                Log.d(TAG, "onClick: stop button");
                stopService(new Intent(this, MusicService.class));
                isPlay = false;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
        saveToSharedPref();
    }

    private void saveToSharedPref() {
        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences(WAS_PLAYED, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(WAS_PLAYED, isPlay);
        editor.apply();
        Log.d(TAG, "saveToSharedPref main: = " + isPlay);
    }
}