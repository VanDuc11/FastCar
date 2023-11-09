package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class LichSuGiaoDich implements Parcelable {
    String _id;
    User User;
    int SoTienGD;
    Date ThoiGian;
    String NoiDung;
    int TrangThai;
    HoaDon HoaDon;

    protected LichSuGiaoDich(Parcel in) {
        _id = in.readString();
        User = in.readParcelable(User.class.getClassLoader());
        SoTienGD = in.readInt();
        ThoiGian = new Date(in.readLong());
        NoiDung = in.readString();
        TrangThai = in.readInt();
        HoaDon = in.readParcelable(HoaDon.class.getClassLoader());
    }

    public LichSuGiaoDich() {
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
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeParcelable(User, i);
        parcel.writeInt(SoTienGD);
        parcel.writeLong(ThoiGian.getTime());
        parcel.writeString(NoiDung);
        parcel.writeInt(TrangThai);
        parcel.writeParcelable(HoaDon, i);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
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

    public HoaDon getHoaDon() {
        return HoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        HoaDon = hoaDon;
    }
}
