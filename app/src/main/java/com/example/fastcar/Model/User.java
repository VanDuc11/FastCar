package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class User implements Parcelable {
    String _id, UID, UserName, SDT, NgaySinh, GioiTinh , Email;
    String So_GPLX, HoTen_GPLX, NgayCap_GPLX, DiaChi_GPLX;
    ArrayList<String> HinhAnh_GPLX;
    int TrangThai_GPLX;
    String So_CCCD, NgayCap_CCCD, NoiCap_CCCD;
    String MatKhau, Avatar;
    Date NgayThamGia;
    int SoDu;
    String TokenFCM;

    public User(String _id, String UID, String userName, String SDT, String ngaySinh, String gioiTinh, String email, String so_GPLX, String hoTen_GPLX, String ngayCap_GPLX, String diaChi_GPLX, ArrayList<String> hinhAnh_GPLX, int trangThai_GPLX, String so_CCCD, String ngayCap_CCCD, String noiCap_CCCD, String matKhau, String avatar, Date ngayThamGia, int soDu, String tokenFCM) {
        this._id = _id;
        this.UID = UID;
        UserName = userName;
        this.SDT = SDT;
        NgaySinh = ngaySinh;
        GioiTinh = gioiTinh;
        Email = email;
        So_GPLX = so_GPLX;
        HoTen_GPLX = hoTen_GPLX;
        NgayCap_GPLX = ngayCap_GPLX;
        DiaChi_GPLX = diaChi_GPLX;
        HinhAnh_GPLX = hinhAnh_GPLX;
        TrangThai_GPLX = trangThai_GPLX;
        So_CCCD = so_CCCD;
        NgayCap_CCCD = ngayCap_CCCD;
        NoiCap_CCCD = noiCap_CCCD;
        MatKhau = matKhau;
        Avatar = avatar;
        NgayThamGia = ngayThamGia;
        SoDu = soDu;
        TokenFCM = tokenFCM;
    }

    public User(String UID, String userName, String email, String matKhau, String avatar, Date ngayThamGia, String tokenFCM) {
        this.UID = UID;
        UserName = userName;
        Email = email;
        MatKhau = matKhau;
        Avatar = avatar;
        NgayThamGia = ngayThamGia;
        TokenFCM = tokenFCM;
    }


    public User(String so_CCCD, String ngayCap_CCCD, String noiCap_CCCD) {
        So_CCCD = so_CCCD;
        NgayCap_CCCD = ngayCap_CCCD;
        NoiCap_CCCD = noiCap_CCCD;
    }

    public User(String userName, String SDT, String ngaySinh, String gioiTinh) {
        UserName = userName;
        this.SDT = SDT;
        NgaySinh = ngaySinh;
        GioiTinh = gioiTinh;
    }

    public User(String tokenFCM, String avatar) {
        TokenFCM = tokenFCM;
        Avatar = avatar;
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
        So_GPLX = in.readString();
        HoTen_GPLX = in.readString();
        NgayCap_GPLX = in.readString();
        DiaChi_GPLX = in.readString();
        HinhAnh_GPLX = in.createStringArrayList();
        TrangThai_GPLX = in.readInt();
        So_CCCD = in.readString();
        NgayCap_CCCD = in.readString();
        NoiCap_CCCD = in.readString();
        MatKhau = in.readString();
        Avatar = in.readString();
        SoDu = in.readInt();
        TokenFCM = in.readString();
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
        parcel.writeString(So_GPLX);
        parcel.writeString(HoTen_GPLX);
        parcel.writeString(NgayCap_GPLX);
        parcel.writeString(DiaChi_GPLX);
        parcel.writeStringList(HinhAnh_GPLX);
        parcel.writeInt(TrangThai_GPLX);
        parcel.writeString(So_CCCD);
        parcel.writeString(NgayCap_CCCD);
        parcel.writeString(NoiCap_CCCD);
        parcel.writeString(MatKhau);
        parcel.writeString(Avatar);
        parcel.writeInt(SoDu);
        parcel.writeString(TokenFCM);
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

    public String getSo_GPLX() {
        return So_GPLX;
    }

    public void setSo_GPLX(String so_GPLX) {
        So_GPLX = so_GPLX;
    }

    public String getHoTen_GPLX() {
        return HoTen_GPLX;
    }

    public void setHoTen_GPLX(String hoTen_GPLX) {
        HoTen_GPLX = hoTen_GPLX;
    }

    public String getNgayCap_GPLX() {
        return NgayCap_GPLX;
    }

    public void setNgayCap_GPLX(String ngayCap_GPLX) {
        NgayCap_GPLX = ngayCap_GPLX;
    }

    public String getDiaChi_GPLX() {
        return DiaChi_GPLX;
    }

    public void setDiaChi_GPLX(String diaChi_GPLX) {
        DiaChi_GPLX = diaChi_GPLX;
    }

    public ArrayList<String> getHinhAnh_GPLX() {
        return HinhAnh_GPLX;
    }

    public void setHinhAnh_GPLX(ArrayList<String> hinhAnh_GPLX) {
        HinhAnh_GPLX = hinhAnh_GPLX;
    }

    public int getTrangThai_GPLX() {
        return TrangThai_GPLX;
    }

    public void setTrangThai_GPLX(int trangThai_GPLX) {
        TrangThai_GPLX = trangThai_GPLX;
    }

    public String getSo_CCCD() {
        return So_CCCD;
    }

    public void setSo_CCCD(String so_CCCD) {
        So_CCCD = so_CCCD;
    }

    public String getNgayCap_CCCD() {
        return NgayCap_CCCD;
    }

    public void setNgayCap_CCCD(String ngayCap_CCCD) {
        NgayCap_CCCD = ngayCap_CCCD;
    }

    public String getNoiCap_CCCD() {
        return NoiCap_CCCD;
    }

    public void setNoiCap_CCCD(String noiCap_CCCD) {
        NoiCap_CCCD = noiCap_CCCD;
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

    public int getSoDu() {
        return SoDu;
    }

    public void setSoDu(int soDu) {
        SoDu = soDu;
    }

    public String getTokenFCM() {
        return TokenFCM;
    }

    public void setTokenFCM(String tokenFCM) {
        TokenFCM = tokenFCM;
    }
}
