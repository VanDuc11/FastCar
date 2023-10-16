package com.example.fastcar.Retrofit;

import com.example.fastcar.Server.HostApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static FastCarServices FC_services() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HostApi.API_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FastCarServices.class);
    }
}
