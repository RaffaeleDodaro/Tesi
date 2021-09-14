package com.tesi.code;

import org.jbibtex.*;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Parser {

    public void parsering(File file) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready())
            {
                //textArea.appendText(reader.readLine()+"\n");
                findAuthor(reader.readLine());
            }
            reader.close();


            }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void findAuthor(String row)
    {
        Pattern pattern = Pattern.compile("author\\s+=\\s+\\{[\\s\\S]*?\\},");
        System.out.println(row);
        System.out.println("Matches: " + pattern.matches("author\\s+=\\s+\\{[\\s\\S]*?\\},", row)); // true
    }
}
