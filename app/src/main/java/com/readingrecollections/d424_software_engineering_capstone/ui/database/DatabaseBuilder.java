package com.readingrecollections.d424_software_engineering_capstone.ui.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

import com.readingrecollections.d424_software_engineering_capstone.ui.dao.AuthorDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.dao.BookDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

@Database(entities = {Author.class, Book.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract class DatabaseBuilder extends androidx.room.RoomDatabase {
    public abstract AuthorDao authorDao();
    public abstract BookDao bookDao();

    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "ReadingRecollections.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
