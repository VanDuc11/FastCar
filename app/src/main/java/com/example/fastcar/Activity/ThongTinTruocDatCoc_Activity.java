package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.R;

public class ThongTinTruocDatCoc_Activity extends AppCompatActivity {
    AppCompatButton btn_datcoc, btn_huychuyen;
    LinearLayout ChitetGia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_truoc_dat_coc);

        mapping();


        btn_datcoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ThanhToan_Activity.class));
            }
        });

        btn_huychuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
            }
        });
        ChitetGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(ThongTinTruocDatCoc_Activity.this);
                View custom = inflater.inflate(R.layout.dialog_giachitiet, null);
                Dialog dialog = new Dialog(ThongTinTruocDatCoc_Activity.this);
                dialog.setContentView(custom);
                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }
                // set kích thước dialog
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // set vị trí dialog
                WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = gravity;
                window.setAttributes(windowAttributes);
                dialog.show();
            }
        });
    }

    @SuppressLint("WrongViewCast")
    void mapping() {
        btn_datcoc = findViewById(R.id.btn_datcoc);
        btn_huychuyen = findViewById(R.id.btn_huychuyen);
        ChitetGia = findViewById(R.id.act_ttt_datCoc_chitietGia);
    }
}