package com.tesi.code.Controller;

import com.tesi.code.Database;
import com.tesi.code.FileHandler;
import com.tesi.code.ParserInProceedings;
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
        ParserInProceedings pip=ParserInProceedings.getInstance();
        db.openConnection(u.inProceedings);
        db.createTableInProceedings();
        //if(!txtShortTitle.getText().equalsIgnoreCase("") && !txtAddress.getText().equalsIgnoreCase(""))
        db.insertIntoDBInProceedings(pip.getYear(),pip.getPages(),pip.getDblp(),txtTitle.getText(),pip.getVolume(),txtShortTitle.getText(),pip.getUrl(),
                    txtAddress.getText(),pip.getPublisher(),pip.getSeries(),pip.getBooktitle(),pip.getDoi(),pip.getAuthor(),pip.getEditor());


        db.readingArticle();
        db.readingAuthor();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ParserInProceedings p = ParserInProceedings.getInstance();
        System.out.println("title: " + p.getTitle());
        txtBookTitle.setText(p.getBooktitle());
        txtTitle.setText(p.getTitle());
        txtShortTitle.setText("");
        txtAddress.setText("");
    }
}