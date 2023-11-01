package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Adapter.PhotoAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.Gson;
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
    LinearLayout ln_noResult;
    NestedScrollView ln_listView;

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
        ln_noResult = findViewById(R.id.ln_no_result_inListXe);
        ln_listView = findViewById(R.id.view_listXe);
    }

    private void getData() {
        ln_noResult.setVisibility(View.GONE);

        // lấy địa chỉ từ intent
        Intent intent = getIntent();
        String diachi = intent.getStringExtra("diachi");

        // lấy user login từ sharedpreferences
        SharedPreferences preferences1 = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences1.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RetrofitClient.FC_services().getListCar_NotUser( user.getEmail(), diachi).enqueue(new Callback<List<Car>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if(response.code() == 200) {
                    ln_noResult.setVisibility(View.GONE);
                    adapter = new DanhSachXeAdapter(getApplicationContext(), response.body(), false);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    ln_listView.setVisibility(View.GONE);
                    ln_noResult.setVisibility(View.VISIBLE);
                }
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