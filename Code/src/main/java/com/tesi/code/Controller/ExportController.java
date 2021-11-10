package com.tesi.code.Controller;

import com.tesi.code.Database;
import com.tesi.code.Main;
import com.tesi.code.Model.Article;
import com.tesi.code.Model.Author;
import com.tesi.code.Model.Editor;
import com.tesi.code.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import com.tesi.code.Model.inProceedings;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExportController implements Initializable {
    @FXML
    private CheckBox cbSaveCompact;

    @FXML
    private ChoiceBox<String> cbChooseType;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtAuthorName;

    @FXML
    private TextField txtAuthorSurname;

    @FXML
    private Button btnFilterByAuthor;

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
    private Button btnAnotherBibtex;

    @FXML
    private TableView<inProceedings> tblViewinProceedings;

    @FXML
    private TableView<Article> tblViewArticle;

    @FXML
    private CheckBox cbFilterByYear;

    @FXML
    private Button btnFilter;

    private final ArrayList<Article> savedArticle = new ArrayList<>();
    private final ArrayList<inProceedings> savedInProceedings = new ArrayList<>();

    @FXML
    void filter(ActionEvent event) {
        type();
        if (!txtArticleTitle.getText().equalsIgnoreCase("") || !txtArticleJournal.getText().equalsIgnoreCase("")
                || cbFilterByYear.isSelected())
            article();

        if (!txtAuthorName.getText().equalsIgnoreCase("") || !txtAuthorSurname.getText().equalsIgnoreCase(""))
            author();

        Database db = Database.getInstance();
        showArticles(db.getFilteredArticlesArticle());
        showInProceedings(db.getFilteredArticlesInProceedings());
    }

    private void type() {
        Database db = Database.getInstance();
        db.cleanAll();
        cleanTbl();

        if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
            db.openConnection(Utility.inProceedings);
            db.filterByTypeInProceedings();
        } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.article)) {
            db.openConnection(Utility.article);
            db.filterByTypeArticle();
        } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.all)) {
            db.openConnection(Utility.inProceedings);
            db.filterByTypeInProceedings();
            db.closeConnection();

            db.openConnection(Utility.article);
            db.filterByTypeArticle();
        }
        db.closeConnection();
    }

    private void article() {
        Database db = Database.getInstance();
        String title = txtArticleTitle.getText();
        String journal = txtArticleJournal.getText();
        int year = cbArticleYear.getValue();

        if (cbChooseType.getValue().equalsIgnoreCase(Utility.all)) {
            db.openConnection(Utility.article);
            if (cbFilterByYear.isSelected())
                db.refilterByArticleArticle(title, journal, year);
            else
                db.refilterByArticleArticle(title, journal, -1);
            db.closeConnection();


            db.openConnection(Utility.inProceedings);
            if (cbFilterByYear.isSelected())
                db.refilterByArticleInProceedings(title, year);
            else
                db.refilterByArticleInProceedings(title, -1);
            db.closeConnection();

        } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
            db.openConnection(Utility.inProceedings);

            if (cbFilterByYear.isSelected())
                db.refilterByArticleInProceedings(title, year);
            else
                db.refilterByArticleInProceedings(title, -1);
            db.closeConnection();

        } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.article)) {
            db.openConnection(Utility.article);
            if (cbFilterByYear.isSelected())
                db.refilterByArticleArticle(title, journal, year);
            else
                db.refilterByArticleArticle(title, journal, -1);
            db.closeConnection();
        }
    }

    private void author() {
        Database db = Database.getInstance();
        if (cbChooseType.getValue().equalsIgnoreCase(Utility.all)) {
            db.openConnection(Utility.inProceedings);
            db.refilterByAuthorInProceedings(txtAuthorSurname.getText(), txtAuthorName.getText());
            db.closeConnection();

            db.openConnection(Utility.article);
            db.refilterByAuthorArticle(txtAuthorSurname.getText(), txtAuthorName.getText());
            db.closeConnection();
        } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
            db.openConnection(Utility.inProceedings);
            db.refilterByAuthorInProceedings(txtAuthorSurname.getText(), txtAuthorName.getText());
            db.closeConnection();
        } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.article)) {
            db.openConnection(Utility.article);
            db.refilterByAuthorArticle(txtAuthorSurname.getText(), txtAuthorName.getText());
            db.closeConnection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbChooseType.setValue(Utility.all.toUpperCase(Locale.ROOT));
        cbChooseType.getItems().add(Utility.all.toUpperCase(Locale.ROOT));
        cbChooseType.getItems().add(Utility.inProceedings);
        cbChooseType.getItems().add(Utility.article);

        for (int i = 2010; i < 2031; i++)
            cbArticleYear.getItems().add(i);

        cbArticleYear.setValue(2019);
        tblViewArticle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblViewinProceedings.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    void saveSelectedArticle(ActionEvent event) throws IOException {
        if (tblViewArticle.getItems().size() > 0 || tblViewinProceedings.getItems().size() > 0) {
            FileChooser fileChooser = new FileChooser();

            // Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                BufferedWriter bOut = new BufferedWriter(new FileWriter(file));

                for (int i = 0; i < tblViewArticle.getItems().size(); i++)
                    if (tblViewArticle.getItems().get(i).isCheck().getValue())
                        if (!existsArticle(tblViewArticle.getItems().get(i)))
                            savedArticle.add(tblViewArticle.getItems().get(i));

                saveArticle(bOut);
                for (int i = 0; i < tblViewinProceedings.getItems().size(); i++)
                    if (tblViewinProceedings.getItems().get(i).isCheck().getValue())
                        if (!existsInProceedings(tblViewinProceedings.getItems().get(i), savedInProceedings))
                            savedInProceedings.add((inProceedings) tblViewinProceedings.getItems().get(i));
                saveInProceedings(bOut);
                bOut.close();
            }
        }
    }

    @FXML
    void anotherBibtex(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnAnotherBibtex.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChooseFile.fxml"));
        Pane root = (Pane) fxmlLoader.load();
        Scene scene = new Scene(root, 600, 442);
        stage.setTitle("Tesi");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void cleanTbl() {
        tblViewinProceedings.getItems().clear();
        tblViewinProceedings.getColumns().clear();
        tblViewinProceedings.refresh();

        tblViewArticle.getItems().clear();
        tblViewArticle.getColumns().clear();
        tblViewArticle.refresh();
    }

    private boolean existsArticle(Article a) {
        for (Article article : savedArticle)
            if (article.getDblp().equals(a.getDblp()))
                return true;
        return false;
    }

    private boolean existsInProceedings(Article a, ArrayList<inProceedings> savedArticle) {
        for (inProceedings inProceedings : savedArticle)
            if (inProceedings.getDblp().equals(a.getDblp()))
                return true;
        return false;
    }

    private void showInProceedings(ArrayList<inProceedings> filteredInProceedings) {

        ObservableList<inProceedings> fxlist = FXCollections.observableList(filteredInProceedings);

        TableColumn<inProceedings, Boolean> column20 = new TableColumn<>("");
        column20.setCellValueFactory(features -> features.getValue().checkProperty());
        column20.setCellFactory(CheckBoxTableCell.forTableColumn(column20));

        TableColumn<inProceedings, Integer> column1 = new TableColumn<>(Utility.year);
        column1.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<inProceedings, String> column2 = new TableColumn<>(Utility.dblp);
        column2.setCellValueFactory(new PropertyValueFactory<>("dblp"));

        TableColumn<inProceedings, String> column3 = new TableColumn<>(Utility.pages);
        column3.setCellValueFactory(new PropertyValueFactory<>("pages"));

        TableColumn<inProceedings, String> column4 = new TableColumn<>(Utility.publisher);
        column4.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<inProceedings, String> column5 = new TableColumn<>(Utility.series);
        column5.setCellValueFactory(new PropertyValueFactory<>("series"));

        TableColumn<inProceedings, String> column6 = new TableColumn<>(Utility.volume);
        column6.setCellValueFactory(new PropertyValueFactory<>("volume"));

        TableColumn<inProceedings, String> column7 = new TableColumn<>(Utility.shortTitle);
        column7.setCellValueFactory(new PropertyValueFactory<>("shortTitle"));

        TableColumn<inProceedings, String> column8 = new TableColumn<>(Utility.url);
        column8.setCellValueFactory(new PropertyValueFactory<>("url"));

        TableColumn<inProceedings, String> column9 = new TableColumn<>(Utility.doi);
        column9.setCellValueFactory(new PropertyValueFactory<>("doi"));

        TableColumn<inProceedings, String> column10 = new TableColumn<>(Utility.address);
        column10.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<inProceedings, String> column11 = new TableColumn<>(Utility.booktitle);
        column11.setCellValueFactory(new PropertyValueFactory<>("booktitle"));

        TableColumn<inProceedings, String> column12 = new TableColumn<>(Utility.title);
        column12.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<inProceedings, String> column13 = new TableColumn<>("Author: " + Utility.name + " " + Utility.surname);
        column13.setCellValueFactory(new PropertyValueFactory<>("allAuthorNameAndSurname"));

        TableColumn<inProceedings, String> column14 = new TableColumn<>("Editor: " + Utility.name + " " + Utility.surname);
        column14.setCellValueFactory(new PropertyValueFactory<>("allEditorNameAndSurname"));

        tblViewinProceedings.getColumns().add(column20);
        tblViewinProceedings.getColumns().add(column1);
        tblViewinProceedings.getColumns().add(column2);
        tblViewinProceedings.getColumns().add(column3);
        tblViewinProceedings.getColumns().add(column4);
        tblViewinProceedings.getColumns().add(column5);
        tblViewinProceedings.getColumns().add(column6);
        tblViewinProceedings.getColumns().add(column7);
        tblViewinProceedings.getColumns().add(column8);
        tblViewinProceedings.getColumns().add(column9);
        tblViewinProceedings.getColumns().add(column10);
        tblViewinProceedings.getColumns().add(column11);
        tblViewinProceedings.getColumns().add(column12);
        tblViewinProceedings.getColumns().add(column13);
        tblViewinProceedings.getColumns().add(column14);

        for (inProceedings a : fxlist)
            tblViewinProceedings.getItems().add(a);
    }

    private void showArticles(ArrayList<Article> filteredArticle) {

        ObservableList<Article> fxlist = FXCollections.observableList(filteredArticle);

        TableColumn<Article, Boolean> column20 = new TableColumn<>("");
        column20.setCellValueFactory(features -> features.getValue().checkProperty());
        column20.setCellFactory(CheckBoxTableCell.forTableColumn(column20));

        TableColumn<Article, Integer> column1 = new TableColumn<>(Utility.year);
        column1.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Article, String> column2 = new TableColumn<>(Utility.dblp);
        column2.setCellValueFactory(new PropertyValueFactory<>("dblp"));

        TableColumn<Article, String> column3 = new TableColumn<>(Utility.pages);
        column3.setCellValueFactory(new PropertyValueFactory<>("pages"));

        TableColumn<Article, String> column4 = new TableColumn<>(Utility.journal);
        column4.setCellValueFactory(new PropertyValueFactory<>("journal"));

        TableColumn<Article, String> column5 = new TableColumn<>(Utility.title);
        column5.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Article, String> column6 = new TableColumn<>(Utility.volume);
        column6.setCellValueFactory(new PropertyValueFactory<>("volume"));

        TableColumn<Article, String> column7 = new TableColumn<>(Utility.shortTitle);
        column7.setCellValueFactory(new PropertyValueFactory<>("shortTitle"));

        TableColumn<Article, String> column8 = new TableColumn<>(Utility.url);
        column8.setCellValueFactory(new PropertyValueFactory<>("url"));

        TableColumn<Article, String> column9 = new TableColumn<>(Utility.doi);
        column9.setCellValueFactory(new PropertyValueFactory<>("doi"));

        TableColumn<Article, String> column10 = new TableColumn<>("Author: " + Utility.name + " " + Utility.surname);
        column10.setCellValueFactory(new PropertyValueFactory<>("allAuthorNameAndSurname"));

        tblViewArticle.getColumns().add(column20);
        tblViewArticle.getColumns().add(column1);
        tblViewArticle.getColumns().add(column2);
        tblViewArticle.getColumns().add(column3);
        tblViewArticle.getColumns().add(column4);
        tblViewArticle.getColumns().add(column5);
        tblViewArticle.getColumns().add(column6);
        tblViewArticle.getColumns().add(column7);
        tblViewArticle.getColumns().add(column8);
        tblViewArticle.getColumns().add(column9);
        tblViewArticle.getColumns().add(column10);

        for (Article a : fxlist)
            tblViewArticle.getItems().add(a);
    }

    private void saveArticle(BufferedWriter bOut) throws IOException {
        for (Article article : savedArticle) {
            int year = article.getYear();
            String pages = article.getPages();
            String dblp = article.getDblp();
            String title = article.getTitle();
            int volume = article.getVolume();
            String shortTitle = article.getShortTitle();
            String url = article.getUrl();
            String doi = article.getDoi();
            String journal = article.getJournal();
            ArrayList<Author> allAuthors = article.getAllAuthors();
            StringBuilder s = new StringBuilder();
            for (Author allAuthor : allAuthors)
                s.append(allAuthor.getName() + " {" + allAuthor.getSurname() + "} and \n               ");

            s.delete(s.length() - 21, s.length());
            if (!cbSaveCompact.isSelected())
                bOut.append("@article{DBLP:" + dblp + ",\n" +
                        "  author      = {" + s + "},\n" +
                        "  title       = {" + title + "},\n" +
                        "  short title = {" + shortTitle + "},\n" +
                        "  journal     = {" + journal + "},\n" +
                        "  volume      = {" + volume + "},\n" +
                        "  pages       = {" + pages + "},\n" +
                        "  year        = {" + year + "},\n" +
                        "  url         = {" + url + "},\n" +
                        "  doi         = {" + doi + "},\n" +
                        "}");
            else
                bOut.append("@article{DBLP:" + dblp + ",\n" +
                        "  author      = {" + s + "},\n" +
                        "  title       = {" + title + "},\n" +
                        "  journal     = {" + shortTitle + "},\n" +
                        "  volume      = {" + volume + "},\n" +
                        "  pages       = {" + pages + "},\n" +
                        "  year        = {" + year + "},\n" +
                        "  url         = {" + url + "},\n" +
                        "  doi         = {" + doi + "},\n" +
                        "}");

            bOut.append("\n\n\n\n");
        }
    }

    private void saveInProceedings(BufferedWriter bOut) throws IOException {
        for (inProceedings savedInProceeding : savedInProceedings) {
            if (savedInProceeding instanceof inProceedings) {
                int year = savedInProceeding.getYear();
                String pages = savedInProceeding.getPages();
                String dblp = savedInProceeding.getDblp();
                String title = savedInProceeding.getTitle();
                int volume = savedInProceeding.getVolume();
                String shortTitle = savedInProceeding.getShortTitle();
                String url = savedInProceeding.getUrl();
                String doi = savedInProceeding.getDoi();
                String publisher = ((inProceedings) savedInProceeding).getPublisher();
                String series = ((inProceedings) savedInProceeding).getSeries();
                String address = ((inProceedings) savedInProceeding).getAddress();
                String booktitle = ((inProceedings) savedInProceeding).getBooktitle();
                ArrayList<Editor> allEditors = ((inProceedings) savedInProceeding).getAllEditors();
                ArrayList<Author> allAuthors = savedInProceeding.getAllAuthors();

                StringBuilder s = new StringBuilder();
                StringBuilder e = new StringBuilder();
                for (Author allAuthor : allAuthors)
                    s.append(allAuthor.getName() + " {" + allAuthor.getSurname() + "} and \n               ");

                for (Editor allEditor : allEditors)
                    e.append(allEditor.getName() + " {" + allEditor.getSurname() + "} and \n               ");

                s.delete(s.length() - 21, s.length());
                e.delete(e.length() - 21, e.length());

                if (!cbSaveCompact.isSelected()) {
                    bOut.append("@inproceedings{DBLP:" + dblp + ",\n" +
                            "  author      = {" + s + "},\n" +
                            "  editor      = {" + e + "},\n" +
                            "  title       = {" + title + "},\n" +
                            "  short title = {" + shortTitle + "},\n" +
                            "  booktitle   = {" + booktitle + "},\n" +
                            "  series      = {" + series + "},\n" +
                            "  volume      = {" + volume + "},\n" +
                            "  pages       = {" + pages + "},\n" +
                            "  publisher   = {" + publisher + "},\n" +
                            "  year        = {" + year + "},\n" +
                            "  url         = {" + url + "},\n" +
                            "  doi         = {" + doi + "},\n" +
                            "  address     = {" + address + "},\n" +
                            "}");
                } else {
                    bOut.append("@inproceedings{DBLP:" + dblp + ",\n" +
                            "  author      = {" + s + "},\n" +
                            "  editor      = {" + e + "},\n" +
                            "  title       = {" + title + "},\n" +
                            "  booktitle   = {" + shortTitle + "},\n" +
                            "  series      = {" + series + "},\n" +
                            "  volume      = {" + volume + "},\n" +
                            "  pages       = {" + pages + "},\n" +
                            "  publisher   = {" + publisher + "},\n" +
                            "  year        = {" + year + "},\n" +
                            "  url         = {" + url + "},\n" +
                            "  doi         = {" + doi + "},\n" +
                            "  address     = {" + address + "},\n" +
                            "}");
                }
                bOut.append("\n\n*************************\n\n");
            }
            bOut.append("\n\n\n\n");
        }
    }
}