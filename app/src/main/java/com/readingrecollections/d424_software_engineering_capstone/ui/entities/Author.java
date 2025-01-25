package com.readingrecollections.d424_software_engineering_capstone.ui.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "author")
public class Author {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String authorFirstName;

    private String authorMiddleName;

    private String authorLastName;

    private String authorFullName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorMiddleName() {
        return authorMiddleName;
    }

    public void setAuthorMiddleName(String authorMiddleName) {
        this.authorMiddleName = authorMiddleName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    // Automatically updates authorFullName based on the other fields
    public void updateFullName() {
        StringBuilder fullNameBuilder = new StringBuilder();

        // Ensures firstName is not null or empty
        // Adds firstName to name builder
        if (authorFirstName != null && !authorFirstName.isEmpty()) {
            fullNameBuilder.append(authorFirstName);
        }
        // Ensures middleName is not null or empty
        // Adds a space if there's already content in the name builder
        // Adds middleName to name builder
        if (authorMiddleName != null && !authorMiddleName.isEmpty()) {
            if (fullNameBuilder.length() > 0) fullNameBuilder.append(" ");
            fullNameBuilder.append(authorMiddleName);
        }
        // Ensures lastName is not null or empty
        // Adds a space if there's already content in the name builder
        // Adds lastName to name builder
        if (authorLastName != null && !authorLastName.isEmpty()) {
            if (fullNameBuilder.length() > 0) fullNameBuilder.append(" ");
            fullNameBuilder.append(authorLastName);
        }

        // Converts the name builder to a string
        // Sets the authorFullName field to that string
        this.authorFullName = fullNameBuilder.toString();
    }
}
