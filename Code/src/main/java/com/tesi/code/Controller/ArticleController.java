package com.tesi.code.Controller;

import com.tesi.code.*;
import com.tesi.code.Parser.GenericParser;
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
        GenericParser gp=GenericParser.getInstance();
        //da eliminare e mettere nell'if sopra
        db.openConnection(u.article);
        db.createTableArticle();
        db.insertIntoDBArticle(gp.getYear(),gp.getPages(),gp.getDblp(),txtTitle.getText(),gp.getVolume(),txtShortTitle.getText(),gp.getUrl()
                ,gp.getDoi(),gp.getAuthor(),txtJournal.getText());
        db.readingArticleArticle();
        db.readingAuthorArticle();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericParser gp= GenericParser.getInstance();
        //System.out.println("title: "+p.getTitle());
        txtJournal.setText(gp.getJournal());
        txtTitle.setText(gp.getTitle());
        txtShortTitle.setText("Va?");
    }
}
