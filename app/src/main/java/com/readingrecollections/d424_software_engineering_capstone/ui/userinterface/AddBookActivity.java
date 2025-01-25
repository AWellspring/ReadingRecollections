package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddBookActivity extends AppCompatActivity {

    // Repository for database operations
    private Repository mRepository;

    // User input for book title
    private EditText bookTitleInput;

    // User input for author name
    private EditText authorInput;

    // User input for book genre
    private EditText bookGenreInput;

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
    private EditText seriesNameInput;

    // User input for series book number
    private EditText seriesNumberInput;

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

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Reading Recollections: Add Book");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Sets dateReadInput to the edit text field
        dateReadInput = findViewById(R.id.date_read_input);

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
                    (view, year1, month1, dayOfMonth1) -> {
                        LocalDate selectedDate = LocalDate.of(year1, month1 + 1, dayOfMonth1); // month is zero-indexed
                        dateReadInput.setText(selectedDate.format(dateFormatter));
                    },
                    year, month, dayOfMonth
            );
            // Displays the datePicker created above
            datePickerDialog.show();
        });
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