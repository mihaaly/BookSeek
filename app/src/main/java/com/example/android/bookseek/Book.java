package com.example.android.bookseek;

/**
 * Class to hold queried book information
 */

public class Book {

    // Title of the book
    private String mTitle;

    // Author(s) of the book
    private String mAuthor;

    // Publisher of the book
    private String mPublisher;

    // Date of publication
    private String mDate;

    // Description of the book
    private String mDescription;

    /**
     * constructor of the class
     * @param title of the book
     * @param author of the book
     * @param publisher of the book
     * @param description of the book
     */
    public Book (String title, String author, String publisher, String date, String description){
        mTitle = title;
        mAuthor = author;
        mPublisher = publisher;
        mDate = date;
        mDescription = description;
    }


    /**
     * Gets the title of the book.
     * @return current value of mTitle.
     */
    public String getTitle(){
        return mTitle;
    }


    /**
     * Gets the author(s) of the book.
     * @return current value of mAuthor
     */
    public String getAuthor(){
        return mAuthor;
    }

    /**
     * Gets the publisher of the book.
     * @return current value of mPublisher
     */
    public String getPublisher(){
        return mPublisher;
    }

    /**
     * Gets the date of publication.
     * @return current value of mDate
     */
    public String getDate(){
        return mDate;
    }

    /**
     * Gets the description book.
     * @return current value of mDescription
     */
    public String getDescription(){
        return mDescription;
    }

}