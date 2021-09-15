package com.tesi.code;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserInProceedings {
    private String type = "";
    private String booktitle = "";
    private int year = 0;
    private String pages = "";
    private String dblp = "";
    private String title = "";
    private String journal = "";
    private int volume = 0;
    private String short_title = "";
    private String address = "";
    private String url = "";
    private String publisher = "";
    private String series = "";
    private String doi = "";

    private String author="";

    private static ParserInProceedings instance = null;

    public static ParserInProceedings getInstance() {
        if (instance == null)
            instance = new ParserInProceedings();
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
            author = matcher.group(1).replaceAll("and \\s+", "and "); }

    private void findBookTitle(String row) {
        Pattern pattern = Pattern.compile("booktitle\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            booktitle = matcher.group(1);
    }

    private void findYear(String row) {
        Pattern pattern = Pattern.compile("year\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            year = Integer.parseInt(matcher.group(1)); }

    private void findPages(String row) {
        Pattern pattern = Pattern.compile("pages\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            pages = matcher.group(1); }

    private void findDBLP(String row) {
        Pattern pattern = Pattern.compile("DBLP:(.+),");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            dblp = matcher.group(1); }

    private void findTitle(String row) {
        Pattern pattern = Pattern.compile("\\s{1,2}title\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            title = matcher.group(1); }

    private void findJournal(String row) {
        Pattern pattern = Pattern.compile("author\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            title = matcher.group(1).replaceAll("and \\s+", "and "); }

    private void findVolume(String row) {
        Pattern pattern = Pattern.compile("volume\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            volume = Integer.parseInt(matcher.group(1)); }

    private void findAddress(String row) {
        Pattern pattern = Pattern.compile("author\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            title = matcher.group(1).replaceAll("and \\s+", "and "); }

    private void findURL(String row) {
        Pattern pattern = Pattern.compile("\\s{1,2}url\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            url = matcher.group(1); }

    private void findPublisher(String row) {
        Pattern pattern = Pattern.compile("publisher\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            publisher = matcher.group(1); }

    private void findSeries(String row) {
        Pattern pattern = Pattern.compile("series\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            series = matcher.group(1); }

    private void findDoi(String row) {
        Pattern pattern = Pattern.compile("doi\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            doi = matcher.group(1); }

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

    public String getBooktitle() {
        return booktitle;
    }

    public int getYear() {
        return year;
    }

    public String getPages() {
        return pages;
    }

    public String getDblp() {
        return dblp;
    }

    public String getJournal() {
        return journal;
    }

    public int getVolume() {
        return volume;
    }

    public String getUrl() {
        return url;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSeries() {
        return series;
    }

    public String getDoi() {
        return doi;
    }

    public String getAuthor() {
        return author;
    }
}
