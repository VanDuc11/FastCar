package com.example.fastcar.Activity.act_bottom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastcar.Activity.DanhSachXe_Activity;
import com.example.fastcar.R;

public class KhamPha_Activity extends AppCompatActivity {
    TextView goDS;
    LinearLayout item1, item2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kham_pha);

        mapping();

        goDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), DanhSachXe_Activity.class);
                startActivity(i);
            }
        });

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate_();
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate_();
            }
        });
    }

    void mapping() {
        goDS = findViewById(R.id.txt_ds);
        item1 = findViewById(R.id.item1_dsxe);
        item2 = findViewById(R.id.item2_dsxe);
    }

    void navigate_() {
        startActivity(new Intent(getBaseContext(), DanhSachXe_Activity.class));
    }

    public void tab1_to_tab2(View view) {
        startActivity(new Intent(getBaseContext(), ThongBao_Activity.class));
    }

    public void tab1_to_tab3(View view) {
        startActivity(new Intent(getBaseContext(), ChuyenXe_Activity.class));
    }

    public void tab1_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    public void tab1_to_tab5(View view) {
        startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
    }

    @Override
    public void onBackPressed() {

    }
}