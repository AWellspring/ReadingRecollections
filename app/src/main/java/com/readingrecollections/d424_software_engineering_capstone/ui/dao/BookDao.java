package com.readingrecollections.d424_software_engineering_capstone.ui.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);

    @Query("SELECT * FROM book")
    List<Book> getAllBooks();
}
