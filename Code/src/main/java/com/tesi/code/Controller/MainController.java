package com.tesi.code.Controller;

import com.tesi.code.FileHandler;
import com.tesi.code.Main;
import com.tesi.code.ParserInProceedings;
import com.tesi.code.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainController {
    private boolean loaded = false;
    private File file = null;

    @FXML
    private Button btnChooseFile;

    @FXML
    private Button btnLoad;

    @FXML
    private TextArea txtTextArea;

    @FXML
    void chooseFile(ActionEvent event) {
        FileHandler f = new FileHandler();
        f.chooseFile(); //mi serve solo in fase di debug. da eliminare poi
        file = f.getFile();
        loaded = true;
    }

    @FXML
    void load(ActionEvent event) throws IOException {
        ParserInProceedings p = ParserInProceedings.getInstance();
        Utility u = Utility.getInstance();

        if (loaded == true || txtTextArea.getText() != "") {
            if (!(txtTextArea.getText().equalsIgnoreCase("")))
                p.parsering(null,txtTextArea.getText());
            else
                p.parsering(file,"");

            if (u.article.equalsIgnoreCase(p.getType())) {
                loadTypeBib(u.article);
            } else {
                loadTypeBib(u.inProceedings);
            }
        }
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