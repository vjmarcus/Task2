package com.example.task2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.util.Objects;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {
    public static final String ACTION_PLAY = "com.example.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.action.PAUSE";
    public static final String ACTION_RESUME = "com.example.action.RESUME";
    public static final String ACTION_RESTORE = "com.example.action.RESTORE";
    public static final String APP_PREFERENCES = "APP_PREFERENCES";
    public static final String APP_PREFERENCES_POSITION = "APP_PREFERENCES_POSITION";
    public static final String TAG = "MyApp";
    private MediaPlayer mediaPlayer = null;
    private int position = 0;
    private String songPath;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadFromSharedPref();
        Uri uri = Uri.parse(songPath);
        mediaPlayer = MediaPlayer.create(this, uri);
        Log.d(TAG, "onCreate: URI = " + uri.toString());
        mediaPlayer.setLooping(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (Objects.requireNonNull(intent.getAction())) {
            case ACTION_PLAY:
                Log.d(TAG, "onStartCommand: " + "ACTION_PLAY");
                Uri songUri = Uri.parse(intent.getStringExtra("song"));
                Log.d(TAG, "onStartCommand: songUri = " + songUri.toString());
                mediaPlayer = MediaPlayer.create(this, songUri);
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                break;
            case ACTION_PAUSE:
                Log.d(TAG, "onStartCommand: " + "ACTION_PAUSE");
                mediaPlayer.pause();
                position = mediaPlayer.getCurrentPosition();
                break;
            case ACTION_RESUME:
                Log.d(TAG, "onStartCommand: " + "ACTION_RESUME");
                mediaPlayer.start();
                break;
            case ACTION_RESTORE:
                Log.d(TAG, "onStartCommand: " + "ACTION_RESTORE");
                loadFromSharedPref();
                mediaPlayer.seekTo(position);
                mediaPlayer.start();
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.stop();
        Log.d(TAG, "onDestroy: stop");
        position = mediaPlayer.getCurrentPosition();
        saveToSharedPref();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    private void saveToSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_POSITION, position);
        editor.apply();
        Log.d(TAG, "saveToSharedPref service: = " + position);
    }

    private void loadFromSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        position = sharedPreferences.getInt(APP_PREFERENCES_POSITION, 0);
        songPath = sharedPreferences.getString(MainActivity.SONG_PATH, null);
        Log.d(TAG, "loadFromSharedPref service: = " + position);
    }
}

