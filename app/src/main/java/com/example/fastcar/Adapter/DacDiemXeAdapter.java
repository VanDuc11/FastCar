package com.example.fastcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.R;

import java.util.List;

public class DacDiemXeAdapter  extends RecyclerView.Adapter<DacDiemXeAdapter.ViewHoder>{
    Context context;
    List<String> list;

    public DacDiemXeAdapter (Context context, List<String> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_dacdiemxe,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class  ViewHoder extends RecyclerView.ViewHolder{
        TextView tvTen,tvLoai;
        ImageView img;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvLoai = itemView.findViewById(R.id.layout_item_dacdiem_tvLoai);
            tvTen = itemView.findViewById(R.id.layout_item_dacdiem_tvTen);
            img = itemView.findViewById(R.id.layout_item_dacdiem_Img);
        }
    }
}
