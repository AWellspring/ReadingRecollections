package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Intent;
import android.os.Bundle;

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

public class ViewBooksActivity extends AppCompatActivity {

    // Declares variables
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private Repository mRepository;

    // Initializes new blank ArrayList
    private List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Finds recyclerView by id in activity_view_books
        recyclerView = findViewById(R.id.recycler_view_books);

        // Sets LinearLayoutManager to display recyclerView in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initializes repository
        mRepository = new Repository(getApplication());

        mRepository.executor.execute(() -> {
            bookList = mRepository.getAllBooks();
            // Return to main thread to update UI
            runOnUiThread(() -> {
                // Calls the loadBooks method defined below to populate RecyclerView
                loadBooks();
            });
        });

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("View Books");
            // Enables the back button in the Action Bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Activates when an book is clicked in the Recycler View
    // Passes the clicked book to the BookDetailsActivity.java
    // Passes along the book title as book_title
    public void onBookClick(Book book) {
        mRepository.executor.execute(() -> {
            int authorId = book.getAuthorId();
            String authorName = mRepository.getAuthorNameById(authorId);
            String fromPage = "ViewBooks";
            Intent intent = new Intent(this, BookDetailsActivity.class);
            intent.putExtra("book_title", book.getTitle());
            intent.putExtra("author_name", authorName);
            intent.putExtra("from_page", fromPage);
            startActivity(intent);
        });
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