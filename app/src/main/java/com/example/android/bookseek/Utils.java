package com.example.android.bookseek;

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
    private Utils(){
    }

    public static ArrayList<Book> getData(String urlString){
        URL url = createUrl(urlString);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error gettin input stream", e);
        }
        ArrayList<Book> books = extractBooks(jsonResponse);
        return books;
    }

    /**
     * Creates URL object from the url string
     * @param stringUrl url string for HTTP request
     * @return URL object
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL", e);
        }
        return url;
    }


    /**
     *
     * @param url URL object for Http request
     * @return JSON response downloaded from the net
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Gets string from InputStream
     * @param inputStream passed in stream from HTTP connection
     * @return InputStream result in String format
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractBooks(String stringJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(stringJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Book objects to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // root JSONObject
            JSONObject root = new JSONObject(stringJSON);

            // Extract “items” JSONArray
            JSONArray items = root.getJSONArray("items");


            // build up a list of Book  objects with the corresponding data.
            // Loop through each "item" in the array
            for (int i = 0; i < items.length(); i++){
                // Get book JSONObject at position i
                JSONObject book = items.getJSONObject(i);

                // Get “volumeInfo” JSONObject
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Extract “title” of the book
                String title = volumeInfo.getString("title");

                //Extract author(s) of the book
                String authors = "";
                // partially inspired by yowiputra
                // source: https://discussions.udacity.com/t/trouble-parsing-jsonarray-within-jsonobject/181438/5?u=mihaaly
                if (volumeInfo.has("authors")){
                    JSONArray authorsJA = volumeInfo.getJSONArray("authors");
                    String authorPartner;
                    StringBuffer authorsBuffer= new StringBuffer(40);

                    if (authorsJA.length() > 0 ){
                        for (int k=0; k < authorsJA.length(); k++) {
                            if (k > 0) {
                                authorsBuffer.append(", ");
                            }
                            authorPartner = authorsJA.getString(k);
                            authorsBuffer.append(authorPartner);
                        }
                        authors = authorsBuffer.toString();
                    }
                }




                // Extract the publisher of the book
                String publisher = volumeInfo.getString("publisher");

                // Extract publication date of the book
                String pubDate = volumeInfo.getString("publishedDate");

                // Extract the description of the book
                String description = volumeInfo.getString("description");

                // Create Book java object from title, authors, publisher, puDate, description
                Book currentBook = new Book(title, authors, publisher, pubDate, description);
                //Add current book to list of books
               books.add(currentBook);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }
}
