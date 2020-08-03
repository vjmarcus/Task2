package com.example.task2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {
    public static final String ACTION_PLAY = "com.example.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.action.PAUSE";
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
            }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.stop();
        Log.d(TAG, "onDestroy: stop");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}

