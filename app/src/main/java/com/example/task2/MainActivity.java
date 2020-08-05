package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private boolean isPlay;
    private boolean wasPlayed;
    private SharedPreferences sharedPreferences;
    private SongsDbHelper dbHelper;
    private List<Song> songs = new ArrayList<>();
    private Song song;
    private SQLiteDatabase database;


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
        }
        printSounds();
    }

    private void printSounds() {
        for (int i = 0; i < songs.size(); i++) {
            Log.d(TAG, "printSounds: " + songs.get(i).getPathToFile());
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
        loadFromContentResolver();
    }

    private void addSongToDb(Song song) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongContract.SongsEntry.COLUMN_TITLE, song.getTitle());
        contentValues.put(SongContract.SongsEntry.COLUMN_AUTHOR, song.getAuthor());
        contentValues.put(SongContract.SongsEntry.COLUMN_GENRE, song.getGenre());
        contentValues.put(SongContract.SongsEntry.COLUMN_PATH_TO_FILE, song.getPathToFile());
        database.insert(SongContract.SongsEntry.TABLE_NAME, null, contentValues);
    }

    private void loadSongFromDb() {
        Cursor cursor = database.query(SongContract.SongsEntry.TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_AUTHOR));
            String genre = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_GENRE));
            String path = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_PATH_TO_FILE));
            Song song = new Song(id, title, author, genre, path);
            Log.d(TAG, "loadSongFromDb: " + song.toString());
        }
        cursor.close();
    }

    private void loadFromContentResolver() {
        Cursor cursor = getContentResolver().query(
                MyContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_AUTHOR));
            String genre = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_GENRE));
            String path = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_PATH_TO_FILE));
            Song song = new Song(id, title, author, genre, path);
            songs.add(song);
            Log.d(TAG, "loadFromContentResolver!!!: " + song.toString());
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
    }

    private void init() {
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playButton:
                if (!isPlay) {
                    Log.d(TAG, "onClick: play button");
                    startService(new Intent(this, MusicService.class)
                            .setAction(MusicService.ACTION_PLAY).putExtra("song",
                                    songs.get(songs.size() - 1).getPathToFile()));
                    isPlay = true;
                }
                break;
            case R.id.pauseButton:
                if (isPlay) {
                    Log.d(TAG, "onClick: pause button");
                    startService(new Intent(this, MusicService.class)
                            .setAction(MusicService.ACTION_PAUSE));
                    isPlay = false;
                }
                break;
            case R.id.stopButton:
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
        wasPlayed = sharedPreferences.getBoolean(APP_PREFERENCES_PLAYED, true);
        Log.d(TAG, "loadFromSharedPref: = " + wasPlayed);
    }
}