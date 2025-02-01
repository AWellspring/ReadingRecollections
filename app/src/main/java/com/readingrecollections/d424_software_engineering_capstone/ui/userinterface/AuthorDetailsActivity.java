package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.readingrecollections.d424_software_engineering_capstone.R;

public class AuthorDetailsActivity extends AppCompatActivity {

    private TextView authorNameView;

    private String authorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_author_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authorNameView = findViewById(R.id.text_author_name);

        authorName = getIntent().getStringExtra("author_name");
        authorNameView.setText(authorName);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Author Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}