package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.readingrecollections.d424_software_engineering_capstone.R;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Author;
import com.readingrecollections.d424_software_engineering_capstone.ui.entities.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> searchResults = new ArrayList<>();
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onAuthorClick(Author author);
        void onBookClick(Book book);
    }

    public SearchAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public void setSearchResults(List<Item> items) {
        this.searchResults = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Item item = searchResults.get(position);
        if (item instanceof Author) {
            return 0; // Author Type
        } else if (item instanceof Book) {
            return 1; // Book Type
        }
        return -1; // Fallback
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == 0) { // Author
            View view = inflater.inflate(R.layout.item_author, parent, false);
            return new AuthorViewHolder(view);
        } else if (viewType == 1) { // Book
            View view = inflater.inflate(R.layout.item_book, parent, false);
            return new BookViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = searchResults.get(position);

        if (holder instanceof AuthorViewHolder) {
            Author author = (Author) item;
            ((AuthorViewHolder) holder).authorName.setText(author.getAuthorFullName());
            holder.itemView.setOnClickListener(v -> {
                onItemClickListener.onAuthorClick(author);
            });

        } else if (holder instanceof BookViewHolder) {
            Book book = (Book) item;
            ((BookViewHolder) holder).bookTitle.setText(book.getTitle());
            holder.itemView.setOnClickListener(v -> {
                onItemClickListener.onBookClick(book);
            });
        }
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    static class AuthorViewHolder extends RecyclerView.ViewHolder {
        TextView authorName;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.author_name);
            }
        }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.recycler_view_books);
        }
    }
}
