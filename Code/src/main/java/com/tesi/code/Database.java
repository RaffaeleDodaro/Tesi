package com.tesi.code;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    private final Utility u = new Utility();

    public void openConnection(String type) throws ClassNotFoundException {
        Connection c = null;
        Statement stmt = null;
        String sql = "";
        try {
            Class.forName("org.sqlite.JDBC");
            if (type.equalsIgnoreCase(u.article))
                c = DriverManager.getConnection("jdbc:sqlite:article.db");
            else
                c = DriverManager.getConnection("jdbc:sqlite:inproceedings.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            if (type.equalsIgnoreCase(u.article))
                createDBArticle(sql, stmt);
            else
                createDBInProceedings(sql, stmt);
            /*stmt.executeUpdate(sql);
            stmt.close();
            c.close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DB aperto");
    }

    public void insertIntoDB(File file, String type, String journal, String title, String short_title, String address) {
        String sql="";
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            if (type.equalsIgnoreCase(u.article))
                c = DriverManager.getConnection("jdbc:sqlite:"+u.article+".db");
            else
                c = DriverManager.getConnection("jdbc:sqlite:"+u.inProceedings+".db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully "+c.getSchema());

            stmt = c.createStatement();
            if (type.equalsIgnoreCase(u.article)) {
                System.out.println("i am here");
                //sql = "INSERT INTO ARTICLE (" + u.articleAttributes + ") " +
                //        "VALUES(2000,\"90--106\",\"confpadlAlvianoDZ21\",\"Data Validation Meets Answer Set\",\"Fundam. Informaticae\",176,\"aaa\",\"http:++\",\"10.3233\")";

                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.commit();
            c.close();
        }
        catch(Exception e){e.printStackTrace();}
    }

    public void reading(String type) {
        Connection c = null;
        Statement stmt = null;
        String sql = "";
        try {
            Class.forName("org.sqlite.JDBC");

            if (type.equalsIgnoreCase(u.article))
                c = DriverManager.getConnection("jdbc:sqlite:"+u.article+".db");
            else
                c = DriverManager.getConnection("jdbc:sqlite:"+u.inProceedings+".db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ARTICLE;" );

            while ( rs.next() ) {
                int year = rs.getInt("YEAR");
                String  pages = rs.getString("PAGES");

                System.out.println( "YEAR = " + year );
                System.out.println( "PAGES = " + pages );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        }
        catch (Exception e){e.printStackTrace();}
    }

    private void createDBInProceedings(String sql, Statement stmt) {
        try {
            sql = "CREATE TABLE IF NOT EXISTS ARTICLE" +
                    "(YEAR INT PRIMARY KEY NOT NULL," +
                    "PAGES TEXT NOT NULL," +
                    "DBLP TEXT NOT NULL," +
                    "TITLE TEXT NOT NULL," +
                    "JOURNAL TEXT NOT NULL," +
                    "VOLUME INT NOT NULL," +
                    "SHORT_TITLE TEXT NOT NULL," +
                    "URL TEXT NOT NULL," +
                    "ADDRESS TEXT NOT NULL," +
                    "PUBLISHER TEXT NOT NULL," +
                    "SERIES TEXT NOT NULL," +
                    "BOOKTITLE TEXT NOT NULL," +
                    "DOI TEXT NOT NULL)";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE AUTHOR" +
                    "(ID INT PRIMARY KEY NOT NULL" +
                    "SURNAME TEXT NOT NULL," +
                    "NAME TEXT NOT NULL)";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE EDITOR" +
                    "(ID INT PRIMARY KEY NOT NULL" +
                    "SURNAME TEXT NOT NULL," +
                    "NAME TEXT NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void createDBArticle(String sql, Statement stmt) {
        try {
            sql = "CREATE TABLE IF NOT EXISTS ARTICLE" +
                    "(YEAR INT PRIMARY KEY NOT NULL," +
                    "PAGES TEXT NOT NULL," +
                    "DBLP TEXT NOT NULL," +
                    "TITLE TEXT NOT NULL," +
                    "JOURNAL TEXT NOT NULL," +
                    "VOLUME INT NOT NULL," +
                    "SHORT_TITLE TEXT  NOT NULL," +
                    "URL TEXT NOT NULL," +
                    "DOI TEXT NOT NULL)";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE AUTHOR" +
                    "(ID INT PRIMARY KEY NOT NULL" +
                    "SURNAME TEXT NOT NULL," +
                    "NAME TEXT NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

}