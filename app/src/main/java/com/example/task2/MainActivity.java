package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyApp";
    private static final String APP_PREFERENCES_PLAYED = "APP_PREFERENCES_PLAYED";
    private static final String APP_PREFERENCES = "APP_PREFERENCES";
    public static final String BROADCAST_ACTION = "com.example.task2.broadcast";
    public static final String SONG_TITLE = "title";
    public static final String SONG_AUTHOR = "author";
    public static final String SONG_GENRE = "genre";
    public static final String SONG_PATH = "path";
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Button chooseAuthorButton;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView genreTextView;
    private boolean isPlay;
    private boolean wasPlayed;
    private SharedPreferences sharedPreferences;
    private SongsDbHelper dbHelper;
    private List<Song> songs = new ArrayList<>();
    private SQLiteDatabase database;
    private BroadcastReceiver broadcastReceiver;
    private String songTitle;
    private String songAuthor;
    private String songGenre;
    private String songPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new SongsDbHelper(this);
        database = dbHelper.getWritableDatabase();
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        init();
        setOnClickListener();
        loadFromSharedPref();
        if (wasPlayed) {
            resumePlayMusic();
            titleTextView.setText(songTitle);
            genreTextView.setText(songGenre);
            authorTextView.setText(songAuthor);
        }
        loadFromContentResolver();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra(SONG_TITLE);
                String author = intent.getStringExtra(SONG_AUTHOR);
                String genre = intent.getStringExtra(SONG_GENRE);
                songPath = intent.getStringExtra(SONG_PATH);
                Log.d(TAG, "onReceive: = " + title + ", "
                        + author + ", " + genre + ", " + songPath);
                titleTextView.setText(title);
                authorTextView.setText(author);
                genreTextView.setText(genre);
                stopService(new Intent(getApplicationContext(), MusicService.class));
                isPlay = false;
            }
        };
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

//        addSongToDb(song = new Song("Двигаться", "Raim", "Молодежная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/raim").toString()));
//        addSongToDb(song = new Song("MAMACITA", "Black Eyed Peas", "Танцевальная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/mamasita").toString()));
//        addSongToDb(song = new Song("HYPNODANCER", "LITTLE BIG - HYPNODANCER", "Танцевальная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/littlebig").toString()));
//        addSongToDb(song = new Song("Lovefool", "twocolors", "Танцевальная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/twocolors").toString()));
//        addSongToDb(song = new Song("El Capon", "El Capon", "Танцевальная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/elcapon").toString()));
//        addSongToDb(song = new Song("Relax", "Junona Boys", "Танцевальная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/junonaboys").toString()));
//        addSongToDb(song = new Song("Fly 2", ". Zivert x NILETTO", "Молодежная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/zivert").toString()));
//        addSongToDb(song = new Song("Topic A7S", "Breaking Me ft. A7", "Молодежная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/topic").toString()));
//        addSongToDb(song = new Song("Луна не знает пути", "ТАЙПАН & Agunda", "Молодежная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/taypan_gunda").toString()));
//        addSongToDb(song = new Song("Dynoro & Fumaratto", "Me Provocas", "Танцевальная",
//                Uri.parse("android.resource://" + getPackageName() + "/raw/dynoro").toString()));

    private void loadFromContentResolver() {
        Cursor cursor = getContentResolver().query(
                MyContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_ID));
            songTitle = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_TITLE));
            songAuthor = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_AUTHOR));
            songGenre = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_GENRE));
            songPath = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_PATH_TO_FILE));
            Song song = new Song(id, songTitle, songAuthor, songGenre, songPath);
            songs.add(song);
//            Log.d(TAG, "loadFromContentResolver!!!: " + song.toString());
        }
    }

    private void resumePlayMusic() {
        Log.d(TAG, "onClick: pause button");
        startService(new Intent(this, MusicService.class)
                .setAction(MusicService.ACTION_RESUME));
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
                        if (wasPlayed) {
                            resumePlayMusic();
                        } else {
                            startNewPlayMusic();
                        }
                    }
                    break;
                case R.id.pauseButton:
                    if (isPlay) {
                        pausePlayMusic();
                    }
                    break;
                case R.id.stopButton:
                    Log.d(TAG, "onClick: stop button");
                    stopService(new Intent(this, MusicService.class));
                    isPlay = false;
                    wasPlayed = false;
                    titleTextView.setText("Выберите песню");
                    authorTextView.setText("");
                    genreTextView.setText("");
                    break;
                case R.id.chooseAuthorButton:
                    Log.d(TAG, "onClick: choose author button");
                    Intent intent = new Intent(this, SecondActivity.class);
                    startActivity(intent);
            }
        }
    }

    private void pausePlayMusic() {
        Log.d(TAG, "onClick: pause button");
        startService(new Intent(this, MusicService.class)
                .setAction(MusicService.ACTION_PAUSE));
        isPlay = false;
        wasPlayed = true;
    }

    private void startNewPlayMusic() {
        Log.d(TAG, "onClick: play button");
        startService(new Intent(this, MusicService.class)
                .setAction(MusicService.ACTION_PLAY).putExtra("song",
                        songPath));
        isPlay = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MusicService.class));
        saveToSharedPref();
        unregisterReceiver(broadcastReceiver);
    }

    private void saveToSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_PLAYED, isPlay);
        editor.putString(SONG_TITLE, songTitle);
        editor.putString(SONG_AUTHOR, songAuthor);
        editor.putString(SONG_GENRE, songGenre);
        editor.putString(SONG_PATH, songPath);
        editor.apply();
        Log.d(TAG, "saveToSharedPref main: = " + isPlay);
    }

    private void loadFromSharedPref() {
        wasPlayed = sharedPreferences.getBoolean(APP_PREFERENCES_PLAYED, true);
        songTitle = sharedPreferences.getString(SONG_TITLE, null);
        songAuthor = sharedPreferences.getString(SONG_AUTHOR, null);
        songGenre = sharedPreferences.getString(SONG_GENRE, null);
        songPath = sharedPreferences.getString(SONG_PATH, null);
        Log.d(TAG, "loadFromSharedPref: wasPlayed = " + wasPlayed);
    }
}