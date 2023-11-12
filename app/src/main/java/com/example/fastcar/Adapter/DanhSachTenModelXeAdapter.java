package com.example.fastcar.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Model.HangXe.HangXe;
import com.example.fastcar.Model.MauXe.MauXe;
import com.example.fastcar.R;

import java.util.List;

public class DanhSachTenModelXeAdapter extends RecyclerView.Adapter<DanhSachTenModelXeAdapter.ViewHolder>{
    private List<MauXe> mauXeList;
    private Context context;
    private TextView setText;
    private Dialog dialog;

    public DanhSachTenModelXeAdapter(List<MauXe> mauXeList, Context context, TextView setText, Dialog dialog) {
        this.mauXeList = mauXeList;
        this.context = context;
        this.setText = setText;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_ten_hang_xe, parent, false);
        return new DanhSachTenModelXeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MauXe mauXe = mauXeList.get(position);
        holder.tv_ten_hang_xe.setText(mauXe.getName());
        holder.item.setOnClickListener(view -> {
            setText.setText(mauXe.getName());
            dialog.dismiss();

        });
    }

    @Override
    public int getItemCount() {
        return mauXeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ten_hang_xe;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten_hang_xe=itemView.findViewById(R.id.tv_ten_hang_xe);
            item = itemView.findViewById(R.id.item_ten_hang);


        }

    }
}
