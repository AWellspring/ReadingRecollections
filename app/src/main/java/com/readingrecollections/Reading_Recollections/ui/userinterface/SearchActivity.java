package com.readingrecollections.Reading_Recollections.ui.userinterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.readingrecollections.Reading_Recollections.R;
import com.readingrecollections.Reading_Recollections.ui.database.Repository;
import com.readingrecollections.Reading_Recollections.ui.entities.Author;
import com.readingrecollections.Reading_Recollections.ui.entities.Book;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private Repository mRepository;

    private EditText searchInput;

    private String searchQuery;

    private String checkIntent;

    private TextView timestamp;

    private TextView noResults;

    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes the repository
        mRepository = new Repository(getApplication());

        // Sets the recyclerView
        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sets the searchAdapter
        searchAdapter = new SearchAdapter(this, new SearchAdapter.OnItemClickListener() {
            @Override
            public void onAuthorClick(Author author) {
                mRepository.executor.execute(() -> {
                    String fromSearch = searchQuery;
                    Intent intent = new Intent(SearchActivity.this, AuthorDetailsActivity.class);
                    intent.putExtra("author_name", author.getAuthorFullName());
                    intent.putExtra("from_search", fromSearch);
                    startActivity(intent);
                });
            }


            @Override
            public void onBookClick(Book book) {
                mRepository.executor.execute(() -> {
                    String fromSearch = searchQuery;
                    int authorId = book.getAuthorId();
                    String authorName = mRepository.getAuthorNameById(authorId);
                    Intent intent = new Intent(SearchActivity.this, BookDetailsActivity.class);
                    intent.putExtra("book_title", book.getTitle());
                    intent.putExtra("author_name", authorName);
                    intent.putExtra("from_search", fromSearch);
                    startActivity(intent);
                });
            }

        });
        recyclerView.setAdapter(searchAdapter);

        checkIntent = getIntent().getStringExtra("from_search");

        if (checkIntent != null) {
            mRepository.executor.execute(() -> {
                searchInput.setText(checkIntent);
                searchQuery = checkIntent;
                search(searchQuery);
            });
        }

        // Sets the searchInput to the edit text field
        searchInput = findViewById(R.id.search_input);

        // Sets the timestamp to the text field
        timestamp = findViewById(R.id.timestamp);

        // Sets the noResults to the text field
        noResults = findViewById(R.id.no_results);

        // Initializes the search button
        Button searchButton = findViewById(R.id.search_button);

        // Sets listener to query the database and display results when searchButton is clicked
        searchButton.setOnClickListener(v -> {
            // Saves the user input to a string
            searchQuery = sanitizeInput(searchInput.getText().toString().trim());
            if (!searchQuery.isEmpty()) {
                search(searchQuery);
            }
        });

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void search(String query) {
        new Thread(() -> {
            List<Item> searchResults = new ArrayList<>();

            // Query authors first
            List<Author> authors = mRepository.searchAuthors(query);
            searchResults.addAll(authors);

            // Query books next
            List<Book> books = mRepository.searchBooks(query);
            searchResults.addAll(books);

            // Update RecyclerView on the main thread
            runOnUiThread(() -> {
                // Add a timestamp at the top
                String timestampText = "Last updated: " + new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).format(new Date());
                timestamp.setText(timestampText);

                searchAdapter.setSearchResults(searchResults);

                if (!searchResults.isEmpty()) {
                    String noResultString = "";
                    noResults.setText(noResultString);
                }

                else if (searchResults.isEmpty()) {
                    String noResultString = "No results found for " + query;
                    noResults.setText(noResultString);
                }
            });
        }).start();
    }

    // Remove the characters ", `, and ; to prevent SQL injection
    private String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[\"`;%]", "");
    }
}