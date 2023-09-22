package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Interface.SelectListener;
import com.example.fastcar.R;

import java.util.ArrayList;
import java.util.List;

public class DanhSachXe_Activity extends AppCompatActivity  implements SelectListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_xe);
        recyclerView =findViewById(R.id.rcy_litsSP);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> itemList = new ArrayList<>();
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");// Replace with your data
        DanhSachXeAdapter adapter = new DanhSachXeAdapter(itemList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(String aa) {
        Intent i = new Intent(getBaseContext(), ChiTietXe_Activity.class);
        startActivity(i);
    }
}