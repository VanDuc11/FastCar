package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.fastcar.R;

public class ChiTietHoaDon_Activity extends AppCompatActivity {
    RelativeLayout btn_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);

        mapping();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    void mapping() {
        btn_close = findViewById(R.id.btn_close_hoadonchitiet);
    }
}