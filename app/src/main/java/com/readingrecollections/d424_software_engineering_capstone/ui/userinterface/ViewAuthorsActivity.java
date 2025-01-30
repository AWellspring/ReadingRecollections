package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import java.util.Collections;
import java.util.Comparator;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.author_sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_first_name_asc) {
            sortAuthors(SortOption.FIRST_NAME_ASC);
            return true;
        } else if (id == R.id.sort_first_name_desc) {
            sortAuthors(SortOption.FIRST_NAME_DESC);
            return true;
        } else if (id == R.id.sort_last_name_asc) {
            sortAuthors(SortOption.LAST_NAME_ASC);
            return true;
        } else if (id == R.id.sort_last_name_desc) {
            sortAuthors(SortOption.LAST_NAME_DESC);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private enum SortOption {
        FIRST_NAME_ASC, FIRST_NAME_DESC, LAST_NAME_ASC, LAST_NAME_DESC
    }

    private void sortAuthors(SortOption option) {
        if (authorList == null || authorList.isEmpty()) return;

        boolean isLastNameFirst = false; // Default to FirstName MiddleName LastName format

        switch (option) {
            case FIRST_NAME_ASC:
                Collections.sort(authorList, Comparator.comparing(Author::getAuthorFirstName));
                break;
            case FIRST_NAME_DESC:
                Collections.sort(authorList, Comparator.comparing(Author::getAuthorFirstName).reversed());
                break;
            case LAST_NAME_ASC:
                Collections.sort(authorList, Comparator.comparing(Author::getAuthorLastName)
                        .thenComparing(Author::getAuthorFirstName));
                isLastNameFirst = true; // Enable LastName, FirstName format
                break;
            case LAST_NAME_DESC:
                Collections.sort(authorList, Comparator.comparing(Author::getAuthorLastName)
                        .thenComparing(Author::getAuthorFirstName, Comparator.reverseOrder()).reversed());
                isLastNameFirst = true; // Enable LastName, FirstName format
                break;
        }

        adapter.setLastNameFirst(isLastNameFirst);

        SharedPreferences preferences = getSharedPreferences("author_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sort_option", option.name());
        editor.apply();
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

                // Retrieve SharedPreferences
                SharedPreferences preferences = getSharedPreferences("author_prefs", MODE_PRIVATE);
                // Check if a preference exists
                String sortOptionName = preferences.getString("sort_option", null);

                SortOption sortOption;
                if (sortOptionName != null) {
                    sortOption = SortOption.valueOf(sortOptionName);
                } else {
                    // No preference found, default to FIRST_NAME_ASC
                    sortOption = SortOption.FIRST_NAME_ASC;
                }

                // Apply sorting
                sortAuthors(sortOption);
            });
        });
    }
}