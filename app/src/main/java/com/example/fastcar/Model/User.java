package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class User implements Parcelable {
    String _id, UID, UserName, SDT, NgaySinh, GioiTinh , Email;
    ArrayList<String> GPLX, CCCD;
    String MatKhau, Avatar;
    Date NgayThamGia;

    public User(String _id, String UID, String userName, String SDT, String ngaySinh, String gioiTinh, String email, ArrayList<String> GPLX, ArrayList<String> CCCD, String matKhau, String avatar, Date ngayThamGia) {
        this._id = _id;
        this.UID = UID;
        UserName = userName;
        this.SDT = SDT;
        NgaySinh = ngaySinh;
        GioiTinh = gioiTinh;
        Email = email;
        this.GPLX = GPLX;
        this.CCCD = CCCD;
        MatKhau = matKhau;
        this.Avatar = avatar;
        this.NgayThamGia = ngayThamGia;
    }

    public User() {
    }

    protected User(Parcel in) {
        _id = in.readString();
        UID = in.readString();
        UserName = in.readString();
        SDT = in.readString();
        NgaySinh = in.readString();
        GioiTinh = in.readString();
        Email = in.readString();
        GPLX = in.createStringArrayList();
        CCCD = in.createStringArrayList();
        MatKhau = in.readString();
        Avatar = in.readString();
        NgayThamGia = new Date(in.readLong());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(UID);
        parcel.writeString(UserName);
        parcel.writeString(SDT);
        parcel.writeString(NgaySinh);
        parcel.writeString(GioiTinh);
        parcel.writeString(Email);
        parcel.writeStringList(GPLX);
        parcel.writeStringList(CCCD);
        parcel.writeString(MatKhau);
        parcel.writeString(Avatar);
        parcel.writeLong(NgayThamGia.getTime());
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public ArrayList<String> getGPLX() {
        return GPLX;
    }

    public void setGPLX(ArrayList<String> GPLX) {
        this.GPLX = GPLX;
    }

    public ArrayList<String> getCCCD() {
        return CCCD;
    }

    public void setCCCD(ArrayList<String> CCCD) {
        this.CCCD = CCCD;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public Date getNgayThamGia() {
        return NgayThamGia;
    }

    public void setNgayThamGia(Date ngayThamGia) {
        NgayThamGia = ngayThamGia;
    }
}
