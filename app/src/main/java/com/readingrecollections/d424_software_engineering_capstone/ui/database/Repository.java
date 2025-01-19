package com.readingrecollections.d424_software_engineering_capstone.ui.database;

import android.app.Application;

import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.readingrecollections.d424_software_engineering_capstone.ui.dao.AuthorDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.dao.BookDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

public class Repository {
    private AuthorDao mAuthorDao;

    private BookDao mBookDao;

    private Executor executor;

    public Repository(Application application){
        DatabaseBuilder db = DatabaseBuilder.getDatabase(application);
        mBookDao = db.bookDao();
        mAuthorDao = db.authorDao();

        executor = Executors.newSingleThreadExecutor();
    }

    public void insertSampleData() {
        executor.execute(() -> {
            insertAuthorIfNotExists("Jim Butcher");
            insertAuthorIfNotExists("Robin McKinley");
            insertAuthorIfNotExists("C. M. Waggoner");

            int authorId = mAuthorDao.getAuthorIdByName("C. M. Waggoner");

            Book book2 = new Book();
            book2.setTitle("The Village Library Demon Hunter's Society");
            book2.setGenre("Fantasy");
            book2.setAuthorId(authorId);
            book2.setDateRead(LocalDate.parse("2024-12-01"));
            mBookDao.insert(book2);
        });
    }

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