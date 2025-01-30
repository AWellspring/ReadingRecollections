package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

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

import java.util.ArrayList;
import java.util.List;

public class ViewAuthorsActivity extends AppCompatActivity {

    // Declares variables
    private RecyclerView recyclerView;
    private AuthorAdapter adapter;
    private Repository mRepository;

    // Initializes new blank ArrayList
    private List<Author> authorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_authors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Finds recyclerView by id in activity_view_authors
        recyclerView = findViewById(R.id.recycler_view_authors);

        // Sets LinearLayoutManager to display recyclerView in a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initializes repository
        mRepository = new Repository(getApplication());

        // Calls the loadAuthors() method defined below
        loadAuthors();

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("View Authors");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Method to fetch authors from the repository and display them
    private void loadAuthors() {
        mRepository.executor.execute(() -> {
            // Retrieves a list of all authors from the repository
            authorList = mRepository.getAllAuthors();

            // Returns to main thread for UI updates
            runOnUiThread(() -> {
                // Creates Author Adapter
                adapter = new AuthorAdapter(authorList, this);
                // Sets the adapter to the RecyclerView, displaying the author data
                recyclerView.setAdapter(adapter);
            });
        });
    }
}