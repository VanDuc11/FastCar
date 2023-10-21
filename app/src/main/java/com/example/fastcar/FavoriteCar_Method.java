package com.example.fastcar;

import android.content.Context;

import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteCar_Method {
    public static void addFavoriteCar(Context context, FavoriteCar favoriteCar) {
        RetrofitClient.FC_services().addFavoriteCar(favoriteCar).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 201) {
                    CustomDialogNotify.showToastCustom(context, "Đã thêm vào mục yêu thích");
                } else {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi thêm xe yêu thích: " + t);
            }
        });
    }

    public static void deleteFavoriteCar(String userId, String carId) {
        RetrofitClient.FC_services().deleteFavoriteCar(userId, carId).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    System.out.println(response.message());
                } else {
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi xoá xe yêu thích: " + t);
            }
        });
    }
}
