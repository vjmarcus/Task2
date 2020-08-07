package com.example.task2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class MyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.task2.data";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/songs");
    private static final String TAG = "MyApp";
    private static final int SINGLE_SONG = 1;
    private static final int ALL_SONGS = 2;
    private static final UriMatcher uriMatcher;
    private SongsDbHelper songsDbHelper;

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
        queryBuilder.setTables(SongContract.TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case ALL_SONGS:
                Log.d(TAG, "query: ALL SONGS ");
                break;
            case SINGLE_SONG:
                Log.d(TAG, "query: SINGLE_SONG");
                break;
            default:
                Toast.makeText(getContext(), "ERROR uriMather", Toast.LENGTH_SHORT).show();
        }
        return queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // DO NOTHING
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
