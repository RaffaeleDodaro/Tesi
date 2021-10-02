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
    private GridPane grdpane;

    private ArrayList<TextField> allTextField=new ArrayList<TextField>();

    @FXML
    void anotherBibtex(ActionEvent event)  throws IOException {
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
        Database db = new Database();
        Utility u = new Utility();
        File file=new FileHandler().getFile();
        GenericParser gp=GenericParser.getInstance();

        db.getAuthors().clear();

        for(int i=0;i<allTextField.size();i+=2)
            db.getAuthors().add(new Author(allTextField.get(i).getText(),allTextField.get(i+1).getText()));

        db.openConnection(u.article);
        db.createTableArticle();
        if(!txtShortTitle.getText().equalsIgnoreCase("") && !txtJournal.getText().equalsIgnoreCase(""))
            db.insertIntoDBArticle(gp.getYear(),gp.getPages(),gp.getDblp(),txtTitle.getText(),gp.getVolume(),txtShortTitle.getText(),gp.getUrl()
                ,gp.getDoi(),txtJournal.getText());
        else if(txtShortTitle.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null,"Insert short title", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        else if(txtJournal.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null,"Insert journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);

        db.closeConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericParser gp= GenericParser.getInstance();

        txtJournal.setText(gp.getJournal());
        txtTitle.setText(gp.getTitle());
        txtShortTitle.setText(gp.getTitle());
        Database db=Database.getInstance();
        db.getAuthors().clear();
        db.calculateAuthor(gp.getAuthor());

        for(int i = 0; i<db.getAuthors().size(); i++)
            loadAuthors(i,db);
    }

    private void loadAuthors(int i, Database db)
    {
        Label labelAuthor = new Label();
        labelAuthor.setFont(new Font("System",27));
        labelAuthor.setText("Author");

        Label labelName = new Label();
        labelName.setFont(new Font("System",27));
        labelName.setText("Name");

        Label labelSurname = new Label();
        labelSurname.setFont(new Font("System",27));
        labelSurname.setText("Surname");

        TextField name = new TextField(db.getAuthors().get(i).getNameAuthor());
        name.setFont(new Font("System",12));

        TextField surname = new TextField(db.getAuthors().get(i).getSurnameAuthor());
        surname.setFont(new Font("System",12));

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

        grdpane.add(paneAuthor, 0, i);
        grdpane.add(paneLabelName, 1, i);
        grdpane.add(paneShowName, 2, i);
        grdpane.add(paneLabelSurname, 3, i);
        grdpane.add(paneShowSurname, 4, i);

        allTextField.add(surname);
        allTextField.add(name);
    }
}