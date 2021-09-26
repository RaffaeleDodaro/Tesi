package com.tesi.code.Model;

public class Editor {
    private int id;
    private String nameEditor;
    private String surnameEditor;

    public Editor(String name, String surname) {
        this.nameEditor=name;
        this.surnameEditor=surname;
    }

    public String getNameEditor() {
        return nameEditor;
    }

    public void setNameEditor(String nameEditor) {
        this.nameEditor = nameEditor;
    }

    public String getSurnameEditor() {
        return surnameEditor;
    }

    public void setSurnameEditor(String surnameEditor) {
        this.surnameEditor = surnameEditor;
    }
}
