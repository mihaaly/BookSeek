package com.example.android.bookseek;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for an ArrayList of custom Book objects.
 */

public class BookAdapter extends ArrayAdapter<Book>{

    /**
     * Constructor
     * @param context
     * @param books list of Book.java objects to be displayed
     */
    public BookAdapter(Activity context, ArrayList<Book> books){
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // pass in the custom layout for a list item
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }

        // get current Book.java element
        Book currentBook = getItem(position);

        /**
         * Author of the book
         */
        // find TextView displaying the author of the book
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.textViewAuthor);
        // get title of the currentBook and set it on that TextView
        authorTextView.setText(currentBook.getAuthor());

        /**
         * Title of the book
         */
        // find TextView displaying the title of the book
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.textViewTitle);
        // get title of the currentBook and set it on that TextView
        titleTextView.setText(currentBook.getTitle());

        /**
         * Publisher of the book
         */
        // find TextView displaying the publisher of the book
        TextView publisherTextView = (TextView) listItemView.findViewById(R.id.textViewPublisher);
        // get title of the currentBook and set it on that TextView
        publisherTextView.setText(currentBook.getPublisher());

        /**
         * Year of publication
         */
        // find TextView displaying the book's publication date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.textViewDate);
        // get date of publication of the currentBook and set it on that TextView
        dateTextView.setText(currentBook.getDate());

        /**
         * Description of the book
         */
        // find TextView displaying the book's description
        TextView blurbTextView = (TextView) listItemView.findViewById(R.id.textViewBlurb);
        // get date of publication of the currentBook and set it on that TextView
        blurbTextView.setText(currentBook.getDescription());

        return listItemView;
    }
}
