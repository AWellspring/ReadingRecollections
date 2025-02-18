package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.readingrecollections.d424_software_engineering_capstone.ui.dao.AuthorDao;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.DatabaseBuilder;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;


@RunWith(AndroidJUnit4.class)
public class RepositoryTest {

    private Repository repository;
    private DatabaseBuilder database;
    private AuthorDao authorDao;

    @Before
    public void setUp() {
        // Get the application context
        Context context = ApplicationProvider.getApplicationContext();

        // Set up in-memory Room database
        database = Room.inMemoryDatabaseBuilder(context, DatabaseBuilder.class)
                .allowMainThreadQueries() // Only for testing
                .build();

        // Get the DAO from the database
        authorDao = database.authorDao(); // Assuming your DatabaseBuilder class has this method

        // Ensure authorDao is not null
        if (authorDao == null) {
            Log.e("RepositoryTest", "authorDao is null, check database setup.");
        }

        // Create the repository, using the application context
        repository = new Repository((Application) context, database);

        // Ensure repository is not null
        if (repository == null) {
            Log.e("RepositoryTest", "repository is null, check initialization.");
        }
    }

    @After
    public void tearDown() {
        // Close the database after the test
        if (database != null) {
            database.close();
        }
    }

    @Test
    public void testGetAuthorByName() {
        // Given: Create and insert an author
        Author author = new Author();
        author.setAuthorFirstName("Jim");
        author.setAuthorLastName("Butcher");
        author.updateFullName(); // Assuming the updateFullName method is used to set the full name
        authorDao.insert(author); // Insert author directly into the DAO

        // When: Retrieve the author by name
        Author retrievedAuthor = repository.getAuthorByName("Jim Butcher");

        // Then: Verify that the correct author is returned
        assertNotNull("Author should not be null", retrievedAuthor);
        assertEquals("Author name should match", "Jim Butcher", retrievedAuthor.getAuthorFullName());
    }

    @Test
    public void testGetAllAuthorNames() {
        // Create and insert authors
        Author author = new Author();
        author.setAuthorFirstName("Jim");
        author.setAuthorLastName("Butcher");
        author.updateFullName();
        authorDao.insert(author);

        Author author2 = new Author();
        author2.setAuthorFirstName("Karen");
        author2.setAuthorLastName("Traviss");
        author2.updateFullName();
        authorDao.insert(author2);

        Author author3 = new Author();
        author3.setAuthorFirstName("Robin");
        author3.setAuthorLastName("McKinley");
        author3.updateFullName();
        authorDao.insert(author3);

        Author author4 = new Author();
        author4.setAuthorFirstName("Sarah");
        author4.setAuthorMiddleName("J.");
        author4.setAuthorLastName("Maas");
        author4.updateFullName();
        authorDao.insert(author4);

        List<String> retrievedAuthors = repository.getAllAuthorNames();

        // Then: Assert that the retrieved list is not empty
        assertNotNull("The list of authors should not be null", retrievedAuthors);
        assertFalse("The list of authors should not be empty", retrievedAuthors.isEmpty());

        // Then: Assert that the correct number of authors are returned
        assertEquals("The number of authors returned is incorrect", 4, retrievedAuthors.size());

        // Then: Assert that the names match the expected values
        assertTrue("The list should contain 'Jim Butcher'", retrievedAuthors.contains("Jim Butcher"));
        assertTrue("The list should contain 'Karen Traviss'", retrievedAuthors.contains("Karen Traviss"));
        assertTrue("The list should contain 'Robin McKinley'", retrievedAuthors.contains("Robin McKinley"));
        assertTrue("The list should contain 'Sarah J. Maas'", retrievedAuthors.contains("Sarah J. Maas"));

    }

}