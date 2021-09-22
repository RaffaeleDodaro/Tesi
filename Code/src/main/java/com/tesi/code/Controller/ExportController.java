package com.tesi.code.Controller;

import com.tesi.code.Database;
import com.tesi.code.Parser.GenericParser;
import com.tesi.code.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ExportController implements Initializable {
    @FXML
    private ChoiceBox<String> cbChooseType;

    @FXML
    private Button btnExport;

    @FXML
    private TextField txtAuthorName;

    @FXML
    private TextField txtAuthorSurname;

    @FXML
    private Button btnFilterByAuthor;

    @FXML
    private TextField txtArticleName;

    @FXML
    private TextField txtArticleTitle;

    @FXML
    private Button btnFilterByArticle;

    @FXML
    private TextField txtArticleJournal;

    @FXML
    private ChoiceBox<Integer> cbArticleYear;

    @FXML
    private Button btnFilterByType;

    @FXML
    void filterByArticle(ActionEvent event) {

    }

    @FXML
    void filterByAuthor(ActionEvent event) {
        Database db=Database.getInstance();

        db.openConnection(Utility.inProceedings);
        db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText());
        db.closeConnection();

        db.openConnection(Utility.article);
        db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText());
        db.closeConnection();
    }

    @FXML
    void filterByType(ActionEvent event) {
        Database db=Database.getInstance();
        if(cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)){
            db.openConnection(Utility.inProceedings);
            db.readingArticleInProceedings();
        }
        else{
            db.openConnection(Utility.article);
            db.readingArticleArticle();
        }
        db.closeConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbChooseType.setValue(Utility.inProceedings);
        cbChooseType.getItems().add(Utility.inProceedings);
        cbChooseType.getItems().add(Utility.article);

        for (int i = 2010; i < 2022; i++)
            cbArticleYear.getItems().add(i);

        cbArticleYear.setValue(2010);
    }

}
