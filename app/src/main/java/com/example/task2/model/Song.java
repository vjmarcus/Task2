package com.example.task2.model;

public class Song {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String pathToFile;

    public Song() {
    }

    public Song(String title, String author, String genre, String pathToFile) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pathToFile = pathToFile;
    }

    public Song(int id, String title, String author, String genre, String pathToFile) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pathToFile = pathToFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", pathToFile='" + pathToFile + '\'' +
                '}';
    }
}
