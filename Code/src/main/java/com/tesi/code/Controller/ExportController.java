package com.tesi.code.Controller;

import com.tesi.code.Database;
import com.tesi.code.Main;
import com.tesi.code.Model.Article;
import com.tesi.code.Model.Author;
import com.tesi.code.Model.Editor;
import com.tesi.code.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import com.tesi.code.Model.inProceedings;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExportController implements Initializable {
    @FXML
    private ChoiceBox<String> cbChooseType;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtAuthorName;

    @FXML
    private TextField txtAuthorSurname;

    @FXML
    private Button btnFilterByAuthor;

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
    private Button btnAnotherBibtex;

    @FXML
    private TableView<inProceedings> tblViewinProceedings;

    @FXML
    private TableView<Article> tblViewArticle;

    @FXML
    private CheckBox cbFilterByYear;

    private final ArrayList<Article> savedArticle = new ArrayList<>();
    private final ArrayList<inProceedings> savedInProceedings = new ArrayList<>();

    @FXML
    void filterByArticle(ActionEvent event) {
        Database db = Database.getInstance();
        if (tblViewinProceedings.getItems().size() == 0 && tblViewArticle.getItems().size() == 0) {

            db.cleanAll();
            cleanTbl();

            String title = txtArticleTitle.getText();
            String journal = txtArticleJournal.getText();
            int year = cbArticleYear.getValue();

            if (cbChooseType.getValue().equalsIgnoreCase(Utility.all)) {
                db.openConnection(Utility.article);
                if (cbFilterByYear.isSelected()) {
                    if (db.findArticleByInfoArticleArticle(title, journal, year))
                        showArticles(db.getFilteredArticlesArticle());

                } else {
                    if (db.findArticleByInfoArticleArticle(title, journal, -1))
                        showArticles(db.getFilteredArticlesArticle());
                }
                db.closeConnection();


                db.openConnection(Utility.inProceedings);
                if (cbFilterByYear.isSelected()) {
                    if (db.findArticleByInfoArticleInProceedings(title, year))
                        showInProceedings(db.getFilteredArticlesInProceedings());
                }

                else {
                    if (db.findArticleByInfoArticleInProceedings(title, -1))
                        showInProceedings(db.getFilteredArticlesInProceedings());
                }
                db.closeConnection();
            }

            else if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
                db.openConnection(Utility.inProceedings);

                if (cbFilterByYear.isSelected())
                {
                    if(!journal.equalsIgnoreCase("") &&
                            title.equalsIgnoreCase("")){
                        JOptionPane.showMessageDialog(null, "I search only for year. inproceedings files have no journals", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                        if (db.findArticleByInfoArticleInProceedings(title, year))
                            showInProceedings(db.getFilteredArticlesInProceedings());
                    }


                    if(!journal.equalsIgnoreCase("") &&
                            !title.equalsIgnoreCase("")){
                        JOptionPane.showMessageDialog(null, "I search for year and for title. inproceedings files have no journals", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                        if (db.findArticleByInfoArticleInProceedings(title, year))
                            showInProceedings(db.getFilteredArticlesInProceedings());
                    }
                }

                else {
                    if(!journal.equalsIgnoreCase("") && title.equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Insert title for search...", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }


                    else if(!journal.equalsIgnoreCase("") && !title.equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "I search for title. inproceedings files have no journals", "ERROR", JOptionPane.INFORMATION_MESSAGE);

                        if (db.findArticleByInfoArticleInProceedings(title, -1))
                            showInProceedings(db.getFilteredArticlesInProceedings());
                    }

                    else if(journal.equalsIgnoreCase("") && !title.equalsIgnoreCase("")) {
                        if (db.findArticleByInfoArticleInProceedings(title, -1))
                            showInProceedings(db.getFilteredArticlesInProceedings());
                    }
                }
                db.closeConnection();
            }

            else if (cbChooseType.getValue().equalsIgnoreCase(Utility.article)) {
                db.openConnection(Utility.article);
                if (cbFilterByYear.isSelected()) {

                    if(!title.equalsIgnoreCase("") && !journal.equalsIgnoreCase("")) {
                        if (db.findArticleByInfoArticleArticle(title, journal, year))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                    else if(title.equalsIgnoreCase("") && journal.equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Insert title and journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }

                    else if(title.equalsIgnoreCase("")) {
                        if (db.findArticleByInfoArticleArticle(title, journal, year))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                    else if(journal.equalsIgnoreCase("")) {
                        if (db.findArticleByInfoArticleArticle(title, journal, year))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                }

                else {
                    if(!title.equalsIgnoreCase("") && !journal.equalsIgnoreCase("")) {
                        if (db.findArticleByInfoArticleArticle(title, journal, -1))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                    else if(title.equalsIgnoreCase("") && journal.equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Insert title and journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }

                    else if(title.equalsIgnoreCase("")) {
                        if (db.findArticleByInfoArticleArticle(title, journal, -1))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                    else if(journal.equalsIgnoreCase("")) {
                        if (db.findArticleByInfoArticleArticle(title, journal, -1))
                            showArticles(db.getFilteredArticlesArticle());
                    }
                }
                db.closeConnection();
            }
        } else {
            if (cbChooseType.getValue().equalsIgnoreCase(Utility.all)) {
                ArrayList<Article> refilteringArticle = new ArrayList<>(tblViewArticle.getItems());
                ArrayList<inProceedings> refilteringInProceedings = new ArrayList<>(tblViewinProceedings.getItems());

                cleanTbl();

                db.openConnection(Utility.inProceedings);
                if (cbFilterByYear.isSelected()) {
                    
                    if(txtArticleTitle.getText().equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Insert title", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }

                    else if(!txtArticleTitle.getText().equalsIgnoreCase("")) {
                        if (db.refilterByArticleInProceedings(refilteringInProceedings, txtArticleTitle.getText(), cbArticleYear.getValue()))
                            showInProceedings(db.getFilteredArticlesInProceedings());
                    }
                } 
                else {
                    if(txtArticleTitle.getText().equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Insert title", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if(!txtArticleTitle.getText().equalsIgnoreCase("")) {
                        if (db.refilterByArticleInProceedings(refilteringInProceedings, txtArticleTitle.getText(), -1))
                            showInProceedings(db.getFilteredArticlesInProceedings());
                    }
                }
                db.closeConnection();

                
                
                db.openConnection(Utility.article);
                if (cbFilterByYear.isSelected()) {

                    if(txtArticleTitle.getText().equalsIgnoreCase("") && txtArticleJournal.getText().equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Insert title and journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }

                    else if(!txtArticleTitle.getText().equalsIgnoreCase("") && txtArticleJournal.getText().equalsIgnoreCase("")) {
                        if (db.refilterByArticleArticle(refilteringArticle, txtArticleTitle.getText(), "", cbArticleYear.getValue()))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                    else if(txtArticleTitle.getText().equalsIgnoreCase("") && !txtArticleJournal.getText().equalsIgnoreCase("")) {
                        if (db.refilterByArticleArticle(refilteringArticle, "", txtArticleJournal.getText(), cbArticleYear.getValue()))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                    else if(!txtArticleTitle.getText().equalsIgnoreCase("") && !txtArticleJournal.getText().equalsIgnoreCase("")) {
                        if (db.refilterByArticleArticle(refilteringArticle, txtArticleTitle.getText(), txtArticleJournal.getText(), cbArticleYear.getValue()))
                            showArticles(db.getFilteredArticlesArticle());
                    }
                }                 
                else {
                    if(txtArticleTitle.getText().equalsIgnoreCase("") && txtArticleJournal.getText().equalsIgnoreCase("")) {
                        JOptionPane.showMessageDialog(null, "Insert title and journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }

                    else if(!txtArticleTitle.getText().equalsIgnoreCase("") && txtArticleJournal.getText().equalsIgnoreCase("")) {
                        if (db.refilterByArticleArticle(refilteringArticle, txtArticleTitle.getText(), "", -1))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                    else if(txtArticleTitle.getText().equalsIgnoreCase("") && !txtArticleJournal.getText().equalsIgnoreCase("")) {
                        if (db.refilterByArticleArticle(refilteringArticle, "", txtArticleJournal.getText(), -1))
                            showArticles(db.getFilteredArticlesArticle());
                    }

                    else if(!txtArticleTitle.getText().equalsIgnoreCase("") && !txtArticleJournal.getText().equalsIgnoreCase("")) {
                        if (db.refilterByArticleArticle(refilteringArticle, txtArticleTitle.getText(), txtArticleJournal.getText(), -1))
                            showArticles(db.getFilteredArticlesArticle());
                    }
                }

                db.closeConnection();
            
            } 
            
            else if (cbChooseType.getValue().equalsIgnoreCase(Utility.article)) {
                
                if (tblViewArticle.getItems().size() > 0) {
                    ArrayList<Article> refilteringArticle = new ArrayList<>(tblViewArticle.getItems());
                    
                    cleanTbl();

                    db.openConnection(Utility.article);
                    
                    if (cbFilterByYear.isSelected()) {
                        if(txtArticleTitle.getText().equalsIgnoreCase("") && txtArticleJournal.getText().equalsIgnoreCase("")) {
                            JOptionPane.showMessageDialog(null, "Insert title and journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                        }

                        else if(!txtArticleTitle.getText().equalsIgnoreCase("") && txtArticleJournal.getText().equalsIgnoreCase("")) {
                            if (db.refilterByArticleArticle(refilteringArticle, txtArticleTitle.getText(), "", cbArticleYear.getValue()))
                                showArticles(db.getFilteredArticlesArticle());
                        }

                        else if(txtArticleTitle.getText().equalsIgnoreCase("") && !txtArticleJournal.getText().equalsIgnoreCase("")) {
                            if (db.refilterByArticleArticle(refilteringArticle, "", txtArticleJournal.getText(), cbArticleYear.getValue()))
                                showArticles(db.getFilteredArticlesArticle());
                        }

                        else if(!txtArticleTitle.getText().equalsIgnoreCase("") && !txtArticleJournal.getText().equalsIgnoreCase("")) {
                            if (db.refilterByArticleArticle(refilteringArticle, txtArticleTitle.getText(), txtArticleJournal.getText(), cbArticleYear.getValue()))
                                showArticles(db.getFilteredArticlesArticle());
                        }
                    } 
                    
                    else {
                        if(txtArticleTitle.getText().equalsIgnoreCase("") && txtArticleJournal.getText().equalsIgnoreCase("")) {
                            JOptionPane.showMessageDialog(null, "Insert title and journal", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                        }

                        else if(!txtArticleTitle.getText().equalsIgnoreCase("") && txtArticleJournal.getText().equalsIgnoreCase("")) {
                            if (db.refilterByArticleArticle(refilteringArticle, txtArticleTitle.getText(), "", -1))
                                showArticles(db.getFilteredArticlesArticle());
                        }

                        else if(txtArticleTitle.getText().equalsIgnoreCase("") && !txtArticleJournal.getText().equalsIgnoreCase("")) {
                            if (db.refilterByArticleArticle(refilteringArticle, "", txtArticleJournal.getText(), -1))
                                showArticles(db.getFilteredArticlesArticle());
                        }

                        else if(!txtArticleTitle.getText().equalsIgnoreCase("") && !txtArticleJournal.getText().equalsIgnoreCase("")) {
                            if (db.refilterByArticleArticle(refilteringArticle, txtArticleTitle.getText(), txtArticleJournal.getText(), -1))
                                showArticles(db.getFilteredArticlesArticle());
                        }
                    }
                }
                db.closeConnection();

            } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
                
                if (tblViewinProceedings.getItems().size() > 0) {

                    ArrayList<inProceedings> refilteringInProceedings = new ArrayList<>(tblViewinProceedings.getItems());
                    
                    db.openConnection(Utility.inProceedings);
                    
                    cleanTbl();
                    
                    if (cbFilterByYear.isSelected()) {
                        if(!txtArticleJournal.getText().equalsIgnoreCase("") && txtArticleTitle.getText().equalsIgnoreCase("")) {
                            JOptionPane.showMessageDialog(null, "I search only for year. inproceedings files have no journals", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                        }

                        else if(!txtArticleJournal.getText().equalsIgnoreCase("") && !txtArticleTitle.getText().equalsIgnoreCase("")) {
                            if (db.refilterByArticleInProceedings(refilteringInProceedings, txtArticleTitle.getText(), cbArticleYear.getValue()))
                                showInProceedings(db.getFilteredArticlesInProceedings());
                        }
                    } 
                    
                    else { //se non ho spuntato l'anno
                        if(txtArticleTitle.getText().equalsIgnoreCase("")) {
                            JOptionPane.showMessageDialog(null, "Insert title or check year", "ERROR", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if(!txtArticleTitle.getText().equalsIgnoreCase("")) {
                            if (db.refilterByArticleInProceedings(refilteringInProceedings, txtArticleTitle.getText(), -1))
                                showInProceedings(db.getFilteredArticlesInProceedings());
                        }
                    }
                    
                    db.closeConnection();
                }
            }
        }
    }

    @FXML
    void filterByAuthor(ActionEvent event) {
        Database db = Database.getInstance();
        if (tblViewinProceedings.getItems().size() == 0 && tblViewArticle.getItems().size() == 0) {
            db.cleanAll();

            if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
                db.openConnection(Utility.inProceedings);
                db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText(), Utility.inProceedings);
                db.closeConnection();
            }

            else if (cbChooseType.getValue().equalsIgnoreCase(Utility.article)) {
                db.openConnection(Utility.article);
                db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText(), Utility.article);
                db.closeConnection();
            }

            else if (cbChooseType.getValue().equalsIgnoreCase(Utility.all)) {

                db.openConnection(Utility.article);
                db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText(), Utility.article);
                db.closeConnection();

                db.openConnection(Utility.inProceedings);
                db.findArticleByAuthorName(txtAuthorSurname.getText(), txtAuthorName.getText(), Utility.inProceedings);
                db.closeConnection();

            }

            cleanTbl();
            showInProceedings(db.getFilteredArticlesInProceedings());
            showArticles(db.getFilteredArticlesArticle());
        }
        else { //se almeno una delle 2 tbl e' piena

            if ((cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings) || cbChooseType.getValue().equalsIgnoreCase(Utility.all))
                    && tblViewinProceedings.getItems().size() > 0) {

                db.openConnection(Utility.inProceedings);
                db.refilterByAuthorInProceedings(new ArrayList<inProceedings>(tblViewinProceedings.getItems()), txtAuthorSurname.getText(), txtAuthorName.getText());
                db.closeConnection();

            }

            if ((cbChooseType.getValue().equalsIgnoreCase(Utility.article) || cbChooseType.getValue().equalsIgnoreCase(Utility.all))
                    && tblViewArticle.getItems().size() > 0) {

                db.openConnection(Utility.article);
                db.refilterByAuthorArticle(new ArrayList<Article>(tblViewArticle.getItems()), txtAuthorSurname.getText(), txtAuthorName.getText());
                db.closeConnection();

            }
            
            cleanTbl();
            showArticles(db.getFilteredArticlesArticle());
            showInProceedings(db.getFilteredArticlesInProceedings());
        }
    }

    @FXML
    void filterByType(ActionEvent event) {
        Database db = Database.getInstance();
        db.cleanAll();
        cleanTbl();

        if (cbChooseType.getValue().equalsIgnoreCase(Utility.inProceedings)) {
            db.openConnection(Utility.inProceedings);
            db.filterByTypeInProceedings();
            showInProceedings(db.getFilteredArticlesInProceedings());
        } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.article)) {
            db.openConnection(Utility.article);
            db.filterByTypeArticle();
            showArticles(db.getFilteredArticlesArticle());
        } else if (cbChooseType.getValue().equalsIgnoreCase(Utility.all)) {
            db.openConnection(Utility.inProceedings);
            db.filterByTypeInProceedings();
            showInProceedings(db.getFilteredArticlesInProceedings());
            db.closeConnection();

            db.openConnection(Utility.article);
            db.filterByTypeArticle();
            showArticles(db.getFilteredArticlesArticle());
        }
        db.closeConnection();



        /*prove per refiltering --> non vedo l'utilità di fare il refiltering qui
        if (db.getFilteredArticlesInProceedings().size() > 0 || db.getFilteredArticlesArticle().size() > 0) {
            ArrayList<Article> refiltering = new ArrayList<>();
            for (int i = 0; i < tblViewArticle.getItems().size(); i++)
                if (tblViewArticle.getItems().get(i).isCheck().getValue())
                    refiltering.add(tblViewArticle.getItems().get(i));

            for (int i = 0; i < tblViewinProceedings.getItems().size(); i++)
                if (tblViewinProceedings.getItems().get(i).isCheck().getValue())
                    refiltering.add(tblViewinProceedings.getItems().get(i));

            db.openConnection(Utility.inProceedings);
            db.refilterByArticle(refiltering, txtArticleTitle.getText(), txtArticleJournal.getText(), cbArticleYear.getValue(), Utility.inProceedings);
            db.closeConnection();

            db.openConnection(Utility.article);
            db.refilterByArticle(refiltering, txtArticleTitle.getText(), txtArticleJournal.getText(), cbArticleYear.getValue(), Utility.article);
            db.closeConnection();
        }*/

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbChooseType.setValue(Utility.all);
        cbChooseType.getItems().add(Utility.all);
        cbChooseType.getItems().add(Utility.inProceedings);
        cbChooseType.getItems().add(Utility.article);

        for (int i = 2010; i < 2031; i++)
            cbArticleYear.getItems().add(i);

        cbArticleYear.setValue(2010);
        tblViewArticle.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblViewinProceedings.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    void saveSelectedArticle(ActionEvent event) throws IOException {
        if (tblViewArticle.getItems().size() > 0 || tblViewinProceedings.getItems().size() > 0) {
            FileChooser fileChooser = new FileChooser();

            // Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                BufferedWriter bOut = new BufferedWriter(new FileWriter(file));

                bOut.append("-------ARTICLE-------\n");
                for (int i = 0; i < tblViewArticle.getItems().size(); i++) {
                    if (tblViewArticle.getItems().get(i).isCheck().getValue()) {
                        if (!existsArticle(tblViewArticle.getItems().get(i))) {
                            savedArticle.add(tblViewArticle.getItems().get(i));
                        }
                    }
                }

                saveArticle(bOut);
                bOut.append("\n\n-------INPROCEEDINGS-------\n");
                for (int i = 0; i < tblViewinProceedings.getItems().size(); i++)
                    if (tblViewinProceedings.getItems().get(i).isCheck().getValue())
                        if (!existsInProceedings(tblViewinProceedings.getItems().get(i), savedInProceedings))
                            savedInProceedings.add((inProceedings) tblViewinProceedings.getItems().get(i));
                saveInProceedings(bOut);
                bOut.close();
            }
        }
    }

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

    private void cleanTbl() {
        tblViewinProceedings.getItems().clear();
        tblViewinProceedings.getColumns().clear();
        tblViewinProceedings.refresh();

        tblViewArticle.getItems().clear();
        tblViewArticle.getColumns().clear();
        tblViewArticle.refresh();
    }

    private boolean existsArticle(Article a) {
        for (Article article : savedArticle)
            if (article.getDblp().equals(a.getDblp()))
                return true;
        return false;
    }

    private boolean existsInProceedings(Article a, ArrayList<inProceedings> savedArticle) {
        for (inProceedings inProceedings : savedArticle)
            if (inProceedings.getDblp().equals(a.getDblp()))
                return true;
        return false;
    }

    private void showInProceedings(ArrayList<inProceedings> filteredInProceedings) {
        /*tblViewinProceedings.getItems().clear();
        tblViewinProceedings.getColumns().clear();
        tblViewinProceedings.refresh();*/

        /*tblViewArticle.getItems().clear();
        tblViewArticle.getColumns().clear();
        tblViewArticle.refresh();*/

        ObservableList<inProceedings> fxlist = FXCollections.observableList(filteredInProceedings);

        TableColumn<inProceedings, Boolean> column20 = new TableColumn<>("");
        column20.setCellValueFactory(features -> features.getValue().checkProperty());
        column20.setCellFactory(CheckBoxTableCell.forTableColumn(column20));

        TableColumn<inProceedings, Integer> column1 = new TableColumn<>(Utility.year);
        column1.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<inProceedings, String> column2 = new TableColumn<>(Utility.dblp);
        column2.setCellValueFactory(new PropertyValueFactory<>("dblp"));

        TableColumn<inProceedings, String> column3 = new TableColumn<>(Utility.pages);
        column3.setCellValueFactory(new PropertyValueFactory<>("pages"));

        TableColumn<inProceedings, String> column4 = new TableColumn<>(Utility.publisher);
        column4.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<inProceedings, String> column5 = new TableColumn<>(Utility.series);
        column5.setCellValueFactory(new PropertyValueFactory<>("series"));

        TableColumn<inProceedings, String> column6 = new TableColumn<>(Utility.volume);
        column6.setCellValueFactory(new PropertyValueFactory<>("volume"));

        TableColumn<inProceedings, String> column7 = new TableColumn<>(Utility.shortTitle);
        column7.setCellValueFactory(new PropertyValueFactory<>("shortTitle"));

        TableColumn<inProceedings, String> column8 = new TableColumn<>(Utility.url);
        column8.setCellValueFactory(new PropertyValueFactory<>("url"));

        TableColumn<inProceedings, String> column9 = new TableColumn<>(Utility.doi);
        column9.setCellValueFactory(new PropertyValueFactory<>("doi"));

        TableColumn<inProceedings, String> column10 = new TableColumn<>(Utility.address);
        column10.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<inProceedings, String> column11 = new TableColumn<>(Utility.booktitle);
        column11.setCellValueFactory(new PropertyValueFactory<>("booktitle"));

        TableColumn<inProceedings, String> column12 = new TableColumn<>(Utility.title);
        column12.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<inProceedings, String> column13 = new TableColumn<>("Author: " + Utility.name + " " + Utility.surname);
        column13.setCellValueFactory(new PropertyValueFactory<>("allAuthorNameAndSurname"));

        TableColumn<inProceedings, String> column14 = new TableColumn<>("Editor: " + Utility.name + " " + Utility.surname);
        column14.setCellValueFactory(new PropertyValueFactory<>("allEditorNameAndSurname"));

        tblViewinProceedings.getColumns().add(column20);
        tblViewinProceedings.getColumns().add(column1);
        tblViewinProceedings.getColumns().add(column2);
        tblViewinProceedings.getColumns().add(column3);
        tblViewinProceedings.getColumns().add(column4);
        tblViewinProceedings.getColumns().add(column5);
        tblViewinProceedings.getColumns().add(column6);
        tblViewinProceedings.getColumns().add(column7);
        tblViewinProceedings.getColumns().add(column8);
        tblViewinProceedings.getColumns().add(column9);
        tblViewinProceedings.getColumns().add(column10);
        tblViewinProceedings.getColumns().add(column11);
        tblViewinProceedings.getColumns().add(column12);
        tblViewinProceedings.getColumns().add(column13);
        tblViewinProceedings.getColumns().add(column14);

        for (inProceedings a : fxlist) {
            tblViewinProceedings.getItems().add(a);
            //System.out.println("PUBLISHER RIGA 367 ");
        }
    }

    private void showArticles(ArrayList<Article> filteredArticle) {
        /*tblViewArticle.getItems().clear();
        tblViewArticle.getColumns().clear();
        tblViewArticle.refresh();*/

        /*tblViewinProceedings.getItems().clear();
        tblViewinProceedings.getColumns().clear();
        tblViewinProceedings.refresh();*/

        ObservableList<Article> fxlist = FXCollections.observableList(filteredArticle);

        TableColumn<Article, Boolean> column20 = new TableColumn<>("");
        column20.setCellValueFactory(features -> features.getValue().checkProperty());
        column20.setCellFactory(CheckBoxTableCell.forTableColumn(column20));

        TableColumn<Article, Integer> column1 = new TableColumn<>(Utility.year);
        column1.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Article, String> column2 = new TableColumn<>(Utility.dblp);
        column2.setCellValueFactory(new PropertyValueFactory<>("dblp"));

        TableColumn<Article, String> column3 = new TableColumn<>(Utility.pages);
        column3.setCellValueFactory(new PropertyValueFactory<>("pages"));

        TableColumn<Article, String> column4 = new TableColumn<>(Utility.journal);
        column4.setCellValueFactory(new PropertyValueFactory<>("journal"));

        TableColumn<Article, String> column5 = new TableColumn<>(Utility.title);
        column5.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Article, String> column6 = new TableColumn<>(Utility.volume);
        column6.setCellValueFactory(new PropertyValueFactory<>("volume"));

        TableColumn<Article, String> column7 = new TableColumn<>(Utility.shortTitle);
        column7.setCellValueFactory(new PropertyValueFactory<>("shortTitle"));

        TableColumn<Article, String> column8 = new TableColumn<>(Utility.url);
        column8.setCellValueFactory(new PropertyValueFactory<>("url"));

        TableColumn<Article, String> column9 = new TableColumn<>(Utility.doi);
        column9.setCellValueFactory(new PropertyValueFactory<>("doi"));

        TableColumn<Article, String> column10 = new TableColumn<>("Author: " + Utility.name + " " + Utility.surname);
        column10.setCellValueFactory(new PropertyValueFactory<>("allAuthorNameAndSurname"));

        tblViewArticle.getColumns().add(column20);
        tblViewArticle.getColumns().add(column1);
        tblViewArticle.getColumns().add(column2);
        tblViewArticle.getColumns().add(column3);
        tblViewArticle.getColumns().add(column4);
        tblViewArticle.getColumns().add(column5);
        tblViewArticle.getColumns().add(column6);
        tblViewArticle.getColumns().add(column7);
        tblViewArticle.getColumns().add(column8);
        tblViewArticle.getColumns().add(column9);
        tblViewArticle.getColumns().add(column10);

        for (Article a : fxlist)
            tblViewArticle.getItems().add(a);
    }

    private void saveArticle(BufferedWriter bOut) throws IOException {
        for (Article article : savedArticle) {
            int year = article.getYear();
            String pages = article.getPages();
            String dblp = article.getDblp();
            String title = article.getTitle();
            int volume = article.getVolume();
            String shortTitle = article.getShortTitle();
            String url = article.getUrl();
            String doi = article.getDoi();
            String journal = article.getJournal();
            String type = article.getType();
            ArrayList<Author> allAuthors = article.getAllAuthors();
            System.out.println("size exportcontroller savearticle: " + allAuthors.size());
            bOut.append("Year: " + year + "\nPages: " + pages + "\nDblp: " + dblp + "\nTitle: " + title + "\nVolume: "
                    + volume + "\nShort title: " + shortTitle + "\nUrl: " + url + "\nDoi: " + doi + "\nJournal: "
                    + journal + "\nType: " + type);
            for (int j = 0; j < allAuthors.size(); j++) {
                bOut.append("\nAutore " + j + ": " + allAuthors.get(j).getName() + " "
                        + allAuthors.get(j).getSurname());
                System.out.println(("\nAutore " + j + ": " + allAuthors.get(j).getName() + " "
                        + allAuthors.get(j).getSurname()));
            }
            bOut.append("\n*************************\n");
        }
    }

    private void saveInProceedings(BufferedWriter printWriter) throws IOException {
        for (inProceedings savedInProceeding : savedInProceedings) {
            if (savedInProceeding instanceof inProceedings) {
                int year = savedInProceeding.getYear();
                String pages = savedInProceeding.getPages();
                String dblp = savedInProceeding.getDblp();
                String title = savedInProceeding.getTitle();
                int volume = savedInProceeding.getVolume();
                String shortTitle = savedInProceeding.getShortTitle();
                String url = savedInProceeding.getUrl();
                String doi = savedInProceeding.getDoi();
                String type = savedInProceeding.getType();
                String publisher = ((inProceedings) savedInProceeding).getPublisher();
                String series = ((inProceedings) savedInProceeding).getSeries();
                String address = ((inProceedings) savedInProceeding).getAddress();
                String booktitle = ((inProceedings) savedInProceeding).getBooktitle();
                ArrayList<Editor> allEditor = ((inProceedings) savedInProceeding).getAllEditors();
                // BooleanProperty check = tblViewinProceedings.getItems().get(i).isCheck();
                ArrayList<Author> allAuthors = savedInProceeding.getAllAuthors();
                printWriter.append("Year: " + year + "\nPages: " + pages + "\nDblp: " + dblp + "\nTitle: " + title
                        + "\nVolume: " + volume + "\nShort title: " + shortTitle + "\nUrl: " + url + "\nDoi: " + doi
                        + "\nPublisher: " + publisher + "\nType: " + type + "\nSeries: " + series + "\nAddress: "
                        + address + "\nbook title: " + booktitle);
                for (int j = 0; j < allAuthors.size(); j++) {
                    printWriter.append("\nAutore " + j + ": " + allAuthors.get(j).getName() + " "
                            + allAuthors.get(j).getSurname());
                }
                for (int j = 0; j < allEditor.size(); j++) {
                    printWriter.append("\nEditore " + j + ": " + allEditor.get(j).getName() + " "
                            + allEditor.get(j).getSurname());
                }
                printWriter.append("\n*************************\n");
            }
        }
    }
}