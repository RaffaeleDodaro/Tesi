package com.tesi.code;

import java.io.File;
import java.sql.*;

public class Database {

    private final Utility u = Utility.getInstance();
    private static Database instance = null;

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    private Connection c = null;

    public void openConnection(String type) {
        try {
            //Class.forName("org.sqlite.JDBC");
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

    public void insertIntoDBArticle(int year, String pages, String dblp, String title, int volume, String shortTitle,
                                    String url, String publisher, String series, String bookTitle, String doi) {

    }

    public void insertIntoDBInProceedings(int year, String pages, String dblp, String title, int volume, String shortTitle,
                                          String url, String address, String publisher, String series, String bookTitle, String doi) {
        String sql = "";
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + u.inProceedings + ".db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully " + c.getSchema());

            System.out.println("i am here");

            //momentaneo
            address = "casdcsda";
            shortTitle = "cascasvfsw";
            //fine momentaneo

            sql = "INSERT INTO ARTICLE VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
            /*sql = "INSERT INTO ARTICLE(" + u.inProceedingsAttributes + ") " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";*/
            PreparedStatement preparedStmt = c.prepareStatement(sql);
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

            preparedStmt.execute();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reading(String type) {
        Connection c = null;
        Statement stmt = null;
        String sql = "";
        try {
            Class.forName("org.sqlite.JDBC");

            if (type.equalsIgnoreCase(u.article))
                c = DriverManager.getConnection("jdbc:sqlite:" + u.article + ".db");
            else
                c = DriverManager.getConnection("jdbc:sqlite:" + u.inProceedings + ".db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ARTICLE;");

            while (rs.next()) {
                int year = rs.getInt("YEAR");
                String pages = rs.getString("PAGES");

                System.out.println("YEAR = " + year);
                System.out.println("PAGES = " + pages);


                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
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
                    "(YEAR INT PRIMARY KEY NOT NULL," +
                    "PAGES varchar(200) NOT NULL," +
                    "DBLP varchar(200) NOT NULL," +
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
                    "(ID INT PRIMARY KEY NOT NULL" +
                    "SURNAME varchar(200) NOT NULL," +
                    "NAME varchar(200) NOT NULL);";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS EDITOR" +
                    "(ID INT PRIMARY KEY NOT NULL" +
                    "SURNAME varchar(200) NOT NULL," +
                    "NAME varchar(200) NOT NULL);";
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch (Exception e)
        {
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