package com.example.android.bookseek;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    // BookAdapter.java custom ArrayAdapter
    private BookAdapter mAdapter;

    private static final String JSON_RESPONSE =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=100";


    /**
     * Inflates App bar (e.g. SearchView action view).
     * @param menu toolbar_menu.xml
     * @return SearchView
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate toolbar_menu.xml
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        // find SearchView in the menu
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // set a custom search hint on it
        searchView.setQueryHint(getString(R.string.search_book));
        return super.onCreateOptionsMenu(menu);
    }

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
        loaderManager.initLoader(0, null, this);

    }

    /**
     * Override LoaderCallback methods:
     */
    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(MainActivity.this, JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        // Clear the adapter of previous book data
        mAdapter.clear();

        // if there is a valid list of Books add them to the adapter
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        // Reset Adapter
        mAdapter.clear();
    }
}
