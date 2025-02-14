package com.readingrecollections.d424_software_engineering_capstone.ui.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;

@Dao
public interface AuthorDao {

    // Inserts the author into the database
    @Insert
    void insert(Author author);

    // Updates the author in the database
    @Update
    void update(Author author);

    @Delete
    void delete(Author author);

    // Retrieves all rows from the author table
    @Query("SELECT * FROM author")
    List<Author> getAllAuthors();

    // Searches author table for authorFullName, returns author object if found
    @Query("SELECT * FROM author WHERE authorFullName = :fullName LIMIT 1")
    Author getAuthorByName(String fullName);

    // Searches author table for authorFullName, returns author id if found
    @Query("SELECT id FROM author WHERE authorFullName = :fullName LIMIT 1")
    int getAuthorIdByName(String fullName);

    // Searches author table for authorId, returns author if found
    @Query("SELECT * FROM author WHERE id = :authorId")
    Author getAuthorById(int authorId);

    @Query("SELECT authorFullName FROM author WHERE id = :authorId")
    String getAuthorNameById(int authorId);

    // Searches author table for all author names, returns list of all author names
    @Query("SELECT authorFullName FROM author")
    List<String> getAllAuthorNames();

    // Searches author table for anything that matches the user query
    // Returns author if found
    @Query("SELECT * FROM author " +
            "WHERE (authorFullName IS NOT NULL AND authorFullName LIKE '%' || :query || '%') " +
            "OR (authorFirstName IS NOT NULL AND authorFirstName LIKE '%' || :query || '%') " +
            "OR (authorMiddleName IS NOT NULL AND authorMiddleName LIKE '%' || :query || '%') " +
            "OR (authorLastName IS NOT NULL AND authorLastName LIKE '%' || :query || '%') " +
            "ORDER BY authorFullName ASC")
    List<Author> searchAuthors(String query);
}

