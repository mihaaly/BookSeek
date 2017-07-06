package com.example.android.bookseek;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Book>> {


    final static String LOG_TAG = "MainActivity";
    // BookAdapter.java custom ArrayAdapter
    private BookAdapter mAdapter;

    // base url
    final static String API_ENDPOINT = "https://www.googleapis.com/books/v1/volumes?maxResults=40&q=";
    // final query url
    String mJsonResponse;




//    private static final String JSON_RESPONSE =
//            "https://www.googleapis.com/books/v1/volumes?maxResults=40&q=android";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find ListView in the layout
        ListView listView = (ListView) findViewById(R.id.list);
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        // set adapter on the ListView to be populated with Book.java objects
        listView.setAdapter(mAdapter);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        Log.i(LOG_TAG, "TEST: initLoader");
        loaderManager.initLoader(0, null, MainActivity.this);


    }


    /**
     * Inflates App bar (e.g. SearchView action view).
     * @param menu toolbar_menu.xml
     * @return SearchView
     *
     * Source:
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate toolbar_menu.xml
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        // find menu item
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        // find action view SearchView
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // set a custom search hint on it
        searchView.setQueryHint(getString(R.string.search_book));




        // monitor and get strings typed into SearchView
        // partially sourced from: https://www.youtube.com/watch?v=9OWmnYPX1uc
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // reset previous query url
                mJsonResponse = "";

                // create query url
                mJsonResponse = API_ENDPOINT + query;
                // fills in spaces for instance, 2nd argument is for escaping character not to be touched in the url
                // source: https://discussions.udacity.com/t/book-listing-app-construct-the-query-url/203167/3?u=mihaaly
                mJsonResponse = Uri.encode(mJsonResponse, ":/?&=");

                // reset loader from previous data
                Log.i(LOG_TAG, "TEST: restartLoader");
                getLoaderManager().restartLoader(0, null, MainActivity.this);

                // return false for automatic keyboard hide
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Override LoaderCallback methods:
     */
    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: onCreateLoader");

        return new BookLoader(MainActivity.this, mJsonResponse);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        Log.i(LOG_TAG, "TEST: onLoadFinished");
        // Clear the adapter of previous book data
        mAdapter.clear();

        // if there is a valid list of Books add them to the adapter
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset");
        // Reset Adapter
        mAdapter.clear();
    }
}
