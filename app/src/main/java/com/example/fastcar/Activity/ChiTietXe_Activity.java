package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fastcar.Adapter.DacDiemXeAdapter;
import com.example.fastcar.Adapter.NhanXetAdapter;
import com.example.fastcar.Adapter.TinhNangAdapter;
import com.example.fastcar.R;

import java.util.ArrayList;
import java.util.List;

public class ChiTietXe_Activity extends AppCompatActivity {
    TextView goThongtin;
    ImageView ic_back;
    RecyclerView reyDacDiem,reyTinhNang,reyNhanXet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);

        mapping();
        load();
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
        goThongtin = findViewById(R.id.btn_thuexe);
        ic_back = findViewById(R.id.icon_back_in_dsxe);
        reyDacDiem = findViewById(R.id.act_chitietxe_reyDacDiem);
        reyTinhNang = findViewById(R.id.act_chitietxe_reyTinhNang);
        reyNhanXet = findViewById(R.id.act_chitietxe_reyNhanXet);
    }
    void load(){
        reyDacDiem.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        List<String> itemList = new ArrayList<>();
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");// Replace with your data
        DacDiemXeAdapter adapter = new DacDiemXeAdapter(this, itemList);
        reyDacDiem.setAdapter(adapter);

        TinhNangAdapter adapter1 = new TinhNangAdapter(this,itemList);
        reyTinhNang.setLayoutManager(new GridLayoutManager(this,2));
        reyTinhNang.setAdapter(adapter1);

        NhanXetAdapter adapter2 = new NhanXetAdapter(this,itemList);
        reyNhanXet.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        reyNhanXet.setAdapter(adapter2);

    }
}