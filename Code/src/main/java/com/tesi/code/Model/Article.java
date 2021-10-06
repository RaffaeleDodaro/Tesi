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
    private BooleanProperty check = new SimpleBooleanProperty(this, "check");
    private ArrayList<Editor> allEditors;

    public Article(String type, int year, String pages, String dblp, String title, int volume, String shortTitle, String url, String journal, String doi, ArrayList<Author> allAuthors, ArrayList<Editor> allEditors) {
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
        this.allEditors = allEditors;
    }

    public BooleanProperty checkProperty() {
        return check;
    }

    public BooleanProperty isCheck() {
        return check;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Author> getAllAuthors() {
        return allAuthors;
    }

    public String getJournal() {
        return journal;
    }

    public int getYear() {
        return year;
    }

    public String getPages() {
        return pages;
    }

    public String getDblp() {
        return dblp;
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

    public String getShortTitle() {
        return shortTitle;
    }

    public String getUrl() {
        return url;
    }

    public String getDoi() {
        return doi;
    }

    public String getAllEditorNameAndSurname() {
        StringBuilder s = new StringBuilder();

        for (Editor a : allEditors)
            s.append(a.getName()).append(" ").append(a.getSurname()).append(", ");

        System.out.println(s.deleteCharAt(s.length() - 2));
        return s.deleteCharAt(s.length() - 1).toString();
    }

    public String getAllAuthorNameAndSurname() {
        StringBuilder s = new StringBuilder();

        for (Author a : allAuthors)
            s.append(a.getName()).append(" ").append(a.getSurname()).append(", ");

        System.out.println(s.deleteCharAt(s.length() - 2));
        return s.deleteCharAt(s.length() - 1).toString();
    }

}
