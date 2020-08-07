package com.example.task2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Utils {
    public static final String SONG_TITLE = "title";
    public static final String SONG_AUTHOR = "author";
    public static final String SONG_GENRE = "genre";
    public static final String SONG_PATH = "path";
    public static final String MOLOD = "Молодежная";
    public static final String TANCEV = "Танцевальная";
    public static String songTitle;
    public static String songAuthor;
    public static String songGenre;
    public static String songPath;
    public static boolean isPlay;
    public static boolean restorePlay;
    public static final String APP_PREFERENCES_PLAYED = "APP_PREFERENCES_PLAYED";
    public static final String APP_PREFERENCES = "APP_PREFERENCES";

    public static void saveToSharedPref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_PLAYED, isPlay);
        editor.putString(SONG_TITLE, songTitle);
        editor.putString(SONG_AUTHOR, songAuthor);
        editor.putString(SONG_GENRE, songGenre);
        editor.putString(SONG_PATH, songPath);
        editor.apply();
    }
    public static void loadFromSharedPref(SharedPreferences sharedPreferences) {
            restorePlay = sharedPreferences.getBoolean(APP_PREFERENCES_PLAYED, true);
            songTitle = sharedPreferences.getString(SONG_TITLE, null);
            songAuthor = sharedPreferences.getString(SONG_AUTHOR, null);
            songGenre = sharedPreferences.getString(SONG_GENRE, null);
            songPath = sharedPreferences.getString(SONG_PATH, null);
    }
}
