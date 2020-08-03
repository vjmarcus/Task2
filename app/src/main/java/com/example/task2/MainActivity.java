package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.JobIntentService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyApp";
    private ImageButton playImageButton;
    private ImageButton pauseImageButton;
    private ImageButton stopImageButton;

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
                Log.d(TAG, "onClick: play button");
                startService(new Intent(this, MusicService.class)
                        .setAction(MusicService.ACTION_PLAY));
                break;
            case R.id.pauseImageButton:
                Log.d(TAG, "onClick: pause button");
                startService(new Intent(this, MusicService.class)
                        .setAction(MusicService.ACTION_PAUSE));
                break;
            case R.id.stopImageButton:
                Log.d(TAG, "onClick: stop button");
                stopService(new Intent(this, MusicService.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}