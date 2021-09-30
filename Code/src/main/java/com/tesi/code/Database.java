package com.tesi.code;

import com.tesi.code.Model.Article;
import com.tesi.code.Model.Author;
import com.tesi.code.Model.Editor;
import com.tesi.code.Model.inProceedings;
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
    private ArrayList<Article> filteredArticlesArticle = new ArrayList<Article>();
    private ArrayList<Article> filteredArticlesInProceedings = new ArrayList<Article>();

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
            System.out.println("database editor: " + editors.size());
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

            System.out.println("JOURNAL: " + journal);

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
                    Author au = new Author(surname, name);
                    //au.setNameAuthor(name);
                    //au.setSurnameAuthor(surname);
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
                    editors.add(new Editor(name, surname));
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

    public void filterByTypeInProceedings() {
        try {
            if (c == null || c.isClosed())
                return;
            filteredArticlesInProceedings.clear();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT DISTINCT ARTICLE.DBLP,YEAR,PAGES,TITLE,VOLUME," +
                            "SHORT_TITLE,URL,ADDRESS,PUBLISHER,SERIES,BOOKTITLE,DOI,AUTHOR.SURNAME,AUTHOR.NAME," +
                            "EDITOR.NAME,EDITOR.SURNAME " +
                            "FROM ARTICLE,AUTHOR,WRITTENBY,EDITOR,HAS " +
                            "WHERE WRITTENBY.DBLP==ARTICLE.DBLP AND WRITTENBY.IDAUTHOR==AUTHOR.ID AND HAS.DBLP=ARTICLE.DBLP AND HAS.IDEDITOR==EDITOR.ID " +
                            "GROUP BY ARTICLE.DBLP " +
                            "ORDER BY ARTICLE.DBLP;");

            String oldDblp = "";
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
                String booktitle = rs.getString("BOOKTITLE");
                String doi = rs.getString("DOI");
                String surnameAuthor = rs.getString("SURNAME");
                String nameAuthor = rs.getString("NAME");
                String surnameEditor = rs.getString("SURNAME");
                String nameEditor = rs.getString("NAME");

                //lista di autori
                if (!oldDblp.equals(dblp)) {
                    //allAuthors.add(new Author(surname, name));
                    readingAuthorInProceedings();
                    readingEditorInProceedings();
                    filteredArticlesInProceedings.add(new inProceedings(Utility.inProceedings, year, pages, dblp, title, volume, shortTitle, url, booktitle, doi, allAuthors, publisher, series, address, booktitle, allEditors));
                    System.out.println("YEAR: " + year + " PAGES: " + pages + " TITLE: " + title + " VOLUME: " + volume + " SHORT TITLE: " + shortTitle + " URL: " + url +
                            " DOI: " + doi + " DBLP: " + dblp + " nameAuthor: " + nameAuthor + " surnameAuthor: " + surnameAuthor + " nameEditor: "
                            + nameEditor + " surnameEditor: " + surnameEditor);
                }
                oldDblp = dblp;
            }
            System.out.println("Database: " + filteredArticlesInProceedings.size());
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanAll() {
        filteredArticlesArticle.clear();
        filteredArticlesInProceedings.clear();
    }

    public void findArticleByAuthorName(String surname, String name, String type) {
        try {
            if (c == null || c.isClosed())
                return;

            PreparedStatement stmt = null;

            String oldDblp = "";
            if (!surname.equalsIgnoreCase("") && !name.equalsIgnoreCase("")) {
                stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR " +
                        "WHERE UPPER(AUTHOR.NAME)==UPPER(name) AND UPPER(AUTHOR.SURNAME)==UPPER(surname) " +
                        "GROUP BY ARTICLE.DBLP " +
                        "ORDER BY ARTICLE.DBLP;");
            } else if (surname.equalsIgnoreCase("")) {
                stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR WHERE UPPER(AUTHOR.NAME) LIKE UPPER(?) " +
                        "GROUP BY ARTICLE.DBLP " +
                        "ORDER BY ARTICLE.DBLP;");
                stmt.setString(1, "%" + name + "%");
            } else if (name.equalsIgnoreCase("")) {
                stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR WHERE UPPER(AUTHOR.SURNAME) LIKE UPPER(?) " +
                        "GROUP BY ARTICLE.DBLP " +
                        "ORDER BY ARTICLE.DBLP;");
                stmt.setString(1, "%" + surname + "%");
            }

            //else if(surname.equalsIgnoreCase("") && name.equalsIgnoreCase(""))
            //error

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int year = rs.getInt("YEAR");
                String pages = rs.getString("PAGES");
                String dblp = rs.getString("DBLP");
                String title = rs.getString("TITLE");
                int volume = rs.getInt("VOLUME");
                String shortTitle = rs.getString("SHORT_TITLE");
                String url = rs.getString("URL");
                String doi = rs.getString("DOI");
                if (!oldDblp.equals(dblp)) {
                    if (type.equalsIgnoreCase(Utility.inProceedings)) {
                        String address = rs.getString("ADDRESS");
                        String publisher = rs.getString("PUBLISHER");
                        String series = rs.getString("SERIES");
                        String bookTitle = rs.getString("BOOKTITLE");
                        readingAuthorArticle();
                        readingEditorInProceedings();
                        filteredArticlesInProceedings.add(new inProceedings(Utility.inProceedings, year, pages, dblp, title, volume, shortTitle, shortTitle, url, doi, allAuthors, publisher, series, address, bookTitle, allEditors));
                    } else {
                        readingAuthorArticle();
                        String journal = rs.getString("JOURNAL");
                        System.out.println("JOURNAL 2" + journal);
                        filteredArticlesArticle.add(new Article(Utility.article, year, pages, dblp, title, volume, shortTitle, url, journal, doi, allAuthors));
                    }
                    System.out.println("YEAR: " + year + " PAGES: " + pages + " TITLE: " + title + " VOLUME: " + volume + " SHORT TITLE: " + shortTitle + " URL: " + url +
                            " ADDRESS: " + " DOI: " + doi + " DBLP: " + dblp);
                }
                oldDblp = dblp;
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
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AUTHOR,ARTICLE,WRITTENBY " +
                    "WHERE AUTHOR.ID==WRITTENBY.IDAUTHOR AND WRITTENBY.DBLP==ARTICLE.DBLP;");

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                allAuthors.add(new Author(surname, name));
                System.out.println("ID AUTHOR: " + id + " NAME: " + name + " SURNAME: " + surname);
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
            System.out.println("readingEditorInProceedings 1: " + getAllEditors().size());
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM EDITOR,ARTICLE,HAS " +
                    "WHERE EDITOR.ID==HAS.IDEDITOR AND HAS.DBLP==ARTICLE.DBLP;");
            allEditors.clear();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                System.out.println("ID EDITOR: " + id + " NAME: " + name + " SURNAME: " + surname);
                allEditors.add(new Editor(name, surname));
            }

            System.out.println("readingEditorInProceedings 2: " + allEditors.size());
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

    private ArrayList<Author> allAuthors = new ArrayList<Author>();//da eliminare...ci sono troppi doppioni
    private ArrayList<Editor> allEditors = new ArrayList<Editor>();//da eliminare...ci sono troppi doppioni

    public ArrayList<Author> getAllAuthors() {
        return allAuthors;
    }

    public ArrayList<Editor> getAllEditors() {
        return allEditors;
    }

    public void filterByTypeArticle() {
        try {
            if (c == null || c.isClosed())
                return;
            filteredArticlesArticle.clear();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT DISTINCT ARTICLE.DBLP,YEAR,PAGES,TITLE,JOURNAL,VOLUME," +
                            "SHORT_TITLE,URL,DOI,SURNAME,NAME " +
                            "FROM ARTICLE,AUTHOR,WRITTENBY " +
                            "WHERE WRITTENBY.DBLP==ARTICLE.DBLP AND WRITTENBY.IDAUTHOR==AUTHOR.ID " +
                            "GROUP BY ARTICLE.DBLP " +
                            "ORDER BY ARTICLE.DBLP;");

            String oldDblp = "";
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
                String surname = rs.getString("SURNAME");
                String name = rs.getString("NAME");

                //lista di autori
                if (!oldDblp.equals(dblp)) {
                    //allAuthors.add(new Author(surname, name));
                    readingAuthorArticle();

                    filteredArticlesArticle.add(new Article(Utility.article, year, pages, dblp, title, volume, shortTitle, url, journal, doi, allAuthors));
                    System.out.println("YEAR: " + year + " PAGES: " + pages + " TITLE: " + title + " VOLUME: " + volume + " SHORT TITLE: " + shortTitle + " URL: " + url +
                            " DOI: " + doi + " DBLP: " + dblp + " JOURNAL: " + journal + " NAME: " + name + " SURNAME: " + surname);
                }
                oldDblp = dblp;
            }
            System.out.println("Database: " + filteredArticlesArticle.size());
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM AUTHOR,ARTICLE,WRITTENBY WHERE AUTHOR.ID==WRITTENBY.IDAUTHOR AND WRITTENBY.DBLP==ARTICLE.DBLP;");
            allAuthors.clear();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                allAuthors.add(new Author(surname, name));
                //     System.out.println("ID: " + id + " NAME: " + name + " SURNAME: " + surname);
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

    public ArrayList<Article> getFilteredArticlesArticle() {
        return filteredArticlesArticle;
    }

    public ArrayList<Article> getFilteredArticlesInProceedings() {
        return filteredArticlesInProceedings;
    }

    public void findArticleByInfoArticleArticle(String title, String journal, int year) {
        try {
            if (c == null || c.isClosed())
                return;
            //'%name%'
            System.out.println("findArticleByInfoArticle");
            PreparedStatement stmt = null;

            String oldDblp = "";
            if (!title.equalsIgnoreCase("") && !journal.equalsIgnoreCase("")) {
                System.out.println("!title and !journal");
                stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR " +
                        "WHERE UPPER(ARTICLE.TITLE) LIKE UPPER(?) AND UPPER(ARTICLE.JOURNAL) LIKE UPPER(?) AND ARTICLE.YEAR==(?)" +
                        "GROUP BY ARTICLE.DBLP " +
                        "ORDER BY ARTICLE.DBLP;");

                stmt.setString(1, "%" + title + "%");
                stmt.setString(2, "%" + journal + "%");
                stmt.setInt(3, year);
            } else if (title.equalsIgnoreCase("")) {
                System.out.println("title");
                stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR " +
                        "WHERE UPPER(ARTICLE.JOURNAL) LIKE UPPER(?) " +
                        "GROUP BY ARTICLE.DBLP " +
                        "ORDER BY ARTICLE.DBLP;");
                stmt.setString(1, "%" + journal + "%");
            } else if (journal.equalsIgnoreCase("")) {
                System.out.println("journal");
                stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR " +
                        "WHERE UPPER(ARTICLE.TITLE) LIKE UPPER(?) " +
                        "GROUP BY ARTICLE.DBLP " +
                        "ORDER BY ARTICLE.DBLP;");
                stmt.setString(1, "%" + title + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int year2 = rs.getInt("YEAR");
                String pages = rs.getString("PAGES");
                String dblp = rs.getString("DBLP");
                String title2 = rs.getString("TITLE");
                int volume = rs.getInt("VOLUME");
                String shortTitle = rs.getString("SHORT_TITLE");
                String url = rs.getString("URL");
                String doi = rs.getString("DOI");
                if (!oldDblp.equals(dblp)) {
                    readingAuthorArticle();
                    String journal2 = rs.getString("JOURNAL");
                    filteredArticlesArticle.add(new Article(Utility.article, year2, pages, dblp, title2, volume, shortTitle, url, journal2, doi, allAuthors));
                    System.out.println("YEAR: " + year2 + " PAGES: " + pages + " TITLE: " + title2 + " VOLUME: " + volume + " SHORT TITLE: " + shortTitle + " URL: " + url +
                            " ADDRESS: " + " DOI: " + doi + " DBLP: " + dblp);
                }
                oldDblp = dblp;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findArticleByInfoArticleInProceedings(String title, int year) {
        try {
            if (c == null || c.isClosed())
                return;

            System.out.println("findArticleByInfoArticleInProceedings");
            PreparedStatement stmt = null;

            String oldDblp = "";
            if (!title.equalsIgnoreCase("")) {
                System.out.println("!title");
                stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR " +
                        "WHERE UPPER(ARTICLE.TITLE) LIKE UPPER(?) AND ARTICLE.YEAR==(?)" +
                        "GROUP BY ARTICLE.DBLP " +
                        "ORDER BY ARTICLE.DBLP;");

                stmt.setString(1, "%" + title + "%");
                stmt.setInt(2, year);
            } else {
                System.out.println("TITLE");
                stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR " +
                        "WHERE ARTICLE.YEAR==(?) " +
                        "GROUP BY ARTICLE.DBLP " +
                        "ORDER BY ARTICLE.DBLP;");
                stmt.setInt(1, year);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int year2 = rs.getInt("YEAR");
                String pages = rs.getString("PAGES");
                String dblp = rs.getString("DBLP");
                String title2 = rs.getString("TITLE");
                int volume = rs.getInt("VOLUME");
                String shortTitle = rs.getString("SHORT_TITLE");
                String url = rs.getString("URL");
                String doi = rs.getString("DOI");
                if (!oldDblp.equals(dblp)) {
                    String address = rs.getString("ADDRESS");
                    String publisher = rs.getString("PUBLISHER");
                    String series = rs.getString("SERIES");
                    String bookTitle = rs.getString("BOOKTITLE");
                    readingAuthorArticle();
                    readingEditorInProceedings();
                    filteredArticlesInProceedings.add(new inProceedings(Utility.inProceedings, year2, pages, dblp, title2, volume, shortTitle, shortTitle, url, doi, allAuthors, publisher, series, address, bookTitle, allEditors));
                    System.out.println("YEAR: " + year2 + " PAGES: " + pages + " TITLE: " + title2 + " VOLUME: " + volume + " SHORT TITLE: " + shortTitle + " URL: " + url +
                            " ADDRESS: " + " DOI: " + doi + " DBLP: " + dblp);
                }
                oldDblp = dblp;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}