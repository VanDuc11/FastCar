package com.example.fastcar.Activity.ThemXe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fastcar.R;

public class TinhNangXe_Activity extends AppCompatActivity {
    TextView btn_tieptuc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinh_nang_xe);
        btn_tieptuc = findViewById(R.id.btn_tieptuc);
        btn_tieptuc.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), ImageXe_Activity.class)));
    }
}