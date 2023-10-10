package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.fastcar.Adapter.LichSuThueXeAdapter;
import com.example.fastcar.R;

import java.util.ArrayList;
import java.util.List;

public class LichSu_ThueXe_Activity extends AppCompatActivity {
    ImageView img_back;
    LichSuThueXeAdapter adapter;
    RecyclerView recyclerView;
    List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_thue_xe);

        mapping();

        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        adapter = new LichSuThueXeAdapter(lists, this);
        recyclerView.setAdapter(adapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    void mapping() {
        img_back = findViewById(R.id.icon_back_in_lsthuexe);
        recyclerView = findViewById(R.id.recyclerView_lsthuexe);
    }


}