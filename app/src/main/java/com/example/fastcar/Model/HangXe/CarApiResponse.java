package com.example.fastcar.Model.HangXe;


import java.util.List;

public class CarApiResponse {
    private Collection collection;
    private List<HangXe> data;

    public CarApiResponse(Collection collection, List<HangXe> data) {
        this.collection = collection;
        this.data = data;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public List<HangXe> getData() {
        return data;
    }

    public void setData(List<HangXe> data) {
        this.data = data;
    }
}