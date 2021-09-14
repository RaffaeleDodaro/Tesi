package com.tesi.code;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.jbibtex.ParseException;

import javax.swing.JFileChooser;
import java.awt.*;
import java.io.*;
import java.nio.Buffer;

import org.jbibtex.BibTeXParser;

public class FileHandler extends Component {
    File file=null;

    public File getFile(){
        System.out.println(file.getName());
        return file;
    }

    public void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(FileHandler.this);
        file = chooser.getSelectedFile();
        System.out.println(file.getName());
        //Parser parser = new Parser();
        //parser.parsering(file);
    }
}
