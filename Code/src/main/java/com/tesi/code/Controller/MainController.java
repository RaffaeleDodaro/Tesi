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
    private Button Export;

    @FXML
    void export(ActionEvent event) throws IOException {
        loadTypeBib("export");
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
        if (loaded == true || txtTextArea.getText() != "") {
            if (!(txtTextArea.getText().equalsIgnoreCase(""))) {
                gp.parsering(null, txtTextArea.getText());
                if (gp.getType().equalsIgnoreCase(Utility.inProceedings))
                    loadTypeBib(Utility.inProceedings);
                else
                    loadTypeBib(Utility.article);
            } else {
                gp.parsering(file, "");
                System.out.println(gp.getAuthor());
                if (gp.getType().equalsIgnoreCase(Utility.inProceedings))
                    loadTypeBib(Utility.inProceedings);
                else
                    loadTypeBib(Utility.article);
            }
            System.out.println("u.article: " + Utility.article + " p.getType: " + gp.getType());
        }
    }

    private void loadTypeBib(String type) throws IOException {
        Stage stage = (Stage) btnLoad.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(type + ".fxml"));
        Pane root = (Pane) fxmlLoader.load();
        Scene scene = new Scene(root, 600, 800);
        stage.setTitle(type);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}