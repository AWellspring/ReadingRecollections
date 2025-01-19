package com.readingrecollections.d424_software_engineering_capstone.ui.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;

@Dao
public interface AuthorDao {
    @Insert
    void insert(Author author);

    @Query("SELECT * FROM author")
    List<Author> getAllAuthors();

    @Query("SELECT * FROM author WHERE authorName = :authorName LIMIT 1")
    Author getAuthorByName(String authorName);

    @Query("SELECT id FROM author WHERE authorName = :authorName LIMIT 1")
    int getAuthorIdByName(String authorName);
}
