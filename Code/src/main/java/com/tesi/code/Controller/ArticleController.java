package com.tesi.code.Controller;

import com.tesi.code.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ArticleController implements Initializable {

    @FXML
    private TextField txtJournal;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtShortTitle;

    @FXML
    private Button btnSave;

    @FXML
    void save(ActionEvent event) throws ClassNotFoundException {
        Database db = new Database();
        Utility u = new Utility();
        File file=new FileHandler().getFile();
        ParserArticle pa=ParserArticle.getInstance();
        //da eliminare e mettere nell'if sopra
        db.openConnection(u.article);
        db.createTableArticle();
        db.insertIntoDBArticle(pa.getYear(),pa.getPages(),pa.getDblp(),txtTitle.getText(),pa.getVolume(),txtShortTitle.getText(),pa.getUrl()
                ,pa.getSeries(),pa.getBooktitle(),pa.getDoi(),pa.getAuthor(),txtJournal.getText());
        db.readingArticleArticle();
        db.readingAuthorArticle();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ParserArticle p= ParserArticle.getInstance();
        //System.out.println("title: "+p.getTitle());
        txtJournal.setText(p.getJournal());
        txtTitle.setText(p.getTitle());
        txtShortTitle.setText("Va?");
    }
}
