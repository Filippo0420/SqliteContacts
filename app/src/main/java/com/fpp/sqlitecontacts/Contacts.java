package com.fpp.sqlitecontacts;

public class Contacts {
    private long id;
    private String name;
    private String surname;
    private String phone;
    private String mail;

    public Contacts(long id, String name, String surname, String phone, String mail) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return mail;
    }

    public void setDescription(String mail) {
        this.mail = mail;
    }

    /*
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

     */
}
