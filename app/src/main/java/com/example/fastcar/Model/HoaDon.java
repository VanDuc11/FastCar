package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class HoaDon implements Parcelable {
    String MaHD;
    User User;
    Car Xe;
    String NgayThue, NgayTra;
    int TongSoNgayThue, PhiDV;
    String MaGiamGia;
    int GiamGia, PhuPhi, TongTien, TienCoc, ThanhToan, TrangThaiHD;
    Date GioTaoHD;

    public HoaDon(String maHD, User user, Car xe, String ngayThue, String ngayTra, int tongSoNgayThue, int phiDV, String maGiamGia, int giamGia, int phuPhi, int tongTien, int tienCoc, int thanhToan, Date gioTaoHD, int trangThaiHD) {
        MaHD = maHD;
        User = user;
        Xe = xe;
        NgayThue = ngayThue;
        NgayTra = ngayTra;
        TongSoNgayThue = tongSoNgayThue;
        PhiDV = phiDV;
        MaGiamGia = maGiamGia;
        GiamGia = giamGia;
        PhuPhi = phuPhi;
        TongTien = tongTien;
        TienCoc = tienCoc;
        ThanhToan = thanhToan;
        GioTaoHD = gioTaoHD;
        TrangThaiHD = trangThaiHD;
    }

    public HoaDon() {
    }

    protected HoaDon(Parcel in) {
        MaHD = in.readString();
        User = in.readParcelable(User.class.getClassLoader());
        Xe = in.readParcelable(Car.class.getClassLoader());
        NgayThue = in.readString();
        NgayTra = in.readString();
        TongSoNgayThue = in.readInt();
        PhiDV = in.readInt();
        MaGiamGia = in.readString();
        GiamGia = in.readInt();
        PhuPhi = in.readInt();
        TongTien = in.readInt();
        TienCoc = in.readInt();
        ThanhToan = in.readInt();
        GioTaoHD = new Date(in.readLong());
        TrangThaiHD = in.readInt();
    }

    public static final Creator<HoaDon> CREATOR = new Creator<HoaDon>() {
        @Override
        public HoaDon createFromParcel(Parcel in) {
            return new HoaDon(in);
        }

        @Override
        public HoaDon[] newArray(int size) {
            return new HoaDon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(MaHD);
        parcel.writeParcelable(User, i);
        parcel.writeParcelable(Xe, i);
        parcel.writeString(NgayThue);
        parcel.writeString(NgayTra);
        parcel.writeInt(TongSoNgayThue);
        parcel.writeInt(PhiDV);
        parcel.writeString(MaGiamGia);
        parcel.writeInt(GiamGia);
        parcel.writeInt(PhuPhi);
        parcel.writeInt(TongTien);
        parcel.writeInt(TienCoc);
        parcel.writeInt(ThanhToan);
        parcel.writeLong(GioTaoHD.getTime());
        parcel.writeInt(TrangThaiHD);
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String maHD) {
        MaHD = maHD;
    }

    public com.example.fastcar.Model.User getUser() {
        return User;
    }

    public void setUser(com.example.fastcar.Model.User user) {
        User = user;
    }

    public Car getXe() {
        return Xe;
    }

    public void setXe(Car xe) {
        Xe = xe;
    }

    public String getNgayThue() {
        return NgayThue;
    }

    public void setNgayThue(String ngayThue) {
        NgayThue = ngayThue;
    }

    public String getNgayTra() {
        return NgayTra;
    }

    public void setNgayTra(String ngayTra) {
        NgayTra = ngayTra;
    }

    public int getPhiDV() {
        return PhiDV;
    }

    public void setPhiDV(int phiDV) {
        PhiDV = phiDV;
    }

    public String getMaGiamGia() {
        return MaGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        MaGiamGia = maGiamGia;
    }

    public int getGiamGia() {
        return GiamGia;
    }

    public void setGiamGia(int giamGia) {
        GiamGia = giamGia;
    }

    public int getPhuPhi() {
        return PhuPhi;
    }

    public void setPhuPhi(int phuPhi) {
        PhuPhi = phuPhi;
    }

    public int getTongTien() {
        return TongTien;
    }

    public void setTongTien(int tongTien) {
        TongTien = tongTien;
    }

    public int getTienCoc() {
        return TienCoc;
    }

    public void setTienCoc(int tienCoc) {
        TienCoc = tienCoc;
    }

    public int getThanhToan() {
        return ThanhToan;
    }

    public void setThanhToan(int thanhToan) {
        ThanhToan = thanhToan;
    }

    public int getTrangThaiHD() {
        return TrangThaiHD;
    }

    public void setTrangThaiHD(int trangThaiHD) {
        TrangThaiHD = trangThaiHD;
    }

    public Date getGioTaoHD() {
        return GioTaoHD;
    }

    public void setGioTaoHD(Date gioTaoHD) {
        GioTaoHD = gioTaoHD;
    }

    public int getTongSoNgayThue() {
        return TongSoNgayThue;
    }

    public void setTongSoNgayThue(int tongSoNgayThue) {
        TongSoNgayThue = tongSoNgayThue;
    }
}
