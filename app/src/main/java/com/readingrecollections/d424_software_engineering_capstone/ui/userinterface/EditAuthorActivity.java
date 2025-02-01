package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;

public class EditAuthorActivity extends AppCompatActivity {

    // Repository for database operations
    private Repository mRepository;

    // User input for author first name
    private EditText authorFirstNameInput;

    // User input for author middle name
    private EditText authorMiddleNameInput;

    // User input for author last name
    private EditText authorLastNameInput;

    // String for author name passed by intent on previous screen
    private String authorName;

    // Author to be retrieved from database using authorName
    private Author author = new Author();

    private TextInputLayout firstNameLayout;

    private TextInputLayout lastNameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_author);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes the repository
        mRepository = new Repository(getApplication());

        // Passes author name from the intent on the previous page
        authorName = getIntent().getStringExtra("author_name");

        // Sets authorFirstNameInput to the edit text field
        authorFirstNameInput = findViewById(R.id.author_first_name_input);

        // Sets authorMiddleNameInput to the edit text field
        authorMiddleNameInput = findViewById(R.id.author_middle_name_input);

        // Sets authorLastNameInput to the edit text field
        authorLastNameInput = findViewById(R.id.author_last_name_input);

        // Sets firstNameLayout to the layout field
        firstNameLayout = findViewById(R.id.author_first_name_input_layout);

        // Sets lastNameLayout to the layout field
        lastNameLayout = findViewById(R.id.author_last_name_input_layout);

        // Verifies the authorName is valid
        if (authorName != null) {
            mRepository.executor.execute(() -> {
                // Calls author from database by authorName
                author = mRepository.getAuthorByName(authorName);

                runOnUiThread(() -> {
                    // Sets the input text to the author's first name
                    if (author.getAuthorFirstName() != null) {
                        authorFirstNameInput.setText(author.getAuthorFirstName());
                    }
                    // Sets the input text to the author's middle name
                    if (author.getAuthorMiddleName() != null) {
                        authorMiddleNameInput.setText(author.getAuthorMiddleName());
                    }
                    // Sets the input text to the author's last name
                    if (author.getAuthorLastName() != null) {
                        authorLastNameInput.setText(author.getAuthorLastName());
                    }
                });
            });
        }

        // Initializes the update author button
        Button updateAuthorButton = findViewById(R.id.update_author_button);

        // Sets listener to trigger the addAuthor method when clicked
        updateAuthorButton.setOnClickListener(view -> updateAuthor());

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Author");
            // Enables the back button in the Action Bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    // Overrides the back button so that the authorName can be passed
    // This allows the user to go back to the author details they were just on
    @Override
    public boolean onSupportNavigateUp() {
        // Create an intent to go back to AuthorDetailsActivity
        Intent intent = new Intent(EditAuthorActivity.this, AuthorDetailsActivity.class);
        // Pass the author name
        intent.putExtra("author_name", authorName);
        startActivity(intent);
        finish();
        return true;
    }

    // Updates author in the database
    private void updateAuthor() {

        // Takes the user input, converts it to a string, and removes extra spaces
        String firstName = authorFirstNameInput.getText().toString().trim();
        String middleName = authorMiddleNameInput.getText().toString().trim();
        String lastName = authorLastNameInput.getText().toString().trim();

        // Used to check validity of firstName and lastName
        boolean isValid = true;

        // Clears existing error messages
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);

        // Validates first name
        if (firstName.isEmpty()) {
            firstNameLayout.setError("Please enter a first name");
            isValid = false;
        }

        // Validates last name
        if (lastName.isEmpty()) {
            lastNameLayout.setError("Please enter a last name");
            isValid = false;
        }

        // Update author if fields are valid
        if (isValid) {
            // Sets author name fields to user input
            mRepository.executor.execute(() -> {
                author.setAuthorFirstName(firstName);
                author.setAuthorMiddleName(middleName);
                author.setAuthorLastName(lastName);
                // Generates full name
                author.updateFullName();

                // Saves updated author to database
                mRepository.updateAuthor(author);

                // Updates authorName with the new name
                authorName = author.getAuthorFullName();

                // Returns to previous screen after update
                runOnUiThread(() -> {
                    Toast.makeText(EditAuthorActivity.this, "Author updated successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditAuthorActivity.this, AuthorDetailsActivity.class);
                    // Passes the new authorName to the AuthorDetails page
                    intent.putExtra("author_name", authorName);
                    startActivity(intent);
                    finish();
                });
            });
        }
    }
}