package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fastcar.Adapter.LichSuGiaoDichApdater;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuGD_Activity extends AppCompatActivity {
    LichSuGiaoDichApdater apdater;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    ShimmerFrameLayout shimmer_view;
    LinearLayout dataView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_gd);
        mapping();
        load();
        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });
    }
    private void mapping() {
        recyclerView = findViewById(R.id.recyclerView_lich_su_giao_dich);
        refreshLayout = findViewById(R.id.refresh_data_inListLichSuGiaoDich);
        dataView = findViewById(R.id.data_view_lichSuGiaoDich);
        shimmer_view = findViewById(R.id.shimmer_view_lichSuGiaoDich);

    }
    private void load() {
        dataView.setVisibility(View.GONE);
        shimmer_view.startShimmerAnimation();
        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        fetchData_Lich_Su_Giao_Dich(user.get_id());
    }
    private void fetchData_Lich_Su_Giao_Dich(String idUser) {
    RetrofitClient.FC_services().getLSGD_ofUser(idUser).enqueue(new Callback<List<LichSuGiaoDich>>() {
        @Override
        public void onResponse(Call<List<LichSuGiaoDich>> call, Response<List<LichSuGiaoDich>> response) {
            dataView.setVisibility(View.VISIBLE);
            shimmer_view.stopShimmerAnimation();
            shimmer_view.setVisibility(View.GONE);
            if(response.code()==200){
                apdater = new LichSuGiaoDichApdater(response.body(),LichSuGD_Activity.this);
                recyclerView.setAdapter(apdater);
                apdater.notifyDataSetChanged();

            }else {
                refreshLayout.setVisibility(View.GONE);
                System.out.println(response.code()+response.message());
            }
        }

        @Override
        public void onFailure(Call<List<LichSuGiaoDich>> call, Throwable t) {
            System.out.println("Có lỗi xảy ra: " + t);
        }
    });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }
}