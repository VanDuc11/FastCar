package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Adapter.PhotoAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachXe_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView ic_back;
    DanhSachXeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_xe);

        mapping();
        getData();

        ic_back.setOnClickListener(view -> onBackPressed());
    }

    void mapping() {
        recyclerView = findViewById(R.id.recyclerView_listXe);
        ic_back = findViewById(R.id.icon_back_in_dsxe);
    }

    private void getData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RetrofitClient.FC_services().getListCar().enqueue(new Callback<List<Car>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                adapter = new DanhSachXeAdapter(getApplicationContext(), response.body());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi thực hiện: " + t);
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getData();
//    }
}