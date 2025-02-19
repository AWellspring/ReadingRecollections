package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class AddAuthorActivity extends AppCompatActivity {

    // Repository for database operations
    private Repository mRepository;

    // User input for author first name
    private EditText authorFirstNameInput;

    // User input for author middle name
    private EditText authorMiddleNameInput;

    // User input for author last name
    private EditText authorLastNameInput;

    private TextInputLayout firstNameLayout;

    private TextInputLayout lastNameLayout;

    private String fromPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_author);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializes the repository
        mRepository = new Repository(getApplication());

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

        fromPage = getIntent().getStringExtra("view_authors");

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddAuthorActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // Initializes the save author button
        Button saveAuthorButton = findViewById(R.id.save_author_button);

        // Sets listener to trigger the addAuthor method when clicked
        saveAuthorButton.setOnClickListener(view -> addAuthor());

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Author");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Overrides the back button to check if an intent was passed
    // If it was, the user came from the ViewAuthors page and is returned there
    // If not, the user came from the Home page and is returned there
    @Override
    public boolean onSupportNavigateUp() {
        if (fromPage != null) {
            // Create an intent to go back to ViewAuthorsActivity
            Intent intent = new Intent(AddAuthorActivity.this, ViewAuthorsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        else {
            // Create an intent to go back to HomeActivity
            Intent intent = new Intent(AddAuthorActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
    }

    // Adds author to database
    private void addAuthor() {

        // Takes the user input, converts it to a string, and removes extra spaces
        // Also calls the sanitizeInput defined below to remove special characters which might
        // be used in SQL injection
        String firstName = sanitizeInput(authorFirstNameInput.getText().toString().trim());
        String middleName = sanitizeInput(authorMiddleNameInput.getText().toString().trim());
        String lastName = sanitizeInput(authorLastNameInput.getText().toString().trim());

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

        // Creates author if fields are valid
        if (isValid) {
            // Sets author name fields to user input
            mRepository.executor.execute(() -> {
                Author author = new Author();
                author.setAuthorFirstName(firstName);
                author.setAuthorMiddleName(middleName);
                author.setAuthorLastName(lastName);
                // Generates full name
                author.updateFullName();

                // Passes author to insertAuthorIfNotExists in Repository
                // Passes on 2 runnables, onSuccess and onAuthorExists
                mRepository.insertAuthorIfNotExists(author,
                        () -> {
                            // Author does not exist in database
                            // Author is saved, success Toast is displayed
                            runOnUiThread(() -> {
                                Toast.makeText(AddAuthorActivity.this, author.getAuthorFullName() + " saved to your library!", Toast.LENGTH_LONG).show();
                                if (fromPage != null) {
                                    // Returns the user to ViewAuthorsActivity
                                    Intent intent = new Intent(AddAuthorActivity.this, ViewAuthorsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    // Returns the user to HomeActivity
                                    Intent intent = new Intent(AddAuthorActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        },
                        () -> {
                            // Author already exists in database
                            // Author is not saved, Toast is displayed
                            runOnUiThread(() -> {
                                Toast.makeText(AddAuthorActivity.this, author.getAuthorFullName() + " is already in your library.", Toast.LENGTH_LONG).show();
                            });
                        });
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

    // Remove the characters ", `, and ; to prevent SQL injection
    private String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[\"`;%]", "");
    }
}