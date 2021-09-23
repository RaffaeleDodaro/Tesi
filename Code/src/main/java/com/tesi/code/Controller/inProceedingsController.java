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
    private GridPane grdpane;

    @FXML
    private Button btnAnotherBibtex;

    @FXML
    private TextField txtEditor;

    @FXML
    private TextField txtAuthor;

    private ArrayList<TextField> allTextAuthor=new ArrayList<TextField>();

    private ArrayList<TextField> allTextEditor=new ArrayList<TextField>();

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
        Database db = Database.getInstance();
        GenericParser gp = GenericParser.getInstance();

        db.getAuthors().clear();
        db.getEditors().clear();
        for(int i=0;i<allTextAuthor.size();i+=2)
        {
            Author a=new Author(allTextAuthor.get(i).getText(),allTextAuthor.get(i+1).getText());
            //a.setSurnameAuthor(allTextAuthor.get(i).getText());
            //a.setNameAuthor(allTextAuthor.get(i+1).getText());
            db.getAuthors().add(a);
        }

        for(int i=0;i<allTextEditor.size();i+=2)
        {
            Editor a=new Editor();
            a.setSurnameEditor(allTextEditor.get(i).getText());
            a.setNameEditor(allTextEditor.get(i+1).getText());
            db.getEditors().add(a);
        }

        db.openConnection(Utility.inProceedings);
        db.createTableInProceedings();
        if(!txtShortTitle.getText().equalsIgnoreCase("") && !txtAddress.getText().equalsIgnoreCase(""))
            db.insertIntoDBInProceedings(gp.getYear(),gp.getPages(),gp.getDblp(),txtTitle.getText(),gp.getVolume(),txtShortTitle.getText(),gp.getUrl(),
                        txtAddress.getText(),gp.getPublisher(),gp.getSeries(),gp.getBooktitle(),gp.getDoi());
        else if(txtShortTitle.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null,"Insert short title", "ERROR", JOptionPane.INFORMATION_MESSAGE);
        else if(txtAddress.getText().equalsIgnoreCase(""))
            JOptionPane.showMessageDialog(null,"Insert address", "ERROR", JOptionPane.INFORMATION_MESSAGE);

        db.filterByTypeInProceedings();
        db.readingAuthorInProceedings();
        db.closeConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericParser gp = GenericParser.getInstance();
        txtBookTitle.setText(gp.getBooktitle());
        txtTitle.setText(gp.getTitle());
        txtShortTitle.setText(gp.getTitle());
        txtAddress.setText("");
        Database db=Database.getInstance();
        db.calculateAuthor(gp.getAuthor());

        for(int i = 0; i<db.getAuthors().size(); i++)
            loadAuthors(i,db);
        for(int i = 0; i<db.getEditors().size(); i++)
            loadEditors(i,db);
    }


    private void loadEditors(int i, Database db)
    {
        Label labelEditor = new Label();
        labelEditor.setFont(new Font("System",27));
        labelEditor.setText("Editor");

        Label labelName = new Label();
        labelName.setFont(new Font("System",27));
        labelName.setText("Name");

        Label labelSurname = new Label();
        labelSurname.setFont(new Font("System",27));
        labelSurname.setText("Surname");

        TextField name = new TextField(db.getEditors().get(i).getNameEditor());
        name.setFont(new Font("System",12));

        TextField surname = new TextField(db.getEditors().get(i).getSurnameEditor());
        surname.setFont(new Font("System",12));

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

        grdpane.add(paneEditor, 0, i);
        grdpane.add(paneLabelName, 1, i);
        grdpane.add(paneShowName, 2, i);
        grdpane.add(paneLabelSurname, 3, i);
        grdpane.add(paneShowSurname, 4, i);

        allTextEditor.add(surname);
        allTextEditor.add(name);
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

        allTextAuthor.add(surname);
        allTextAuthor.add(name);
    }
}