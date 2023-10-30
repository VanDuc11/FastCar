package com.example.fastcar.Activity.ThemXe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fastcar.R;

public class ThemXe_Activity extends AppCompatActivity {
    RelativeLayout img_back,img_add;
    TextView btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_them_xe);
        mapping();
        img_back.setOnClickListener(view -> onBackPressed());
        img_add.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), ThongTinCoBan_Activity.class)));
        btn_add.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), ThongTinCoBan_Activity.class)) );
    }
    private void mapping() {
        img_back = findViewById(R.id.icon_back_in_themxe);
        img_add = findViewById(R.id.icon_add_in_themxe);
        btn_add = findViewById(R.id.btn_themxe);
    }
}