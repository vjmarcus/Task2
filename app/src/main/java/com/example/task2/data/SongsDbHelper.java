package com.example.task2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SongsDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "songsDB";
    private static final int DB_VERSION = 7;

    public SongsDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SongContract.SongsEntry.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SongContract.SongsEntry.DROP_COMMAND);
        onCreate(sqLiteDatabase);
    }
}
