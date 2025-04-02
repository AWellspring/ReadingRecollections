package com.readingrecollections.Reading_Recollections.ui.userinterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.readingrecollections.Reading_Recollections.R;
import com.readingrecollections.Reading_Recollections.ui.database.Repository;
import com.readingrecollections.Reading_Recollections.ui.entities.Author;
import com.readingrecollections.Reading_Recollections.ui.entities.Book;

import java.util.List;

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

    private String fromSearch;

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

        // Passes fromSearch from the intent on the Search page
        fromSearch = getIntent().getStringExtra("from_search");

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

        // Initializes the delete author button
        Button deleteAuthorButton = findViewById(R.id.delete_author_button);

        // Sets listener to trigger the deleteAuthor method when clicked
        deleteAuthorButton.setOnClickListener(view -> deleteAuthor());

        // Initializes the home button
        ImageButton homeButton = findViewById(R.id.home_button);

        // Sets listener to return the user to Home when homeButton is clicked
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditAuthorActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

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
        if (fromSearch != null) {
            intent.putExtra("from_search", fromSearch);
        }
        // Pass the author name
        intent.putExtra("author_name", authorName);
        startActivity(intent);
        finish();
        return true;
    }

    // Updates author in the database
    private void updateAuthor() {

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
                    Toast.makeText(EditAuthorActivity.this, authorName + " updated in your library!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditAuthorActivity.this, AuthorDetailsActivity.class);
                    // Passes the new authorName to the AuthorDetails page
                    if (fromSearch != null) {
                        intent.putExtra("from_search", fromSearch);
                    }
                    intent.putExtra("author_name", authorName);
                    startActivity(intent);
                    finish();
                });
            });
        }
    }

    // Checks to see if the user can delete the author
    // Triggers a confirmation to make sure the user wants to delete the author
    private void deleteAuthor() {
        mRepository.executor.execute(() -> {
            // Check if there are books associated with the author
            List<Book> booksByAuthor = mRepository.getBooksByAuthor(author.getId());

            // If books exist, trigger message saying the author cannot be deleted
            if (!booksByAuthor.isEmpty()) {
                runOnUiThread(() -> cannotDeleteDialog());
            }
            // If there are no books by author, trigger delete confirmation
            else {
                runOnUiThread(() -> deleteConfirmationDialog());
            }
        });
    }

    // Displays a pop up message saying the books cannot be deleted
    // Explains that there are books by this author in the user's library
    private void cannotDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cannot Delete")
                .setMessage("You have books by " + authorName + " in your library.")
                // Dismisses dialog
                .setPositiveButton("Go back", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Displays a pop up message verifying user wants to delete author
    private void deleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Author")
                .setMessage("Are you sure you want to delete " + authorName + " from your library?")
                // Triggers the author deletion
                .setPositiveButton("Delete", (dialog, which) -> confirmDeleteAuthor())
                // Dismisses dialog
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Deletes author from the database upon user confirmation
    private void confirmDeleteAuthor() {
        mRepository.executor.execute(() -> {
            mRepository.deleteAuthor(author);

            // Return to View Authors page with success message
            runOnUiThread(() -> {
                Toast.makeText(EditAuthorActivity.this, authorName + " deleted from your library!", Toast.LENGTH_LONG).show();
                if (fromSearch != null) {
                    Intent intent = new Intent(EditAuthorActivity.this, SearchActivity.class);
                    intent.putExtra("from_search", fromSearch);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(EditAuthorActivity.this, ViewAuthorsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        });
    }

    // Remove the characters ", `, and ; to prevent SQL injection
    private String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[\"`;%]", "");
    }
}