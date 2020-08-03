package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.task2.data.DataBaseHandler;
import com.example.task2.model.Song;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyApp";
    private static final String APP_PREFERENCES_PLAYED = "APP_PREFERENCES_PLAYED";
    private static final String APP_PREFERENCES = "APP_PREFERENCES";
    private ImageButton playImageButton;
    private ImageButton pauseImageButton;
    private ImageButton stopImageButton;
    private boolean isPlay;
    private boolean wasPlayed;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        init();
        setOnClickListener();
        loadFromSharedPref();
        if (wasPlayed) {
            resumePlayMusic();
        }
        DataBaseHandler dataBaseHandler = new DataBaseHandler(this);
//        dataBaseHandler.deleteDatabase(this);
//        dataBaseHandler.addSong(new Song("Плачу на техно", "Cream Soda feat. ХЛЕБ",
//                "dance",  "android.resource://" + getPackageName() + "/" + R.raw.creamsoda));
//        dataBaseHandler.addSong(new Song("Hypnodancer", " Little Big", "dance",
//                "android.resource://" + getPackageName() + "/" + R.raw.littlebig));
//        dataBaseHandler.addSong(new Song("Lovefool", "twocolors", "dance",
//                "android.resource://" + getPackageName() + "/" + R.raw.twocolors));
//        dataBaseHandler.addSong(new Song("Shut Up Chicken", "El Capon", "Хип-хоп",
//                "android.resource://" + getPackageName() + "/" + R.raw.elcapon));
//        dataBaseHandler.addSong(new Song("Relax", "Junona Boys", "Хип-хоп",
//                "android.resource://" + getPackageName() + "/" + R.raw.junonaboys));
//        dataBaseHandler.addSong(new Song("Fly 2", "Zivert feat. NILETTO", "Хип-хоп",
//                "android.resource://" + getPackageName() + "/" + R.raw.zivert));
//        dataBaseHandler.addSong(new Song("Breaking Me", "Topic feat. A7S", "Евродэнс",
//                "android.resource://" + getPackageName() + "/" + R.raw.topic));
//        dataBaseHandler.addSong(new Song("Луна не знает пути", "Тайпан feat. Agunda", "Евродэнс",
//                "android.resource://" + getPackageName() + "/" + R.raw.taypan_gunda));
//        dataBaseHandler.addSong(new Song("Me Provocas", "Dynoro feat. Fumaratto", "Евродэнс",
//                "android.resource://" + getPackageName() + "/" + R.raw.dynoro));
//        dataBaseHandler.addSong(new Song("Двигаться", "G RaiM ", "Евродэнс",
//                "android.resource://" + getPackageName() + "/" + R.raw.raim));
//        List<Song> songs = dataBaseHandler.getAllSongs();
//        for (int i = 0; i < songs.size(); i++) {
//            Log.d(TAG, "onCreate: = " + songs.get(i).toString());
//        }
    }

    private void resumePlayMusic() {
        Log.d(TAG, "onClick: pause button");
        startService(new Intent(this, MusicService.class)
                .setAction(MusicService.ACTION_RESUME));
        isPlay = true;
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
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_PLAYED, isPlay);
        editor.apply();
        Log.d(TAG, "saveToSharedPref main: = " + isPlay);
    }

    private void loadFromSharedPref() {
        wasPlayed = sharedPreferences.getBoolean(APP_PREFERENCES_PLAYED, true );
        Log.d(TAG, "loadFromSharedPref: = " + wasPlayed);
    }
}