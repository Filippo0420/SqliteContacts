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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
