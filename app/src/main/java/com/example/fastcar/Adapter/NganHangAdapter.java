package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Model.NganHang;
import com.example.fastcar.R;

import java.text.DecimalFormat;
import java.util.List;

public class NganHangAdapter extends RecyclerView.Adapter<NganHangAdapter.ViewHolder> {
    List<NganHang> lists;
    Context context;

    public NganHangAdapter(Context context, List<NganHang> lists) {
        this.lists = lists;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenNH, tv_chuTK, tv_stk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tenNH = itemView.findViewById(R.id.tv_tenNganHang);
            tv_chuTK = itemView.findViewById(R.id.tv_tenChuTK_NganHang);
            tv_stk = itemView.findViewById(R.id.tv_STK_NganHang);
        }
    }


    @NonNull
    @Override
    public NganHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_thenganhang, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NganHangAdapter.ViewHolder holder, int position) {
        NganHang nganHang = lists.get(position);

        holder.tv_tenNH.setText(nganHang.getTenNH());
        holder.tv_chuTK.setText(nganHang.getTenChuTK());
        holder.tv_stk.setText(formatNumber(nganHang.getSoTK()));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public String formatNumber(String number) {
        StringBuilder formatted = new StringBuilder();
        int length = number.length();
        for (int i = 0; i < length; i += 4) {
            int endIndex = Math.min(i + 4, length);
            formatted.append(number, i, endIndex);
            if (endIndex < length) {
                formatted.append(" ");
            }
        }
        return formatted.toString();
    }

}
