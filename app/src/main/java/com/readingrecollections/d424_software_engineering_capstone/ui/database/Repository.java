package com.readingrecollections.d424_software_engineering_capstone.ui.database;

import android.app.Application;

import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.readingrecollections.d424_software_engineering_capstone.ui.dao.AuthorDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.dao.BookDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

// Manages operations between the UI and the database
public class Repository {

    // Allows access to author database operations
    private AuthorDao mAuthorDao;

    // Allows access to book database operations
    private BookDao mBookDao;

    // Handles background task execution without blocking the main thread
    private Executor executor;

    // Initializes the repository
    // Calls the DatabaseBuilder  and passes through the book and author daos
    // Assigns the single-thread executor
    public Repository(Application application){
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        mBookDao = db.bookDao();
        mAuthorDao = db.authorDao();

        executor = Executors.newSingleThreadExecutor();
    }

    // Adds sample data to the database
    public void insertSampleData() {
        executor.execute(() -> {

            // Calls function defined below to insert author if not already in database
            insertAuthorIfNotExists("Jim Butcher");
            insertAuthorIfNotExists("Robin McKinley");
            insertAuthorIfNotExists("C. M. Waggoner");

            // Creates authorId variable
            // Queries database to find author by name and retrieve that author's id
            int authorId = mAuthorDao.getAuthorIdByName("C. M. Waggoner");

            // Creates a new book entity and sets its parameters
            // Sets the author id to match the previously retrieved author id
            // Inserts into database
            Book book2 = new Book();
            book2.setTitle("The Village Library Demon Hunter's Society");
            book2.setGenre("Fantasy");
            book2.setAuthorId(authorId);
            book2.setDateRead(LocalDate.parse("2024-12-01"));
            mBookDao.insert(book2);
        });
    }

    // Adds an author to the database if they don't already exist
    private void insertAuthorIfNotExists(String authorName) {
        // Check if the author already exists
        Author existingAuthor = mAuthorDao.getAuthorByName(authorName);
        if (existingAuthor == null) {
            // Author does not exist, so they are added
            Author newAuthor = new Author();
            newAuthor.setAuthorName(authorName);
            mAuthorDao.insert(newAuthor);
        }
    }
}