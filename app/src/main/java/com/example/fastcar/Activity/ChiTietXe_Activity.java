package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fastcar.R;

public class ChiTietXe_Activity extends AppCompatActivity {
 AppCompatButton goThongtin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);
        goThongtin = findViewById(R.id.btn_thuexe);
        goThongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),ThongTinThue_Activity.class);
                startActivity(i);
            }
        });
    }
}