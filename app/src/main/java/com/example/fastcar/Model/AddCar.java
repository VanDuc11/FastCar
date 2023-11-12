package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class AddCar implements Serializable {
    String  BKS, HangXe, MauXe, NSX;
    int SoGhe;
    String ChuyenDong, LoaiNhienLieu;
    float TieuHao;
    String MoTa;
    String DiaChiXe;
    int GiaThue1Ngay;

    String id_user;

    public AddCar(String BKS, String hangXe, String mauXe, String NSX, int soGhe, String chuyenDong, String loaiNhienLieu, float tieuHao, String moTa, String diaChiXe, int giaThue1Ngay, String id_user) {
        this.BKS = BKS;
        HangXe = hangXe;
        MauXe = mauXe;
        this.NSX = NSX;
        SoGhe = soGhe;
        ChuyenDong = chuyenDong;
        LoaiNhienLieu = loaiNhienLieu;
        TieuHao = tieuHao;
        MoTa = moTa;
        DiaChiXe = diaChiXe;
        GiaThue1Ngay = giaThue1Ngay;
        this.id_user = id_user;
    }

    public AddCar() {
    }

    public String getBKS() {
        return BKS;
    }

    public void setBKS(String BKS) {
        this.BKS = BKS;
    }

    public String getHangXe() {
        return HangXe;
    }

    public void setHangXe(String hangXe) {
        HangXe = hangXe;
    }

    public String getMauXe() {
        return MauXe;
    }

    public void setMauXe(String mauXe) {
        MauXe = mauXe;
    }

    public String getNSX() {
        return NSX;
    }

    public void setNSX(String NSX) {
        this.NSX = NSX;
    }

    public int getSoGhe() {
        return SoGhe;
    }

    public void setSoGhe(int soGhe) {
        SoGhe = soGhe;
    }

    public String getChuyenDong() {
        return ChuyenDong;
    }

    public void setChuyenDong(String chuyenDong) {
        ChuyenDong = chuyenDong;
    }

    public String getLoaiNhienLieu() {
        return LoaiNhienLieu;
    }

    public void setLoaiNhienLieu(String loaiNhienLieu) {
        LoaiNhienLieu = loaiNhienLieu;
    }

    public float getTieuHao() {
        return TieuHao;
    }

    public void setTieuHao(float tieuHao) {
        TieuHao = tieuHao;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getDiaChiXe() {
        return DiaChiXe;
    }

    public void setDiaChiXe(String diaChiXe) {
        DiaChiXe = diaChiXe;
    }

    public int getGiaThue1Ngay() {
        return GiaThue1Ngay;
    }

    public void setGiaThue1Ngay(int giaThue1Ngay) {
        GiaThue1Ngay = giaThue1Ngay;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
