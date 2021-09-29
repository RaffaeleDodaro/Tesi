package com.tesi.code.Controller;

import com.tesi.code.Database;
import com.tesi.code.Model.Article;
import com.tesi.code.Parser.GenericParser;
import com.tesi.code.Utility;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.tesi.code.Model.inProceedings;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExportController implements Initializable {
    @FXML
    private ChoiceBox<String> cbChooseType;

    @FXML
    private Button btnExport;

    @FXML
    private TextField txtAuthorName;

    @FXML
    private TextField txtAuthorSurname;

    @FXML
    private Button btnFilterByAuthor;

    @FXML
    private TextField txtArticleName;

    @FXML
    private TextField txtArticleTitle;

    @FXML
    private Button btnFilterByArticle;

    @FXML
    private TextField txtArticleJournal;

    @FXML
    private ChoiceBox<Integer> cbArticleYear;

    @FXML
    private Button btnFilterByType;


    @FXML
    private Button btnShow;

    @FXML
    private TableView<Article> tblViewinProceedings;

    @FXML
    private TableView<Article> tblViewArticle;

    @FXML
    void filterByArticle(ActionEvent event) {
        System.out.println("quiii");
        String title = txtArticleTitle.getText();
        String journal = txtArticleTitle.getText();
        int year = cbArticleYear.getValue();

        Database db = Database.getInstance();
        db.openConnection(Utility.article);
        db.cleanAll();
        db.findArticleByInfoArticleArticle(title, journal, year);
        db.closeConnection();
        db.openConnection(Utility.inProceedings);
        db.findArticleByInfoArticleInProceedings(title, year);
        db.closeConnection();
        showInProceedings(db.getFilteredArticlesInProceedings(), db);
        showArticles(db.getFilteredArticlesArticle(), db);
        db.cleanAll();
    }
/*

                    YEAR INT NOT NULL," +
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
 */

    @FXML
    void filterByAuthor(ActionEvent event) {
        Database db = Database.getInstance();
        db.cleanAll();
        db.openConnection(Utility.inProceedings);
        db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText(), Utility.inProceedings);
        db.closeConnection();

        db.openConnection(Utility.article);
        db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText(), Utility.article);
        db.closeConnection();

        showInProceedings(db.getFilteredArticlesInProceedings(), db);
        showArticles(db.getFilteredArticlesArticle(), db);
        db.cleanAll();
    }

    @FXML
    void filterByType(ActionEvent event) {
        Database db = Database.getInstance();
        db.cleanAll();
        tblViewinProceedings.getItems().clear();
        tblViewArticle.getItems().clear();
        if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
            db.openConnection(Utility.inProceedings);
            //System.out.println(Utility.inProceedings);
            db.filterByTypeInProceedings();
            ArrayList<Article> filteredArticle = db.getFilteredArticlesInProceedings();
            showInProceedings(filteredArticle, db);
        } else {
            db.openConnection(Utility.article);
            db.filterByTypeArticle();
            ArrayList<Article> filteredArticle = db.getFilteredArticlesArticle();
            showArticles(filteredArticle, db);
        }
        db.closeConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbChooseType.setValue(Utility.article);
        cbChooseType.getItems().add(Utility.inProceedings);
        cbChooseType.getItems().add(Utility.article);

        for (int i = 2010; i < 2022; i++)
            cbArticleYear.getItems().add(i);

        cbArticleYear.setValue(2010);
    }

    @FXML
    void show(ActionEvent event) {
        Database db = Database.getInstance();
        //System.out.println("SHOW filteredArticle.size() " + filteredArticle.size());


    }

    private void showInProceedings(ArrayList<Article> filteredArticle, Database db) {
        tblViewinProceedings.getItems().clear();
        for (int i = 0; i < filteredArticle.size(); i++) {

            TableColumn<Article, String> column1 = new TableColumn<>(Utility.year);
            int year = filteredArticle.get(i).getYear();
            column1.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(year)));


            TableColumn<Article, String> column2 = new TableColumn<>(Utility.dblp);
            String dblp = filteredArticle.get(i).getDblp();
            column2.setCellValueFactory(c -> new SimpleStringProperty(dblp));


            TableColumn<Article, String> column3 = new TableColumn<>(Utility.pages);
            String pages = filteredArticle.get(i).getPages();
            column3.setCellValueFactory(c -> new SimpleStringProperty(pages));

            TableColumn<Article, String> column5 = new TableColumn<>(Utility.title);
            String title = filteredArticle.get(i).getTitle();
            column5.setCellValueFactory(c -> new SimpleStringProperty(title));


            TableColumn<Article, String> column6 = new TableColumn<>(Utility.volume);
            String volume = String.valueOf(filteredArticle.get(i).getVolume());
            column6.setCellValueFactory(c -> new SimpleStringProperty(volume));


            TableColumn<Article, String> column7 = new TableColumn<>(Utility.shortTitle);
            String shortTitle = filteredArticle.get(i).getShortTitle();
            column7.setCellValueFactory(c -> new SimpleStringProperty(shortTitle));


            TableColumn<Article, String> column8 = new TableColumn<>(Utility.url);
            String url = filteredArticle.get(i).getUrl();
            column8.setCellValueFactory(c -> new SimpleStringProperty(url));


            TableColumn<Article, String> column9 = new TableColumn<>(Utility.doi);
            String doi = filteredArticle.get(i).getDoi();
            column9.setCellValueFactory(c -> new SimpleStringProperty(doi));

            if (filteredArticle.get(i) instanceof inProceedings) {
                TableColumn<Article, String> column14 = new TableColumn<>("Publisher");
                String publisher = ((inProceedings) filteredArticle.get(i)).getPublisher();
                column14.setCellValueFactory(c -> new SimpleStringProperty(publisher));


                TableColumn<Article, String> column15 = new TableColumn<>("Series");
                String series = ((inProceedings) filteredArticle.get(i)).getSeries();
                column15.setCellValueFactory(c -> new SimpleStringProperty(series));

                TableColumn<Article, String> column16 = new TableColumn<>("Address");
                String address = ((inProceedings) filteredArticle.get(i)).getAddress();
                column16.setCellValueFactory(c -> new SimpleStringProperty(address));

                TableColumn<Article, String> column17 = new TableColumn<>("Booktitle");
                String booktitle = ((inProceedings) filteredArticle.get(i)).getBookTitle();
                column17.setCellValueFactory(c -> new SimpleStringProperty(booktitle));


                tblViewinProceedings.getColumns().add(column1);
                tblViewinProceedings.getColumns().add(column2);
                tblViewinProceedings.getColumns().add(column3);
                tblViewinProceedings.getColumns().add(column5);
                tblViewinProceedings.getColumns().add(column6);
                tblViewinProceedings.getColumns().add(column7);
                tblViewinProceedings.getColumns().add(column8);
                tblViewinProceedings.getColumns().add(column9);
                tblViewinProceedings.getColumns().add(column14);
                tblViewinProceedings.getColumns().add(column15);
                tblViewinProceedings.getColumns().add(column16);
                tblViewinProceedings.getColumns().add(column17);
            }

            System.out.println("db.getAllAuthors().size() " + db.getAllAuthors().size());


            for (int j = 0; j < db.getAllAuthors().size(); j++) {
                System.out.println("Author number J: " + j);
                TableColumn<Article, String> column10 = new TableColumn<>(Utility.name + " Author");
                String name = db.getAllAuthors().get(j).getNameAuthor();
                column10.setCellValueFactory(c -> new SimpleStringProperty(name));


                TableColumn<Article, String> column11 = new TableColumn<>(Utility.surname + " Author");
                String surname = db.getAllAuthors().get(j).getSurnameAuthor();
                column11.setCellValueFactory(c -> new SimpleStringProperty(surname));


                tblViewinProceedings.getColumns().add(column10);
                tblViewinProceedings.getColumns().add(column11);
            }

            System.out.println("db.getAllEditors().size(): " + db.getAllEditors().size());
            for (int j = 0; j < db.getAllEditors().size(); j++) {
                System.out.println("Editor number H: " + j);
                TableColumn<Article, String> column12 = new TableColumn<>(Utility.name + " Editor");
                String name = db.getAllEditors().get(j).getNameEditor();
                column12.setCellValueFactory(c -> new SimpleStringProperty(name));


                TableColumn<Article, String> column13 = new TableColumn<>(Utility.surname + " Editor");
                String surname = db.getAllEditors().get(j).getSurnameEditor();
                column13.setCellValueFactory(c -> new SimpleStringProperty(surname));


                tblViewinProceedings.getColumns().add(column12);
                tblViewinProceedings.getColumns().add(column13);
            }


            tblViewinProceedings.getItems().add(filteredArticle.get(i));
            System.out.println("I: " + i);
        }
    }


    private void showArticles(ArrayList<Article> filteredArticle, Database db) {
        tblViewArticle.getItems().clear();
        for (int i = 0; i < filteredArticle.size(); i++) {
            if (filteredArticle.get(i) instanceof Article) {
                TableColumn<Article, String> column1 = new TableColumn<>(Utility.year);
                int year = filteredArticle.get(i).getYear();
                column1.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(year)));


                TableColumn<Article, String> column2 = new TableColumn<>(Utility.dblp);
                String dblp = filteredArticle.get(i).getDblp();
                column2.setCellValueFactory(c -> new SimpleStringProperty(dblp));


                TableColumn<Article, String> column3 = new TableColumn<>(Utility.pages);
                String pages = filteredArticle.get(i).getPages();
                column3.setCellValueFactory(c -> new SimpleStringProperty(pages));


                TableColumn<Article, String> column4 = new TableColumn<>(Utility.journal);
                String journal = filteredArticle.get(i).getJournal();
                column4.setCellValueFactory(c -> new SimpleStringProperty(journal));

                System.out.println("JOURNAL 3 " + journal);

                TableColumn<Article, String> column5 = new TableColumn<>(Utility.title);
                String title = filteredArticle.get(i).getTitle();
                column5.setCellValueFactory(c -> new SimpleStringProperty(title));


                TableColumn<Article, String> column6 = new TableColumn<>(Utility.volume);
                String volume = String.valueOf(filteredArticle.get(i).getVolume());
                column6.setCellValueFactory(c -> new SimpleStringProperty(volume));


                TableColumn<Article, String> column7 = new TableColumn<>(Utility.shortTitle);
                String shortTitle = filteredArticle.get(i).getShortTitle();
                column7.setCellValueFactory(c -> new SimpleStringProperty(shortTitle));


                TableColumn<Article, String> column8 = new TableColumn<>(Utility.url);
                String url = filteredArticle.get(i).getUrl();
                column8.setCellValueFactory(c -> new SimpleStringProperty(url));


                TableColumn<Article, String> column9 = new TableColumn<>(Utility.doi);
                String doi = filteredArticle.get(i).getDoi();
                column9.setCellValueFactory(c -> new SimpleStringProperty(doi));


                tblViewArticle.getColumns().add(column1);
                tblViewArticle.getColumns().add(column2);
                tblViewArticle.getColumns().add(column3);
                tblViewArticle.getColumns().add(column4);
                tblViewArticle.getColumns().add(column5);
                tblViewArticle.getColumns().add(column6);
                tblViewArticle.getColumns().add(column7);
                tblViewArticle.getColumns().add(column8);
                tblViewArticle.getColumns().add(column9);

                System.out.println("db.getAllAuthors().size() " + db.getAllAuthors().size());


                for (int j = 0; j < db.getAllAuthors().size(); j++) {
                    System.out.println("Author number J: " + j);
                    TableColumn<Article, String> column10 = new TableColumn<>(Utility.name + " Author");
                    String name = db.getAllAuthors().get(j).getNameAuthor();
                    column10.setCellValueFactory(c -> new SimpleStringProperty(name));


                    TableColumn<Article, String> column11 = new TableColumn<>(Utility.surname + " Author");
                    String surname = db.getAllAuthors().get(j).getSurnameAuthor();
                    column11.setCellValueFactory(c -> new SimpleStringProperty(surname));


                    tblViewArticle.getColumns().add(column10);
                    tblViewArticle.getColumns().add(column11);
                }


                tblViewArticle.getItems().add(filteredArticle.get(i));
                System.out.println("I: " + i);
            }
        }
    }
}
