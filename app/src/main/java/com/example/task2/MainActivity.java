package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task2.data.MyContentProvider;
import com.example.task2.data.SongContract;
import com.example.task2.data.SongsDbHelper;
import com.example.task2.model.Song;
import com.example.task2.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String BROADCAST_ACTION = "com.example.task2.broadcast";
    public static final String SONG_TITLE = "title";
    public static final String SONG_AUTHOR = "author";
    public static final String SONG_GENRE = "genre";
    public static final String SONG_PATH = "path";
    private static final String APP_PREFERENCES = "APP_PREFERENCES";
    private static final String TAG = "MyApp";

    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Button chooseAuthorButton;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView genreTextView;
    private boolean isPlay;

    private List<Song> songs = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private String songTitle;
    private String songAuthor;
    private String songGenre;
    private String songPath;

    @Override
    protected void onStart() {
        super.onStart();
//        initBroadcastReceiver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SongsDbHelper dbHelper = new SongsDbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        init();
        setOnClickListener();
        Utils.loadFromSharedPref(sharedPreferences);

        if (isRestorePlay()) {
            restorePlayMusic();
            titleTextView.setText(songTitle);
            genreTextView.setText(songGenre);
            authorTextView.setText(songAuthor);
        }

        loadFromContentResolver();
        initBroadcastReceiver();
    }

    private Boolean isRestorePlay() {
        return Utils.restorePlay;
    }

    private void initBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                songTitle = intent.getStringExtra(SONG_TITLE);
                songAuthor = intent.getStringExtra(SONG_AUTHOR);
                songGenre = intent.getStringExtra(SONG_GENRE);
                songPath = intent.getStringExtra(SONG_PATH);
                Log.d(TAG, "onReceive: = " + songTitle + ", "
                        + songAuthor + ", " + songGenre + ", " + songPath);
                titleTextView.setText(songTitle);
                authorTextView.setText(songAuthor);
                genreTextView.setText(songGenre);
                stopService(new Intent(getApplicationContext(), MusicService.class));
                isPlay = false;
                Utils.restorePlay = false;
            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void loadFromContentResolver() {
        Cursor cursor = getContentResolver().query(
                MyContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(SongContract.COLUMN_ID));
            songTitle = cursor.getString(cursor.getColumnIndex(SongContract.COLUMN_TITLE));
            songAuthor = cursor.getString(cursor.getColumnIndex(SongContract.COLUMN_AUTHOR));
            songGenre = cursor.getString(cursor.getColumnIndex(SongContract.COLUMN_GENRE));
            songPath = cursor.getString(cursor.getColumnIndex(SongContract.COLUMN_PATH_TO_FILE));
            Song song = new Song(id, songTitle, songAuthor, songGenre, songPath);
            songs.add(song);
        }
    }

    private void resumePlayMusic() {
        Log.d(TAG, "onClick: pause button");
        startForegroundService(new Intent(this, MusicService.class)
                .setAction(MusicService.ACTION_RESUME));
        isPlay = true;
    }

    private void restorePlayMusic() {
        Log.d(TAG, "onClick: pause button");
        startForegroundService(new Intent(this, MusicService.class)
                .setAction(MusicService.ACTION_RESTORE));
        isPlay = true;
    }

    private void setOnClickListener() {
        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        chooseAuthorButton.setOnClickListener(this);
    }

    private void init() {
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        chooseAuthorButton = findViewById(R.id.chooseAuthorButton);
        titleTextView = findViewById(R.id.titleTextView);
        authorTextView = findViewById(R.id.authorTextView);
        genreTextView = findViewById(R.id.genreTextView);
    }

    @Override
    public void onClick(View view) {
        // load song from sharedPref, if null - choose song!
        if (songPath == null && view.getId() != R.id.chooseAuthorButton) {
            Toast.makeText(this, "Choose song!", Toast.LENGTH_SHORT).show();
        } else {
            switch (view.getId()) {
                case R.id.playButton:
                    if (!isPlay) {
                        updatePlayMusic();
                    }
                    break;
                case R.id.pauseButton:
                    if (isPlay) {
                        pausePlayMusic();
                    }
                    break;
                case R.id.stopButton:
                    Log.d(TAG, "onClick: stop button");
                    isPlay = false;
                    Utils.restorePlay = false;
                    titleTextView.setText("Выберите песню");
                    authorTextView.setText("");
                    genreTextView.setText("");
                    startForegroundService(new Intent(this, MusicService.class)
                            .setAction(MusicService.ACTION_STOP));
                    break;
                case R.id.chooseAuthorButton:
                    Log.d(TAG, "onClick: choose author button");
                    Intent intent = new Intent(this, SecondActivity.class);
                    startActivity(intent);
            }
        }
    }

    private void updatePlayMusic() {
        if (Utils.restorePlay) {
            resumePlayMusic();
        } else {
            startNewPlayMusic();
        }
    }

    private void pausePlayMusic() {
        Log.d(TAG, "onClick: pause button");
        startForegroundService(new Intent(this, MusicService.class)
                .setAction(MusicService.ACTION_PAUSE));
        isPlay = false;
        Utils.restorePlay = true;
    }

    private void startNewPlayMusic() {
        Log.d(TAG, "onClick: play button");
        startForegroundService(new Intent(this, MusicService.class)
                .setAction(MusicService.ACTION_PLAY).putExtra("song",
                        songPath));
        isPlay = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, MusicService.class));
        Utils.saveToSharedPref(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }
}