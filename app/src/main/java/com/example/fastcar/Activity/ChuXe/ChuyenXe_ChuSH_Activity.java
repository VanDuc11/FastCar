package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.fastcar.Activity.act_bottom.ChuyenXe_Activity;
import com.example.fastcar.Adapter.ChuyenXeChuSHAdapter;
import com.example.fastcar.Adapter.DanhSachChuyenXeAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChuyenXe_ChuSH_Activity extends AppCompatActivity {
    Car car;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    RecyclerView recyclerView_chuyenXe;
    ChuyenXeChuSHAdapter adapter;
    LinearLayout ln_noResult;
    ImageView btn_back;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_xe_chu_sh);

        mapping();
        load();

        btn_back.setOnClickListener(view -> onBackPressed());

        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });
    }

    private void mapping(){
        recyclerView_chuyenXe = findViewById(R.id.recyclerView_chuyenxeChuSH);
        ln_noResult = findViewById(R.id.ln_no_result_inChuyenXeChuSH);
        refreshLayout = findViewById(R.id.refresh_data_inChuyenXeChuSH);
        data_view = findViewById(R.id.data_view_inChuyenXe_ofChuSH);
        btn_back = findViewById(R.id.icon_back_inChuyenXe_ofChuSH);
        shimmer_view = findViewById(R.id.shimmer_view_inChuyenXe_ofChuSH);
    }

    private void load() {
        Intent intent = getIntent();
        car = intent.getParcelableExtra("car");

        ln_noResult.setVisibility(View.GONE);
        data_view.setVisibility(View.GONE);
        shimmer_view.startShimmerAnimation();

        fetchChuyenXe_ofCar(car.get_id());
    }

    private void fetchChuyenXe_ofCar(String idCar) {
        RetrofitClient.FC_services().getListHoaDon_byCar(idCar).enqueue(new Callback<List<HoaDon>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if(!response.body().isEmpty()) {
                        ln_noResult.setVisibility(View.GONE);
                        adapter = new ChuyenXeChuSHAdapter(getBaseContext(), response.body());
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

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }
}