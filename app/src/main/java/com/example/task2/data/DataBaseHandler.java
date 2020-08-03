package com.example.task2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.task2.model.Song;
import com.example.task2.utlis.Util;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "MyApp";
    public DataBaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + Util.TABLE_NAME + "(" +
                Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_TITLE + " TEXT,"
                + Util.KEY_AUTHOR + " TEXT,"
                + Util.KEY_GENRE + " TEXT"
                + Util.KEY_FILE_PATH + " "  + ")");
    }
    //

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addSong(Song song) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_TITLE, song.getTitle());
        contentValues.put(Util.KEY_AUTHOR, song.getAuthor());
        contentValues.put(Util.KEY_GENRE, song.getGenre());
        // TODO put PATH
        db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
    }

    public Song getSong(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID, Util.KEY_TITLE,
                        Util.KEY_AUTHOR, Util.KEY_GENRE, Util.KEY_FILE_PATH}, Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Song song = new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3));
        return song;
    }

    public List<Song> getAllSongs () {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Song> songs = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Util.TABLE_NAME, null);
        if (cursor.moveToFirst()){
            do {
                Song song = new Song();
                song.setId(Integer.parseInt(cursor.getString(0)));
                song.setTitle(cursor.getString(1));
                song.setAuthor(cursor.getString(2));
                song.setGenre(cursor.getString(3));
                song.setPathToFile(cursor.getString(4));
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }
    public static void deleteDatabase(Context context) {
        context.deleteDatabase(Util.TABLE_NAME);
    }
}
