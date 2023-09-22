package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fastcar.R;

public class KhamPha_Activity extends AppCompatActivity {
    TextView goDS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kham_pha);
        goDS = findViewById(R.id.txt_ds);
        goDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),DanhSachXe_Activity.class);
                startActivity(i);
            }
        });
    }
}