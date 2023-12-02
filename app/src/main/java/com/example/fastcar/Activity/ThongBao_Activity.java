package com.example.fastcar.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Adapter.ThongBaoAdapter;
import com.example.fastcar.Model.ThongBao;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongBao_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    ThongBaoAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        mapping();
        load();
    }

    private void mapping() {
        recyclerView = findViewById(R.id.recyclerView_thongbao);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void load() {
        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String ngaythamgia = sdf.format(user.getNgayThamGia());
        RetrofitClient.FC_services().getThongbao(user.get_id(),ngaythamgia).enqueue(new Callback<List<ThongBao>>() {
            @Override
            public void onResponse(Call<List<ThongBao>> call, Response<List<ThongBao>> response) {
                if(response.code()==200){
                    adapter = new ThongBaoAdapter(ThongBao_Activity.this, response.body());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    System.out.println(response.code()+":"+response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ThongBao>> call, Throwable t) {
                System.out.println("lỗi : "+t);
            }
        });

    }

    public void back_in_ThongBaoACT(View view) {
        onBackPressed();
    }
}
