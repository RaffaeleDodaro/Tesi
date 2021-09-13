package com.tesi.code;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.jbibtex.ParseException;

import javax.swing.JFileChooser;
import java.io.*;
import java.nio.Buffer;

import org.jbibtex.BibTeXParser;

public class FileHandler {
    File file=null;

    public File getFile(){
        System.out.println(file.getName());
        return file;
    }

    public void chooseFile(TextArea textArea) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        file = chooser.getSelectedFile();
        System.out.println(file.getName());
        //Parser parser = new Parser();
        //parser.parsering(file);
    }
}
