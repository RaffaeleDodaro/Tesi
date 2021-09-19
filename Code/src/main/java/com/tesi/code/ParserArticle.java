package com.tesi.code;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserArticle {
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

    private String author = "";

    private static ParserArticle instance = null;

    public static ParserArticle getInstance() {
        if (instance == null)
            instance = new ParserArticle();
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
                regex(conc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < text.split("\\n").length; i++)
                conc += text.split("\\n")[i];
            regex(conc);
        }
    }
    private void regex(String conc) {
        type=find("@(\\w+)\\{",conc);
        dblp=find("DBLP:(.+),\\s+author",conc);
        title=find("title\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        volume=Integer.parseInt(find("volume\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc));
        pages=find("pages\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        publisher=find("publisher\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        year=Integer.parseInt(find("year\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc));
        url=find("\\s{1,2}url\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        doi=find("doi\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        author=find("author\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        journal=find("journal\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
    }
    private String find(String p, String row)
    {
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            return matcher.group(1);
        return "";
    }

    private void findType(String row) {

        type=find("@(\\w+)\\{",row);
        /*
            Pattern pattern = Pattern.compile("@(\\w+)\\{");
            Matcher matcher = pattern.matcher(row);
            if (matcher.find()) {

                type = matcher.group(1);
                System.out.println("type: " + type);
            }
        */
        System.out.println("type: " + type);
    }

    private void findAuthor(String row) {
        Pattern pattern = Pattern.compile("author\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            author = matcher.group(1).replaceAll("and \\s+", "and ");
    }

    private void findBookTitle(String row) {
        Pattern pattern = Pattern.compile("booktitle\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find()) {
            booktitle = matcher.group(1);
            System.out.println("booktitle: " + booktitle);
        }
    }

    private void findYear(String row) {
        Pattern pattern = Pattern.compile("year\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            year = Integer.parseInt(matcher.group(1));
    }

    private void findPages(String row) {
        Pattern pattern = Pattern.compile("pages\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            pages = matcher.group(1);
    }

    private void findDBLP(String row) {
        Pattern pattern = Pattern.compile("DBLP:(.+),");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            dblp = matcher.group(1);
    }

    private void findTitle(String row) {
        Pattern pattern = Pattern.compile("\\s{1,2}title\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            title = matcher.group(1);
    }

    private void findJournal(String row) {
        Pattern pattern = Pattern.compile("journal\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            title = matcher.group(1).replaceAll("and \\s+", "and ");
    }

    private void findVolume(String row) {
        Pattern pattern = Pattern.compile("volume\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            volume = Integer.parseInt(matcher.group(1));
    }

    private void findURL(String row) {
        Pattern pattern = Pattern.compile("\\s{1,2}url\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            url = matcher.group(1);
    }

    private void findPublisher(String row) {
        Pattern pattern = Pattern.compile("publisher\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            publisher = matcher.group(1);
    }

    private void findSeries(String row) {
        Pattern pattern = Pattern.compile("series\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            series = matcher.group(1);
    }

    private void findDoi(String row) {
        Pattern pattern = Pattern.compile("doi\\s+\\=\\s+\\{([\\s\\S]*?)\\},");
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            //come prova per il momento lo metto all'interno della text box book title
            //sostituisco "and <tanti spazi>" con "and "
            doi = matcher.group(1);
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