package com.example.fastcar.Retrofit;

import com.example.fastcar.Server.HostApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static FastCarServices FC_services() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HostApi.API_URL + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FastCarServices.class);
    }

    public static FastCarServices FC_services_HangXe() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HostApi.Api_URL_hang + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FastCarServices.class);
    }

    public static FastCarServices FC_services_Banks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HostApi.API_BANKS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FastCarServices.class);
    }

    public static FastCarServices GoongIO_Services() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HostApi.API_GOONG)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FastCarServices.class);
    }
}
