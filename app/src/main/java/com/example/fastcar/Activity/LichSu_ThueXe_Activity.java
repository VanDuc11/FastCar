package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.fastcar.Adapter.LichSuThueXeAdapter;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_thue_xe);

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
        img_back = findViewById(R.id.icon_back_in_lsthuexe);
        recyclerView = findViewById(R.id.recyclerView_lsthuexe);
        ln_noResult = findViewById(R.id.ln_no_result_inLSChuyenXe);
        refreshLayout = findViewById(R.id.refresh_data_inLSThueXe);
    }

    private void load() {
        ln_noResult.setVisibility(View.GONE);
        RetrofitClient.FC_services().getListHoaDon("0,4").enqueue(new Callback<List<HoaDon>>() {
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        ln_noResult.setVisibility(View.GONE);
                        lists = response.body();
                        adapter = new LichSuThueXeAdapter(LichSu_ThueXe_Activity.this, lists);
                        recyclerView.setAdapter(adapter);
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