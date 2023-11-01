package com.example.fastcar.Retrofit;

import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.Model.Voucher;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FastCarServices {
    // Car Model URL: xe/
    @GET("xe/list")
    Call<List<Car>> getListCar();

    @GET("xe/listXe_user/{email}")
    Call<List<Car>> getListCar_ofUser(@Path("email") String email_user);

    @GET("xe/listXe_NotUser/{email}")
    Call<List<Car>> getListCar_NotUser(@Path("email") String email_user, @Query("DiaChiXe") String diachixe);

    @GET("xe/top5xe/{email}")
    Call<List<Car>> getListTop5Car(@Path("email") String email_user);

    @POST("xe/create")
    Call<ResMessage> createXe(@Body Car car);

    // User Model URL: user/
    @GET("user/list")
    Call<List<User>> getListUser(@Query("Email") String email);

    @GET("user/findUserEmail")
    Call<List<User>> findUserEmail();

    // Voucher Model URL: voucher/
    @GET("voucher/list")
    Call<List<Voucher>> getListVoucher();

    // HoaDon model URL: hoadon/
    @GET("hoadon/list")
    Call<List<HoaDon>> getListHoaDon(@Query("User") String id_user, @Query("TrangThaiHD") String TrangThaiHD);

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("hoadon/create")
    Call<ResMessage> createHoaDon(@Body HoaDon hoaDon);

    @POST("hoadon/update_trangthaiHD/{maHD}")
    Call<String> updateTrangThaiHD(@Path("maHD") String maHD, @Body HoaDon hoaDon);

    // Feedback model URL: feedback/
    @GET("feedback/list")
    Call<List<FeedBack>> getListFeedBack(@Query("Xe") String id_xe);

    @POST("feedback/create")
    Call<ResMessage> createFeedBack(@Body FeedBack feedBack);

    // Favorite Car model

    @GET("favoriteCar/list/{userId}")
    Call<List<Car>> getListFavoriteCar(@Path("userId") String id_user);

    @GET("favoriteCar/find/{userId}/{carId}")
    Call<List<Car>> findFavoriteCar(@Path("userId") String id_user, @Path("carId") String id_car);

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("favoriteCar/add")
    Call<ResMessage> addFavoriteCar(@Body FavoriteCar favoriteCar);

    @Headers("Content-Type: application/json; charset=utf-8")
    @DELETE("favoriteCar/delete/{userId}/{carId}")
    Call<ResMessage> deleteFavoriteCar(
            @Path("userId") String userId, @Path("carId") String carId);
}
