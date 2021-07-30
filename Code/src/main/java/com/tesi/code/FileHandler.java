package com.tesi.code;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.jbibtex.ParseException;

import javax.swing.JFileChooser;
import java.io.*;
import java.nio.Buffer;

import org.jbibtex.BibTeXParser;

public class FileHandler {

    public void chooseFile(TextArea textArea) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        Parser parser = new Parser();
        parser.parsering(file);
    }
}
