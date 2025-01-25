package com.readingrecollections.d424_software_engineering_capstone.ui.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

import com.readingrecollections.d424_software_engineering_capstone.ui.dao.AuthorDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.dao.BookDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

// Marks this class as a database
// Specifies the 2 tables, author and book
// Version needs to be updated after any schema changes
@Database(entities = {Author.class, Book.class}, version = 5)
@TypeConverters({DateConverter.class})
public abstract class DatabaseBuilder extends androidx.room.RoomDatabase {

    // Creates instances of the author and book daos for interactions
    public abstract AuthorDao authorDao();
    public abstract BookDao bookDao();

    // Declares the database builder instance
    private static volatile DatabaseBuilder INSTANCE;

    // Allows retrieval of the database
    static DatabaseBuilder getDatabase(final Context context) {
        // Checks if instance already exists
        if (INSTANCE == null) {
            // Only one thread can initialize the database at a time
            synchronized (DatabaseBuilder.class){
                if (INSTANCE == null) {
                    // Initializes the database, calls this class, and names the SQLite database file
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "ReadingRecollections.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
