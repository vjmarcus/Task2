package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.task2.adapter.SongAdapter;
import com.example.task2.data.MyContentProvider;
import com.example.task2.data.SongContract;
import com.example.task2.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MyApp";
    private Spinner chooseAuthorSpinner;
    private Spinner chooseGenreSpinner;
    private RecyclerView recyclerView;
    private List<Song> songs = new ArrayList<>();
    private List<Song> songsFiltered = new ArrayList<>();
    private List<String> authors = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private ArrayAdapter<String> authorsAdapter;
    private String itemSelectedInSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        init();
        loadFromContentResolverToSongsList();
        fillAuthorsFromSongs();
        fillGenresFromSongs();
        setAdapters();
        chooseAuthorSpinner.setOnItemSelectedListener(this);
        chooseGenreSpinner.setOnItemSelectedListener(this);

    }

    private void fillRecycler(String filteredKey, String sortType) {
        songsFiltered.clear();
        switch (sortType) {
            case "genre":
                for (int i = 0; i < songs.size(); i++) {
                    if (songs.get(i).getGenre().equals(filteredKey)) {
                        songsFiltered.add(songs.get(i));
                    }
                }
                break;
            case "author":
                for (int i = 0; i < songs.size(); i++) {
                    if (songs.get(i).getAuthor().equals(filteredKey)) {
                        songsFiltered.add(songs.get(i));
                    }
                }
                break;
        }
        SongAdapter songAdapter = new SongAdapter(songsFiltered);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songAdapter);
    }

    private void printList(List list) {
        for (int i = 0; i < list.size(); i++) {
            Log.d(TAG, "printList: = " + list.get(i).toString());
        }
    }

    private void setAdapters() {
        authorsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, authors);
        authorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseAuthorSpinner.setAdapter(authorsAdapter);
        ArrayAdapter<String> genresAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genres);
        genresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseGenreSpinner.setAdapter(genresAdapter);
    }

    private void init() {
        chooseAuthorSpinner = findViewById(R.id.spinner_choose_author);
        chooseGenreSpinner = findViewById(R.id.spinner_choose_genre);
        recyclerView = findViewById(R.id.recycler);
    }

    private void loadFromContentResolverToSongsList() {
        Cursor cursor = getContentResolver().query(
                MyContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);
        Log.d(TAG, "SecondActivity loadFromContentResolver:");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_TITLE));
            String author = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_AUTHOR));
            String genre = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_GENRE));
            String path = cursor.getString(cursor.getColumnIndex(SongContract.SongsEntry.COLUMN_PATH_TO_FILE));
            Song song = new Song(id, title, author, genre, path);
            songs.add(song);
            Log.d(TAG, "SecondActivity loadFromContentResolver ->: " + song.toString());
        }
    }

    private void fillAuthorsFromSongs() {
        authors.add("Выберите автора");
        for (int i = 0; i < songs.size(); i++) {
            authors.add(songs.get(i).getAuthor());
        }
    }

    private void fillGenresFromSongs() {
        genres.add("Выберите жанр");
        for (int i = 0; i < songs.size(); i++) {
            String genre = songs.get(i).getGenre();
            if (!genres.contains(genre)) {
                genres.add(genre);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        itemSelectedInSpinner = (String) adapterView.getItemAtPosition(i);
        int indexValue = adapterView.getSelectedItemPosition();
        // как добавить все варианты, если они изменяются динамически?
//        switch (itemSelectedInSpinner){
//            case "Выберите автора":
//                Log.d(TAG, "onItemSelected: выберите автора");
//                break;
//            case "Выберите жанр":
//                Log.d(TAG, "onItemSelected: выберите жанр");
//                break;
//            case "Молодежная":
//                fillRecycler("Молодежная");
//                break;
//            case "Танцевальная":
//                fillRecycler("Танцевальная");
//                break;

        Log.d(TAG, "onItemSelected: = " + itemSelectedInSpinner);
        Log.d(TAG, "onItemSelected: = " + indexValue);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //do nothing
    }
}