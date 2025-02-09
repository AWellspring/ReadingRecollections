package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_AUTHOR = 0;
    private static final int TYPE_BOOK = 1;

    private List<Item> items = new ArrayList<>();
    private Context context;

    private final OnBookClickListener onBookClickListener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public ItemAdapter(Context context, OnBookClickListener onBookClickListener) {
        this.context = context;
        this.onBookClickListener = onBookClickListener;
    }

    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Author) {
            return TYPE_AUTHOR;
        } else {
            return TYPE_BOOK;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_AUTHOR) {
            View view = inflater.inflate(R.layout.item_book_header, parent, false);
            return new AuthorViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_book2, parent, false);
            return new BookViewHolder(view, onBookClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);
        if (holder instanceof AuthorViewHolder) {
            ((AuthorViewHolder) holder).bind((Author) item);
        } else if (holder instanceof BookViewHolder) {
            ((BookViewHolder) holder).bind((Book) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class AuthorViewHolder extends RecyclerView.ViewHolder {
        TextView authorName;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.header_text);
        }

        public void bind(Author author) {
            authorName.setText(author.getAuthorFullName());

        }
    }

    // BookViewHolder with onClick handling
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle;

        // Now accepting onBookClickListener in the constructor
        public BookViewHolder(View itemView, final OnBookClickListener onBookClickListener) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.recycler_view_books);

            // Set up the click listener for the book title
            itemView.setOnClickListener(v -> {
                Item item = (Item) itemView.getTag();
                if (item instanceof Book && onBookClickListener != null) {
                    onBookClickListener.onBookClick((Book) item);
                }
            });
        }

        public void bind(Book book) {
            bookTitle.setText(book.getTitle());
            itemView.setTag(book);  // Set the book as the tag of the itemView
        }
    }
}