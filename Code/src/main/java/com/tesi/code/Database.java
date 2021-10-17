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
    private ArrayList<inProceedings> filteredArticlesInProceedings = new ArrayList<inProceedings>();

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

    public boolean insertIntoDBInProceedings
            (int year, String pages, String dblp, String title, String volume, String shortTitle,
             String url, String address, String publisher, String series, String bookTitle, String doi) {
        try {
            if (c == null || c.isClosed())
                return false;

            if (!existsDblp(dblp)) {
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


                for (int i = 0; i < authors.size(); i++) {
                    if (!(compareAuthor(authors.get(i).getName(), authors.get(i).getSurname()))) { //se nome e cognome non ci sono gia'
                        preparedStmt = c.prepareStatement("INSERT INTO AUTHOR VALUES(?,?,?);");
                        //System.out.println("surname author: " + authors.get(i).getSurnameAuthor() + " name author: " + authors.get(i).getNameAuthor());
                        preparedStmt.setString(2, authors.get(i).getSurname());
                        preparedStmt.setString(3, authors.get(i).getName());
                        preparedStmt.executeUpdate();
                    }
                }

                for (int i = 0; i < editors.size(); i++) {
                    if (!(compareEditor(editors.get(i).getName(), editors.get(i).getSurname()))) { //se nome e cognome non ci sono gia'
                        preparedStmt = c.prepareStatement("INSERT INTO EDITOR VALUES(?,?,?);");
                        //System.out.println("surname editor: " + editors.get(i).getSurnameEditor() + " name editor: " + editors.get(i).getNameEditor());
                        preparedStmt.setString(2, editors.get(i).getSurname());
                        preparedStmt.setString(3, editors.get(i).getName());
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
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insertIntoDBArticle(int year, String pages, String dblp, String title, String volume, String shortTitle,
                                       String url, String doi, String journal) {
        try {
            if (c == null || c.isClosed())
                return false;
            if (!existsDblp(dblp)) {
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

                for (int i = 0; i < authors.size(); i++) {
                    if (!(compareAuthor(authors.get(i).getName(), authors.get(i).getSurname()))) { //se nome e cognome non ci sono gia'
                        preparedStmt = c.prepareStatement("INSERT INTO AUTHOR VALUES(?,?,?);");
                        preparedStmt.setString(2, authors.get(i).getSurname());
                        preparedStmt.setString(3, authors.get(i).getName());
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
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void calculateAuthor(String author) {
        Pattern pattern = Pattern.compile("(^[^\\s]*)\\s(.+)");
        Matcher matcher;
        String[] splittedAuthor = gp.getAuthor().split(" and ");
        for (String s : splittedAuthor) {
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                String name = matcher.group(1);
                String surname = matcher.group(2);
                if (!compareAuthor(surname, name))
                    authors.add(new Author(surname, name));
            }
        }
    }

    public void calculateEditor(String editor) {
        Pattern pattern = Pattern.compile("(^[^\\s]*)\\s(.+)");
        Matcher matcher;
        String[] splittedEditor = gp.getEditor().split(" and ");
        for (String s : splittedEditor) {
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                String name = matcher.group(1);
                String surname = matcher.group(2);
                if (!compareEditor(surname, name))
                    editors.add(new Editor(name, surname));
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

    public void cleanAll() {
        filteredArticlesArticle.clear();
        filteredArticlesInProceedings.clear();
        authors.clear();
        editors.clear();
    }

    public void readingAuthorInProceedings(String dblp, ArrayList<Author> author) {
        try {
            if (c == null || c.isClosed())
                return;

            PreparedStatement stmt = c.prepareStatement("SELECT AUTHOR.NAME,AUTHOR.SURNAME FROM AUTHOR,ARTICLE,WRITTENBY" +
                    " WHERE AUTHOR.ID==WRITTENBY.IDAUTHOR" +
                    " AND WRITTENBY.DBLP==ARTICLE.DBLP " +
                    " AND ARTICLE.DBLP==(?);");

            stmt.setString(1, dblp);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
//                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                author.add(new Author(surname, name));
                //System.out.println("ID AUTHOR: " + id + " NAME: " + name + " SURNAME: " + surname);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readingEditorInProceedings(String dblp, ArrayList<Editor> editor) {
        try {
            if (c == null || c.isClosed())
                return;
            PreparedStatement stmt = c.prepareStatement("SELECT EDITOR.NAME,EDITOR.SURNAME FROM EDITOR,ARTICLE,HAS " +
                    " WHERE EDITOR.ID==HAS.IDEDITOR" +
                    " AND HAS.DBLP==ARTICLE.DBLP" +
                    " AND ARTICLE.DBLP==(?);");
            stmt.setString(1, dblp);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
//                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                editor.add(new Editor(name, surname));
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
            e.printStackTrace();
        }
    }

    public void filterByTypeInProceedings() {
        try {
            if (c == null || c.isClosed())
                return;
            Statement stmt = c.createStatement();

            //if(filteredArticlesInProceedings.size()==0) {
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
                if (!oldDblp.equalsIgnoreCase(dblp)) {
                    authors = new ArrayList<>();
                    editors = new ArrayList<>();
                    readingAuthorInProceedings(dblp, authors);
                    readingEditorInProceedings(dblp, editors);
                    filteredArticlesInProceedings.add(new inProceedings(Utility.inProceedings, year, pages, dblp, title, volume, shortTitle, url, booktitle, doi, authors, publisher, series, address, editors));
                    System.out.println("YEAR: " + year + " PAGES: " + pages + " TITLE: " + title + " VOLUME: " + volume + " SHORT TITLE: " + shortTitle + " URL: " + url +
                            " DOI: " + doi + " DBLP: " + dblp + " nameAuthor: " + nameAuthor + " surnameAuthor: " + surnameAuthor + " nameEditor: "
                            + nameEditor + " surnameEditor: " + surnameEditor);
                }
                oldDblp = dblp;
            }

            //}
            //else {
            //rifiltraggio

            //}
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterByTypeArticle() {
        try {
            if (c == null || c.isClosed())
                return;
            Statement stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM ARTICLE,WRITTENBY,AUTHOR" +
                            " WHERE ARTICLE.DBLP==WRITTENBY.DBLP" +
                            " AND WRITTENBY.IDAUTHOR==AUTHOR.ID" +
                            " GROUP BY ARTICLE.DBLP " +
                            " ORDER BY ARTICLE.DBLP;");

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

                if (!oldDblp.equalsIgnoreCase(dblp)) {
                    ArrayList<Author> aut = new ArrayList<>();
                    readingAuthorArticle(dblp, aut);
                    Article a = new Article(Utility.article, year, pages, dblp, title, volume, shortTitle, url, journal, doi, aut, new ArrayList<Editor>());
                    filteredArticlesArticle.add(a);
                }
                oldDblp = dblp;
            }
            stmt.close();
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readingAuthorArticle(String dblp, ArrayList<Author> aut) {
        try {
            if (c == null || c.isClosed())
                return;
            PreparedStatement stmt = c.prepareStatement("SELECT AUTHOR.NAME,AUTHOR.SURNAME" +
                    " FROM ARTICLE,WRITTENBY,AUTHOR" +
                    " WHERE ARTICLE.DBLP==WRITTENBY.DBLP" +
                    " AND WRITTENBY.IDAUTHOR==AUTHOR.ID" +
                    " AND ARTICLE.DBLP==(?);");
            stmt.setString(1, dblp);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                Author a = new Author(surname, name);
                System.out.println("RIGA 520 " + surname);
                aut.add(a);
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean existsDblp(String dblp) {
        try {
            if (c == null || c.isClosed())
                return false;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ARTICLE;");

            while (rs.next()) {
                if (rs.getString("DBLP").equalsIgnoreCase(dblp))
                    return true;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean compareAuthor(String name, String surname) {
        try {
            if (c == null || c.isClosed())
                return false;
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM AUTHOR;");

            while (rs.next()) {
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
                if (name.equalsIgnoreCase(author.getName()) && surname.equalsIgnoreCase(author.getSurname()))
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
                if (name.equalsIgnoreCase(editor.getName()) && surname.equalsIgnoreCase(editor.getSurname()))
                    return id;
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void refilterByAuthorArticle(String surname, String name) {
        try {
            if (c == null || c.isClosed())
                return;

            PreparedStatement stmt = null;

            String oldDblp = "";

            ArrayList<Article> filteredArticle=(ArrayList)filteredArticlesArticle.clone();
            filteredArticlesArticle.clear();

            for (Article a : filteredArticle) {
                if (!surname.equalsIgnoreCase("") && !name.equalsIgnoreCase("")) {
                    stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                            " WHERE ARTICLE.DBLP==(?)" +
                            " AND UPPER(AUTHOR.SURNAME) LIKE UPPER(?)" +
                            " AND UPPER(AUTHOR.NAME) LIKE UPPER(?)" +
                            " AND WRITTENBY.DBLP==ARTICLE.DBLP" +
                            " AND WRITTENBY.IDAUTHOR==AUTHOR.ID" +
                            " GROUP BY ARTICLE.DBLP" +
                            " ORDER BY ARTICLE.DBLP;");

                    stmt.setString(1, a.getDblp());
                    stmt.setString(2, "%" + surname + "%");
                    stmt.setString(3, "%" + name + "%");
                } else if (surname.equalsIgnoreCase("")) {
                    stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                            " WHERE ARTICLE.DBLP==(?)" +
                            " AND UPPER(AUTHOR.NAME) LIKE UPPER(?)" +
                            " AND WRITTENBY.DBLP==ARTICLE.DBLP" +
                            " AND WRITTENBY.IDAUTHOR==AUTHOR.ID" +
                            " GROUP BY ARTICLE.DBLP" +
                            " ORDER BY ARTICLE.DBLP;");

                    stmt.setString(1, a.getDblp());
                    stmt.setString(2, "%" + name + "%");
                } else if (name.equalsIgnoreCase("")) {
                    stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                            " WHERE ARTICLE.DBLP==(?)" +
                            " AND UPPER(AUTHOR.SURNAME) LIKE UPPER(?)" +
                            " AND WRITTENBY.DBLP==ARTICLE.DBLP" +
                            " AND WRITTENBY.IDAUTHOR==AUTHOR.ID" +
                            " GROUP BY ARTICLE.DBLP" +
                            " ORDER BY ARTICLE.DBLP;");

                    stmt.setString(1, a.getDblp());
                    stmt.setString(2, "%" + surname + "%");
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    if (!oldDblp.equals(a.getDblp()))
                        filteredArticlesArticle.add(a);
                    oldDblp = a.getDblp();
                }
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refilterByAuthorInProceedings(String surname, String name) {
        try {
            if (c == null || c.isClosed())
                return;

            PreparedStatement stmt = null;

            String oldDblp = "";
            ArrayList<inProceedings> filteredArticle= (ArrayList<inProceedings>) filteredArticlesInProceedings.clone();
            System.out.println("RIGA 881 filteredArticle: " + filteredArticle.size());
            filteredArticlesInProceedings.clear();
            //filteredArticlesArticle.clear();
            System.out.println("CIAO 0: " + filteredArticle.size());
            for (inProceedings a : filteredArticle) {
                if (!surname.equalsIgnoreCase("") && !name.equalsIgnoreCase("")) {
                    stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                            " WHERE ARTICLE.DBLP==(?)" +
                            " AND UPPER(AUTHOR.SURNAME) LIKE UPPER(?)" +
                            " AND UPPER(AUTHOR.NAME) LIKE UPPER(?)" +
                            " AND WRITTENBY.DBLP==ARTICLE.DBLP" +
                            " AND WRITTENBY.IDAUTHOR==AUTHOR.ID" +
                            " GROUP BY ARTICLE.DBLP" +
                            " ORDER BY ARTICLE.DBLP;");

                    stmt.setString(1, a.getDblp());
                    stmt.setString(2, "%" + surname + "%");
                    stmt.setString(3, "%" + name + "%");
                } else if (surname.equalsIgnoreCase("")) {
                    stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                            " WHERE ARTICLE.DBLP==(?)" +
                            " AND UPPER(AUTHOR.NAME) LIKE UPPER(?)" +
                            " AND WRITTENBY.DBLP==ARTICLE.DBLP" +
                            " AND WRITTENBY.IDAUTHOR==AUTHOR.ID" +
                            " GROUP BY ARTICLE.DBLP" +
                            " ORDER BY ARTICLE.DBLP;");

                    stmt.setString(1, a.getDblp());
                    stmt.setString(2, "%" + name + "%");

                } else if (name.equalsIgnoreCase("")) {
                    stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                            " WHERE ARTICLE.DBLP==(?)" +
                            " AND UPPER(AUTHOR.SURNAME) LIKE UPPER(?)" +
                            " AND WRITTENBY.DBLP==ARTICLE.DBLP" +
                            " AND WRITTENBY.IDAUTHOR==AUTHOR.ID" +
                            " GROUP BY ARTICLE.DBLP" +
                            " ORDER BY ARTICLE.DBLP;");

                    stmt.setString(1, a.getDblp());
                    stmt.setString(2, "%" + surname + "%");
                }

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    if (!oldDblp.equals(a.getDblp())) {
                        filteredArticlesInProceedings.add(a);
                        System.out.println("CIAO 1: ");
                        System.out.println("CIAO 2: " + rs.getString("NAME"));
                    }
                    oldDblp = a.getDblp();
                }
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refilterByArticleArticle(String title, String journal, Integer year) {
        try {
            if (c == null || c.isClosed())
                return;
            PreparedStatement stmt = null;

            String oldDblp = "";
//            filteredArticlesInProceedings.clear();

            ArrayList<Article> filteredArticle=(ArrayList)filteredArticlesArticle.clone();;
            filteredArticlesArticle.clear();


            for (Article a : filteredArticle) {
                if (!title.equalsIgnoreCase("") && !journal.equalsIgnoreCase("")) {
                    System.out.println("RIGA 899 " + a.getDblp());
                    if (year != -1) {
                        stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                                " WHERE ARTICLE.DBLP==(?)" +
                                " AND UPPER(ARTICLE.TITLE) LIKE UPPER(?)" +
                                " AND UPPER(ARTICLE.JOURNAL) LIKE UPPER(?)" +
                                " AND ARTICLE.YEAR==(?)" +
                                " GROUP BY ARTICLE.DBLP" +
                                " ORDER BY ARTICLE.DBLP;");
                        stmt.setInt(4, year);
                    } else stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                            " WHERE ARTICLE.DBLP==(?)" +
                            " AND UPPER(ARTICLE.TITLE) LIKE UPPER(?)" +
                            " AND UPPER(ARTICLE.JOURNAL) LIKE UPPER(?)" +
                            " GROUP BY ARTICLE.DBLP" +
                            " ORDER BY ARTICLE.DBLP;");
                    stmt.setString(1, a.getDblp());
                    stmt.setString(2, "%" + title + "%");
                    stmt.setString(3, "%" + journal + "%");
                } else if (journal.equalsIgnoreCase("")) {
                    if (year != -1) {
                        stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                                " WHERE UPPER(ARTICLE.TITLE) LIKE UPPER(?) " +
                                " AND ARTICLE.DBLP==(?)" +
                                " AND ARTICLE.YEAR==(?)" +
                                " GROUP BY ARTICLE.DBLP" +
                                " ORDER BY ARTICLE.DBLP;");
                        stmt.setInt(3, year);
                    } else
                        stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                                " WHERE UPPER(ARTICLE.TITLE) LIKE UPPER(?) " +
                                " AND ARTICLE.DBLP==(?)" +
                                " GROUP BY ARTICLE.DBLP" +
                                " ORDER BY ARTICLE.DBLP;");
                    stmt.setString(1, "%" + title + "%");
                    stmt.setString(2, a.getDblp());
                } else if (title.equalsIgnoreCase("")) {
                    if (year != -1) {
                        stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR" +
                                " WHERE UPPER(ARTICLE.JOURNAL) LIKE UPPER(?)" +
                                " AND ARTICLE.DBLP==(?)" +
                                " AND ARTICLE.YEAR==(?)" +
                                " GROUP BY ARTICLE.DBLP " +
                                " ORDER BY ARTICLE.DBLP;");
                        stmt.setInt(3, year);
                    } else stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR" +
                            " WHERE UPPER(ARTICLE.JOURNAL) LIKE UPPER(?)" +
                            " AND ARTICLE.DBLP==(?)" +
                            " GROUP BY ARTICLE.DBLP " +
                            " ORDER BY ARTICLE.DBLP;");
                    stmt.setString(1, "%" + journal + "%");
                    stmt.setString(2, a.getDblp());
                }
                else if (title.equalsIgnoreCase("") && journal.equalsIgnoreCase("")) {
                    if (year != -1) {
                        stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR" +
                                " WHERE ARTICLE.DBLP==(?)" +
                                " AND ARTICLE.YEAR==(?)" +
                                " GROUP BY ARTICLE.DBLP " +
                                " ORDER BY ARTICLE.DBLP;");
                        stmt.setInt(2, year);
                        stmt.setString(1, a.getDblp());
                    }
                }

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println("riga 937 " + a.getDblp());
                    if (!oldDblp.equals(a.getDblp())) {
                        filteredArticlesArticle.add(a);
                    }
                    oldDblp = a.getDblp();
                }
            }
            stmt.close();
            if (filteredArticlesArticle.size() == 0) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refilterByArticleInProceedings(String title, Integer year) {
        try {
            if (c == null || c.isClosed())
                return;
            if(filteredArticlesInProceedings.size()==0) {
                return;
            }
            ArrayList<inProceedings> filteredArticle=(ArrayList<inProceedings>) filteredArticlesInProceedings.clone();;
            filteredArticlesInProceedings.clear();

            PreparedStatement stmt = null;

            String oldDblp = "";
            System.out.println("RIGA 962: " + filteredArticle.size());

            for (inProceedings a : filteredArticle) {
                System.out.println("DATABASE RIGA 964");
                if (!title.equalsIgnoreCase("")) {
                    if (year != -1) {
                        stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                                " WHERE ARTICLE.DBLP==(?)" +
                                " AND UPPER(ARTICLE.TITLE) LIKE UPPER(?)" +
                                " AND ARTICLE.YEAR==(?)" +
                                " GROUP BY ARTICLE.DBLP" +
                                " ORDER BY ARTICLE.DBLP;");
                        stmt.setInt(3, year);
                    } else stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR,WRITTENBY" +
                            " WHERE ARTICLE.DBLP==(?)" +
                            " AND UPPER(ARTICLE.TITLE) LIKE UPPER(?)" +
                            " GROUP BY ARTICLE.DBLP" +
                            " ORDER BY ARTICLE.DBLP;");
                    stmt.setString(1, a.getDblp());
                    stmt.setString(2, "%" + title + "%");
                } else {
                    System.out.println("DATABASE RIGA 977");
                    if (year != -1) {
                        stmt = c.prepareStatement("SELECT * FROM ARTICLE,AUTHOR " +
                                " WHERE ARTICLE.DBLP==(?)" +
                                " AND  ARTICLE.YEAR==(?)" +
                                " GROUP BY ARTICLE.DBLP" +
                                " ORDER BY ARTICLE.DBLP;");
                        stmt.setInt(2, year);
                        stmt.setString(1, a.getDblp());
                        System.out.println("RIGA 985: " + a.getDblp());
                    }
                    else return;
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    if (!oldDblp.equals(a.getDblp())) {
                        filteredArticlesInProceedings.add(a);
                    }
                    oldDblp = a.getDblp();
                }
            }
            stmt.close();

            System.out.println("RIGA 994: " + filteredArticlesInProceedings.size());
            if (filteredArticlesInProceedings.size() == 0) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public ArrayList<Editor> getEditors() {
        return editors;
    }

    public ArrayList<Article> getFilteredArticlesArticle() {
        return filteredArticlesArticle;
    }

    public ArrayList<inProceedings> getFilteredArticlesInProceedings() {
        return filteredArticlesInProceedings;
    }
}
