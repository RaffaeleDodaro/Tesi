package com.tesi.code;

import com.tesi.code.Parser.GenericParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {

    private static Database instance = null;
    //private ParserInProceedings pip = ParserInProceedings.getInstance();
    private GenericParser gp = GenericParser.getInstance();
    private Connection c = null;

    private ArrayList<Author> authors = new ArrayList<Author>();
    private ArrayList<Editor> editors = new ArrayList<Editor>();

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public void openConnection(String type) {
        try {
            if (type.equalsIgnoreCase(Utility.article))
                c = DriverManager.getConnection("jdbc:sqlite:" + Utility.article + ".db");
            else
                c = DriverManager.getConnection("jdbc:sqlite:" + Utility.inProceedings + ".db");
            //System.out.println("c.getCatalog() Opened database successfully " + c.getCatalog());

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

    public void insertIntoDBInProceedings
            (int year, String pages, String dblp, String title, String volume, String shortTitle,
             String url, String address, String publisher, String series, String bookTitle, String doi) {
        try {
            if (c == null || c.isClosed())
                return;

            PreparedStatement preparedStmt = c.prepareStatement("INSERT INTO ARTICLE VALUES(?,?,?,?,?,?,?,?,?,?,?,?);");
            preparedStmt.setInt(1, year);
            preparedStmt.setString(2, pages);
            preparedStmt.setString(3, dblp);
            preparedStmt.setString(4, title);
            preparedStmt.setString(5, volume);
            preparedStmt.setString(6, shortTitle);
            preparedStmt.setString(7, url);
            preparedStmt.setString(8, address);
            preparedStmt.setString(9, publisher);
            preparedStmt.setString(10, series);
            preparedStmt.setString(11, bookTitle);
            preparedStmt.setString(12, doi);

            preparedStmt.executeUpdate();

            //calculateAuthor(author);
            //calculateEditor(editor);

            for (int i = 0; i < authors.size(); i++) {
                if (!(compareAuthor(authors.get(i).getNameAuthor(), authors.get(i).getSurnameAuthor()))) { //se nome e cognome non ci sono gia'
                    preparedStmt = c.prepareStatement("INSERT INTO AUTHOR VALUES(?,?,?);");
                    //System.out.println("surname author: " + authors.get(i).getSurnameAuthor() + " name author: " + authors.get(i).getNameAuthor());
                    preparedStmt.setString(2, authors.get(i).getSurnameAuthor());
                    preparedStmt.setString(3, authors.get(i).getNameAuthor());
                    preparedStmt.executeUpdate();
                }
            }

            for (int i = 0; i < editors.size(); i++) {
                if (!(compareEditor(editors.get(i).getNameEditor(), editors.get(i).getSurnameEditor()))) { //se nome e cognome non ci sono gia'
                    preparedStmt = c.prepareStatement("INSERT INTO EDITOR VALUES(?,?,?);");
                    //System.out.println("surname editor: " + editors.get(i).getSurnameEditor() + " name editor: " + editors.get(i).getNameEditor());
                    preparedStmt.setString(2, editors.get(i).getSurnameEditor());
                    preparedStmt.setString(3, editors.get(i).getNameEditor());
                    preparedStmt.executeUpdate();
                }
            }

            for (int i = 0; i < authors.size(); i++) {
                preparedStmt = c.prepareStatement("INSERT INTO WRITTENBY VALUES(?,?);");
                preparedStmt.setString(1, dblp);
                preparedStmt.setInt(2, recuperateIdAuthor(authors.get(i)));
                preparedStmt.executeUpdate();
            }

            for (int i = 0; i < editors.size(); i++) {
                preparedStmt = c.prepareStatement("INSERT INTO HAS VALUES(?,?);");
                preparedStmt.setString(1, dblp);
                preparedStmt.setInt(2, recuperateIdEditor(editors.get(i)));
                preparedStmt.executeUpdate();
            }
            preparedStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertIntoDBArticle(int year, String pages, String dblp, String title, String volume, String shortTitle,
                                    String url, String doi, String journal) {
        try {
            if (c == null || c.isClosed())
                return;

            PreparedStatement preparedStmt = c.prepareStatement("INSERT INTO ARTICLE VALUES(?,?,?,?,?,?,?,?,?);");
            preparedStmt.setInt(1, year);
            preparedStmt.setString(2, pages);
            preparedStmt.setString(3, dblp);
            preparedStmt.setString(4, title);
            preparedStmt.setString(5, journal);
            preparedStmt.setString(6, volume);
            preparedStmt.setString(7, shortTitle);
            preparedStmt.setString(8, url);
            preparedStmt.setString(9, doi);

            preparedStmt.executeUpdate();

            //calculateAuthor(author);

            for (int i = 0; i < authors.size(); i++) {
                if (!(compareAuthor(authors.get(i).getNameAuthor(), authors.get(i).getSurnameAuthor()))) { //se nome e cognome non ci sono gia'
                    preparedStmt = c.prepareStatement("INSERT INTO AUTHOR VALUES(?,?,?);");
                    preparedStmt.setString(2, authors.get(i).getSurnameAuthor());
                    preparedStmt.setString(3, authors.get(i).getNameAuthor());
                    preparedStmt.executeUpdate();
                }
            }

            for (int i = 0; i < authors.size(); i++) {
                preparedStmt = c.prepareStatement("INSERT INTO WRITTENBY VALUES(?,?);");
                preparedStmt.setString(1, dblp);
                preparedStmt.setInt(2, recuperateIdAuthor(authors.get(i)));
                preparedStmt.executeUpdate();
            }

            preparedStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateAuthor(String author) {
        Pattern pattern = Pattern.compile("(^[^\\s]*)\\s(.+)");
        Matcher matcher;
        String[] splittedAuthor = gp.getAuthor().split(" and ");
        for (int i = 0; i < splittedAuthor.length; i++) {
            matcher = pattern.matcher(splittedAuthor[i]);
            if (matcher.find()) {
                String name = matcher.group(1);
                String surname = matcher.group(2);
                if (!compareAuthor(surname, name)) {
                    Author au = new Author();
                    au.setNameAuthor(name);
                    au.setSurnameAuthor(surname);
                    authors.add(au);
                } else
                    System.out.println("UGUALI");
                //System.out.println("Autore " + i + " nome " + name + " cognome " + surname);
            }
        }
    }

    public void calculateEditor(String editor) {
        Pattern pattern = Pattern.compile("(^[^\\s]*)\\s(.+)");
        Matcher matcher;
        String[] splittedEditor = gp.getEditor().split(" and ");
        for (int i = 0; i < splittedEditor.length; i++) {
            matcher = pattern.matcher(splittedEditor[i]);
            //System.out.println("splittedEditor[i] " + splittedEditor[i]);
            if (matcher.find()) {
                String name = matcher.group(1);
                String surname = matcher.group(2);
                if (!compareEditor(surname, name)) {
                    Editor e = new Editor();
                    e.setNameEditor(name);
                    e.setSurnameEditor(surname);
                    editors.add(e);
                } else
                    System.out.println("UGUALI");
            }
        }
    }

    public void createTableInProceedings() {
        try {
            if (c == null || c.isClosed())
                return;

            String sql = "";
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
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "SURNAME varchar(200) NOT NULL," +
                    "NAME varchar(200) NOT NULL);";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS EDITOR" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "SURNAME varchar(200) NOT NULL," +
                    "NAME varchar(200) NOT NULL);";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS WRITTENBY" +
                    "(DBLP varchar(200) NOT NULL," +
                    "IDAUTHOR INTEGER NOT NULL," +
                    "PRIMARY KEY (DBLP,IDAUTHOR));";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS HAS" +
                    "(DBLP varchar(200) NOT NULL," +
                    "IDEDITOR INTEGER NOT NULL," +
                    "PRIMARY KEY (DBLP,IDEDITOR));";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readingArticleInProceedings() {
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

    public void readingAuthorInProceedings() {
        try {
            if (c == null || c.isClosed())
                return;
            //System.out.println("reading author");
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AUTHOR;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                System.out.println("ID: " + id + " NAME: " + name + " SURNAME: " + surname);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readingEditorInProceedings() {
        try {
            if (c == null || c.isClosed())
                return;
            //System.out.println("reading author");
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM EDITOR;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                //System.out.println("ID: " + id + " NAME: " + name + " SURNAME: " + surname);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTableArticle() {
        try {
            String sql = "";
            sql = "CREATE TABLE IF NOT EXISTS ARTICLE" +
                    "(YEAR INT NOT NULL," +
                    "PAGES varchar(200) NOT NULL," +
                    "DBLP varchar(200) PRIMARY KEY NOT NULL," +
                    "TITLE varchar(200) NOT NULL," +
                    "JOURNAL varchar(200) NOT NULL," +
                    "VOLUME INT NOT NULL," +
                    "SHORT_TITLE varchar(200)  NOT NULL," +
                    "URL varchar(200) NOT NULL," +
                    "DOI varchar(200) NOT NULL);";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS AUTHOR" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "SURNAME varchar(200) NOT NULL," +
                    "NAME varchar(200) NOT NULL);";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS WRITTENBY" +
                    "(DBLP varchar(200) NOT NULL," +
                    "IDAUTHOR INTEGER NOT NULL," +
                    "PRIMARY KEY (DBLP,IDAUTHOR));";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void readingArticleArticle() {
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
                String journal = rs.getString("JOURNAL");
                int volume = rs.getInt("VOLUME");
                String shortTitle = rs.getString("SHORT_TITLE");
                String url = rs.getString("URL");
                String doi = rs.getString("DOI");
                System.out.println("YEAR: " + year + " PAGES: " + pages + " TITLE: " + title + " VOLUME: " + volume + " SHORT TITLE: " + shortTitle + " URL: " + url +
                        " DOI: " + doi + " DBLP: " + dblp + " JOURNAL: " + journal);

            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readingAuthorArticle() {
        try {
            if (c == null || c.isClosed())
                return;
            //System.out.println("reading author");
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AUTHOR;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                System.out.println("ID: " + id + " NAME: " + name + " SURNAME: " + surname);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean compareAuthor(String name, String surname) {
        try {
            if (c == null || c.isClosed())
                return false;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AUTHOR;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name2 = rs.getString("NAME");
                String surname2 = rs.getString("SURNAME");
                String tot = surname2 + " " + name2;
                if (tot.equalsIgnoreCase(surname + " " + name))
                    return true;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean compareEditor(String name, String surname) {
        try {
            if (c == null || c.isClosed())
                return false;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM EDITOR;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name2 = rs.getString("NAME");
                String surname2 = rs.getString("SURNAME");
                String tot = surname2 + " " + name2;
                if (tot.equalsIgnoreCase(surname + " " + name))
                    return true;
            }
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int recuperateIdAuthor(Author author) {
        try {
            if (c == null || c.isClosed())
                return -1;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AUTHOR;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                if (name.equalsIgnoreCase(author.getNameAuthor()) && surname.equalsIgnoreCase(author.getSurnameAuthor()))
                    return id;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int recuperateIdEditor(Editor editor) {
        try {
            if (c == null || c.isClosed())
                return -1;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM EDITOR;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                if (name.equalsIgnoreCase(editor.getNameEditor()) && surname.equalsIgnoreCase(editor.getSurnameEditor()))
                    return id;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public ArrayList<Editor> getEditors() {
        return editors;
    }

    public void setEditors(ArrayList<Editor> editors) {
        this.editors = editors;
    }

    public void findArticleByAuthorName(String surname,String name)
    {
        try {
            if (c == null || c.isClosed())
                return;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ARTICLE,AUTHOR WHERE AUTHOR.NAME==name AND AUTHOR.SURNAME==surname;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name2 = rs.getString("NAME");
                String surname2 = rs.getString("SURNAME");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}