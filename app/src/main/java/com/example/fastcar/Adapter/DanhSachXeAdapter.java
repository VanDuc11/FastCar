package com.example.fastcar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fastcar.Activity.ChiTietXe_Activity;
import com.example.fastcar.R;

import java.util.List;

public class DanhSachXeAdapter extends RecyclerView.Adapter<DanhSachXeAdapter.ViewHolder> {
    private List<String> itemList;
    private Context context;


    public DanhSachXeAdapter(List<String> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item1_danhsachxe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachXeAdapter.ViewHolder holder, int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, ChiTietXe_Activity.class));
            }
        });

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
