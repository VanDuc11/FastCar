package com.example.fastcar.Model;

public class TinhNangXe {
    String tenTinhNang;
    int image;

    public TinhNangXe(String tenTinhNang, int image) {
        this.tenTinhNang = tenTinhNang;
        this.image = image;
    }

    public TinhNangXe() {
    }

    public String getTenTinhNang() {
        return tenTinhNang;
    }

    public void setTenTinhNang(String tenTinhNang) {
        this.tenTinhNang = tenTinhNang;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
