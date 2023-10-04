package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.fastcar.R;

public class ChiTietXe_Activity extends AppCompatActivity {
    AppCompatButton goThongtin;
    RelativeLayout ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);

        mapping();
        goThongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),ThongTinThue_Activity.class);
                startActivity(i);
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    void mapping() {
//        goThongtin = findViewById(R.id.btn_thuexe);
//        ic_back = findViewById(R.id.icon_back_in_chitietxe);
    }
}