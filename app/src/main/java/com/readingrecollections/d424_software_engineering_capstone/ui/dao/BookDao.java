package com.readingrecollections.d424_software_engineering_capstone.ui.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

@Dao
public interface BookDao {

    // Inserts the book into the database
    @Insert
    void insert(Book book);

    // Retrieves all rows from the book table
    @Query("SELECT * FROM book")
    List<Book> getAllBooks();

    @Query("SELECT * FROM Book WHERE authorId = :authorId")
    List<Book> getBooksByAuthor(int authorId);

    // Searches book table for all series names
    // Returns list of series names, with each series only on the list once
    // Does not return null
    @Query("SELECT DISTINCT seriesName FROM book WHERE seriesName IS NOT NULL")
    List<String> getAllSeriesNames();
}
