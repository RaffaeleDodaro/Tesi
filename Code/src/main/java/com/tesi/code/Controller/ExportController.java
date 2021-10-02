package com.tesi.code.Controller;

import com.tesi.code.Database;
import com.tesi.code.Model.Article;
import com.tesi.code.Model.Author;
import com.tesi.code.Model.Editor;
import com.tesi.code.Parser.GenericParser;
import com.tesi.code.Utility;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import com.tesi.code.Model.inProceedings;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExportController implements Initializable {
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
    private TableView<Article> tblViewinProceedings;

    @FXML
    private TableView<Article> tblViewArticle;

    private ArrayList<Article> savedArticle = new ArrayList<>();
    private ArrayList<inProceedings> savedInProceedings = new ArrayList<>();

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
        cbChooseType.setValue(Utility.inProceedings);
        cbChooseType.getItems().add(Utility.inProceedings);
        cbChooseType.getItems().add(Utility.article);

        for (int i = 2010; i < 2031; i++)
            cbArticleYear.getItems().add(i);

        cbArticleYear.setValue(2010);
        tblViewArticle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblViewinProceedings.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    void saveSelectedArticle(ActionEvent event) throws IOException {
        if (tblViewArticle.getItems().size() > 0 || tblViewinProceedings.getItems().size() > 0) {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                BufferedWriter bOut = new BufferedWriter(new FileWriter(file));

                bOut.append("-------ARTICLE-------\n");
                for (int i = 0; i < tblViewArticle.getItems().size(); i++) {
                    if (tblViewArticle.getItems().get(i).isCheck().getValue()) {
                        if (!existsArticle(tblViewArticle.getItems().get(i), savedArticle))
                            savedArticle.add(tblViewArticle.getItems().get(i));
                    }
                }
                saveArticle(bOut);
                bOut.append("\n\n-------INPROCEEDINGS-------\n");
                for (int i = 0; i < tblViewinProceedings.getItems().size(); i++)
                    if (tblViewinProceedings.getItems().get(i).isCheck().getValue()) {
                        if (!existsInProceedings(tblViewinProceedings.getItems().get(i), savedInProceedings))
                            savedInProceedings.add((inProceedings) tblViewinProceedings.getItems().get(i));
                    }
                saveInProceedings(bOut);

                bOut.close();
            }
        }
    }

    private boolean existsArticle(Article a, ArrayList<Article> savedArticle) {
        for (int i = 0; i < savedArticle.size(); i++)
            if (savedArticle.get(i).getDblp().equals(a.getDblp()))
                return true;
        return false;
    }

    private boolean existsInProceedings(Article a, ArrayList<inProceedings> savedArticle) {
        for (int i = 0; i < savedArticle.size(); i++)
            if (savedArticle.get(i).getDblp().equals(a.getDblp()))
                return true;
        return false;
    }

    private void showInProceedings(ArrayList<Article> filteredArticle, Database db) {

        tblViewinProceedings.getItems().clear();

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

        TableColumn<Article, String> column4 = new TableColumn<>(Utility.publisher);
        column4.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<Article, String> column5 = new TableColumn<>(Utility.series);
        column5.setCellValueFactory(new PropertyValueFactory<>("series"));

        TableColumn<Article, String> column6 = new TableColumn<>(Utility.volume);
        column6.setCellValueFactory(new PropertyValueFactory<>("volume"));

        TableColumn<Article, String> column7 = new TableColumn<>(Utility.shortTitle);
        column7.setCellValueFactory(new PropertyValueFactory<>("shortTitle"));

        TableColumn<Article, String> column8 = new TableColumn<>(Utility.url);
        column8.setCellValueFactory(new PropertyValueFactory<>("url"));

        TableColumn<Article, String> column9 = new TableColumn<>(Utility.doi);
        column9.setCellValueFactory(new PropertyValueFactory<>("doi"));

        TableColumn<Article, String> column10 = new TableColumn<>(Utility.address);
        column10.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Article, String> column11 = new TableColumn<>(Utility.booktitle);
        column11.setCellValueFactory(new PropertyValueFactory<>("booktitle"));

        TableColumn<Article, String> column12 = new TableColumn<>(Utility.title);
        column12.setCellValueFactory(new PropertyValueFactory<>("title"));

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


        for (Article a : fxlist)
            tblViewinProceedings.getItems().add(a);

        /*for (int j = 0; j < db.getAllAuthors().size(); j++) {
            TableColumn<Article, String> column13 = new TableColumn<>("Author's " + Utility.name);
            String name = db.getAllAuthors().get(j).getNameAuthor();
            column13.setCellValueFactory(new PropertyValueFactory<>("arraylistToString"));
            System.out.println("\n"+"Name: " + name);

            TableColumn<Article, String> column14 = new TableColumn<>("Author's " + Utility.surname);
            String surname = db.getAllAuthors().get(j).getSurnameAuthor();
            column14.setCellValueFactory(c -> new SimpleStringProperty(surname));
            System.out.println("Surname: " + surname);

            tblViewinProceedings.getColumns().add(column13);
            tblViewinProceedings.getColumns().add(column14);
        }*/
    }

    private void showArticles(ArrayList<Article> filteredArticle, Database db) {
        tblViewArticle.getItems().clear();

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


        for (Article a : fxlist)
            tblViewArticle.getItems().add(a);

        /*for (int j = 0; j < db.getAllAuthors().size(); j++) {
            TableColumn<Article, String> column10 = new TableColumn<>("Author's " + Utility.name);
            String name = db.getAllAuthors().get(j).getNameAuthor();
            column10.setCellValueFactory(new PropertyValueFactory<>("arraylistToString"));
            System.out.println("\n"+"Name: " + name);

            TableColumn<Article, String> column11 = new TableColumn<>("Author's " + Utility.surname);
            String surname = db.getAllAuthors().get(j).getSurnameAuthor();
            column11.setCellValueFactory(c -> new SimpleStringProperty(surname));
            System.out.println("Surname: " + surname);

            tblViewArticle.getColumns().add(column10);
            tblViewArticle.getColumns().add(column11);
        }*/

    }

    private void saveArticle(BufferedWriter bOut) throws IOException {
        for (int i = 0; i < savedArticle.size(); i++) {
            int year = savedArticle.get(i).getYear();
            String pages = savedArticle.get(i).getPages();
            String dblp = savedArticle.get(i).getDblp();
            String title = savedArticle.get(i).getTitle();
            int volume = savedArticle.get(i).getVolume();
            String shortTitle = savedArticle.get(i).getShortTitle();
            String url = savedArticle.get(i).getUrl();
            String doi = savedArticle.get(i).getDoi();
            String journal = savedArticle.get(i).getJournal();
            String type = savedArticle.get(i).getType();
            ArrayList<Author> allAuthors = savedArticle.get(i).getAllAuthors();
            bOut.append("year: " + year + "\npages: " + pages + "\ndblp: " + dblp + "\ntitle: " + title +
                    "\nvolume: " + volume + "\nShort title: " + shortTitle + "\nurl: " + url + "\ndoi: " + doi +
                    "\njournal: " + journal + "\ntype: " + type);
            for (int j = 0; j < allAuthors.size(); j++) {
                bOut.append("\nAutore " + j + ": " + allAuthors.get(j).getNameAuthor() + " " + allAuthors.get(j).getSurnameAuthor());
            }
            bOut.append("\n*************************\n");
        }
    }

    private void saveInProceedings(BufferedWriter printWriter) throws IOException {
        for (int i = 0; i < savedInProceedings.size(); i++) {
            if (savedInProceedings.get(i) instanceof inProceedings) {
                int year = savedInProceedings.get(i).getYear();
                String pages = savedInProceedings.get(i).getPages();
                String dblp = savedInProceedings.get(i).getDblp();
                String title = savedInProceedings.get(i).getTitle();
                int volume = savedInProceedings.get(i).getVolume();
                String shortTitle = savedInProceedings.get(i).getShortTitle();
                String url = savedInProceedings.get(i).getUrl();
                String doi = savedInProceedings.get(i).getDoi();
                String type = savedInProceedings.get(i).getType();
                String publisher = ((inProceedings) savedInProceedings.get(i)).getPublisher();
                String series = ((inProceedings) savedInProceedings.get(i)).getSeries();
                String address = ((inProceedings) savedInProceedings.get(i)).getAddress();
                String booktitle = ((inProceedings) savedInProceedings.get(i)).getBooktitle();
                ArrayList<Editor> allEditor = ((inProceedings) savedInProceedings.get(i)).getAllEditors();
                //BooleanProperty check = tblViewinProceedings.getItems().get(i).isCheck();
                ArrayList<Author> allAuthors = savedInProceedings.get(i).getAllAuthors();
                printWriter.append("year: " + year + "\npages: " + pages + "\ndblp: " + dblp + "\ntitle: " + title +
                        "\nvolume: " + volume + "\nShort title: " + shortTitle + "\nurl: " + url + "\ndoi: " + doi +
                        "\npublisher" + publisher + "\ntype: " + type + "\nseries: " + series + "\naddress: " + address +
                        "\nbook title: " + booktitle);
                for (int j = 0; j < allAuthors.size(); j++) {
                    printWriter.append("\nAutore " + j + ": " + allAuthors.get(j).getNameAuthor() + " " + allAuthors.get(j).getSurnameAuthor());
                }
                for (int j = 0; j < allEditor.size(); j++) {
                    printWriter.append("\nEditore " + j + ": " + allEditor.get(j).getNameEditor() + " " + allEditor.get(j).getSurnameEditor());
                }
                printWriter.append("\n*************************\n");
            }
        }
    }

}
