package com.tesi.code.Model;

import javafx.beans.property.StringProperty;

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

    public Article(int year, String pages, String dblp, String title, int volume, String shortTitle, String url, String journal, String doi) {
        this.year = year;
        this.pages = pages;
        this.dblp = dblp;
        this.title = title;
        this.volume = volume;
        this.shortTitle = shortTitle;
        this.url = url;
        this.journal = journal;
        this.doi = doi;
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
        System.out.println("quiii");
        return dblp;
    }

    public void dblpProperty() {
        System.out.println("quiii");

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
