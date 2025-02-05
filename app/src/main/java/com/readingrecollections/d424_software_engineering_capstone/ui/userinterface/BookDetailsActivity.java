package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

public class BookDetailsActivity extends AppCompatActivity {

    private TextView bookTitleView;

    private String bookTitle;

    private int authorId;

    private String authorName;
    private Repository mRepository;

    private Book book;

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

        // Passes book title from the intent on the previous page
        bookTitle = getIntent().getStringExtra("book_title");

        // Passes the author name from the intent on the previous page
        authorName = getIntent().getStringExtra("author_name");

        // Sets the TextView text to bookTitle
        bookTitleView.setText(bookTitle);

        if (bookTitle != null) {
            mRepository.executor.execute(() -> {
                authorId = mRepository.getAuthorIdByName(authorName);
                book = mRepository.getBookByTitleAndAuthor(bookTitle, authorId);
            });
        }
    }
}