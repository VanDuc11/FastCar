package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class FeedBack implements Parcelable {
    User User;
    Car Xe;
    String NoiDung;
    int SoSao;
    Date ThoiGian;

    public FeedBack(User user, Car xe, String noiDung, int soSao, Date thoiGian) {
        this.User = user;
        this.Xe = xe;
        NoiDung = noiDung;
        SoSao = soSao;
        ThoiGian = thoiGian;
    }

    public FeedBack() {
    }


    protected FeedBack(Parcel in) {
        User = in.readParcelable(User.class.getClassLoader());
        Xe = in.readParcelable(Car.class.getClassLoader());
        NoiDung = in.readString();
        SoSao = in.readInt();
        ThoiGian = new Date(in.readLong());
    }

    public static final Creator<FeedBack> CREATOR = new Creator<FeedBack>() {
        @Override
        public FeedBack createFromParcel(Parcel in) {
            return new FeedBack(in);
        }

        @Override
        public FeedBack[] newArray(int size) {
            return new FeedBack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(Xe, i);
        parcel.writeString(NoiDung);
        parcel.writeInt(SoSao);
        parcel.writeLong(ThoiGian.getTime());
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

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public int getSoSao() {
        return SoSao;
    }

    public void setSoSao(int soSao) {
        SoSao = soSao;
    }

    public Date getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        ThoiGian = thoiGian;
    }
}
