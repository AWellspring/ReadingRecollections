package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;

import java.util.List;

// RecyclerView.Adapter binds data to the RecyclerView
// This manages Authors and displays their information in the RecyclerView
public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private List<Author> authors;
    private Context context;

    // Takes a list of authors and context, initializes the variables with this info
    public AuthorAdapter(List<Author> authors, Context context) {
        this.authors = authors;
        this.context = context;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Calls item_author.xml for styling for the RecyclerView items
        View view = LayoutInflater.from(context).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    // Binds data to an author based on position
    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        // Retrieves the author at the specified position
        Author author = authors.get(position);
        // Sets the text view to the author's full name
        holder.textView.setText(author.getAuthorFullName());

        //holder.itemView.setOnClickListener(v -> {
            //Intent intent = new Intent(context, AuthorDetailActivity.class);
            //intent.putExtra("AUTHOR_ID", author.getId());
            //context.startActivity(intent);
        //});
    }

    // Returns how many authors are in the list
    // Tells the RecyclerView how many items to display
    @Override
    public int getItemCount() {
        return authors.size();
    }

    // ViewHolder to display the views
    static class AuthorViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        // Constructs the ViewHolder by initializing the itemView
        AuthorViewHolder(View itemView) {
            super(itemView);
            // sets textView to the itemView
            textView = itemView.findViewById(R.id.author_name);
        }
    }
}
