package com.tesi.code;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainController {
    @FXML
    private Button btnChooseFile;

    @FXML
    private Button btnLoad;

    @FXML
    private TextArea txtTextArea;

    private boolean loaded = false;

    @FXML
    void chooseFile(ActionEvent event) {
        FileHandler f = new FileHandler();
        f.chooseFile(txtTextArea); //mi serve solo in fase di debug. da eliminare poi
        loaded = true;
    }

    @FXML
    void load(ActionEvent event) throws IOException, ClassNotFoundException {
        Database db = new Database();
        Utility u = new Utility();
        /*
            if(loaded == true || txtTextArea.getText()!="") {
                if("@article"){
                    loadTypeBib(u.Article);
                    db.connection(u.Article);
                }
                else{
                    loadTypeBib(u.inProceedings);
                    db.connection(u.inProceedings);
                }
            }
       */

        db.connection(u.Article);
        //db.connection(u.inProceedings);
        db.insertIntoDB(u.Article);
        db.reading(u.Article);
    }

    private void loadTypeBib(String type) throws IOException {
        Stage stage = (Stage) btnLoad.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(type + ".fxml"));
        Pane root = (Pane) fxmlLoader.load();
        Scene scene = new Scene(root, 600, 442);
        stage.setTitle(type);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}