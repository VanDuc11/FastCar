package com.example.fastcar.Activity.ChuXe.QuanLyChuyenXe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fastcar.Activity.MaGiamGia_Activity;
import com.example.fastcar.Activity.ThongBao_Activity;
import com.example.fastcar.Activity.act_bottom.HoTro_Activity;
import com.example.fastcar.Model.Car;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyChuyenXe_ChuSH_Activity extends AppCompatActivity {
    TextView btn_active,btn_upcoming;
    ViewPager viewPager;
    MyPagerAdapter adapter;
    StringBuilder allCarID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_chuyen_xe_chu_sh);
        fect_data();

        mapping();

       loadItemSelected();
       viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Đang cuộn giữa các trang
            }
            @Override
            public void onPageSelected(int position) {
                // Trang đã được chọn
                switch (position) {
                    case 0:
                        btn_active.setBackgroundResource(R.drawable.custom_item_selected);
                        btn_active.setTextColor(getResources().getColor(R.color.white));


                        btn_upcoming.setBackgroundResource(R.drawable.custom_item_non_selected);
                        btn_upcoming.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 1:
                        btn_upcoming.setBackgroundResource(R.drawable.custom_item_selected);
                        btn_upcoming.setTextColor(getResources().getColor(R.color.white));

                        btn_active.setBackgroundResource(R.drawable.custom_item_non_selected);
                        btn_active.setTextColor(getResources().getColor(R.color.black));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Trạng thái cuộn thay đổi (SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING)
            }
        });
    }
    private  void mapping(){
        btn_active = findViewById(R.id.layout_acitve);
        btn_upcoming = findViewById(R.id.layout_upcoming);
        btn_active.setBackgroundResource(R.drawable.custom_item_selected);
        btn_active.setTextColor(getResources().getColor(R.color.white));
        btn_upcoming.setBackgroundResource(R.drawable.custom_item_non_selected);
        btn_upcoming.setTextColor(getResources().getColor(R.color.black));
        viewPager = findViewById(R.id.viewPager);
        adapter = new MyPagerAdapter(getSupportFragmentManager(),allCarID+"");
        viewPager.setAdapter(adapter);
    }
    private void loadItemSelected() {
        btn_active.setOnClickListener(view -> {
            viewPager.setCurrentItem(0);
            System.out.println(allCarID+"aaaaaaaaaaaaa");
            btn_active.setBackgroundResource(R.drawable.custom_item_selected);
            btn_active.setTextColor(getResources().getColor(R.color.white));

            btn_upcoming.setBackgroundResource(R.drawable.custom_item_non_selected);
            btn_upcoming.setTextColor(getResources().getColor(R.color.black));
        });

        btn_upcoming.setOnClickListener(view -> {

            viewPager.setCurrentItem(1);

            btn_upcoming.setBackgroundResource(R.drawable.custom_item_selected);
            btn_upcoming.setTextColor(getResources().getColor(R.color.white));

            btn_active.setBackgroundResource(R.drawable.custom_item_non_selected);
            btn_active.setTextColor(getResources().getColor(R.color.black));
        });

    }

    private void  fect_data(){
      allCarID = new StringBuilder();
        RetrofitClient.FC_services().getListCar_ofUser("canhpnph23993@fpt.edu.vn","1").enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if(response.code()==200){
                    for(Car car : response.body()) {
                        if(response.body().size() > 0) {
                            allCarID.append(car.get_id()).append(",");
                        }
                    }
                    if(allCarID.length() > 0) {
                        allCarID.deleteCharAt(allCarID.length() - 1);
                    }
                }else {
                    System.out.println(response.code()+":"+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Lỗi : "+t);
            }
        });


    }

}