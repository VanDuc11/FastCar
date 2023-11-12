package com.example.fastcar.Model.MauXe;

import java.util.List;

public class CarModelApiResponse {
    private Collection collection;
    private List<MauXe> data;

    public CarModelApiResponse(Collection collection, List<MauXe> data) {
        this.collection = collection;
        this.data = data;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public List<MauXe> getData() {
        return data;
    }

    public void setData(List<MauXe> data) {
        this.data = data;
    }
}
