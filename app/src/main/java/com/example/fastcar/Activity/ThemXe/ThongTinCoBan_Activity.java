package com.example.fastcar.Activity.ThemXe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fastcar.Dialog.Dialog_Thoat_DangKy;
import com.example.fastcar.R;

public class ThongTinCoBan_Activity extends AppCompatActivity {
    RelativeLayout ic_back, ic_close;
    TextView sl_soSan, sl_soTD, btn_nextScreen, sl_xang, sl_dau, sl_dien;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_co_ban);

        mapping();
        load();
        loadItemSelected();

        ic_back.setOnClickListener(view -> onBackPressed());

        ic_close.setOnClickListener(view -> Dialog_Thoat_DangKy.showDialog(this, false));

        btn_nextScreen.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), ThongTinChiTiet_Activity.class)));
    }

    private void mapping() {
        sl_soSan = findViewById(R.id.sl_soSan);
        sl_soTD = findViewById(R.id.sl_soTuDong);
        btn_nextScreen = findViewById(R.id.btn_continue_ttcb_inThemXe);
        ic_back = findViewById(R.id.icon_back_in_thongtincoban);
        ic_close = findViewById(R.id.icon_close_in_thongtincoban);
        sl_xang = findViewById(R.id.sl_xang);
        sl_dau = findViewById(R.id.sl_dau);
        sl_dien = findViewById(R.id.sl_dien);
    }

    private void load() {
        sl_soSan.setBackgroundResource(R.drawable.custom_item_selected);
        sl_soSan.setTextColor(getResources().getColor(R.color.white));

        sl_xang.setBackgroundResource(R.drawable.custom_item_selected);
        sl_xang.setTextColor(getResources().getColor(R.color.white));

    }

    private void loadItemSelected() {
        sl_soSan.setOnClickListener(view -> {
            sl_soSan.setBackgroundResource(R.drawable.custom_item_selected);
            sl_soSan.setTextColor(getResources().getColor(R.color.white));

            sl_soTD.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_soTD.setTextColor(getResources().getColor(R.color.black));
        });

        sl_soTD.setOnClickListener(view -> {
            sl_soSan.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_soSan.setTextColor(getResources().getColor(R.color.black));

            sl_soTD.setBackgroundResource(R.drawable.custom_item_selected);
            sl_soTD.setTextColor(getResources().getColor(R.color.white));
        });

        sl_xang.setOnClickListener(view -> {
            sl_xang.setBackgroundResource(R.drawable.custom_item_selected);
            sl_xang.setTextColor(getResources().getColor(R.color.white));

            sl_dau.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_dau.setTextColor(getResources().getColor(R.color.black));

            sl_dien.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_dien.setTextColor(getResources().getColor(R.color.black));
        });

        sl_dau.setOnClickListener(view -> {
            sl_xang.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_xang.setTextColor(getResources().getColor(R.color.black));

            sl_dien.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_dien.setTextColor(getResources().getColor(R.color.black));

            sl_dau.setBackgroundResource(R.drawable.custom_item_selected);
            sl_dau.setTextColor(getResources().getColor(R.color.white));
        });

        sl_dien.setOnClickListener(view -> {
            sl_xang.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_xang.setTextColor(getResources().getColor(R.color.black));

            sl_dau.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_dau.setTextColor(getResources().getColor(R.color.black));

            sl_dien.setBackgroundResource(R.drawable.custom_item_selected);
            sl_dien.setTextColor(getResources().getColor(R.color.white));
        });
    }

}