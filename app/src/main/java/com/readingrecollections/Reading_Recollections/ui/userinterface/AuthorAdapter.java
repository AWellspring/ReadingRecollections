package com.readingrecollections.Reading_Recollections.ui.userinterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readingrecollections.Reading_Recollections.R;
import com.readingrecollections.Reading_Recollections.ui.entities.Author;

import java.util.List;

// RecyclerView.Adapter binds data to the RecyclerView
// This manages Authors and displays their information in the RecyclerView
public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private List<Author> authors;
    private Context context;
    private boolean isLastNameFirst = false;

    // Declares on click listener for when the user clicks an author name
    private OnAuthorClickListener listener;

    // Declares interface that requires implementing onAuthorClick
    // The author which the user clicks is passed as a parameter
    public interface OnAuthorClickListener {
        void onAuthorClick(Author author);
    }

    // Takes a list of authors and context, initializes the variables with this info
    public AuthorAdapter(List<Author> authors, Context context, OnAuthorClickListener listener) {
        this.authors = authors;
        this.context = context;
        this.listener = listener;
    }

    // Sets whether user wants to sort by first name or last name
    public void setLastNameFirst(boolean isLastNameFirst) {
        this.isLastNameFirst = isLastNameFirst;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Calls item_author.xml for styling for the RecyclerView items
        View view = LayoutInflater.from(context).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view, listener, authors);
    }

    // Binds data to an author based on position
    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        // Retrieves the author at the specified position
        Author author = authors.get(position);

        // Creates empty string to pass name format to
        String formattedName;

        // If the user selects a view sorted by last name
        if (isLastNameFirst) {
            // Format as "LastName, FirstName MiddleName"
            formattedName = author.getAuthorLastName();
            if (!author.getAuthorFirstName().isEmpty()) {
                formattedName += ", " + author.getAuthorFirstName();
            }
            if (!author.getAuthorMiddleName().isEmpty()) {
                formattedName += " " + author.getAuthorMiddleName();
            }
        }
        // If the user selects a view sorted by first name
        else {
            // Default "FirstName MiddleName LastName"
            formattedName = author.getAuthorFirstName();
            if (!author.getAuthorMiddleName().isEmpty()) {
                formattedName += " " + author.getAuthorMiddleName();
            }
            if (!author.getAuthorLastName().isEmpty()) {
                formattedName += " " + author.getAuthorLastName();
            }
        }

        // Displays the formatted author name
        holder.textView.setText(formattedName);

        // Sets click listener for each author
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Passes the author to the onAuthorClick
                listener.onAuthorClick(author);
            }
        });
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
        AuthorViewHolder(View itemView, OnAuthorClickListener listener, List<Author> authors) {
            super(itemView);
            // sets textView to the itemView
            textView = itemView.findViewById(R.id.author_name);

            // Sets an onClickListener to the itemView
            itemView.setOnClickListener(v -> {
                // Ensures the listener is valid and the position is valid
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    // Gets the author by the position in the adapter and triggers onAuthorClick
                    listener.onAuthorClick(authors.get(getAdapterPosition()));
                }
            });
        }
    }
}
