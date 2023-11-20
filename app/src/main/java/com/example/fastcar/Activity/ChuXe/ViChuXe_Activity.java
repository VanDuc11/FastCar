package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ViChuXe_Activity extends AppCompatActivity {
    RelativeLayout btnBack;
    String emailUser;
    User user;
    TextView tvSoDu, btnRutTien, btnLSGD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_chu_xe);

        mapping();
        load();

        btnBack.setOnClickListener(view -> onBackPressed());
        btnRutTien.setOnClickListener(view -> {});
        btnLSGD.setOnClickListener(view -> {});
    }

    private void mapping() {
        btnBack = findViewById(R.id.icon_back_in_vichuxe);
        tvSoDu = findViewById(R.id.tv_sodu_vichuxe);
        btnRutTien = findViewById(R.id.btn_RutTien_vichuxe);
        btnLSGD = findViewById(R.id.btn_LSGD_vichuxe);
    }

    private void load() {
        Intent intent = getIntent();
        emailUser = intent.getStringExtra("emailUser");
        fetchData_UserLogin(emailUser);

    }

    private void fetchData_UserLogin(String emailUser) {
        RetrofitClient.FC_services().getListUser(emailUser).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                List<User> list = response.body();
                user = list.get(0);
                tvSoDu.setText(NumberFormatVND.format(user.getSoDu()));

                if(user.getSoDu() == 0) {
                    btnRutTien.setEnabled(false);
                    btnRutTien.setBackgroundResource(R.drawable.disable_custom_btn4);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch user có email: " + emailUser + " --- " + t);
            }
        });
    }
}