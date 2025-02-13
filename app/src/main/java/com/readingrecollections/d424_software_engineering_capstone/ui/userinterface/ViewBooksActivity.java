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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Button;

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
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

import java.util.ArrayList;
import java.util.List;

public class ViewBooksActivity extends AppCompatActivity {

    // Declares variables
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
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

        adapter = new ItemAdapter(this, this::onBookClick);
        recyclerView.setAdapter(adapter);

        // Initializes repository
        mRepository = new Repository(getApplication());

        Button addBookButton = findViewById(R.id.add_book_button);

        // Sets onClick to take the user to the AddAuthorActivity page
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromPage = "View Books";
                Intent intent = new Intent(ViewBooksActivity.this, AddBookActivity.class);
                intent.putExtra("view_books", fromPage);
                startActivity(intent);
            }
        });

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewBooksActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("View Books");
            // Enables the back button in the Action Bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences preferences = getSharedPreferences("SortPreferences", MODE_PRIVATE);
        int sortOption = preferences.getInt("sort_option", 3); // Default to Author (A-Z)

        applySortOption(sortOption);
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
        PopupMenu popupMenu = new PopupMenu(ViewBooksActivity.this, view);

        // Create the menu items manually
        Menu menu = popupMenu.getMenu();
        int titleAsc = 1;
        int titleDesc = 2;
        int authorNameAsc = 3;
        int authorNameDesc = 4;
        int genreSort = 5;
        int seriesSort = 6;
        int dateRead = 7;

        // Sets text for each option
        menu.add(Menu.NONE, titleAsc, Menu.NONE, "Title (A-Z)");
        menu.add(Menu.NONE, titleDesc, Menu.NONE, "Title (Z-A)");
        menu.add(Menu.NONE, authorNameAsc, Menu.NONE, "Author (A-Z)");
        menu.add(Menu.NONE, authorNameDesc, Menu.NONE, "Author (Z-A)");
        menu.add(Menu.NONE, genreSort, Menu.NONE, "Genre");
        menu.add(Menu.NONE, seriesSort, Menu.NONE, "Series");
        menu.add(Menu.NONE, dateRead, Menu.NONE, "Date Read");

        // Set the click listener for these menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            SharedPreferences preferences = getSharedPreferences("SortPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            if (itemId == titleAsc) {
                editor.putInt("sort_option", titleAsc);
                mRepository.getItemsGroupedByTitle(items -> {
                    runOnUiThread(() -> {
                        adapter.setItems(items);
                    });
                });
            }
            else if (itemId == titleDesc) {
                editor.putInt("sort_option", titleDesc);
                mRepository.getItemsGroupedByTitleDesc(items -> {
                    runOnUiThread(() -> {
                        adapter.setItems(items);
                    });
                });
            }
            else if (itemId == authorNameAsc) {
                editor.putInt("sort_option", authorNameAsc);
                mRepository.getItemsGroupedByAuthor(items -> {
                    runOnUiThread(() -> {
                        adapter.setItems(items);
                    });
                });
            }
            else if (itemId == authorNameDesc) {
                editor.putInt("sort_option", authorNameDesc);
                mRepository.getItemsGroupedByAuthorLastName(items -> {
                    runOnUiThread(() -> {
                        adapter.setItems(items);
                    });
                });
            }
            else if (itemId == genreSort) {
                editor.putInt("sort_option", genreSort);
                mRepository.getItemsGroupedByGenre(items -> {
                    runOnUiThread(() -> {
                        adapter.setItems(items);
                    });
                });
            }
            else if (itemId == seriesSort) {
                editor.putInt("sort_option", seriesSort);
                mRepository.getItemsGroupedBySeries(items -> {
                    runOnUiThread(() -> {
                        adapter.setItems(items);
                    });
                });
            }
            else if (itemId == dateRead) {
                editor.putInt("sort_option", dateRead);
                mRepository.getItemsGroupedByDate(items -> {
                    runOnUiThread(() -> {
                        adapter.setItems(items);
                    });
                });
            }
            else {
                return false;
            }
            editor.apply();
            return true;
        });

        // Show the popup menu
        popupMenu.show();
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
    private void applySortOption(int sortOption) {
        if (sortOption == 1) {
            mRepository.getItemsGroupedByTitle(items -> runOnUiThread(() -> adapter.setItems(items)));
        } else if (sortOption == 2) {
            mRepository.getItemsGroupedByTitleDesc(items -> runOnUiThread(() -> adapter.setItems(items)));
        } else if (sortOption == 3) {
            mRepository.getItemsGroupedByAuthor(items -> runOnUiThread(() -> adapter.setItems(items)));
        } else if (sortOption == 4) {
            mRepository.getItemsGroupedByAuthorLastName(items -> runOnUiThread(() -> adapter.setItems(items)));
        } else if (sortOption == 5) {
            mRepository.getItemsGroupedByGenre(items -> runOnUiThread(() -> adapter.setItems(items)));
        } else if (sortOption == 6) {
            mRepository.getItemsGroupedBySeries(items -> runOnUiThread(() -> adapter.setItems(items)));
        } else if (sortOption == 7) {
            mRepository.getItemsGroupedByDate(items -> runOnUiThread(() -> adapter.setItems(items)));
        }
    }
}