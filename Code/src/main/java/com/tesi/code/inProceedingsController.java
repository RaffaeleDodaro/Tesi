package com.tesi.code;

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
        Database db = new Database();
        Utility u = new Utility();
        File file=new FileHandler().getFile();

        //da eliminare e mettere nell'if sopra
        db.openConnection(u.article);
        db.insertIntoDB(u.article,txtBookTitle.getText(),txtTitle.getText(),txtShortTitle.getText(),txtAddress.getText());
        //db.reading(u.article);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtBookTitle.setText("Ciao");
        txtTitle.setText("Come");
        txtShortTitle.setText("Va?");
        txtAddress.setText("Tutto bene");
    }

}
