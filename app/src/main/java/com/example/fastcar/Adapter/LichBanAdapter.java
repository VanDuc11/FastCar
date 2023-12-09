package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.R;

import java.util.List;

public class LichBanAdapter extends RecyclerView.Adapter<LichBanAdapter.ViewHolder> {
    List<String> lists;
    Context context;

    public LichBanAdapter(List<String> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_time_inItem_lichban);
        }
    }

    @NonNull
    @Override
    public LichBanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item1_lichban, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LichBanAdapter.ViewHolder holder, int position) {
        String[] dates = lists.get(position).split(" - ");
        holder.tv.setText("Từ 00:00 ngày " + dates[0] + "\n" + "đến 23:59 ngày " + dates[1]);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

}
