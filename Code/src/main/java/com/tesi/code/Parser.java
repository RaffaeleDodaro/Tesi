package com.tesi.code;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private String type = "";
    private String title = "";
    private String short_title = "";
    private String address = "";

    private static Parser instance = null;

    public static Parser getInstance() {
        if (instance == null)
            instance = new Parser();
        return instance;
    }

    public void parsering(File file, String text) {
        String conc = "";
        if (text.equalsIgnoreCase("")) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready())
                    conc += reader.readLine();
                reader.close();
                findAuthor(conc);
                findType(conc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < text.split("\\n").length; i++)
                conc += text.split("\\n")[i];
            findAuthor(conc);
            findType(conc);
        }
    }

    private void findType(String row) {
        Pattern pattern = Pattern.compile("@(\\w+)\\{");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            type = matcher.group(1);
    }

    private void findAuthor(String row) {
        Pattern pattern = Pattern.compile("author\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            title = matcher.group(1).replaceAll("and \\s+", "and "); }


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
