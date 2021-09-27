package com.tesi.code.Model;

import java.util.ArrayList;

public class inProceedings extends Article{

    private String publisher;
    private String series;
    private String address;
    private String booktitle;
    private String journal;
    private ArrayList editors=new ArrayList<Editor>();

    public inProceedings(int year, String pages, String dblp, String title, int volume, String s, String shortTitle, String url, String doi, ArrayList<Author> allAuthors, String publisher, String series, String address, String booktitle, ArrayList<Editor> editors) {
        super(year, pages, dblp, title, volume, shortTitle, url,"", doi, allAuthors);
        this.publisher = publisher;
        this.series = series;
        this.address = address;
        this.booktitle = booktitle;
        this.editors=editors;
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
