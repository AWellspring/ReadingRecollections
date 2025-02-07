package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.readingrecollections.d424_software_engineering_capstone.R;

import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;

public class HomeActivity extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Calls the add author button from activity_home.xml
        Button buttonAddAuthor = findViewById(R.id.add_author_button);

        // Sets onClick to take the user to the AddAuthorActivity.xml page
        buttonAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddAuthorActivity.class);
                startActivity(intent);
            }
        });

        // Calls the view author button from activity_home.xml
        Button buttonViewAuthors = findViewById(R.id.view_all_authors_button);

        // Sets onClick to take the user to the ViewAuthorsActivity.xml page
        buttonViewAuthors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ViewAuthorsActivity.class);
                startActivity(intent);
            }
        });

        // Calls the add book button from activity_home.xml
        Button buttonAddBook = findViewById(R.id.add_book_button);

        // Sets onClick to take the user to the AddBookActivity.xml page
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

        // Calls the view all books button from activity_home.xml
        Button buttonViewBooks = findViewById(R.id.view_all_books_button);

        // Sets onClick to take the user to the ViewBooksActivity.xml page
        buttonViewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ViewBooksActivity.class);
                startActivity(intent);
            }
        });

        // Initializes a repository for managing data operations
        repository = new Repository(getApplication());

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Clicking the back button takes the user to the parent activity defined in Manifest
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}