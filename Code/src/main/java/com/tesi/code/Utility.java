package com.tesi.code;

public class Utility {
    public final String article = "article";
    public final String inProceedings = "inproceedings";
    public final String inProceedingsAttributes = "YEAR,PAGES,DBLP,TITLE,VOLUME,SHORT_TITLE,URL" +
            ",ADDRESS,PUBLISHER,SERIES,BOOKTITLE,DOI";
    public final String articleAttributes = "YEAR,PAGES,DBLP,TITLE,JOURNAL,VOLUME,SHORT_TITLE,URL,DOI";

    private static Utility instance = null;

    public static Utility getInstance() {
        if (instance == null)
            instance = new Utility();
        return instance;
    }
}
