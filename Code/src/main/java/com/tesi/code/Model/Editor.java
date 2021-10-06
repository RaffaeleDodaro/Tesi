package com.tesi.code.Model;

public class Editor {
    //private int id;
    private String name;
    private String surname;

    public Editor(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
