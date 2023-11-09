package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Car implements Parcelable {
    String _id, BKS, HangXe, MauXe, NSX;
    int SoGhe;
    String ChuyenDong, LoaiNhienLieu;
    float TieuHao;
    String MoTa;
    ArrayList<String> HinhAnh;
    String DangKyXe, DangKiem, BaoHiem;
    String DiaChiXe;
    int GiaThue1Ngay;
    User ChuSH;
    int TrangThai, SoChuyen;
    float TrungBinhSao;

    public Car(String _id, String BKS, String hangXe, String mauXe, String NSX, int soGhe, String chuyenDong, String loaiNhienLieu, float tieuHao, String moTa, ArrayList<String> hinhAnh, String dangKyXe, String dangKiem, String baoHiem, String diaChiXe, int giaThue1Ngay, User chuSH, int trangThai, int soChuyen, float trungBinhSao) {
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
        this.DangKyXe = dangKyXe;
        this.DangKiem = dangKiem;
        this.BaoHiem = baoHiem;
        this.DiaChiXe = diaChiXe;
        this.GiaThue1Ngay = giaThue1Ngay;
        this.ChuSH = chuSH;
        this.TrangThai = trangThai;
        this.SoChuyen = soChuyen;
        this.TrungBinhSao = trungBinhSao;
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
        TieuHao = in.readFloat();
        MoTa = in.readString();
        HinhAnh = in.createStringArrayList();
        DangKyXe = in.readString();
        DangKiem = in.readString();
        BaoHiem = in.readString();
        DiaChiXe = in.readString();
        GiaThue1Ngay = in.readInt();
        ChuSH = in.readParcelable(User.class.getClassLoader());
        TrangThai = in.readInt();
        SoChuyen = in.readInt();
        TrungBinhSao = in.readFloat();
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

    public User getChuSH() {
        return ChuSH;
    }

    public void setChuSH(User chuSH) {
        ChuSH = chuSH;
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

    public float getTrungBinhSao() {
        return TrungBinhSao;
    }

    public void setTrungBinhSao(float trungBinhSao) {
        TrungBinhSao = trungBinhSao;
    }

    public String getDangKyXe() {
        return DangKyXe;
    }

    public void setDangKyXe(String dangKyXe) {
        DangKyXe = dangKyXe;
    }

    public String getDangKiem() {
        return DangKiem;
    }

    public void setDangKiem(String dangKiem) {
        DangKiem = dangKiem;
    }

    public String getBaoHiem() {
        return BaoHiem;
    }

    public void setBaoHiem(String baoHiem) {
        BaoHiem = baoHiem;
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
        parcel.writeFloat(TieuHao);
        parcel.writeString(MoTa);
        parcel.writeStringList(HinhAnh);
        parcel.writeString(DangKyXe);
        parcel.writeString(DangKiem);
        parcel.writeString(BaoHiem);
        parcel.writeString(DiaChiXe);
        parcel.writeInt(GiaThue1Ngay);
        parcel.writeParcelable(ChuSH, i);
        parcel.writeInt(TrangThai);
        parcel.writeInt(SoChuyen);
        parcel.writeFloat(TrungBinhSao);
    }
}
