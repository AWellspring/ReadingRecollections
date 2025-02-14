package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Intent;
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

import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.DateConverter;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

import java.time.format.DateTimeFormatter;

public class BookDetailsActivity extends AppCompatActivity {

    private TextView bookTitleView;

    private TextView authorNameView;

    private TextView bookGenre;

    private TextView dateRead;

    private TextView isSeries;

    private TextView seriesName;

    private TextView seriesNumber;

    private String bookTitle;

    private int authorId;

    private String authorName;
    private Repository mRepository;

    private Book book;

    private String fromPage;

    private String fromSearch;

    private String fromAuthor;

    private DateConverter dateConverter = new DateConverter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes repository
        mRepository = new Repository(getApplication());

        // Finds TextView by id
        bookTitleView = findViewById(R.id.text_book_title);
        authorNameView = findViewById(R.id.author_name);
        bookGenre = findViewById(R.id.book_genre);
        dateRead = findViewById(R.id.date_read);
        isSeries = findViewById(R.id.is_series);
        seriesName = findViewById(R.id.series_name);
        seriesNumber = findViewById(R.id.series_number);

        // Passes book title from the intent on the previous page
        bookTitle = getIntent().getStringExtra("book_title");

        // Passes the author name from the intent on the previous page
        authorName = getIntent().getStringExtra("author_name");

        // Passes the fromPage from the intent on the ViewBooksActivity
        fromPage = getIntent().getStringExtra("from_page");

        // Passes the fromSearch from the intent on the SearchActivity
        fromSearch = getIntent().getStringExtra("from_search");

        // Passes the fromAuthor from the intent on the AuthorDetailsActivity
        fromAuthor = getIntent().getStringExtra("from_author");

        // Sets the TextView text to bookTitle
        bookTitleView.setText(bookTitle);

        // Sets the TextView text to authorName
        authorNameView.setText(authorName);

        if (bookTitle != null) {
            mRepository.executor.execute(() -> {
                authorId = mRepository.getAuthorIdByName(authorName);
                book = mRepository.getBookByTitleAndAuthor(bookTitle, authorId);

                runOnUiThread(() -> {
                    if (book.getGenre() != null) {
                        bookGenre.setText(book.getGenre());
                    }
                    if (book.getDateRead() != null) {
                        dateRead.setText(DateConverter.fromLocalDate(book.getDateRead()));
                    }
                    if (book.getSeries()) {
                        isSeries.setText("Yes");
                    }
                    if (book.getSeriesName() != null) {
                        seriesName.setText(book.getSeriesName());
                    }
                    if (book.getSeriesNumber() != 0) {
                        seriesNumber.setText(Integer.toString(book.getSeriesNumber()));
                    }
                });
            });
        }

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Book Details");
            // Enables the back button in the Action Bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Calls the edit book button from activity_book_details.xml
        Button buttonEditBook = findViewById(R.id.edit_book_button);

        // Sets onClick to take the user to the EditBookActivity.xml page
        buttonEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(BookDetailsActivity.this, EditBookActivity.class);
                    intent.putExtra("author_name", authorName);
                    intent.putExtra("book_title", bookTitle);
                    intent.putExtra("from_author", authorName);
                    if (fromAuthor != null) {
                        intent.putExtra("from_author_page", fromAuthor);
                    }
                    if (fromSearch != null) {
                        intent.putExtra("from_search", fromSearch);
                    }
                    if (fromPage != null) {
                        intent.putExtra("from_page", fromPage);
                    }
                    startActivity(intent);
                    finish();
            }
        });
    }

    // Overrides the back button
    // Checks if the fromPage String is populated
    // If it is, the user came from ViewBooks and will be returned there
    // If it isn't, the user came from AuthorDetails and will be returned there with an intent
    @Override
    public boolean onSupportNavigateUp() {
        if (fromPage != null) {
            Intent intent = new Intent(BookDetailsActivity.this, ViewBooksActivity.class);
            startActivity(intent);
            finish();
        }
        else if (fromAuthor != null) {
            Intent intent = new Intent(BookDetailsActivity.this, AuthorDetailsActivity.class);
            if (fromSearch != null) {
                intent.putExtra("from_search", fromSearch);
            }
            intent.putExtra("author_name", authorName);
            startActivity(intent);
            finish();
        }
        else if (fromSearch != null) {
            Intent intent = new Intent(BookDetailsActivity.this, SearchActivity.class);
            intent.putExtra("from_search", fromSearch);
            startActivity(intent);
            finish();
        }
        return true;
    }
}