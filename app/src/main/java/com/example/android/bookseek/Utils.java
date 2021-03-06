package com.example.android.bookseek;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods are gathered here
 */

public final class Utils {

    public static final String LOG_TAG = Utils.class.getName();

    // Constructor
    private Utils() {
    }

    /**
     * Method called by BookLoader.java's loadInBackground()
     *
     * @param urlString query url
     * @param context
     * @return list of custom Book objects
     */
    public static ArrayList<Book> getData(String urlString, Context context) {
        Log.i(LOG_TAG, "TEST: getData");

        // create URL object containing the query url
        URL url = createUrl(urlString);

        // downloaded raw jsonResponse
        String jsonResponse = null;

        // try to make HTTP request and download JSON data
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error getting input stream", e);
        }

        // extract desired information from the downloaded raw JSON data and put them in an ArrayList
        // of custom Book objects
        ArrayList<Book> books = extractBooks(jsonResponse, context);

        // return the list
        return books;
    }

    /**
     * Creates URL object from the url string
     *
     * @param stringUrl url query string for HTTP request
     * @return URL object
     */
    private static URL createUrl(String stringUrl) {

        // empty URL object
        URL url = null;

        // try to create URL object from the query string
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL", e);
        }

        // return URL object containing the query url
        return url;
    }

    /**
     * Creates connection, InputStream, downloads raw JSON data
     *
     * @param url URL object for Http request
     * @return raw JSON response downloaded from the net
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {

        // downloaded raw JSON data
        String jsonResponse = "";

        //
        if (url == null) {
            return jsonResponse;
        }

        // empty HttpURLConnection object for creating network connection
        HttpURLConnection urlConnection = null;

        // empty InputStream object for receiving the data
        InputStream inputStream = null;

        // try to establish connection, finally close stream, disconnect connection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // get response code, if it is ok, get data from InputStream
            if (urlConnection.getResponseCode() == 200) {
                // get stream
                inputStream = urlConnection.getInputStream();
                // get raw data into a String object
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        // return raw JSON data
        return jsonResponse;
    }

    /**
     * Gets string from InputStream
     *
     * @param inputStream passed in stream from HTTP connection
     * @return InputStream result in String format
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        // mutable String object for storing data temporarily
        StringBuilder output = new StringBuilder();

        // if there is an input stream read it and feed it into the StringBuilder
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // feed one line at a time
            String line = reader.readLine();
            // while there is a new line feed it into the StringBuilder and feed another line
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        // return raw JSON data as a single String object
        return output.toString();
    }

    /**
     * Parse JSON data
     *
     * @param stringJSON downloaded raw JSON data
     * @param context
     * @return ArrayList of custom Book objects
     */
    private static ArrayList<Book> extractBooks(String stringJSON, Context context) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(stringJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Book objects to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse if there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // root JSONObject
            JSONObject root = new JSONObject(stringJSON);

            // if any books were found proceed with the parsing
            if (root.has("items")) {
                // Extract “items” JSONArray
                JSONArray items = root.getJSONArray("items");

                // build up a list of Book  objects with the corresponding data.
                // Loop through each "items" in the array
                for (int i = 0; i < items.length(); i++) {
                    // Get book JSONObject at position i
                    JSONObject book = items.getJSONObject(i);

                    // Get “volumeInfo” JSONObject
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    // Extract “title” of the book
                    String keyLabelTitle = "title";
                    String title = getKeyValue(volumeInfo, keyLabelTitle, context, R.string.no_title);

                    /**
                     * Extract author(s) of the book
                     */
                    String authors;
                    // partially inspired by yowiputra
                    // source: https://discussions.udacity.com/t/trouble-parsing-jsonarray-within-jsonobject/181438/5?u=mihaaly
                    // if there is "authors" key
                    if (volumeInfo.has("authors")) {
                        // get JSONArray for authors
                        JSONArray authorsJA = volumeInfo.getJSONArray("authors");
                        // temporary storage
                        String authorPartner;
                        // StringBuffer object for building up authors list
                        StringBuffer authorsBuffer = new StringBuffer(40);
                        // if "authors" key is not empty
                        if (authorsJA.length() > 0) {
                            for (int k = 0; k < authorsJA.length(); k++) {
                                if (k > 0) {
                                    authorsBuffer.append(", ");
                                }
                                authorPartner = authorsJA.getString(k);
                                authorsBuffer.append(authorPartner);
                            }
                            authors = authorsBuffer.toString();
                            // if "authors" is empty
                        } else {
                            authors = context.getString(R.string.no_author);
                        }
                        // if there are no authors
                    } else {
                        authors = context.getString(R.string.no_author);
                    }

                    // Extract the publisher of the book
                    String keyLabelPublisher = "publisher";
                    String publisher = getKeyValue(volumeInfo, keyLabelPublisher, context, R.string.no_publisher);


                    // Extract publication date of the book
                    String keyLabelPubDate = "publishedDate";
                    String pubDate = getKeyValue(volumeInfo, keyLabelPubDate, context, R.string.no_pub_date);

                    // Extract the description of the book
                    String keyLabelDescription = "description";
                    String description = getKeyValue(volumeInfo, keyLabelDescription, context, R.string.no_description);

                    // Create Book java object from title, authors, publisher, puDate, description
                    Book currentBook = new Book(title, authors, publisher, pubDate, description);
                    //Add current book to list of books
                    books.add(currentBook);
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

    /**
     * Extracts value of a given key to String, or an error message
     *
     * @param volumeInfo JSONObject containing JSON keys with info on a book
     * @param keyLabel   label of a JSON key (e.g. "title")
     * @param context
     * @param resourceID of the string in case the JSON key is missing
     * @return String with the value of the JSON key or the error message
     */
    private static String getKeyValue(JSONObject volumeInfo, String keyLabel, Context context, int resourceID) {
        // it will contain or the value of the JSON key or an error message
        String keyValue = "";
        try {
            // if the searched key exists
            if (volumeInfo.has(keyLabel)) {
                keyValue = volumeInfo.getString(keyLabel);
            } else { // get an error message
                keyValue = context.getString(resourceID);
            }
        } catch (JSONException j) {
            Log.e(LOG_TAG, "Problem retrieving JSON results");
        }
        return keyValue;
    }
}
