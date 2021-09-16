package com.tesi.code;

import java.io.File;
import java.sql.*;

public class Database {

    private final Utility u = Utility.getInstance();
    private static Database instance = null;
    private int lastAuthorId = -1;
    private int lastEditorId = -1;

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    private Connection c = null;

    public void openConnection(String type) {
        try {
            if (type.equalsIgnoreCase(u.article))
                c = DriverManager.getConnection("jdbc:sqlite:" + u.article + ".db");
            else
                c = DriverManager.getConnection("jdbc:sqlite:" + u.inProceedings + ".db");
            System.out.println("c.getCatalog() Opened database successfully " + c.getCatalog());

            if (c != null && !c.isClosed())
                System.out.println("Connected! " + c.getCatalog());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (c == null)
                return;
            c.close();
            c = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertIntoDBArticle(int year, String pages, String dblp, String title, int volume, String shortTitle,
                                    String url, String publisher, String series, String bookTitle, String doi) {

    }

    public void insertIntoDBInProceedings(int year, String pages, String dblp, String title, int volume, String shortTitle,
                                          String url, String address, String publisher, String series, String bookTitle, String doi,
                                          String author, String editor) {
        try {
            if (c == null || c.isClosed())
                return;

            //momentaneo
            address = "casdcsda";
            shortTitle = "cascasvfsw";
            //fine momentaneo


            PreparedStatement preparedStmt = c.prepareStatement("INSERT INTO ARTICLE VALUES(?,?,?,?,?,?,?,?,?,?,?,?);");
            preparedStmt.setInt(1, year);
            preparedStmt.setString(2, pages);
            preparedStmt.setString(3, dblp);
            preparedStmt.setString(4, title);
            preparedStmt.setInt(5, volume);
            preparedStmt.setString(6, shortTitle);
            preparedStmt.setString(7, url);
            preparedStmt.setString(8, address);
            preparedStmt.setString(9, publisher);
            preparedStmt.setString(10, series);
            preparedStmt.setString(11, bookTitle);
            preparedStmt.setString(12, doi);

            preparedStmt.executeUpdate();
            preparedStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reading(String type) {
        try {
            if (c == null || c.isClosed())
                return;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ARTICLE;");

            while (rs.next()) {
                int year = rs.getInt("YEAR");
                String pages = rs.getString("PAGES");
                String dblp = rs.getString("DBLP");
                String title = rs.getString("TITLE");
                int volume = rs.getInt("VOLUME");
                String shortTitle = rs.getString("SHORT_TITLE");
                String url = rs.getString("URL");
                String address = rs.getString("ADDRESS");
                String publisher = rs.getString("PUBLISHER");
                String series = rs.getString("SERIES");
                String bookTitle = rs.getString("BOOKTITLE");
                String doi = rs.getString("DOI");
                System.out.println("YEAR: " + year + " PAGES: " + pages + " TITLE: " + title + " VOLUME: " + volume + " SHORT TITLE: " + shortTitle + " URL: " + url +
                        " ADDRESS: " + address + " PUBLISHER: " + publisher + " SERIES: " + series + " BOOKTITLE: " + bookTitle + " DOI: " + doi + " DBLP: " + dblp);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTableInProceedings() {
        try {
            if (c == null || c.isClosed())
                return;

            String sql = "";
            System.out.println("qui");
            sql = "CREATE TABLE IF NOT EXISTS ARTICLE" +
                    "(YEAR INT NOT NULL," +
                    "PAGES varchar(200) NOT NULL," +
                    "DBLP varchar(200) PRIMARY KEY NOT NULL," +
                    "TITLE varchar(200) NOT NULL," +
                    "VOLUME INT NOT NULL," +
                    "SHORT_TITLE varchar(200) NOT NULL," +
                    "URL varchar(200) NOT NULL," +
                    "ADDRESS varchar(200) NOT NULL," +
                    "PUBLISHER varchar(200) NOT NULL," +
                    "SERIES varchar(200) NOT NULL," +
                    "BOOKTITLE varchar(200) NOT NULL," +
                    "DOI varchar(200) NOT NULL);";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS AUTHOR" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "SURNAME varchar(200) NOT NULL," +
                    "NAME varchar(200) NOT NULL);";
            lastAuthorId += 1;
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS EDITOR" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                    "SURNAME varchar(200) NOT NULL," +
                    "NAME varchar(200) NOT NULL);";
            lastEditorId += 1;
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTableArticle() {
        try {
            String sql = "";
            sql = "CREATE TABLE IF NOT EXISTS ARTICLE" +
                    "(YEAR INT PRIMARY KEY NOT NULL," +
                    "PAGES varchar(200) NOT NULL," +
                    "DBLP varchar(200) NOT NULL," +
                    "TITLE varchar(200) NOT NULL," +
                    "JOURNAL varchar(200) NOT NULL," +
                    "VOLUME INT NOT NULL," +
                    "SHORT_TITLE varchar(200)  NOT NULL," +
                    "URL varchar(200) NOT NULL," +
                    "DOI varchar(200) NOT NULL);";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS AUTHOR" +
                    "(ID INT PRIMARY KEY NOT NULL" +
                    "SURNAME varchar(200) NOT NULL," +
                    "NAME varchar(200) NOT NULL);";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

}