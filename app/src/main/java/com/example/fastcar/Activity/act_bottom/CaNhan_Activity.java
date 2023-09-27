package com.example.fastcar.Activity.act_bottom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastcar.Activity.LichSu_ThueXe_Activity;
import com.example.fastcar.Activity.ThongTin_User_Activity;
import com.example.fastcar.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CaNhan_Activity extends AppCompatActivity {
    LinearLayout btnInfoUser, btnLogout, btnVoucher, btnLichSuThueXe, btnDeleteAccount;
    CircleImageView avt_user;
    TextView tv_name_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ca_nhan);

        mapping();

        // Thông tin cá nhân
        btnInfoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ThongTin_User_Activity.class));
            }
        });

        // Voucher

        // Lịch sử thuê xe
        btnLichSuThueXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), LichSu_ThueXe_Activity.class));
            }
        });

        // Đăng xuất

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void mapping() {
        btnInfoUser = findViewById(R.id.btn_info_user);
        btnVoucher = findViewById(R.id.btn_voucher);
        btnLichSuThueXe = findViewById(R.id.btn_lichsu_thuexe);
        btnLogout = findViewById(R.id.btn_logout);
        btnDeleteAccount = findViewById(R.id.btn_delete_account);
        avt_user = findViewById(R.id.avt_user_in_user);
        tv_name_user = findViewById(R.id.tv_name_user_in_user);
    }

    public void tab5_to_tab1(View view) {
        startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
    }

    public void tab5_to_tab2(View view) {
        startActivity(new Intent(getBaseContext(), ThongBao_Activity.class));
    }

    public void tab5_to_tab3(View view) {
        startActivity(new Intent(getBaseContext(), ChuyenXe_Activity.class));
    }

    public void tab5_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    @Override
    public void onBackPressed() {
    }
}