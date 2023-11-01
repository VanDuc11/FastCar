package com.example.fastcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.R;

import java.util.ArrayList;

public class TinhNangXeAdpater extends RecyclerView.Adapter<TinhNangXeAdpater.ViewHolder>{
    ArrayList<String> list;
    Context context;

    public TinhNangXeAdpater(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenTinhNang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tenTinhNang = itemView.findViewById(R.id.tv_tenTinhNang_inItem);
        }
    }

    @NonNull
    @Override
    public TinhNangXeAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_tinhnang_xe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TinhNangXeAdpater.ViewHolder holder, int position) {
        final boolean[] isSelected = {false};
        String tinhnang = list.get(position);
        holder.tv_tenTinhNang.setText(tinhnang);

        holder.itemView.setOnClickListener(view -> {
            if(isSelected[0]) {
                holder.itemView.setBackgroundResource(R.drawable.custom_border_item_tinhnangxe_non_selected);
                isSelected[0] = false;
            } else {
                holder.itemView.setBackgroundResource(R.drawable.custom_border_item_tinhnangxe_selected);
                isSelected[0] = true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
