package com.example.fastcar.Model.MauXe;

public class MauXe {
    private int id;
    private String make_id;
    private String name;

    public MauXe(int id, String make_id, String name) {
        this.id = id;
        this.make_id = make_id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake_id() {
        return make_id;
    }

    public void setMake_id(String make_id) {
        this.make_id = make_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
