package com.tesi.code.Model;

public class Author {
    private String nameAuthor;
    private String surnameAuthor;

    public Author(String surname, String name) {
        this.nameAuthor = name;
        this.surnameAuthor = surname;
        //this.id = id;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getSurnameAuthor() {
        return surnameAuthor;
    }

    public void setSurnameAuthor(String surnameAuthor) {
        this.surnameAuthor = surnameAuthor;
    }
}
