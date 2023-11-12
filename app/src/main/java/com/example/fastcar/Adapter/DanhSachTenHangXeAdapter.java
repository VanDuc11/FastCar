package com.example.fastcar.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Model.HangXe.HangXe;
import com.example.fastcar.R;

import java.util.List;

public class DanhSachTenHangXeAdapter extends RecyclerView.Adapter<DanhSachTenHangXeAdapter.ViewHolder>{
    private List<HangXe> hangXeList;
    private Context context;
    private TextView setText,make_id;

    private Dialog dialog;

    public DanhSachTenHangXeAdapter(List<HangXe> hangXeList, Context context, TextView setText, TextView make_id, Dialog dialog) {
        this.hangXeList = hangXeList;
        this.context = context;
        this.setText = setText;
        this.make_id = make_id;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_ten_hang_xe, parent, false);
        return new DanhSachTenHangXeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HangXe hangXe = hangXeList.get(position);
        holder.tv_ten_hang_xe.setText(hangXe.getName());
        holder.item.setOnClickListener(view -> {
          setText.setText(hangXe.getName());
          make_id.setText(String.valueOf(hangXe.getId()));
            dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return hangXeList.size();
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
