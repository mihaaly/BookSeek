package com.example.android.bookseek;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * AsyncTaskLoader to fetch data in background and preserve results throughout activity state changes
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    // url fro the query
    private String mUrl;

    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * fetch data in background thread
     * @return ArrayList of Book objects
     */
    @Override
    public ArrayList<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // feed data into ArryList of Book objects
        ArrayList<Book> books = Utils.getData(mUrl, getContext());
        return books;
    }
}
