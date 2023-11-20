package com.example.fastcar.Activity.act_bottom;

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

import com.example.fastcar.Activity.LichSu_ThueXe_Activity;
import com.example.fastcar.Adapter.DanhSachChuyenXeAdapter;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChuyenXe_Activity extends AppCompatActivity {
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    RecyclerView recyclerView_chuyenXe;
    DanhSachChuyenXeAdapter adapter;
    LinearLayout ln_noResult;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_xe);

        mapping();
        load();

        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });
    }

    void mapping() {
        recyclerView_chuyenXe = findViewById(R.id.recyclerView_chuyenxe);
        ln_noResult = findViewById(R.id.ln_no_result_inChuyenXe);
        refreshLayout = findViewById(R.id.refresh_data_inChuyenXe);
        data_view = findViewById(R.id.data_view_inChuyenXe);
        shimmer_view = findViewById(R.id.shimmer_view_inChuyenXe);
    }

    private void load() {
        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        ln_noResult.setVisibility(View.GONE);
        data_view.setVisibility(View.GONE);
        shimmer_view.startShimmerAnimation();

        RetrofitClient.FC_services().getListHoaDonUser( user.get_id(), "1,2,3,4,5").enqueue(new Callback<List<HoaDon>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if(!response.body().isEmpty()) {
                        ln_noResult.setVisibility(View.GONE);
                        adapter = new DanhSachChuyenXeAdapter(ChuyenXe_Activity.this, response.body());
                        recyclerView_chuyenXe.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        refreshLayout.setVisibility(View.GONE);
                        ln_noResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
                refreshLayout.setVisibility(View.GONE);
                ln_noResult.setVisibility(View.VISIBLE);
            }
        });
    }

    public void tab2_to_tab1(View view) {
        startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
    }

    public void tab2_to_tab3(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    public void tab2_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
    }

    public void fromChuyenXe_to_LSChuyenXe(View view) {
        startActivity(new Intent(getBaseContext(), LichSu_ThueXe_Activity.class));
    }

    @Override
    public void onBackPressed() {
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        load();
//    }
}