package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.task2.data.MyContentProvider;
import com.example.task2.data.SongContract;
import com.example.task2.data.SongsDbHelper;
import com.example.task2.model.Song;

import java.util.ArrayList;
import java.util.Currency;
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

        addSongToDb();
//        loadSongFromDb();
        loadFromContentResolver();

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        init();
        setOnClickListener();
        loadFromSharedPref();
        if (wasPlayed) {
            resumePlayMusic();
        }
        printSounds();
//        DataBaseHandler dataBaseHandler = new DataBaseHandler(this);
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
    private void printSounds() {
        for (int i = 0; i < songs.size(); i++) {
            Log.d(TAG, "printSounds: " + songs.get(i).getPathToFile());
        }
    }

    private void addSongToDb() {
        song = new Song("asdfsdf", "asdfsdff", "Танцевальная",
                Uri.parse("android.resource://" + getPackageName() + "/raw/raim").toString());
        songs.add(song);
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
        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_AUTHOR));
            String genre = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_GENRE));
            String path = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_PATH_TO_FILE));
            Song song = new Song(title, author, genre, path);
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

            String title = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_AUTHOR));
            String genre = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_GENRE));
            String path = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_PATH_TO_FILE));
            Song song = new Song(title, author, genre, path);
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
                            .setAction(MusicService.ACTION_PLAY).putExtra("song",
                                    songs.get(songs.size() - 1).getPathToFile()));
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
        wasPlayed = sharedPreferences.getBoolean(APP_PREFERENCES_PLAYED, true);
        Log.d(TAG, "loadFromSharedPref: = " + wasPlayed);
    }
}