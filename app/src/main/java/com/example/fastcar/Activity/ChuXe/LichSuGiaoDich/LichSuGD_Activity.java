package com.example.fastcar.Activity.ChuXe.LichSuGiaoDich;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastcar.Activity.ChuXe.QuanLyChuyenXe.MyPagerAdapter;
import com.example.fastcar.Adapter.LichSuGiaoDichApdater;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuGD_Activity extends AppCompatActivity {
    private ViewPager viewPagerLSGD;
    private PagerAdapter_LSGD pagerAdapter_lsgd;
    private TextView btnAll, btnRutTien, btnHoanTien, btnNhanTien;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_gd);

        mapping();
        load();
        loadItemSelected();

        viewPagerLSGD.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        loadUIItemSelected(0);
                        break;
                    case 1:
                        viewPagerLSGD.requestLayout();
                        loadUIItemSelected(1);
                        break;
                    case 2:
                        viewPagerLSGD.requestLayout();
                        loadUIItemSelected(2);
                        break;
                    case 3:
                        viewPagerLSGD.requestLayout();
                        loadUIItemSelected(3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_back.setOnClickListener(view -> onBackPressed());
    }

    private void mapping() {
        btn_back = findViewById(R.id.btn_back_lich_su_giao_dich);
        viewPagerLSGD = findViewById(R.id.viewPager_LSGD);
        btnAll = findViewById(R.id.btn_tatca_lsgd);
        btnRutTien = findViewById(R.id.btn_ruttien_lsgd);
        btnHoanTien = findViewById(R.id.btn_hoantien_lsgd);
        btnNhanTien = findViewById(R.id.btn_nhantien_lsgd);
    }

    private void load() {
        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        loadUIItemSelected(0);
        pagerAdapter_lsgd = new PagerAdapter_LSGD(getSupportFragmentManager(), user.getEmail());
        viewPagerLSGD.setAdapter(pagerAdapter_lsgd);
    }

    private void loadItemSelected() {
        btnAll.setOnClickListener(view -> {
            viewPagerLSGD.setCurrentItem(0);
            loadUIItemSelected(0);
        });

        btnRutTien.setOnClickListener(view -> {
            viewPagerLSGD.setCurrentItem(1);
            loadUIItemSelected(1);
        });

        btnNhanTien.setOnClickListener(view -> {
            viewPagerLSGD.setCurrentItem(2);
            loadUIItemSelected(2);
        });

        btnHoanTien.setOnClickListener(view -> {
            viewPagerLSGD.setCurrentItem(3);
            loadUIItemSelected(3);
        });
    }

    private void loadUIItemSelected(int index) {
        if (index == 0) {
            btnAll.setBackgroundResource(R.drawable.custom_item_selected);
            btnAll.setTextColor(getResources().getColor(R.color.white));
            btnRutTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnRutTien.setTextColor(getResources().getColor(R.color.black));
            btnNhanTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnNhanTien.setTextColor(getResources().getColor(R.color.black));
            btnHoanTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnHoanTien.setTextColor(getResources().getColor(R.color.black));
        } else if (index == 1) {
            btnAll.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnAll.setTextColor(getResources().getColor(R.color.black));
            btnRutTien.setBackgroundResource(R.drawable.custom_item_selected);
            btnRutTien.setTextColor(getResources().getColor(R.color.white));
            btnNhanTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnNhanTien.setTextColor(getResources().getColor(R.color.black));
            btnHoanTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnHoanTien.setTextColor(getResources().getColor(R.color.black));
        } else if (index == 2) {
            btnAll.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnAll.setTextColor(getResources().getColor(R.color.black));
            btnRutTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnRutTien.setTextColor(getResources().getColor(R.color.black));
            btnNhanTien.setBackgroundResource(R.drawable.custom_item_selected);
            btnNhanTien.setTextColor(getResources().getColor(R.color.white));
            btnHoanTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnHoanTien.setTextColor(getResources().getColor(R.color.black));
        } else {
            btnAll.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnAll.setTextColor(getResources().getColor(R.color.black));
            btnRutTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnRutTien.setTextColor(getResources().getColor(R.color.black));
            btnNhanTien.setBackgroundResource(R.drawable.custom_item_non_selected);
            btnNhanTien.setTextColor(getResources().getColor(R.color.black));
            btnHoanTien.setBackgroundResource(R.drawable.custom_item_selected);
            btnHoanTien.setTextColor(getResources().getColor(R.color.white));
        }
    }

}