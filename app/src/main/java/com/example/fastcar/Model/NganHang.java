package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NganHang implements Parcelable {
    String _id;
    String TenNH, TenChuTK, SoTK;
    User User;

    protected NganHang(Parcel in) {
        _id = in.readString();
        TenNH = in.readString();
        TenChuTK = in.readString();
        SoTK = in.readString();
        User = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<NganHang> CREATOR = new Creator<NganHang>() {
        @Override
        public NganHang createFromParcel(Parcel in) {
            return new NganHang(in);
        }

        @Override
        public NganHang[] newArray(int size) {
            return new NganHang[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(TenNH);
        parcel.writeString(TenChuTK);
        parcel.writeString(SoTK);
        parcel.writeParcelable(User, i);
    }

    public NganHang(String tenNH, String tenChuTK, String soTK, com.example.fastcar.Model.User user) {
        TenNH = tenNH;
        TenChuTK = tenChuTK;
        SoTK = soTK;
        User = user;
    }

    public NganHang() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTenNH() {
        return TenNH;
    }

    public void setTenNH(String tenNH) {
        TenNH = tenNH;
    }

    public String getTenChuTK() {
        return TenChuTK;
    }

    public void setTenChuTK(String tenChuTK) {
        TenChuTK = tenChuTK;
    }

    public String getSoTK() {
        return SoTK;
    }

    public void setSoTK(String soTK) {
        SoTK = soTK;
    }

    public com.example.fastcar.Model.User getUser() {
        return User;
    }

    public void setUser(com.example.fastcar.Model.User user) {
        User = user;
    }
}
