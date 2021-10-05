package com.tesi.code.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;

public class Article {
    private int year;
    private String pages;
    private String dblp;
    private String title;
    private int volume;
    private String shortTitle;
    private String url;
    private String doi;
    private String journal;
    private ArrayList<Author> allAuthors;
    private String type;
    private BooleanProperty check=new SimpleBooleanProperty(this,"check");
    
    public Article(String type, int year, String pages, String dblp, String title, int volume, String shortTitle, String url, String journal, String doi, ArrayList<Author> allAuthors) {
        this.type = type;
        this.year = year;
        this.pages = pages;
        this.dblp = dblp;
        this.title = title;
        this.volume = volume;
        this.shortTitle = shortTitle;
        this.url = url;
        this.doi = doi;
        this.allAuthors = allAuthors;
        this.journal = journal;
    }


    public BooleanProperty checkProperty() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check.set(check);
    }

    public BooleanProperty isCheck() {
        return check;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Author> getAllAuthors() {
        System.out.println("RIGA 56 article size: " + allAuthors.size());
        return allAuthors;
    }

    public String getJournal() {
        return journal;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getDblp() {
        return dblp;
    }

    public void setDblp(String dblp) {
        this.dblp = dblp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }
}
