package com.tesi.code.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;

public class inProceedings extends Article {

    private String publisher;
    private String series;
    private String address;
    private String booktitle;
    private ArrayList<Editor> allEditors = new ArrayList<>();
    private BooleanProperty check = new SimpleBooleanProperty(this, "check");

    public inProceedings(String type, int year, String pages, String dblp, String title, int volume, String shortTitle, String url, String booktitle,
                         String doi, ArrayList<Author> allAuthors, String publisher, String series, String address, ArrayList<Editor> editors) {
        super(type, year, pages, dblp, title, volume, shortTitle, url, "", doi, allAuthors, editors);
        this.publisher = publisher;
        this.series = series;
        this.address = address;
        this.booktitle = booktitle;
        this.allEditors = editors;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSeries() {
        return series;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<Editor> getAllEditors() {
        return allEditors;
    }
}