package com.example.task2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.task2.util.Utils;

import java.util.Objects;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {
    public static final String ACTION_PLAY = "com.example.action.PLAY";
    public static final String ACTION_PAUSE = "com.example.action.PAUSE";
    public static final String ACTION_RESUME = "com.example.action.RESUME";
    public static final String ACTION_RESTORE = "com.example.action.RESTORE";
    public static final String ACTION_STOP = "com.example.action.STOP";
    public static final String APP_PREFERENCES = "APP_PREFERENCES";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final String TAG = "MyApp";
    private MediaPlayer mediaPlayer = null;
    private String songPath;
    private SharedPreferences sharedPreferences;

    public MusicService() {
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Utils.loadToSharePrefFromService(sharedPreferences);
        Uri uri = Uri.parse(Utils.songPath);
        mediaPlayer = MediaPlayer.create(this, uri);
        Log.d(TAG, "onCreate: URI = " + uri.toString());
        mediaPlayer.setLooping(false);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        createForegroundService();
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
                Utils.position = mediaPlayer.getCurrentPosition();
                break;
            case ACTION_RESUME:
                Log.d(TAG, "onStartCommand: " + "ACTION_RESUME");
                mediaPlayer.start();
                break;
            case ACTION_RESTORE:
                Log.d(TAG, "onStartCommand: " + "ACTION_RESTORE");
                Utils.loadToSharePrefFromService(sharedPreferences);
                mediaPlayer.seekTo(Utils.position);
                mediaPlayer.start();
                break;
            case ACTION_STOP:
                Log.d(TAG, "onStartCommand: " + "ACTION_STOP");
                stopForeground(true);
                stopSelf();
                break;
        }
        return START_STICKY;
    }

    private void createForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("text text")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) mediaPlayer.stop();
        Log.d(TAG, "onDestroy: stop");
        Utils.position = mediaPlayer.getCurrentPosition();
        Utils.saveToSharedPref(getApplicationContext());
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}

