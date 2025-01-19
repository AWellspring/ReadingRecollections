package com.readingrecollections.d424_software_engineering_capstone.ui.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;

import com.readingrecollections.d424_software_engineering_capstone.ui.database.DateConverter;

@Entity(foreignKeys = @ForeignKey(entity = Author.class,
        parentColumns = "id",
        childColumns = "authorId",
        onDelete = ForeignKey.CASCADE))
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;

    private String genre;

    private int authorId;

    @TypeConverters(DateConverter.class)
    private LocalDate dateRead;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public LocalDate getDateRead() {
        return dateRead;
    }

    public void setDateRead(LocalDate dateRead) {
        this.dateRead = dateRead;
    }
}
