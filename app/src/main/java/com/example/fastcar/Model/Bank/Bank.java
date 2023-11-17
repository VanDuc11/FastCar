package com.example.fastcar.Model.Bank;

public class Bank {
    int id;
    String name, shortName, logo;

    public Bank(int id, String name, String shortName, String logo) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.logo = logo;
    }

    public Bank() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
