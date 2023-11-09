package com.example.fastcar.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FavoriteCar implements Parcelable {
    User User;
    Car Xe;

    public FavoriteCar(User user, Car xe) {
        User = user;
        Xe = xe;
    }

    public FavoriteCar() {
    }


    protected FavoriteCar(Parcel in) {
        User = in.readParcelable(User.class.getClassLoader());
        Xe = in.readParcelable(Car.class.getClassLoader());
    }

    public static final Creator<FavoriteCar> CREATOR = new Creator<FavoriteCar>() {
        @Override
        public FavoriteCar createFromParcel(Parcel in) {
            return new FavoriteCar(in);
        }

        @Override
        public FavoriteCar[] newArray(int size) {
            return new FavoriteCar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(User, i);
        parcel.writeParcelable(Xe, i);
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
}
