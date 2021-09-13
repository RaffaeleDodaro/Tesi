package com.tesi.code;

import org.jbibtex.*;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Parser {

    public void parsering(File file) {
        Reader reader;
        try {
            reader = new FileReader(file);
  /*          while (reader.ready())
                //textArea.appendText(reader.readLine()+"\n");
            reader.close();

            BibTeXParser bibTeXParser= new BibTeXParser();
            BibTeXDatabase database = bibTeXParser.parse(reader);
            System.out.println(database);
            Writer writer= new FileWriter("/home/raffaele/cdprova.txt");
            BibTeXParser bibTeXParser= new BibTeXParser();
            BibTeXDatabase database = bibTeXParser.parse(reader);
            org.jbibtex.BibTeXFormatter bibtexFormatter = new org.jbibtex.BibTeXFormatter();

            bibtexFormatter.format(database, writer);

            writer.close();
*/
            BibTeXParser bibTeXParser= new BibTeXParser();
            BibTeXDatabase database = bibTeXParser.parse(reader);

            Map<Key, BibTeXEntry> entryMap = database.getEntries();

            Collection<BibTeXEntry> entries = entryMap.values();
            for(BibTeXEntry entry : entries){
                Value value = entry.getField(BibTeXEntry.KEY_TITLE);
                if(value == null){
                    continue;
                }
                String latexString = value.toUserString();
                LaTeXParser latexParser = new LaTeXParser();

                List<LaTeXObject> latexObjects = latexParser.parse(latexString);

                org.jbibtex.LaTeXPrinter latexPrinter = new LaTeXPrinter();

                String plainTextString = latexPrinter.print(latexObjects);
                //System.out.println(plainTextString);
            }


            //System.out.println(database);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
