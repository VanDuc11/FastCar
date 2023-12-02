package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ThongBao implements Parcelable {
    private String TieuDe;
    private int GiaTri;
    private int GiaTriMax;
    private String NoiDung;
    private String HinhAnh;

    private Date createdAt;

    private Date updatedAt;
    private User User;

    protected ThongBao(Parcel in) {
        TieuDe = in.readString();
        GiaTri = in.readInt();
        GiaTriMax = in.readInt();
        NoiDung = in.readString();
        HinhAnh = in.readString();
        User = in.readParcelable(com.example.fastcar.Model.User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TieuDe);
        dest.writeInt(GiaTri);
        dest.writeInt(GiaTriMax);
        dest.writeString(NoiDung);
        dest.writeString(HinhAnh);
        dest.writeParcelable(User, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThongBao> CREATOR = new Creator<ThongBao>() {
        @Override
        public ThongBao createFromParcel(Parcel in) {
            return new ThongBao(in);
        }

        @Override
        public ThongBao[] newArray(int size) {
            return new ThongBao[size];
        }
    };

    public ThongBao() {
    }

    public ThongBao(String tieuDe, int giaTri, int giaTriMax, String noiDung, String hinhAnh, Date createdAt, Date updatedAt, com.example.fastcar.Model.User user) {
        TieuDe = tieuDe;
        GiaTri = giaTri;
        GiaTriMax = giaTriMax;
        NoiDung = noiDung;
        HinhAnh = hinhAnh;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        User = user;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public com.example.fastcar.Model.User getUser() {
        return User;
    }

    public void setUser(com.example.fastcar.Model.User user) {
        User = user;
    }
}
