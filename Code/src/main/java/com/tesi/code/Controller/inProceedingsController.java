package com.tesi.code.Controller;

import com.tesi.code.*;
import com.tesi.code.Model.Author;
import com.tesi.code.Model.Editor;
import com.tesi.code.Parser.GenericParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private GridPane grdAuthor;

    @FXML
    private Button btnAnotherBibtex;

    @FXML
    private TextField txtEditor;

    @FXML
    private TextField txtAuthor;

    private ArrayList<TextField> allTextAuthor = new ArrayList<TextField>();

    private ArrayList<TextField> allTextEditor = new ArrayList<TextField>();

    @FXML
    private GridPane grdEditor;

    @FXML
    void anotherBibtex(ActionEvent event) throws IOException {
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
    void save(ActionEvent event) {
        Database db = Database.getInstance();
        GenericParser gp = GenericParser.getInstance();

        db.getAuthors().clear();
        db.getEditors().clear();
        for (int i = 0; i < allTextAuthor.size(); i += 2)
            db.getAuthors().add(new Author(allTextAuthor.get(i).getText(), allTextAuthor.get(i + 1).getText()));


        for (int i = 0; i < allTextEditor.size(); i += 2) {
            db.getEditors().add(new Editor(allTextEditor.get(i + 1).getText(), allTextEditor.get(i).getText()));
        }

        db.openConnection(Utility.inProceedings);
        db.createTableInProceedings();
        if (!txtShortTitle.getText().equalsIgnoreCase("") && !txtAddress.getText().equalsIgnoreCase(""))
            db.insertIntoDBInProceedings(gp.getYear(), gp.getPages(), gp.getDblp(), txtTitle.getText(), gp.getVolume(), txtShortTitle.getText(), gp.getUrl(),
                    txtAddress.getText(), gp.getPublisher(), gp.getSeries(), gp.getBooktitle(), gp.getDoi());
        else if (txtShortTitle.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null, "Insert short title", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        else if (txtAddress.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null, "Insert address", "ERROR", JOptionPane.INFORMATION_MESSAGE);

            db.closeConnection();
        btnSave.setVisible(false);
        JOptionPane.showMessageDialog(null, "Saved correctly article", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericParser gp = GenericParser.getInstance();
        Database db = Database.getInstance();
        txtBookTitle.setText(gp.getBooktitle());
        txtTitle.setText(gp.getTitle());
        txtShortTitle.setText(gp.getTitle());
        txtAddress.setText("");
        db.calculateAuthor(gp.getAuthor());
        db.calculateEditor(gp.getEditor());

        grdAuthor.getChildren().clear();
        grdEditor.getChildren().clear();
        for (int i = 0; i < db.getAuthors().size(); i++)
            loadAuthors(i, db);
        for (int i = 0; i < db.getEditors().size(); i++)
            loadEditors(i, db);
    }

    private void loadEditors(int i, Database db) {

        Label labelEditor = new Label();
        labelEditor.setFont(new Font("System", 27));
        labelEditor.setText("Editor");

        Label labelName = new Label();
        labelName.setFont(new Font("System", 27));
        labelName.setText("Name");

        Label labelSurname = new Label();
        labelSurname.setFont(new Font("System", 27));
        labelSurname.setText("Surname");

        TextField name = new TextField(db.getEditors().get(i).getName());
        name.setFont(new Font("System", 12));

        TextField surname = new TextField(db.getEditors().get(i).getSurname());
        surname.setFont(new Font("System", 12));

        Pane paneEditor = new Pane();
        Pane paneLabelName = new Pane();
        Pane paneShowName = new Pane();
        Pane paneLabelSurname = new Pane();
        Pane paneShowSurname = new Pane();

        paneEditor.setPrefWidth(50);
        paneEditor.setPrefHeight(50);

        paneShowName.setPrefWidth(366);
        paneShowName.setPrefHeight(25);

        paneLabelName.setPrefWidth(366);
        paneLabelName.setPrefHeight(25);

        paneLabelSurname.setPrefWidth(366);
        paneLabelSurname.setPrefHeight(25);

        paneEditor.getChildren().add(labelEditor);
        paneShowName.getChildren().add(name);
        paneLabelSurname.getChildren().add(labelSurname);
        paneLabelName.getChildren().add(labelName);
        paneShowSurname.getChildren().add(surname);

        grdEditor.add(paneEditor, 0, i);
        grdEditor.add(paneLabelName, 1, i);
        grdEditor.add(paneShowName, 2, i);
        grdEditor.add(paneLabelSurname, 3, i);
        grdEditor.add(paneShowSurname, 4, i);

        grdEditor.getRowConstraints().add(new RowConstraints(30));

        allTextEditor.add(surname);
        allTextEditor.add(name);
    }


    private void loadAuthors(int i, Database db) {
        Label labelAuthor = new Label();
        labelAuthor.setFont(new Font("System", 27));
        labelAuthor.setText("Author");

        Label labelName = new Label();
        labelName.setFont(new Font("System", 27));
        labelName.setText("Name");

        Label labelSurname = new Label();
        labelSurname.setFont(new Font("System", 27));
        labelSurname.setText("Surname");

        TextField name = new TextField(db.getAuthors().get(i).getName());
        name.setFont(new Font("System", 12));

        TextField surname = new TextField(db.getAuthors().get(i).getSurname());
        surname.setFont(new Font("System", 12));

        Pane paneAuthor = new Pane();
        Pane paneLabelName = new Pane();
        Pane paneShowName = new Pane();
        Pane paneLabelSurname = new Pane();
        Pane paneShowSurname = new Pane();

        paneAuthor.setPrefWidth(50);
        paneAuthor.setPrefHeight(50);

        paneShowName.setPrefWidth(366);
        paneShowName.setPrefHeight(25);

        paneLabelName.setPrefWidth(366);
        paneLabelName.setPrefHeight(25);

        paneLabelSurname.setPrefWidth(366);
        paneLabelSurname.setPrefHeight(25);

        paneAuthor.getChildren().add(labelAuthor);
        paneShowName.getChildren().add(name);
        paneLabelSurname.getChildren().add(labelSurname);
        paneLabelName.getChildren().add(labelName);
        paneShowSurname.getChildren().add(surname);

        grdAuthor.add(paneAuthor, 0, i);
        grdAuthor.add(paneLabelName, 1, i);
        grdAuthor.add(paneShowName, 2, i);
        grdAuthor.add(paneLabelSurname, 3, i);
        grdAuthor.add(paneShowSurname, 4, i);

        grdAuthor.getRowConstraints().add(new RowConstraints(30));

        allTextAuthor.add(surname);
        allTextAuthor.add(name);
    }
}