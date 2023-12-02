package com.example.fastcar.Model;

public class HangXeFilter {
    String tenHangXe;
    int soluong;
    boolean isSelected;

    public HangXeFilter(String tenHangXe, int soluong) {
        this.tenHangXe = tenHangXe;
        this.soluong = soluong;
    }

    public String getTenHangXe() {
        return tenHangXe;
    }

    public void setTenHangXe(String tenHangXe) {
        this.tenHangXe = tenHangXe;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
