package com.example.fastcar.Activity.act_bottom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fastcar.Activity.ChiTietHoaDon_Activity;
import com.example.fastcar.R;

public class ChuyenXe_Activity extends AppCompatActivity {
    LinearLayout item1, item2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_xe);

        mapping();

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ChiTietHoaDon_Activity.class));
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ChiTietHoaDon_Activity.class));
            }
        });
    }

    void mapping() {
        item1 = findViewById(R.id.item1_chuyenxe1);
        item2 = findViewById(R.id.item1_chuyenxe2);
    }

    public void tab3_to_tab1(View view) {
        startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
    }

    public void tab3_to_tab2(View view) {
        startActivity(new Intent(getBaseContext(), ThongBao_Activity.class));
    }

    public void tab3_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    public void tab3_to_tab5(View view) {
        startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
    }

    @Override
    public void onBackPressed() {
    }
}