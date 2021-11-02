package com.tesi.code.Controller;

import com.tesi.code.*;
import com.tesi.code.Model.Author;
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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private Button btnAnotherBibtex;

    @FXML
    private TextField txtEditor;

    @FXML
    private TextField txtAuthor;

    @FXML
    private GridPane grd;

    private ArrayList<TextField> allTextField = new ArrayList<TextField>();

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
    void save(ActionEvent event) throws ClassNotFoundException {
        Database db = Database.getInstance();
        File file = new FileHandler().getFile();
        GenericParser gp = GenericParser.getInstance();

        db.getAuthors().clear();

        for (int i = 0; i < allTextField.size(); i += 2)
            db.getAuthors().add(new Author(allTextField.get(i).getText(), allTextField.get(i + 1).getText()));

        db.openConnection(Utility.article);
        db.createTableArticle();
        if (!txtShortTitle.getText().equalsIgnoreCase("") && !txtJournal.getText().equalsIgnoreCase("")) {
            if (db.insertIntoDBArticle(gp.getYear(), gp.getPages(), gp.getDblp(), txtTitle.getText(), gp.getVolume(), txtShortTitle.getText(), gp.getUrl()
                    , gp.getDoi(), txtJournal.getText())) {
                db.closeConnection();
                btnSave.setVisible(false);
                JOptionPane.showMessageDialog(null, "Article correctly saved", "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Article exists in database", "Error", JOptionPane.INFORMATION_MESSAGE);
        } else if (txtShortTitle.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null, "Insert short title", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        else if (txtJournal.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null, "Insert journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericParser gp = GenericParser.getInstance();

        txtJournal.setText(gp.getJournal());
        txtTitle.setText(gp.getTitle());
        txtShortTitle.setText(gp.getJournal());
        Database db = Database.getInstance();
        db.getAuthors().clear();
        db.calculateAuthor(gp.getAuthor());

        grd.getChildren().clear();
        for (int i = 0; i < db.getAuthors().size(); i++)
            loadAuthors(i, db);
    }

    private void loadAuthors(int i, Database db) {
        Label labelType = new Label();
        labelType.setFont(new Font("System", 12));
        labelType.setText("Author");

        Label labelName = new Label();
        labelName.setFont(new Font("System", 12));
        labelName.setText("Name");

        Label labelSurname = new Label();
        labelSurname.setFont(new Font("System", 12));
        labelSurname.setText("Surname");

        TextField name = new TextField(db.getAuthors().get(i).getName());
        name.setFont(new Font("System", 12));

        TextField surname = new TextField(db.getAuthors().get(i).getSurname());
        surname.setFont(new Font("System", 12));

        grd.add(labelType, 0, i);
        grd.add(labelName, 1, i);
        grd.add(name, 2, i);
        grd.add(labelSurname, 3, i);
        grd.add(surname, 4, i);
        grd.getRowConstraints().add(new RowConstraints(40));

        allTextField.add(surname);
        allTextField.add(name);
    }
}