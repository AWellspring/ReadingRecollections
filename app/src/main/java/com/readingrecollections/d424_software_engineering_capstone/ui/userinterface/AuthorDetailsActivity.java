package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

import java.util.ArrayList;
import java.util.List;

public class AuthorDetailsActivity extends AppCompatActivity {

    private TextView authorNameView;

    private String authorName;

    private RecyclerView recyclerView;

    private Repository mRepository;

    private List<Book> bookList = new ArrayList<>();

    private int authorId;

    // Signifies came from SearchActivity
    private String fromSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_author_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Finds recyclerView by id in activity_view_authors
        recyclerView = findViewById(R.id.recycler_view_books);

        // Sets LinearLayoutManager to display recyclerView in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initializes repository
        mRepository = new Repository(getApplication());

        // Finds TextView by id
        authorNameView = findViewById(R.id.text_author_name);

        // Passes author name from the intent on the previous page
        authorName = getIntent().getStringExtra("author_name");

        // Passes the fromSearch from the intent on the SearchActivity
        fromSearch = getIntent().getStringExtra("from_search");

        // Sets the TextView text to authorName
        authorNameView.setText(authorName);

        // Verifies the authorName is valid
        if (authorName != null) {
            mRepository.executor.execute(() -> {
                // Checks repository for authorName and sets authorId to matching id
                authorId = mRepository.getAuthorIdByName(authorName);

                // Sets bookList to all books by the author with authorId
                bookList = mRepository.getBooksByAuthor(authorId);

                // Return to main thread to update UI
                runOnUiThread(() -> {
                    // Calls the loadBooks method defined below to populate RecyclerView
                    loadBooks();
                });
            });
        }

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthorDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Author Details");
            // Enables the back button in the Action Bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Calls the edit author button from activity_author_details.xml
        Button buttonEditAuthor = findViewById(R.id.edit_author_button);

        // Sets onClick to take the user to the EditAuthorActivity.xml page
        buttonEditAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthorDetailsActivity.this, EditAuthorActivity.class);
                intent.putExtra("author_name", authorName);
                startActivity(intent);
            }
        });

        // Calls the add book button from activity_author_details.xml
        Button buttonAddBook = findViewById(R.id.add_book_by_author_button);

        // Sets onClick to take the user to the activity_add_book.xml page
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(AuthorDetailsActivity.this, AddBookActivity.class);
               intent.putExtra("author_name", authorName);
               startActivity(intent);
           }
        });
    }

    // Overrides the back button
    // Checks if the fromSearch String is populated
    // If it is, the user came from SearchActivity and will be returned there
    // If it isn't, the user came from ViewAuthors and will be returned
    @Override
    public boolean onSupportNavigateUp() {
        if (fromSearch != null) {
            Intent intent = new Intent(AuthorDetailsActivity.this, SearchActivity.class);
            intent.putExtra("from_search", fromSearch);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(AuthorDetailsActivity.this, ViewAuthorsActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    // Activates when an book is clicked in the Recycler View
    // Passes the clicked book to the BookDetailsActivity.java
    // Passes along the book title as book_title
    public void onBookClick(Book book) {
        Intent intent = new Intent(this, BookDetailsActivity.class);
        intent.putExtra("book_title", book.getTitle());
        intent.putExtra("author_name", authorName);
        startActivity(intent);
    }

    // Method to fetch book titles from the repository and display them
    private void loadBooks() {
        mRepository.executor.execute(() -> {
            // Returns to main thread for UI updates
            runOnUiThread(() -> {
                // Creates Book Adapter
                BookAdapter adapter = new BookAdapter(bookList, this, this::onBookClick);
                // Sets the adapter to the RecyclerView, displaying the author data
                recyclerView.setAdapter(adapter);
            });
        });
    }
}