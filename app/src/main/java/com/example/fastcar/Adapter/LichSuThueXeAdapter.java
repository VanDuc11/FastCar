package com.example.fastcar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Activity.ChiTietHoaDon_Activity;
import com.example.fastcar.R;

import java.util.List;

public class LichSuThueXeAdapter extends RecyclerView.Adapter<LichSuThueXeAdapter.ViewHolder> {

    List<String> listXeDaThue;
    Context context;

    public LichSuThueXeAdapter(List<String> listXeDaThue, Context context) {
        this.listXeDaThue = listXeDaThue;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public LichSuThueXeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item1_lichsuthuexe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichSuThueXeAdapter.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context, ChiTietHoaDon_Activity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listXeDaThue.size();
    }


}
