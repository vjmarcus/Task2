package com.example.task2.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SongsDb {

    private static final String TAG = "MyApp";
    private static final String DB_NAME = "songsDB";
    private static final int DB_VERSION = 1;
    private static final String SONG_TABLE = "songs";
    private static final String SONG_ID = "_id";
    private static final String SONG_TITLE = "title";
    private static final String SONG_AUTHOR = "author";
    private static final String SONG_GENRE = "genre";
    private static final String SONG_PATH_TO_FILE = "path";

    private static final String TABLE_CREATE = "CREATE TABLE " + SONG_TABLE + "(" +
            SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SONG_TITLE + " TEXT, " +
            SONG_AUTHOR + " TEXT, " +
            SONG_GENRE + " TEXT, " +
            SONG_PATH_TO_FILE + "TEXT " + ");";

    public static void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: TABLE_CREATE");
        db.execSQL(TABLE_CREATE);
    }
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade db");
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }
}
