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
    private TableView<Article> tblView;

    @FXML
    private Button btnShow;

    @FXML
    void filterByArticle(ActionEvent event) {

    }

    @FXML
    void filterByAuthor(ActionEvent event) {
        Database db = Database.getInstance();

        db.openConnection(Utility.inProceedings);
        db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText());
        db.closeConnection();

        db.openConnection(Utility.article);
        db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText());
        db.closeConnection();
    }

    @FXML
    void filterByType(ActionEvent event) {
        Database db = Database.getInstance();
        if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
            db.openConnection(Utility.inProceedings);
            System.out.println("db filterbytype " + db.getEditors().size() + " " + db.getAllEditors().size());
            System.out.println(Utility.inProceedings);
            db.filterByTypeInProceedings();
            System.out.println("db filterbytype " + db.getEditors().size() + " " + db.getAllEditors().size());
        } else {
            db.openConnection(Utility.article);
            db.filterByTypeArticle();
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
        ArrayList<Article> filteredArticle = db.getFilteredArticles();
        System.out.println(filteredArticle.size());

        for (int i = 0; i < filteredArticle.size(); i++) {
            TableColumn<Article, String> column1 = new TableColumn<Article, String>(Utility.year);
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


            tblView.getColumns().add(column1);
            tblView.getColumns().add(column2);
            tblView.getColumns().add(column3);
            tblView.getColumns().add(column4);
            tblView.getColumns().add(column5);
            tblView.getColumns().add(column6);
            tblView.getColumns().add(column7);
            tblView.getColumns().add(column8);
            tblView.getColumns().add(column9);


            for(int j=0;j<db.getAllAuthors().size();j++) {
                System.out.println("J: "+j);
                TableColumn<Article, String> column10 = new TableColumn<>(Utility.name+" Author");
                String name = db.getAllAuthors().get(j).getNameAuthor();
                column10.setCellValueFactory(c -> new SimpleStringProperty(name));


                TableColumn<Article, String> column11 = new TableColumn<>(Utility.surname+" Author");
                String surname = db.getAllAuthors().get(j).getSurnameAuthor();
                column11.setCellValueFactory(c -> new SimpleStringProperty(surname));


                tblView.getColumns().add(column10);
                tblView.getColumns().add(column11);
            }

            System.out.println("size export controller: " + db.getAllEditors().size());
            for(int j=0;j<db.getAllEditors().size();j++) {
                System.out.println("H: "+j);
                TableColumn<Article, String> column12 = new TableColumn<>(Utility.name+" Editor");
                String name = db.getAllEditors().get(j).getNameEditor();
                column12.setCellValueFactory(c -> new SimpleStringProperty(name));


                TableColumn<Article, String> column13 = new TableColumn<>(Utility.surname+" Editor");
                String surname = db.getAllEditors().get(j).getSurnameEditor();
                column13.setCellValueFactory(c -> new SimpleStringProperty(surname));


                tblView.getColumns().add(column12);
                tblView.getColumns().add(column13);
            }

            tblView.getItems().add(filteredArticle.get(i));
        }
    }
}
