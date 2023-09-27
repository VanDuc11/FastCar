package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.R;

public class ThongTinTruocDatCoc_Activity extends AppCompatActivity {
    AppCompatButton btn_datcoc, btn_huychuyen;
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
    }

    void mapping() {
        btn_datcoc = findViewById(R.id.btn_datcoc);
        btn_huychuyen = findViewById(R.id.btn_huychuyen);
    }
}