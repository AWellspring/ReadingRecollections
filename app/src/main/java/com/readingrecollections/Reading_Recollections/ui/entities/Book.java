package com.readingrecollections.Reading_Recollections.ui.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDate;

import com.readingrecollections.Reading_Recollections.ui.database.DateConverter;
import com.readingrecollections.Reading_Recollections.ui.userinterface.Item;

@Entity(foreignKeys = @ForeignKey(entity = Author.class,
        parentColumns = "id",
        childColumns = "authorId",
        onDelete = ForeignKey.CASCADE))
public class Book implements Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;

    private String genre;

    private int authorId;

    @TypeConverters(DateConverter.class)
    private LocalDate dateRead;

    private Boolean isSeries;

    private String seriesName;

    private double seriesNumber;

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

    public Boolean getSeries() {
        return isSeries;
    }

    public void setSeries(Boolean series) {
        isSeries = series;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public double getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(double seriesNumber) {
        this.seriesNumber = seriesNumber;
    }
}
