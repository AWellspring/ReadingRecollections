package com.readingrecollections.d424_software_engineering_capstone.ui.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

@Dao
public interface BookDao {

    // Inserts the book into the database
    @Insert
    void insert(Book book);

    // Updates the book in the database
    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    // Retrieves all rows from the book table
    @Query("SELECT * FROM book ORDER BY title ASC")
    List<Book> getAllBooks();

    @Query("SELECT * FROM book ORDER BY title DESC")
    List<Book> getAllBooksDesc();

    @Query("SELECT * FROM Book WHERE authorId = :authorId")
    List<Book> getBooksByAuthor(int authorId);

    // Searches book table for all series names
    // Returns list of series names, with each series only on the list once
    // Does not return null
    @Query("SELECT DISTINCT seriesName FROM book WHERE seriesName IS NOT NULL")
    List<String> getAllSeriesNames();

    // Searches book table for all genres
    // Returns list of genres, with each genre only on the list once
    // Does not return null
    @Query("SELECT DISTINCT genre FROM book WHERE genre IS NOT NULL ORDER BY genre ASC")
    List<String> getAllGenres();

    // Searches book table for book with matching title and author id
    // Returns book
    @Query("SELECT * FROM book WHERE title = :bookTitle AND authorId = :authorId")
    Book getBookByTitleAndAuthor(String bookTitle, int authorId);

    @Query("SELECT * FROM Book INNER JOIN Author ON Book.authorId = Author.Id ORDER BY Author.authorFullName ASC, Book.title ASC")
    List<Book> getBooksGroupedByAuthor();

    @Query("SELECT * FROM Book INNER JOIN Author ON Book.authorId = Author.Id ORDER BY Author.authorFullName DESC, Book.title ASC")
    List<Book> getBooksGroupedByAuthorLastName();

    @Query("SELECT * FROM Book WHERE genre IS NOT NULL ORDER BY genre ASC, title ASC")
    List<Book> getBooksGroupedByGenre();

    @Query("SELECT * FROM Book WHERE seriesName IS NOT NULL ORDER BY seriesName ASC, seriesNumber ASC")
    List<Book> getBooksGroupedBySeries();

    @Query("SELECT * FROM Book WHERE dateRead IS NOT NULL ORDER BY dateRead DESC, title ASC")
    List<Book> getBooksGroupedByDate();
}
