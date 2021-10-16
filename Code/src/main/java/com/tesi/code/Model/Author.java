package com.tesi.code.Model;

public class Author {
    private String name;
    private String surname;
    //private int id;

    public Author(String surname, String name) {
        this.name = name;
        this.surname = surname;
        //this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}