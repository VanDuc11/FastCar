package com.example.fastcar.Model.HangXe;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class HangXe  {
    private  int id;
    private String name;

    public HangXe(int id, String name) {
        this.id = id;
        this.name = name;
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
}
