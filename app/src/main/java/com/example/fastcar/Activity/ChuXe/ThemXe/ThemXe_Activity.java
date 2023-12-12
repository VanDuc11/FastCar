package com.example.fastcar.Activity.ChuXe.ThemXe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fastcar.Activity.act_bottom.CaNhan_Activity;
import com.example.fastcar.Adapter.DanhSachXeCuaToiAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemXe_Activity extends AppCompatActivity {
    RelativeLayout img_back, img_add;
    TextView btn_add;
    RecyclerView recyclerView;
    DanhSachXeCuaToiAdapter adapter;
    LinearLayout ln_no_result;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_xe);

        mapping();
        load();

        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });

        img_back.setOnClickListener(view -> {
            Intent intent = getIntent();
            if (intent.getBooleanExtra("isLoaded", false)) {
                Intent intent1 = new Intent(this, CaNhan_Activity.class);
                startActivity(intent1);
                finish();
            } else {
                onBackPressed();
                finish();
            }
        });

        img_add.setOnClickListener(view -> nextToScreen());
        btn_add.setOnClickListener(view -> nextToScreen());
    }

    private void mapping() {
        img_back = findViewById(R.id.icon_back_in_themxe);
        img_add = findViewById(R.id.icon_add_in_themxe);
        btn_add = findViewById(R.id.btn_themxe);
        ln_no_result = findViewById(R.id.ln_no_result_inThemXe);
        recyclerView = findViewById(R.id.recyclerView_listXeCuaToi);
        data_view = findViewById(R.id.data_view_inXeCuaToi);
        shimmer_view = findViewById(R.id.shimmer_view_inXeCuaToi);
        refreshLayout = findViewById(R.id.refresh_data_inListXeCuaToi);
    }

    private void load() {
        ln_no_result.setVisibility(View.GONE);
        data_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();
        // lấy user từ shared
        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        // get data
        RetrofitClient.FC_services().getListCar_ofUser(user.getEmail(), "0,1,2,3,4").enqueue(new Callback<List<Car>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    adapter = new DanhSachXeCuaToiAdapter(ThemXe_Activity.this, response.body());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    ln_no_result.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    ln_no_result.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi getListCar_ofUser(): " + user.getEmail() + " --- " + t);
            }
        });
    }

    private void nextToScreen() {
        startActivity(new Intent(getBaseContext(), ThongTinCoBan_Activity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }
}