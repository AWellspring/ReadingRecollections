package com.readingrecollections.d424_software_engineering_capstone.ui.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "author")
public class Author {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String authorName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
