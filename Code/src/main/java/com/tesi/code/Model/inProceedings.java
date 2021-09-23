package com.tesi.code.Model;

import java.util.ArrayList;

public class inProceedings extends Article{

    private String publisher;
    private String series;
    private String address;
    private String booktitle;

    public inProceedings(int year, String pages, String dblp, String title, int volume, String shortTitle, String url, String bookTitle, String doi, ArrayList<Author> allAuthors) {
        super(year, pages, dblp, title, volume, shortTitle, url, bookTitle, doi,allAuthors);
    }


    public String getBookTitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
