package com.example.fastcar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Activity.ChiTietHoaDon_Activity;
import com.example.fastcar.R;

import java.util.List;

public class DanhSachChuyenXeAdapter extends RecyclerView.Adapter<DanhSachChuyenXeAdapter.ViewHolder> {

    List<String> listChuyenXe;
    Context context;

    public DanhSachChuyenXeAdapter(List<String> listChuyenXe, Context context) {
        this.listChuyenXe = listChuyenXe;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public DanhSachChuyenXeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item1_chuyenxe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachChuyenXeAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, ChiTietHoaDon_Activity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChuyenXe.size();
    }


}
