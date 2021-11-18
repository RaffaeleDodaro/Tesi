package com.tesi.code.Controller;

import com.tesi.code.*;
import com.tesi.code.Model.Author;
import com.tesi.code.Model.Editor;
import com.tesi.code.Parser.GenericParser;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class InProceedingsController implements Initializable {

    @FXML
    private TextField txtBookTitle;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtShortTitle;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEditor;

    @FXML
    private TextField txtAuthor;

    @FXML
    private GridPane grd;

    private ArrayList<TextField> allTextAuthor = new ArrayList<TextField>();
    private ArrayList<TextField> allTextEditor = new ArrayList<TextField>();
    private final Button btnSave = new Button("Save");
    private final Button btnAddAnotherBibtex = new Button("Add Another BibTeX");

    void anotherBibtex() throws IOException {
        Stage stage = (Stage) btnAddAnotherBibtex.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Utility.chooseFile + ".fxml"));
        Pane root = (Pane) fxmlLoader.load();
        Scene scene = new Scene(root, 600, 442);
        stage.setTitle("Tesi".toUpperCase(Locale.ROOT));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    void save() {
        Database db = Database.getInstance();
        GenericParser gp = GenericParser.getInstance();

        db.getAuthors().clear();
        db.getEditors().clear();

        for (int i = 0; i < allTextAuthor.size(); i += 2)
            db.getAuthors().add(new Author(allTextAuthor.get(i).getText(), allTextAuthor.get(i + 1).getText()));

        for (int i = 0; i < allTextEditor.size(); i += 2)
            db.getEditors().add(new Editor(allTextEditor.get(i + 1).getText(), allTextEditor.get(i).getText()));

        db.openConnection(Utility.inProceedings);
        db.createTableInProceedings();

        if (!txtShortTitle.getText().equalsIgnoreCase("") && !txtAddress.getText().equalsIgnoreCase("")) {
            if (db.insertIntoDBInProceedings(gp.getYear(), gp.getPages(), gp.getDblp(), txtTitle.getText(), gp.getVolume(), txtShortTitle.getText(), gp.getUrl(),
                    txtAddress.getText(), gp.getPublisher(), gp.getSeries(), gp.getBooktitle(), gp.getDoi())) {
                btnSave.setVisible(false);
                JOptionPane.showMessageDialog(null, "Article correctly saved", "SAVED", JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Article exists in database", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        }

        else if (txtShortTitle.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null, "Insert short title", "ERROR", JOptionPane.INFORMATION_MESSAGE);

        else if (txtAddress.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null, "Insert address", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        db.closeConnection();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericParser gp = GenericParser.getInstance();

        txtBookTitle.setText(gp.getBooktitle());
        txtTitle.setText(gp.getTitle());
        txtShortTitle.setText(gp.getBooktitle());
        txtAddress.setText(gp.getBooktitle());
        Database db = Database.getInstance();
        db.getAuthors().clear();
        db.getEditors().clear();

        db.calculateAuthor(gp.getAuthor());
        db.calculateEditor(gp.getEditor());

        grd.getChildren().clear();

        for (int i = 0; i < db.getAuthors().size(); i++)
            load(i, db, "Author", 0);
        for (int i = 0; i < db.getEditors().size(); i++)
            load(i, db, "Editor", db.getAuthors().size() + i);

        btnSave.setFont(new Font("System", 19));
        btnAddAnotherBibtex.setFont(new Font("System", 19));

        grd.add(btnSave, 2, db.getAuthors().size() + db.getEditors().size());
        grd.add(btnAddAnotherBibtex, 3, db.getAuthors().size() + db.getEditors().size());

        btnSave.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                save();
            }

        });
        btnAddAnotherBibtex.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    anotherBibtex();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void load(int i, Database db, String type, int sum) {
        Label labelType = new Label();
        labelType.setFont(new Font("MS Outlook", 22));
        labelType.setText(type + ":");

        Label labelName = new Label();
        labelName.setFont(new Font("MS Outlook", 22));
        labelName.setText(Utility.name);

        Label labelSurname = new Label();
        labelSurname.setFont(new Font("MS Outlook", 22));
        labelSurname.setText("    " + Utility.surname);

        TextField name;
        TextField surname;
        if (type.equalsIgnoreCase(Utility.author)) {
            name = new TextField(db.getAuthors().get(i).getName());
            surname = new TextField(db.getAuthors().get(i).getSurname());
        } else {
            name = new TextField(db.getEditors().get(i).getName());
            surname = new TextField(db.getEditors().get(i).getSurname());
        }
        name.setFont(new Font("MS Outlook", 18));
        surname.setFont(new Font("MS Outlook", 18));
        if (type.equalsIgnoreCase(Utility.author)) {
            grd.add(labelType, 0, i);
            grd.add(labelName, 1, i);
            grd.add(name, 2, i);
            grd.add(labelSurname, 3, i);
            grd.add(surname, 4, i);
        } else {
            grd.add(labelType, 0, sum);
            grd.add(labelName, 1, sum);
            grd.add(name, 2, sum);
            grd.add(labelSurname, 3, sum);
            grd.add(surname, 4, sum);

        }
        grd.getRowConstraints().add(new RowConstraints(40));
        if (type.equalsIgnoreCase(Utility.author)) {
            allTextAuthor.add(surname);
            allTextAuthor.add(name);
        } else {
            allTextEditor.add(surname);
            allTextEditor.add(name);
        }
    }
}