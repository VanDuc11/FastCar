package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Car implements Parcelable {
    String _id, BKS, HangXe, MauXe, NSX;
    int SoGhe;
    String ChuyenDong, LoaiNhienLieu;
    int TieuHao;
    String MoTa;
    ArrayList<String> HinhAnh;
    int TrangThai, SoChuyen;
    ArrayList<FeedBack> FeedBack;

    public Car(String _id, String BKS, String hangXe, String mauXe, String NSX, int soGhe, String chuyenDong, String loaiNhienLieu, int tieuHao, String moTa, ArrayList<String> hinhAnh, int trangThai, int soChuyen, ArrayList<FeedBack> feedBack) {
        this._id = _id;
        this.BKS = BKS;
        this.HangXe = hangXe;
        this.MauXe = mauXe;
        this.NSX = NSX;
        this.SoGhe = soGhe;
        this.ChuyenDong = chuyenDong;
        this.LoaiNhienLieu = loaiNhienLieu;
        this.TieuHao = tieuHao;
        this.MoTa = moTa;
        this.HinhAnh = hinhAnh;
        this.TrangThai = trangThai;
        this.SoChuyen = soChuyen;
        this.FeedBack = feedBack;
    }


    public Car() {
    }

    protected Car(Parcel in) {
        _id = in.readString();
        BKS = in.readString();
        HangXe = in.readString();
        MauXe = in.readString();
        NSX = in.readString();
        SoGhe = in.readInt();
        ChuyenDong = in.readString();
        LoaiNhienLieu = in.readString();
        TieuHao = in.readInt();
        MoTa = in.readString();
        HinhAnh = in.createStringArrayList();
        TrangThai = in.readInt();
        SoChuyen = in.readInt();
        FeedBack = in.createTypedArrayList(com.example.fastcar.Model.FeedBack.CREATOR);
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

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

    public int getTieuHao() {
        return TieuHao;
    }

    public void setTieuHao(int tieuHao) {
        TieuHao = tieuHao;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public ArrayList<String> getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(ArrayList<String> hinhAnh) {
        HinhAnh = hinhAnh;
    }


    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    public int getSoChuyen() {
        return SoChuyen;
    }

    public void setSoChuyen(int soChuyen) {
        SoChuyen = soChuyen;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<FeedBack> getFeedBack() {
        return FeedBack;
    }

    public void setFeedBack(ArrayList<FeedBack> feedBack) {
        FeedBack = feedBack;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(BKS);
        parcel.writeString(HangXe);
        parcel.writeString(MauXe);
        parcel.writeString(NSX);
        parcel.writeInt(SoGhe);
        parcel.writeString(ChuyenDong);
        parcel.writeString(LoaiNhienLieu);
        parcel.writeInt(TieuHao);
        parcel.writeString(MoTa);
        parcel.writeStringList(HinhAnh);
        parcel.writeInt(TrangThai);
        parcel.writeInt(SoChuyen);
        parcel.writeTypedList(FeedBack);
    }
}
