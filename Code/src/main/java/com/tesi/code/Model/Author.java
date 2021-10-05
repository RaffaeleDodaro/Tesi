package com.tesi.code.Model;

public class Author {
    private String name;
    private String surname;

    public Author(String surname, String name) {
        this.name = name;
        this.surname = surname;
        //this.id = id;
    }

    public String getNameAuthor() {
        return name;
    }

    public void setNameAuthor(String nameAuthor) {
        this.name = nameAuthor;
    }

    public String getSurnameAuthor() {
        return surname;
    }

    public void setSurnameAuthor(String surnameAuthor) {
        this.surname = surnameAuthor;
    }
}
