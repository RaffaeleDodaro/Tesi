package com.tesi.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

    public void parsering(File file) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            while (reader.ready())
                //textArea.appendText(reader.readLine()+"\n");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
