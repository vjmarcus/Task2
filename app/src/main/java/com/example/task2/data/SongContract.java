package com.example.task2.data;

import android.provider.BaseColumns;

public class SongContract {
    // if implements baseColumns _id not needed
    public static final class SongsEntry implements BaseColumns {
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_PATH_TO_FILE = "path";

        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "(" + _ID +" INTEGER" + " PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_GENRE + " TEXT, "
                + COLUMN_PATH_TO_FILE + " TEXT" + ")";

        public static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
