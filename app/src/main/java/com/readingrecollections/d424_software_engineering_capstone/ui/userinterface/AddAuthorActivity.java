package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.database.Repository;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;

public class AddAuthorActivity extends AppCompatActivity {

    // Repository for database operations
    private Repository mRepository;

    // User input for author name
    private EditText authorNameInput;

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

        // Sets authorNameInput to the user input
        authorNameInput = findViewById(R.id.author_name_input);

        // Initializes the save author button
        Button saveAuthorButton = findViewById(R.id.save_author_button);

        // Sets listener to trigger the addAuthor method when clicked
        saveAuthorButton.setOnClickListener(view -> addAuthor());

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Reading Recollections: Add Author");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Adds author to database
    private void addAuthor() {

        // Takes the user input, converts it to a string, and removes extra spaces
        String authorName = authorNameInput.getText().toString().trim();

        // Checks whether the string is empty
        if (!authorName.isEmpty()) {

            // Uses a background thread
            mRepository.executor.execute(() -> {

                // Checks database for author by name
                Author existingAuthor = mRepository.getAuthorByName(authorName);

                // Returns to main thread for UI display
                runOnUiThread(() -> {

                    // If the author is in the database, display message
                    if (existingAuthor != null) {
                        Toast.makeText(this, "Author already in your library", Toast.LENGTH_LONG).show();
                    }

                    // Adds the author to the database
                    else {
                        // Runs in a background thread
                        mRepository.insertAuthorIfNotExists(authorName, () -> {

                            // Returns to main thread for UI display
                            runOnUiThread(() -> {
                                Toast.makeText(AddAuthorActivity.this, "Author saved to your library!", Toast.LENGTH_LONG).show();

                                // Returns user to previous page
                                finish();
                            });
                        });
                    }
                });
            });
        }

        // Prompts user to enter a string if field is empty
        else{
            Toast.makeText(this, "Please enter the author's name.", Toast.LENGTH_LONG).show();
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