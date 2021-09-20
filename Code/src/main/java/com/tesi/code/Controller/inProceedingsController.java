package com.tesi.code.Controller;

import com.tesi.code.Database;
import com.tesi.code.FileHandler;
import com.tesi.code.Parser.GenericParser;
import com.tesi.code.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class inProceedingsController implements Initializable {

    @FXML
    private TextField txtBookTitle;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtShortTitle;

    @FXML
    private TextField txtAddress;

    @FXML
    private Button btnSave;

    @FXML
    void save(ActionEvent event) throws ClassNotFoundException {
        Database db = Database.getInstance();
        Utility u = Utility.getInstance();
        File file = new FileHandler().getFile();
        GenericParser gp = GenericParser.getInstance();
        db.openConnection(u.inProceedings);
        db.createTableInProceedings();
        //if(!txtShortTitle.getText().equalsIgnoreCase("") && !txtAddress.getText().equalsIgnoreCase(""))
        db.insertIntoDBInProceedings(gp.getYear(),gp.getPages(),gp.getDblp(),txtTitle.getText(),gp.getVolume(),txtShortTitle.getText(),gp.getUrl(),
                    txtAddress.getText(),gp.getPublisher(),gp.getSeries(),gp.getBooktitle(),gp.getDoi(),gp.getAuthor(),gp.getEditor());


        db.readingArticleInProceedings();
        db.readingAuthorInProceedings();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericParser gp = GenericParser.getInstance();
        txtBookTitle.setText(gp.getBooktitle());
        txtTitle.setText(gp.getTitle());
        txtShortTitle.setText("");
        txtAddress.setText("");
    }
}