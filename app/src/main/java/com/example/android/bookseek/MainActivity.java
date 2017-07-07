package com.example.android.bookseek;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

    ListView mListView;

    // TextView that is displayed when the list is empty or there is no connection
    private TextView mEmptyStateTextView;

    // loading spinner
    private View mLoadingSpinner;

    // Get a reference to the ConnectivityManager to check state of network connectivity
    ConnectivityManager mConnectivityManager;
    // Get details on the currently active default data network
    NetworkInfo mActiveNetwork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find loading spinner
        mLoadingSpinner = findViewById(R.id.loading_spinner);
        // at start up hide spinner
        mLoadingSpinner.setVisibility(View.GONE);

        // find ListView in the layout
        mListView = (ListView) findViewById(R.id.list);
        // find empty state TextView
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyStateTextView.setText(R.string.start_up);
        // set empty state view onto the list
        mListView.setEmptyView(mEmptyStateTextView);


        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        // set adapter on the ListView to be populated with Book.java objects
        mListView.setAdapter(mAdapter);

        mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        mActiveNetwork = mConnectivityManager.getActiveNetworkInfo();

        if (mActiveNetwork != null && mActiveNetwork.isConnected()) {
            //https://discussions.udacity.com/t/searchview-loaders-data-lost-after-phone-is-rotated/299919/4?u=mihaaly
            // rowlandmtetezi: thanks
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.i(LOG_TAG, "TEST: initLoader");
            loaderManager.initLoader(0, null, MainActivity.this);
        } else {
            mLoadingSpinner.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_connection);
        }





////        // if there is a network connection get data
//        if (mActiveNetwork != null && mActiveNetwork.isConnected()){
//            mEmptyStateTextView.setVisibility(View.GONE);
//            mListView.setVisibility(View.VISIBLE);
//
//
//            // otherwise display an error message
//        } else {
//            mListView.setVisibility(View.GONE);
//            // update empty state view with error message
//            mEmptyStateTextView.setVisibility(View.VISIBLE);
//            mEmptyStateTextView.setText(R.string.no_connection);
//        }
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
        Log.i(LOG_TAG, "TEST: onCreateOptionsMenu");

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
                Log.i(LOG_TAG, "TEST: onQueryTextSubmit");

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
        mEmptyStateTextView.setVisibility(View.GONE);
        mLoadingSpinner.setVisibility(View.VISIBLE);
        return new BookLoader(MainActivity.this, mJsonResponse);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> books) {
        Log.i(LOG_TAG, "TEST: onLoadFinished");

        // load is done, spinner may gone
        mLoadingSpinner.setVisibility(View.GONE);

//        // no data found
//        mEmptyStateTextView.setVisibility(View.VISIBLE);
//        mEmptyStateTextView.setText(R.string.no_book);

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list then add them to the adapter's
        // data set. This will trigger the ListView to update.
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
