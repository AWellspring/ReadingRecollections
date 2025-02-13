package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

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

public class ViewAuthorsActivity extends AppCompatActivity implements AuthorAdapter.OnAuthorClickListener {

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

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAuthorsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("View Authors");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Calls the add author button from activity_view_authors.xml
        Button addAuthorButton = findViewById(R.id.add_author_button);

        // Sets onClick to take the user to the AddAuthorActivity page
        addAuthorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromPage = "View Authors";
                Intent intent = new Intent(ViewAuthorsActivity.this, AddAuthorActivity.class);
                intent.putExtra("view_authors", fromPage);
                startActivity(intent);
            }
        });
    }

    // Activates when an author is clicked in the Recycler View
    // Passes the clicked author to the AuthorDetailsActivity.java
    // Passes along the author name as author_name
    public void onAuthorClick(Author author) {
        Intent intent = new Intent(this, AuthorDetailsActivity.class);
        intent.putExtra("author_name", author.getAuthorFullName());
        startActivity(intent);
    }

    // Dropdown menu displaying sort options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu with the "Sort by..." item
        getMenuInflater().inflate(R.menu.author_sort_menu, menu);
        MenuItem sortItem = menu.findItem(R.id.action_sort_by);
        if (sortItem != null) {
            SpannableString spannable = new SpannableString(sortItem.getTitle());

            // Set the font size and color
            spannable.setSpan(new AbsoluteSizeSpan(18, true), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Set font size
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Set color

            // Applies styled title to the menu item
            sortItem.setTitle(spannable);
        }
        return true;
    }

    // Create and show the PopupMenu with sort options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_by) {
            showSortOptions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Displays the sort options in the Popup Menu
    private void showSortOptions() {
        // Get the view for the "Sort by..." item
        View view = findViewById(R.id.action_sort_by); // ActionBar item
        PopupMenu popupMenu = new PopupMenu(ViewAuthorsActivity.this, view);

        // Create the menu items manually
        Menu menu = popupMenu.getMenu();
        int firstNameAsc = 1;
        int firstNameDesc = 2;
        int lastNameAsc = 3;
        int lastNameDesc = 4;

        // Sets text for each option
        menu.add(Menu.NONE, firstNameAsc, Menu.NONE, "First Name (A-Z)");
        menu.add(Menu.NONE, firstNameDesc, Menu.NONE, "First Name (Z-A)");
        menu.add(Menu.NONE, lastNameAsc, Menu.NONE, "Last Name (A-Z)");
        menu.add(Menu.NONE, lastNameDesc, Menu.NONE, "Last Name (Z-A)");

        // Set the click listener for these menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            // Sort authors based on option chosen
            if (itemId == firstNameAsc) {
                sortAuthors(SortOption.FIRST_NAME_ASC);
                return true;
            }
            else if (itemId == firstNameDesc) {
                sortAuthors(SortOption.FIRST_NAME_DESC);
                return true;
            }
            else if (itemId == lastNameAsc) {
                sortAuthors(SortOption.LAST_NAME_ASC);
                return true;
            }
            else if (itemId == lastNameDesc) {
                sortAuthors(SortOption.LAST_NAME_DESC);
                return true;
            }
            else {
                return false;
            }
        });

        // Show the popup menu
        popupMenu.show();
    }

    // Defines the four sorting options
    private enum SortOption {
        FIRST_NAME_ASC, FIRST_NAME_DESC, LAST_NAME_ASC, LAST_NAME_DESC
    }

    // Sorts the authors according to the chosen method
    private void sortAuthors(SortOption option) {
        // Checks if author list is empty
        if (authorList == null || authorList.isEmpty()) return;

        // Defaults to FirstName MiddleName LastName format
        boolean isLastNameFirst = false;

        // Checks the selected SortOption and sorts authors accordingly
        switch (option) {
            // Sorts by first name A-Z
            case FIRST_NAME_ASC:
                Collections.sort(authorList, Comparator.comparing(Author::getAuthorFirstName));
                break;
            // Sorts by first name Z-A
            case FIRST_NAME_DESC:
                Collections.sort(authorList, Comparator.comparing(Author::getAuthorFirstName).reversed());
                break;
            // Sorts by last name A-Z
            case LAST_NAME_ASC:
                Collections.sort(authorList, Comparator.comparing(Author::getAuthorLastName)
                        .thenComparing(Author::getAuthorFirstName));
                // Enables LastName, FirstName format
                isLastNameFirst = true;
                break;
            // Sorts by last name Z-A
            case LAST_NAME_DESC:
                Collections.sort(authorList, Comparator.comparing(Author::getAuthorLastName)
                        .thenComparing(Author::getAuthorFirstName, Comparator.reverseOrder()).reversed());
                // Enables LastName, FirstName format
                isLastNameFirst = true;
                break;
        }

        // Tells the adapter if names are sorted by last name first
        adapter.setLastNameFirst(isLastNameFirst);

        // Stores user sort option for future display of page
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
                adapter = new AuthorAdapter(authorList, this, this::onAuthorClick);
                // Sets the adapter to the RecyclerView, displaying the author data
                recyclerView.setAdapter(adapter);

                // Retrieve SharedPreferences
                SharedPreferences preferences = getSharedPreferences("author_prefs", MODE_PRIVATE);
                // Check if a preference exists
                String sortOptionName = preferences.getString("sort_option", null);

                // Checks to see if there's a user preference saved
                SortOption sortOption;
                if (sortOptionName != null) {
                    sortOption = SortOption.valueOf(sortOptionName);
                }
                else {
                    // No preference found, default to FIRST_NAME_ASC
                    sortOption = SortOption.FIRST_NAME_ASC;
                }

                // Apply sorting
                sortAuthors(sortOption);
            });
        });
    }
}