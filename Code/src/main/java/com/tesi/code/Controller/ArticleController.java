package com.tesi.code.Controller;

import com.tesi.code.*;
import com.tesi.code.Parser.GenericParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
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
    private Button btnAnotherBibtex;

    @FXML
    void anotherBibtex(ActionEvent event)  throws IOException {
        Stage stage = (Stage) btnAnotherBibtex.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChooseFile.fxml"));
        Pane root = (Pane) fxmlLoader.load();
        Scene scene = new Scene(root, 600, 442);
        stage.setTitle("Tesi");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void save(ActionEvent event) throws ClassNotFoundException {
        Database db = new Database();
        Utility u = new Utility();
        File file=new FileHandler().getFile();
        GenericParser gp=GenericParser.getInstance();
        //da eliminare e mettere nell'if sopra
        db.openConnection(u.article);
        db.createTableArticle();
        if(!txtShortTitle.getText().equalsIgnoreCase("") && !txtJournal.getText().equalsIgnoreCase(""))
            db.insertIntoDBArticle(gp.getYear(),gp.getPages(),gp.getDblp(),txtTitle.getText(),gp.getVolume(),txtShortTitle.getText(),gp.getUrl()
                ,gp.getDoi(),gp.getAuthor(),txtJournal.getText());
        else if(txtShortTitle.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null,"Insert short title", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        else if(txtJournal.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null,"Insert journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        db.readingArticleArticle();
        db.readingAuthorArticle();
        db.closeConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericParser gp= GenericParser.getInstance();
        txtJournal.setText(gp.getJournal());
        txtTitle.setText(gp.getTitle());
        txtShortTitle.setText("Va?");
    }
}
