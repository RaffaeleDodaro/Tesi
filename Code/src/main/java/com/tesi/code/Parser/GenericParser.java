package com.tesi.code.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericParser {
    private String type = "";
    private String booktitle = "";
    private int year = 0;
    private String pages = "";
    private String dblp = "";
    private String volume = "";
    private String url = "";
    private String publisher = "";
    private String series = "";
    private String doi = "";
    private String allEditor="";
    private String allAuthor="";
    private String title="";
    private String address="";
    private String journal = "";

    private static GenericParser instance = null;

    public static GenericParser getInstance() {
        if (instance == null)
            instance = new GenericParser();
        return instance;
    }
    public void parsering(File file, String text) {
        String conc = "";
        if (text.equalsIgnoreCase("")) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready())
                    //tutto il testo le converto in unica riga in modo tale da applicare le regex
                    conc += reader.readLine();
                reader.close();
                regex(conc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //tutto il testo le converto in unica riga in modo tale da applicare le regex
            for (int i = 0; i < text.split("\\n").length; i++)
                conc += text.split("\\n")[i];
            regex(conc);
        }
    }

    private void regex(String conc) {
        type=find("@(\\w+)\\{",conc);
        dblp=find("DBLP:(.+),\\s+author",conc);
        allAuthor=find("author\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        allEditor=find("editor\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        title=find("title\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        booktitle=find("booktitle\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        series=find("series\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        volume=find("volume\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        pages=find("pages\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        publisher=find("publisher\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        year=Integer.parseInt(find("year\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc));
        url=find("\\s{1,2}url\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        doi=find("doi\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
        journal=find("journal\\s+\\=\\s+\\{([\\s\\S]*?)\\},",conc);
    }
    private String find(String p,String row)
    {
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(row);
        if (matcher.find())
            return matcher.group(1).replaceAll("\\s{2,100}", "and ");
        return "";
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getDblp() {
        return dblp;
    }

    public void setDblp(String dblp) {
        this.dblp = dblp;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getEditor() {
        return allEditor;
    }

    public void setEditor(String allEditor) {
        this.allEditor = allEditor;
    }

    public String getAuthor() {
        return allAuthor;
    }

    public void setAuthor(String allAuthor) {
        this.allAuthor = allAuthor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
