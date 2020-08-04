package com.example.task2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    private static final String TAG = "MyApp";
    private SongsDbHelper songsDbHelper;
    private static final int SINGLE_SONG = 1;
    private static final int ALL_SONGS = 2;
    private static final String AUTHORITY = "com.example.task2.data";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/songs");

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "songs", ALL_SONGS);
        uriMatcher.addURI(AUTHORITY, "songs/#", SINGLE_SONG);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // DO NOTHING
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // DO NOTHING
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // DO NOTHING
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        songsDbHelper = new SongsDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = songsDbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SongContract.SongsEntry.TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case ALL_SONGS:
                Log.d(TAG, "query: ALL SONGS " );
                break;
            case SINGLE_SONG:
                Log.d(TAG, "query: SINGLE_SONG");
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // DO NOTHING
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
