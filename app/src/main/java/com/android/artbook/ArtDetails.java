package com.android.artbook;

import android.graphics.Bitmap;

public class ArtDetails {

    private String artName, artistName, bookSummary;
    private Bitmap bookImage;

    public ArtDetails(String artName, String artistName, String bookSummary, Bitmap bookImage) {
        this.artName = artName;
        this.artistName = artistName;
        this.bookSummary = bookSummary;
        this.bookImage = bookImage;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getBookSummary() {
        return bookSummary;
    }

    public void setBookSummary(String bookSummary) {
        this.bookSummary = bookSummary;
    }

    public Bitmap getBookImage() {
        return bookImage;
    }

    public void setBookImage(Bitmap bookImage) {
        this.bookImage = bookImage;
    }
}

