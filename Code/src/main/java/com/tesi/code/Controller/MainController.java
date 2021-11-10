package com.tesi.code.Controller;

import com.tesi.code.FileHandler;
import com.tesi.code.Main;
import com.tesi.code.Parser.GenericParser;
import com.tesi.code.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

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
    private Button Export;

    @FXML
    void export(ActionEvent event) throws IOException {
        load("export");
    }

    @FXML
    void chooseFile(ActionEvent event) {
        FileHandler f = new FileHandler();
        f.chooseFile();
        file = f.getFile();
        loaded = true;
    }

    @FXML
    void load(ActionEvent event) throws IOException {
        GenericParser gp = GenericParser.getInstance();
        if (loaded || !txtTextArea.getText().equalsIgnoreCase("")) {
            if (!txtTextArea.getText().equalsIgnoreCase(""))
                gp.parsering(null, txtTextArea.getText());
            else
                gp.parsering(file, "");

            if (gp.getType().equalsIgnoreCase(Utility.inProceedings))
                //loadTypeBib(Utility.inProceedings);
                load(Utility.inProceedings);
            else
                load(Utility.article);
        }
    }


    private void load(String type) throws IOException {
        Stage stage = (Stage) btnLoad.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(type + ".fxml"));

        if (!type.equalsIgnoreCase("export")) {
            if (!type.equalsIgnoreCase("inproceedings")) {
                ScrollPane root = fxmlLoader.load();
                Scene scene = new Scene(root, 689, 357);
                stage.setScene(scene);
            } else {
                ScrollPane root = fxmlLoader.load();
                Scene scene = new Scene(root, 647, 447);
                stage.setScene(scene);
            }
        } else {
            Pane root = (Pane) fxmlLoader.load();
            Scene scene = new Scene(root, 600, 600);
            stage.setScene(scene);
        }

        stage.setTitle(type.toUpperCase(Locale.ROOT));
        stage.setResizable(false);
        stage.show();
    }
}