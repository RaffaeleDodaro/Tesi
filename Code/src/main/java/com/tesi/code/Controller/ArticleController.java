package com.tesi.code.Controller;

import com.tesi.code.Database;
import com.tesi.code.FileHandler;
import com.tesi.code.Utility;
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

        //da eliminare e mettere nell'if sopra
        db.openConnection(u.article);
        db.insertIntoDB(file, u.article, txtJournal.getText(), txtTitle.getText(), txtShortTitle.getText(),"");
        //db.reading(u.article);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtJournal.setText("Ciao");
        txtTitle.setText("Come");
        txtShortTitle.setText("Va?");
    }
}
