package com.example.fastcar.Retrofit;

import com.example.fastcar.Model.Car;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FastCarServices {
    // Car Model
    @GET("quanlyxe/listxe")
    Call<List<Car>> getListCar();
}
