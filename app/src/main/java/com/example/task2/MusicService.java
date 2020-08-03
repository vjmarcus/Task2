package com.example.task2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {
    public static final String ACTION_PLAY = "com.example.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.action.PAUSE";
    public static final String ACTION_RESUME = "com.example.action.RESUME";
    public static final String APP_PREFERENCES = "APP_PREFERENCES";
    public static final String APP_PREFERENCES_POSITION = "APP_PREFERENCES_POSITION";
    public static final String TAG = "MyApp";
    private MediaPlayer mediaPlayer = null;
    private int position = 0;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.intergalactic);
        mediaPlayer.setLooping(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            if (intent.getAction().equals(ACTION_PLAY)) {
                Log.d(TAG, "onStartCommand: " + "ACTION_PLAY");
                mediaPlayer = MediaPlayer.create(this, R.raw.intergalactic);
                mediaPlayer.seekTo(position);
                mediaPlayer.start();
            } else if (intent.getAction().equals(ACTION_PAUSE)) {
                Log.d(TAG, "onStartCommand: " + "ACTION_PAUSE");
                mediaPlayer.pause();
                position = mediaPlayer.getCurrentPosition();
            } else if (intent.getAction().equals(ACTION_RESUME)) {
                Log.d(TAG, "onStartCommand: " + "ACTION_RESUME");
                loadFromSharedPref();
                mediaPlayer.seekTo(position);
                mediaPlayer.start();
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
        Log.d(TAG, "saveToSharedPref: = " + position);
    }
    private void loadFromSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        position = sharedPreferences.getInt(APP_PREFERENCES_POSITION, 0 );
        Log.d(TAG, "loadFromSharedPref: = " + position);
    }
}

