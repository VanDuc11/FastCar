package com.example.fastcar.Activity.KhachHang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.fastcar.Adapter.LichSuThueXeAdapter;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSu_ThueXe_Activity extends AppCompatActivity {
    RelativeLayout img_back;
    LichSuThueXeAdapter adapter;
    RecyclerView recyclerView;
    List<HoaDon> lists = new ArrayList<>();
    LinearLayout ln_noResult;
    SwipeRefreshLayout refreshLayout;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_thue_xe);

        mapping();
        load();

        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });
    }

    private void mapping() {
        img_back = findViewById(R.id.icon_back_in_lsthuexe);
        recyclerView = findViewById(R.id.recyclerView_lsthuexe);
        ln_noResult = findViewById(R.id.ln_no_result_inLSChuyenXe);
        refreshLayout = findViewById(R.id.refresh_data_inLSThueXe);
        data_view = findViewById(R.id.data_view_inLSThueXe);
        shimmer_view = findViewById(R.id.shimmer_view_inLSThueXe);
    }

    private void load() {
        ln_noResult.setVisibility(View.GONE);
        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        RetrofitClient.FC_services().getListHoaDonUser(user.get_id(), "0,6").enqueue(new Callback<List<HoaDon>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                data_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        ln_noResult.setVisibility(View.GONE);
                        lists = response.body();
                        adapter = new LichSuThueXeAdapter(LichSu_ThueXe_Activity.this, lists);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        ln_noResult.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
            }
        });
        img_back.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }
}