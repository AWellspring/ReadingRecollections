package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.DateConverter;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditBookActivity extends AppCompatActivity {

    // Repository for database operations
    private Repository mRepository;

    // User input for book title
    private EditText bookTitleInput;

    // User input for author name
    private AutoCompleteTextView authorInput;

    // User input for book genre
    private AutoCompleteTextView bookGenreInput;

    // User input for date read
    private EditText dateReadInput;

    // Formatter for LocalDate
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    // Accesses the RadioGroup
    private RadioGroup seriesRadioGroup;

    // User input for yes series radio button
    private RadioButton seriesYes;

    // User input for no series radio button
    private RadioButton seriesNo;

    // User input for series name
    private AutoCompleteTextView seriesNameInput;

    // User input for series book number
    private EditText seriesNumberInput;

    private TextInputLayout titleLayout;

    private TextInputLayout authorLayout;

    private TextInputLayout seriesInputLayout;

    private ArrayAdapter<String> authorAdaptor;

    private ArrayAdapter<String> seriesAdaptor;

    private ArrayAdapter<String> genreAdaptor;

    private List<String> authorNames = new ArrayList<>();

    private List<String> seriesNames = new ArrayList<>();

    private List<String> bookGenres = new ArrayList<>();

    private String authorName;

    private String bookTitle;

    private Author author;

    private int authorId;

    private Book book;

    private String fromPage;

    private String fromSearch;

    private String fromAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes repository
        mRepository = new Repository(getApplication());

        // Sets the fields to their edit text fields
        bookTitleInput = findViewById(R.id.book_title_input);
        authorInput = findViewById(R.id.author_input);
        bookGenreInput = findViewById(R.id.book_genre_input);
        dateReadInput = findViewById(R.id.date_read_input);
        seriesRadioGroup = findViewById(R.id.series_radio_group);
        seriesYes = findViewById(R.id.series_yes);
        seriesNo = findViewById(R.id.series_no);
        seriesNameInput = findViewById(R.id.series_name_input);
        seriesNumberInput = findViewById(R.id.series_number_input);

        titleLayout = findViewById(R.id.book_title_input_layout);
        authorLayout = findViewById(R.id.author_input_layout);
        seriesInputLayout = findViewById(R.id.series_input_layout);

        // Loads authors from the database
        loadAuthors();

        // Loads series from the database
        loadSeries();

        // Loads genres from the database
        loadGenres();

        // Passes the author name from the intent on the previous page
        authorName = getIntent().getStringExtra("author_name");

        // Passes book title from the intent on the previous page
        bookTitle = getIntent().getStringExtra("book_title");

        // Passes fromPage from the intent to determine previous page
        fromPage = getIntent().getStringExtra("from_page");

        // Passes fromSearch from the intent to determine previous page
        fromSearch = getIntent().getStringExtra("from_search");

        // passes fromAuthor from the intent to determine previous page
        fromAuthor = getIntent().getStringExtra("from_author_page");

        // Initialize update button
        Button updateBookButton = findViewById(R.id.update_book_button);

        // Set listener to trigger the updateBook method when clicked
        updateBookButton.setOnClickListener(view -> updateBook());

        // Initializes the delete author button
        Button deleteBookButton = findViewById(R.id.delete_book_button);

        // Sets listener to trigger the deleteAuthor method when clicked
        deleteBookButton.setOnClickListener(view -> deleteBook());

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditBookActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Update Book");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sets listener to trigger when the dateReadInput is clicked
        dateReadInput.setOnClickListener(v -> {

            // Creates a calender and sets it with the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Creates the datePicker and allows the user to select a date
            // The default date on click is set as the current date
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditBookActivity.this,
                    // Convert the selected date to LocalDate
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay);

                        // Use DateConverter.formatter to format the selected date
                        String formattedDate = selectedDate.format(DateConverter.formatter);

                        // Set the formatted date in the EditText field
                        dateReadInput.setText(formattedDate);
                    },
                    year, month, dayOfMonth
            );
            // Set the maximum date to today to prevent future dates
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            // Displays the datePicker created above
            datePickerDialog.show();
        });

        if (bookTitle != null) {
            mRepository.executor.execute(() -> {
                authorId = mRepository.getAuthorIdByName(authorName);
                book = mRepository.getBookByTitleAndAuthor(bookTitle, authorId);

                runOnUiThread(() -> {
                    if (bookTitle != null) {
                        bookTitleInput.setText(bookTitle);
                    }
                    if (authorName != null) {
                        authorInput.setText(authorName);
                    }
                    if (book.getGenre() != null) {
                        bookGenreInput.setText(book.getGenre());
                    }
                    if (book.getDateRead() != null) {
                        dateReadInput.setText(DateConverter.fromLocalDate(book.getDateRead()));
                    }
                    if (book.getSeries()) {
                        seriesRadioGroup.check(seriesYes.getId());
                    }
                    if (!book.getSeries()) {
                        seriesRadioGroup.check(seriesNo.getId());
                    }
                    if (book.getSeriesName() != null) {
                        seriesNameInput.setText(book.getSeriesName());
                    }
                    if (book.getSeriesNumber() != 0) {
                        seriesNumberInput.setText(Integer.toString(book.getSeriesNumber()));
                    }
                });
            });
        }
    }

    // Overrides the back button
    // Checks if the fromPage String is populated
    // If it is, the user came from BookDetails from ViewAllBooks
    // fromPage will be passed as an intent
    // If it isn't, the user came from BookDetails from AuthorDetails
    @Override
    public boolean onSupportNavigateUp() {
            Intent intent = new Intent(EditBookActivity.this, BookDetailsActivity.class);
            // Pass the author name and book title
            intent.putExtra("author_name", authorName);
            intent.putExtra("book_title", bookTitle);
            if (fromPage != null) {
                intent.putExtra("from_page", fromPage);
            }
            if (fromSearch != null) {
                intent.putExtra("from_search", fromSearch);
            }
            if (fromAuthor != null) {
                intent.putExtra("from_author", fromAuthor);
            }
            startActivity(intent);
            finish();
            return true;
    }

    // Fetches authors from database for the authorInput suggestion
    private void loadAuthors() {
        mRepository.executor.execute(() -> {

            // Fetches the authors from database
            List<String> authors = mRepository.getAllAuthorNames();

            // Returns to main thread for UI updates
            runOnUiThread(() -> {
                // Clears current list of authors
                authorNames.clear();
                // Adds retrieved authors to authorNames
                authorNames.addAll(authors);

                // Adapter attaches dropdown menu to authorNames
                authorAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, authorNames);

                // Connects the adapter to the AutoCompleteTextView
                authorInput.setAdapter(authorAdaptor);
            });
        });
    }

    // Fetches series from database for the seriesInput suggestion
    private void loadSeries() {
        mRepository.executor.execute(() -> {

            // Fetches the series from database
            List<String> series = mRepository.getAllSeriesNames();

            // Returns to main thread for UI updates
            runOnUiThread(() -> {
                // Clears current list of series
                seriesNames.clear();
                // Adds retrieved series to seriesNames
                seriesNames.addAll(series);

                // Adapter attaches dropdown menu to seriesNames
                seriesAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, seriesNames);

                // Connects the adapter to the AutoCompleteTextView
                seriesNameInput.setAdapter(seriesAdaptor);
            });
        });
    }

    // Fetches genres from database for the genreInput suggestion
    private void loadGenres() {
        mRepository.executor.execute(() -> {

            // Fetches the genres from database
            List<String> genres = mRepository.getAllGenres();

            // Returns to main thread for UI updates
            runOnUiThread(() -> {
                // Clears current list of genres
                bookGenres.clear();
                // Adds retrieved genres to bookGenres
                bookGenres.addAll(genres);

                // Adapter attaches dropdown menu to bookGenres
                genreAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bookGenres);

                // Connects the adapter to the AutoCompleteTextView
                bookGenreInput.setAdapter(genreAdaptor);
            });
        });
    }

    private void updateBook() {
        // Takes the user input, converts it to a string, and removes extra spaces
        String title = bookTitleInput.getText().toString().trim();
        String authorFullName = authorInput.getText().toString().trim();
        String genre = bookGenreInput.getText().toString().trim();
        String dateRead = dateReadInput.getText().toString().trim();
        String seriesName = seriesNameInput.getText().toString().trim();
        String seriesNumber = seriesNumberInput.getText().toString().trim();

        // Used to check validity of firstName and lastName
        boolean isValid = true;

        // Clears existing error messages
        titleLayout.setError(null);
        authorLayout.setError(null);
        seriesInputLayout.setError(null);


        // Validate book title
        if (title.isEmpty()) {
            titleLayout.setError("Please enter a book title");
            isValid = false;
        }

        // Validate author
        if (authorFullName.isEmpty()) {
            authorLayout.setError("Please enter the author's name");
            isValid = false;
        }

        if ((!seriesName.isEmpty() || !seriesNumber.isEmpty()) && seriesRadioGroup.getCheckedRadioButtonId() != R.id.series_yes) {
            // If seriesName or seriesNumber is not empty, the "Yes" radio button must be selected
            seriesInputLayout.setError("Series info below");
            isValid = false;
        }

        // Creates book if inputs are valid
        if (isValid) {
            // Retrieve author and associate with book
            mRepository.executor.execute(() -> {
                // Lookup author by name to see if they exist in database
                Author author = mRepository.getAuthorByName(authorFullName);

                // If author is not found, prompt the user to add the author first
                if (author == null) {
                    runOnUiThread(() -> {
                        authorLayout.setError("Author not found! Please add the author first.");
                    });
                    return;
                }
                book.setTitle(title);
                // Link book to the author by ID
                book.setAuthorId(author.getId());
                book.setGenre(genre);

                // Convert the dateRead (String) to LocalDate if it is not empty
                if (!dateRead.isEmpty()) {
                    LocalDate parsedDate = LocalDate.parse(dateRead, DateConverter.formatter); // Parsing the date from the string
                    book.setDateRead(parsedDate);
                }

                // Handle series if selected
                if (seriesRadioGroup.getCheckedRadioButtonId() == R.id.series_yes) {
                    book.setSeries(true);

                    // If seriesName is not empty, sets seriesName to input
                    if (!seriesName.isEmpty()) {
                        book.setSeriesName(seriesNameInput.getText().toString().trim());
                    }

                    // If seriesNumber is not empty, sets seriesNumber to input
                    if (!seriesNumber.isEmpty()) {
                        book.setSeriesNumber(Integer.parseInt(seriesNumberInput.getText().toString().trim()));
                    }
                } else {
                    book.setSeries(false);
                }
                // Updates book in repository
                mRepository.executor.execute(() -> {
                    mRepository.updateBook(book);

                    runOnUiThread(() -> {
                                Toast.makeText(EditBookActivity.this, title + " updated!", Toast.LENGTH_LONG).show();
                            });

                        bookTitle = book.getTitle();
                        authorName = mRepository.getAuthorNameById(book.getAuthorId());
                        // Create an intent to go back to BookDetailsActivity
                        Intent intent = new Intent(EditBookActivity.this, BookDetailsActivity.class);
                        // Pass the author name and book title
                        intent.putExtra("author_name", authorName);
                        intent.putExtra("book_title", bookTitle);
                        if (fromPage != null) {
                            intent.putExtra("from_page", fromPage);
                        }
                        if (fromSearch != null) {
                            intent.putExtra("from_search", fromSearch);
                        }
                        if (fromAuthor != null) {
                            intent.putExtra("from_author", fromAuthor);
                        }
                        startActivity(intent);
                        finish();
                });
            });
        }
    }

    // Displays a pop up message verifying user wants to delete author
    // Triggers a confirmation to make sure the user wants to delete the book
    private void deleteBook() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete " + bookTitle + " from your library?")
                // Triggers the book deletion
                .setPositiveButton("Delete", (dialog, which) -> confirmDeleteBook())
                // Dismisses dialog
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Deletes book from the database upon user confirmation
    private void confirmDeleteBook() {
        mRepository.executor.execute(() -> {
            mRepository.deleteBook(book);

            // Return to Author Details page with success message
            runOnUiThread(() -> {
                Toast.makeText(EditBookActivity.this, "Book deleted from your library!", Toast.LENGTH_LONG).show();
                if (fromPage !=  null) {
                    Intent intent = new Intent(EditBookActivity.this, ViewBooksActivity.class);
                    intent.putExtra("from_page", fromPage);
                    startActivity(intent);
                    finish();
                }
                else if (fromAuthor != null) {
                    Intent intent = new Intent(EditBookActivity.this, AuthorDetailsActivity.class);
                    intent.putExtra("author_name", authorName);
                    if (fromSearch != null) {
                        intent.putExtra("from_search", fromSearch);
                    }
                    startActivity(intent);
                    finish();
                }
                else if (fromSearch != null) {
                    Intent intent = new Intent(EditBookActivity.this, SearchActivity.class);
                    intent.putExtra("from_search", fromSearch);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(EditBookActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        });
    }
}