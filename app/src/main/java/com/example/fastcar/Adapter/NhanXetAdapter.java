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

public class NhanXetAdapter extends RecyclerView.Adapter<NhanXetAdapter.ViewHoder>{
    Context context;
    List<String> list;

    public NhanXetAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nhanxet,parent,false);
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
        TextView tvTen,tvSao,tvDate;
        ImageView img;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.layout_item_nhanXet_tvName);
            tvSao = itemView.findViewById(R.id.layout_item_nhanXet_tvSao);
            tvDate = itemView.findViewById(R.id.layout_item_nhanXet_tvDate);
            img = itemView.findViewById(R.id.layout_item_nhanXet_img);
        }
    }
}
