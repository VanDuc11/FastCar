package com.example.fastcar.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Adapter.ThongBaoAdapter;
import com.example.fastcar.R;

import java.util.ArrayList;
import java.util.List;

public class ThongBao_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    ThongBaoAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        mapping();
        load();
    }

    private void mapping() {
        recyclerView = findViewById(R.id.recyclerView_thongbao);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void load() {
        List<String> list = new ArrayList<>();
        list.add("demo");
        list.add("demo");
        list.add("demo");
        list.add("demo");
        list.add("demo");
        list.add("demo");
        list.add("demo");
        list.add("demo");

        adapter = new ThongBaoAdapter(ThongBao_Activity.this, list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void back_in_ThongBaoACT(View view) {
        onBackPressed();
    }
}
