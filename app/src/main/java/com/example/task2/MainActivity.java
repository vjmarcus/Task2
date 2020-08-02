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
    //https://habr.com/ru/post/339416/
//https://startandroid.ru/ru/uroki/vse-uroki-spiskom/161-urok-96-service-obratnaja-svjaz-s-pomoschju-broadcastreceiver.html
    private static final String TAG = "MyApp";
    public final static String BROADCAST_ACTION = "com.example.task2.broadcast";
    private ImageButton playImageButton;
    private ImageButton pauseImageButton;
    private ImageButton stopImageButton;
    private MediaPlayer mediaPlayer;
    private Boolean isPlayed;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setOnClickListener();
        getResources().openRawResource(R.raw.intergalactic);
        isPlayed = false;
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int task = intent.getIntExtra("task", 0);
                Log.d(TAG, "onReceive: = " + task);
                if (task == 1) {
                    Log.d(TAG, "onReceive: WORKING!");
                }

            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(broadcastReceiver, intentFilter);
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
                startService(new Intent(this, MusicService.class));
                break;
            case R.id.pauseImageButton:
                Intent intent = new Intent(this, MusicService.class).
                        putExtra("task", 1);
                sendBroadcast(intent);
                break;
            case R.id.stopImageButton:

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}