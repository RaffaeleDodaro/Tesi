package com.tesi.code;

import org.jbibtex.*;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private String type = "";
    private String title = "";
    private String short_title = "";
    private String address = "";

    private static Parser instance = null;

    public static Parser getInstance() {
        if (instance == null) {
            instance = new Parser();
        }
        return instance;
    }

    public void parsering(File file) {
        String conc = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready())
                conc += reader.readLine();
            reader.close();
            System.out.println(conc);
            findAuthor(conc);
            findType(conc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findType(String row) {
        Pattern pattern = Pattern.compile("@(\\w+)\\{");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find()) {
            type = matcher.group(1);
            System.out.println("type: " + type);
        }
    }

    public void findAuthor(String row) {
        Pattern pattern = Pattern.compile("author\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
            //come prova per il momento lo metto all'interno della text box book title
            title = matcher.group(1).replaceAll("and \\s+", "and "); //sostituisco "and <tanti spazi>" con "and "
            System.out.println("title2: " + title);
        }
    }


    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getShort_title() {
        return short_title;
    }

    public String getAddress() {
        return address;
    }
}
