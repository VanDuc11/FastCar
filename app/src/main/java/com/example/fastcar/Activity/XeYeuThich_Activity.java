package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XeYeuThich_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    DanhSachXeAdapter adapter;
    LinearLayout ln_no_result;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xe_yeu_thich);

        mapping();
        load();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void mapping() {
        recyclerView = findViewById(R.id.recyclerView_xeYeuThich);
        ln_no_result = findViewById(R.id.ln_no_result_inXeYeuThich);
        refreshLayout = findViewById(R.id.refresh_data_inXeYeuThich);
    }

    private void load() {
        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        ln_no_result.setVisibility(View.GONE);

        RetrofitClient.FC_services().getListFavoriteCar(user.get_id()).enqueue(new Callback<List<Car>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        adapter = new DanhSachXeAdapter(XeYeuThich_Activity.this, response.body(), false);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        ln_no_result.setVisibility(View.GONE);
                    } else {
                        refreshLayout.setVisibility(View.GONE);
                        ln_no_result.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi get list favorite car: " + t);
            }
        });
    }

    public void back_in_XeYeuThichACT(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }
}