package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class LichSuGiaoDich implements Parcelable {
    String _id;
    String MaLSGD;
    User User;
    int SoTienGD;
    Date ThoiGian;
    String NoiDung;
    int TrangThai;
    HoaDon HoaDon;
    NganHang NganHang;
    int title;
    String HinhAnh;

    protected LichSuGiaoDich(Parcel in) {
        _id = in.readString();
        MaLSGD = in.readString();
        User = in.readParcelable(User.class.getClassLoader());
        SoTienGD = in.readInt();
        NoiDung = in.readString();
        TrangThai = in.readInt();
        HoaDon = in.readParcelable(HoaDon.class.getClassLoader());
        NganHang = in.readParcelable(NganHang.class.getClassLoader());
        title = in.readInt();
        HinhAnh = in.readString();
    }

    public static final Creator<LichSuGiaoDich> CREATOR = new Creator<LichSuGiaoDich>() {
        @Override
        public LichSuGiaoDich createFromParcel(Parcel in) {
            return new LichSuGiaoDich(in);
        }

        @Override
        public LichSuGiaoDich[] newArray(int size) {
            return new LichSuGiaoDich[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(MaLSGD);
        dest.writeParcelable(User, flags);
        dest.writeInt(SoTienGD);
        dest.writeString(NoiDung);
        dest.writeInt(TrangThai);
        dest.writeParcelable(HoaDon, flags);
        dest.writeParcelable(NganHang, flags);
        dest.writeInt(title);
        dest.writeString(HinhAnh);
    }

    public LichSuGiaoDich() {
    }

    public LichSuGiaoDich(String _id, String maLSGD, User user, int soTienGD, Date thoiGian, String noiDung, int trangThai, HoaDon hoaDon, NganHang nganHang, int title, String hinhAnh) {
        this._id = _id;
        this.MaLSGD = maLSGD;
        this.User = user;
        this.SoTienGD = soTienGD;
        this.ThoiGian = thoiGian;
        this.NoiDung = noiDung;
        this.TrangThai = trangThai;
        this.HoaDon = hoaDon;
        this.NganHang = nganHang;
        this.title = title;
        this.HinhAnh = hinhAnh;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMaLSGD() {
        return MaLSGD;
    }

    public void setMaLSGD(String maLSGD) {
        MaLSGD = maLSGD;
    }

    public com.example.fastcar.Model.User getUser() {
        return User;
    }

    public void setUser(com.example.fastcar.Model.User user) {
        User = user;
    }

    public int getSoTienGD() {
        return SoTienGD;
    }

    public void setSoTienGD(int soTienGD) {
        SoTienGD = soTienGD;
    }

    public Date getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        ThoiGian = thoiGian;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public int getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(int trangThai) {
        TrangThai = trangThai;
    }

    public com.example.fastcar.Model.HoaDon getHoaDon() {
        return HoaDon;
    }

    public void setHoaDon(com.example.fastcar.Model.HoaDon hoaDon) {
        HoaDon = hoaDon;
    }

    public com.example.fastcar.Model.NganHang getNganHang() {
        return NganHang;
    }

    public void setNganHang(com.example.fastcar.Model.NganHang nganHang) {
        NganHang = nganHang;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
