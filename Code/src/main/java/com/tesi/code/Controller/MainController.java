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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
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
    private TextField txtUrl;


    @FXML
    void export(ActionEvent event) throws IOException {
        loadScene("export");
    }

    private String downloadFile() throws IOException {
        StringBuilder res = new StringBuilder();
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new URL(txtUrl.getText()).openStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while (br.ready()) {
                res.append(br.readLine());
                res.append(System.lineSeparator());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Errore nella lettura dell'url del file", "ERROR", JOptionPane.INFORMATION_MESSAGE);

            return "";
        }
        System.out.println(res.toString());
        return res.toString();
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
        if (loaded || !txtTextArea.getText().equalsIgnoreCase("") || !txtUrl.getText().equalsIgnoreCase("")) {
            if (!txtTextArea.getText().equalsIgnoreCase(""))
                gp.parsering(null, txtTextArea.getText());
            else if (!txtUrl.getText().equalsIgnoreCase("")) {
                String res = downloadFile();
                if (res.equalsIgnoreCase("")) {
                    JOptionPane.showMessageDialog(null, "Errore nella lettura dell'url del file", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else
                    gp.parsering(null, res);
            } else
                gp.parsering(file, "");

            if (gp.getType().equalsIgnoreCase(Utility.inProceedings))
                loadScene(Utility.inProceedings);
            else
                loadScene(Utility.article);
        }
    }


    private void loadScene(String type) throws IOException {
        Stage stage = (Stage) btnLoad.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(type + ".fxml"));

        if (type.equalsIgnoreCase("export")) {
            Pane root = (Pane) fxmlLoader.load();
            Scene scene = new Scene(root, 600, 600);
            stage.setScene(scene);
        } else {
            ScrollPane root = fxmlLoader.load();
            Scene scene;
            if (type.equalsIgnoreCase("inproceedings"))
                scene = new Scene(root, 695, 447);
            else
                scene = new Scene(root, 695, 371);

            stage.setScene(scene);
        }

        stage.setTitle(type.toUpperCase(Locale.ROOT));
        stage.setResizable(false);
        stage.show();
    }
}