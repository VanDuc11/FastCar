package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Interface.SelectListener;
import com.example.fastcar.R;

import java.util.ArrayList;
import java.util.List;

public class DanhSachXe_Activity extends AppCompatActivity implements SelectListener {
    RecyclerView recyclerView;
    ImageView ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_xe);

        mapping();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> itemList = new ArrayList<>();
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");// Replace with your data
        DanhSachXeAdapter adapter = new DanhSachXeAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClicked(String aa) {
        Intent i = new Intent(getBaseContext(), ChiTietXe_Activity.class);
        startActivity(i);
    }

    void mapping() {
        recyclerView = findViewById(R.id.rcy_litsSP);
        ic_back = findViewById(R.id.icon_back_in_dsxe);
    }
}