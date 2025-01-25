package com.readingrecollections.d424_software_engineering_capstone.ui.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;

@Dao
public interface AuthorDao {

    // Inserts the author into the database
    @Insert
    void insert(Author author);

    // Retrieves all rows from the author table
    @Query("SELECT * FROM author")
    List<Author> getAllAuthors();

    // Searches author table for authorFullName, returns author object if found
    @Query("SELECT * FROM author WHERE authorFullName = :fullName LIMIT 1")
    Author getAuthorByName(String fullName);

    // Searches author table for authorFullName, returns author id if found
    @Query("SELECT id FROM author WHERE authorFullName = :fullName LIMIT 1")
    int getAuthorIdByName(String fullName);
}
