package com.example.fastcar.Activity.act_bottom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fastcar.Activity.ChiTietHoaDon_Activity;
import com.example.fastcar.Adapter.DanhSachChuyenXeAdapter;
import com.example.fastcar.R;

import java.util.ArrayList;
import java.util.List;

public class ChuyenXe_Activity extends AppCompatActivity {
    RecyclerView recyclerView_chuyenXe;
    DanhSachChuyenXeAdapter adapter;
    List<String> listChuyenXe = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_xe);

        mapping();

        listChuyenXe.add("1");
        listChuyenXe.add("1");
        listChuyenXe.add("1");
        listChuyenXe.add("1");
        adapter = new DanhSachChuyenXeAdapter(listChuyenXe, this);
        recyclerView_chuyenXe.setAdapter(adapter);

    }

    void mapping() {
        recyclerView_chuyenXe = findViewById(R.id.recyclerView_chuyenxe);
    }

    public void tab2_to_tab1(View view) {
        startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
    }

    public void tab2_to_tab3(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    public void tab2_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
    }

    @Override
    public void onBackPressed() {
    }
}