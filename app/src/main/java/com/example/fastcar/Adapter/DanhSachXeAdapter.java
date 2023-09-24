package com.example.fastcar.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Interface.SelectListener;
import com.example.fastcar.R;
import com.example.fastcar.ViewHodelder.DanhSachXeHolder;

import java.util.List;

public class DanhSachXeAdapter extends RecyclerView.Adapter<DanhSachXeHolder>{
    private List<String> itemList;
    private SelectListener listener;


    public DanhSachXeAdapter(List<String> itemList, SelectListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DanhSachXeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item1_danhsachxe, parent, false);
        return new DanhSachXeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachXeHolder holder, int position) {
        String item = itemList.get(position);
        holder.name_xe.setText(item);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
