package com.example.task2.data;

public class SongContract {
    public static final String TABLE_NAME = "songs";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_PATH_TO_FILE = "path";

    public static final String CREATE_COMMAND = "CREATE TABLE " + TABLE_NAME
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_AUTHOR + " TEXT, "
            + COLUMN_GENRE + " TEXT, "
            + COLUMN_PATH_TO_FILE + " TEXT" + ")";
}
