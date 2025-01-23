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

    private Repository mRepository;

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

        mRepository = new Repository(getApplication());

        authorNameInput = findViewById(R.id.author_name_input);

        Button saveAuthorButton = findViewById(R.id.save_author_button);

        saveAuthorButton.setOnClickListener(view -> addAuthor());

        // Sets the Action Bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Reading Recollections: Add Author");
        }
        // Enables the back button in the Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addAuthor() {

        String authorName = authorNameInput.getText().toString().trim();

        if (!authorName.isEmpty()) {

            mRepository.executor.execute(() -> {

                Author existingAuthor = mRepository.getAuthorByName(authorName);

                runOnUiThread(() -> {

                    if (existingAuthor != null) {
                        Toast.makeText(this, "Author already in your library", Toast.LENGTH_SHORT).show();
                    } else {
                        mRepository.insertAuthorIfNotExists(authorName, () -> {
                            runOnUiThread(() -> {
                                Toast.makeText(AddAuthorActivity.this, "Author saved to your library!", Toast.LENGTH_SHORT).show();

                                finish();
                            });
                        });
                    }
                });
            });
        }
        else{
            Toast.makeText(this, "Please enter the author's name.", Toast.LENGTH_SHORT).show();
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