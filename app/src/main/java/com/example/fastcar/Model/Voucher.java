package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Voucher implements Parcelable {
    String _id;
    String MaGiamGia, Code;
    int GiaTri, GiaTriMax;
    String NoiDung, HinhAnh;
    Date HSD;
    boolean TrangThai;
    boolean isSelected;

    public Voucher(String _id, String maGiamGia, String code, int giaTri, int giaTriMax, String noiDung, String hinhAnh, Date HSD, boolean trangThai, boolean isSelected) {
        this._id = _id;
        MaGiamGia = maGiamGia;
        Code = code;
        GiaTri = giaTri;
        GiaTriMax = giaTriMax;
        NoiDung = noiDung;
        HinhAnh = hinhAnh;
        this.HSD = HSD;
        TrangThai = trangThai;
        isSelected = isSelected;
    }

    public Voucher() {
    }


    protected Voucher(Parcel in) {
        _id = in.readString();
        MaGiamGia = in.readString();
        Code = in.readString();
        GiaTri = in.readInt();
        GiaTriMax = in.readInt();
        NoiDung = in.readString();
        HinhAnh = in.readString();
        TrangThai = in.readByte() != 0;
        isSelected = in.readByte() != 0;
    }

    public static final Creator<Voucher> CREATOR = new Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel in) {
            return new Voucher(in);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMaGiamGia() {
        return MaGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        MaGiamGia = maGiamGia;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getGiaTri() {
        return GiaTri;
    }

    public void setGiaTri(int giaTri) {
        GiaTri = giaTri;
    }

    public int getGiaTriMax() {
        return GiaTriMax;
    }

    public void setGiaTriMax(int giaTriMax) {
        GiaTriMax = giaTriMax;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public Date getHSD() {
        return HSD;
    }

    public void setHSD(Date HSD) {
        this.HSD = HSD;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean trangThai) {
        TrangThai = trangThai;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(MaGiamGia);
        parcel.writeString(Code);
        parcel.writeInt(GiaTri);
        parcel.writeInt(GiaTriMax);
        parcel.writeString(NoiDung);
        parcel.writeString(HinhAnh);
        parcel.writeByte((byte) (TrangThai ? 1 : 0));
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
