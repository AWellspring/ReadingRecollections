package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.os.Bundle;
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
    }
}