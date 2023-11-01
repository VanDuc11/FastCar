package com.example.fastcar.Activity.ThemXe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fastcar.Dialog.Dialog_Thoat_DangKy;
import com.example.fastcar.R;

public class GiaChoThue_Activity extends AppCompatActivity {
    TextView btn_tieptuc ;
    RelativeLayout btn_back, btn_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gia_cho_thue);

        mapping();
        load();

        btn_tieptuc.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), Upload_ImageXe_Activity.class)));
        btn_back.setOnClickListener(view -> onBackPressed());
        btn_close.setOnClickListener(view -> Dialog_Thoat_DangKy.showDialog(this, false));
    }

    private void mapping() {
        btn_tieptuc = findViewById(R.id.btn_tieptuc_in_giaChoThue);
        btn_back = findViewById(R.id.icon_back_in_giaChoThue);
        btn_close = findViewById(R.id.icon_close_in_giaChoThue);
    }

    private void load() {

    }
}