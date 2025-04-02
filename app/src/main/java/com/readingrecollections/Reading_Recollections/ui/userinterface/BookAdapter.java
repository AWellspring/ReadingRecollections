package com.readingrecollections.Reading_Recollections.ui.userinterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readingrecollections.Reading_Recollections.R;
import com.readingrecollections.Reading_Recollections.ui.entities.Book;

import java.util.List;

// RecyclerView.Adapter binds data to the RecyclerView
// This manages Books and displays their information in the RecyclerView
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books;
    private Context context;

    // Declares on click listener for when the user clicks an book title
    private OnBookClickListener listener;

    // Declares interface that requires implementing onBookClick
    // The book which the user clicks is passed as a parameter
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    // Takes a list of books and context, initializes the variables with this info
    public BookAdapter(List<Book> books, Context context, OnBookClickListener listener) {
        this.books = books;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Calls item_book.xml for styling for the RecyclerView items
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view, listener, books);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Retrieves the book at the specified position
        Book book = books.get(position);

        // Creates string for book title
        String bookTitle = book.getTitle();

        // Displays the book title in the view
        holder.textView.setText(bookTitle);

        // Sets click listener for each book
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Passes the book to the onBookClick method
                listener.onBookClick(book);
            }
        });
    }

    // Returns how many books are in the list
    // Tells the RecyclerView how many items to display
    @Override
    public int getItemCount() {
        return books.size();
    }

    // ViewHolder to display the views
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        // Constructs the ViewHolder by initializing the itemView
        BookViewHolder(View itemView, OnBookClickListener listener, List<Book> books) {
            super(itemView);
            // sets textView to the itemView
            textView = itemView.findViewById(R.id.recycler_view_books);

            // Sets an onClickListener to the itemView
            itemView.setOnClickListener(v -> {
                // Ensures the listener is valid and the position is valid
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    // Gets the book by the position in the adapter and triggers onBookClick
                    listener.onBookClick(books.get(getAdapterPosition()));
                }
            });
        }
    }
}
