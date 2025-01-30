package com.readingrecollections.d424_software_engineering_capstone.ui.database;

import android.app.Application;

import java.time.LocalDate;
import java.util.List;
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
    public Executor executor;

    // Initializes the repository
    // Calls the DatabaseBuilder and passes through the book and author daos
    // Assigns the single-thread executor
    public Repository(Application application){
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        mBookDao = db.bookDao();
        mAuthorDao = db.authorDao();

        executor = Executors.newSingleThreadExecutor();
    }

    // Adds an author to the database if they don't already exist
    public void insertAuthorIfNotExists(Author author, Runnable onSuccess, Runnable onAuthorExists) {
        executor.execute(() -> {
            // Generates the author's full name
            author.updateFullName();

            // Check if the author already exists by comparing full name
            Author existingAuthor = mAuthorDao.getAuthorByName(author.getAuthorFullName());

            if (existingAuthor == null) {
                // Author does not exist, so they are added
                mAuthorDao.insert(author);

                // Returns success runnable, prompting success Toast
                if (onSuccess != null) {
                    onSuccess.run();
                }
            }

            // Returns author exists runnable, prompting author exists Toast
            else {
                if (onAuthorExists != null) {
                    onAuthorExists.run();
                }
            }
        });
    }

    public void insertBook(Book book, Runnable onSuccess, Runnable onFailure) {
        executor.execute(() -> {
            try {
                // Insert the book into the database
                mBookDao.insert(book);

                if (onSuccess != null) {
                    onSuccess.run();
                }
            } catch (Exception e) {
                // In case of an error, run the failure callback on the UI thread
                onFailure.run();
            }
        });
    }

    // Returns author whose name matches
    public Author getAuthorByName(String fullName) {
        return mAuthorDao.getAuthorByName(fullName);
    }

    // Returns a list of all author names
    public List<String> getAllAuthorNames() {
        return mAuthorDao.getAllAuthorNames();
    }

    // Returns a list of all authors; accessed by the RecyclerView
    // Will pass author information along to author details page
    public List<Author> getAllAuthors() {
        return mAuthorDao.getAllAuthors();
    }
}