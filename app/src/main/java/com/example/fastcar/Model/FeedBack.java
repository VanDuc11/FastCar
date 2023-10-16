package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FeedBack implements Parcelable {
    User user;
    String NoiDung;
    int Sao;

    public FeedBack(User user, String noiDung, int sao) {
        this.user = user;
        NoiDung = noiDung;
        Sao = sao;
    }

    public FeedBack() {
    }

    protected FeedBack(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        NoiDung = in.readString();
        Sao = in.readInt();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public int getSao() {
        return Sao;
    }

    public void setSao(int sao) {
        Sao = sao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable((Parcelable) user, i);
        parcel.writeString(NoiDung);
        parcel.writeInt(Sao);
    }
}
