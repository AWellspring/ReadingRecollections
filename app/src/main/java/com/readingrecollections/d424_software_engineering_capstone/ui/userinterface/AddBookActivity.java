package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class AddBookActivity extends AppCompatActivity {

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

    private Author author;

    private String tempName;

    private String fromBook;

    private String fromSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes the repository
        mRepository = new Repository(getApplication());

        // Passes author name from the intent on the Author Details page
        tempName = getIntent().getStringExtra("author_name");

        // Passes fromBook from the intent on the View Books page
        fromBook = getIntent().getStringExtra("view_books");

        // Passes fromSearch from the intent on Search page
        fromSearch = getIntent().getStringExtra("from_search");

        // If coming from the Author Details page
        if (tempName != null) {
            // Set authorName to the intent
            authorName = getIntent().getStringExtra("author_name");

            mRepository.executor.execute(() -> {
                        // Calls author from database by authorName
                        author = mRepository.getAuthorByName(authorName);

                        runOnUiThread(() -> {
                            // Sets the input text to the author's first name
                            if (author.getAuthorFullName() != null) {
                                authorInput.setText(author.getAuthorFullName());
                            }
                        });
                    });
        }


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

        // Initialize save button
        Button saveBookButton = findViewById(R.id.save_book_button);

        // Set listener to trigger the submitBook method when clicked
        saveBookButton.setOnClickListener(view -> addBook());

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBookActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Book");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateReadInput.setText("");

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
                    AddBookActivity.this,
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
    }

    // Overrides the back button so that the authorName can be passed
    // If the user came from the AuthorDetails page, they are returned there
    // Otherwise, they are returned to the Home page
    @Override
    public boolean onSupportNavigateUp() {
        // If coming from the Author Details page
        if (tempName != null) {
            // Create an intent to go back to AuthorDetailsActivity
            Intent intent = new Intent(AddBookActivity.this, AuthorDetailsActivity.class);
            if (fromSearch != null) {
                intent.putExtra("from_search", fromSearch);
            }
            // Pass the author name
            intent.putExtra("author_name", authorName);
            startActivity(intent);
            finish();
            return true;
        }
        // If coming from the View Books page
        else if (fromBook != null) {
            // Create an intent to go back to ViewBooksActivity
            Intent intent = new Intent(AddBookActivity.this, ViewBooksActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        // If coming from the Home page
        else {
            Intent intent = new Intent(AddBookActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
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

    // Adds book to database
    private void addBook() {
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

                // Create a new Book object
                Book book = new Book();
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
                        double seriesNumberValue = Double.parseDouble(seriesNumberInput.getText().toString().trim());
                        book.setSeriesNumber(seriesNumberValue);
                    }
                }
                else {
                    book.setSeries(false);
                }

                // Insert book into database
                mRepository.insertBook(book,
                        () -> {
                            // Success callback
                            runOnUiThread(() -> {
                                Toast.makeText(AddBookActivity.this, title + " saved to your library!", Toast.LENGTH_LONG).show();
                                // If coming from the Author Details page
                                if (tempName != null) {
                                    // Create an intent to go back to AuthorDetailsActivity
                                    Intent intent = new Intent(AddBookActivity.this, AuthorDetailsActivity.class);
                                    if (fromSearch != null) {
                                        intent.putExtra("from_search", fromSearch);
                                    }
                                    // Pass the author name
                                    intent.putExtra("author_name", authorName);
                                    startActivity(intent);
                                    finish();
                                }
                                // If coming from the View Books page
                                else if (fromBook != null) {
                                    // Create an intent to go back to ViewBooksActivity
                                    Intent intent = new Intent(AddBookActivity.this, ViewBooksActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                // Otherwise, returns user to the Home page
                                else {
                                    Intent intent = new Intent(AddBookActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        },
                        () -> {
                            // Failure callback (in case something goes wrong)
                            runOnUiThread(() -> {
                                Toast.makeText(AddBookActivity.this, "Failed to add book.", Toast.LENGTH_LONG).show();
                            });
                        }
                );
            });
        }
    }

    // Clicking the back button takes the user to the parent activity defined in Manifest
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}