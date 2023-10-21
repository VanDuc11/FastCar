package com.example.fastcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.R;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHoder>{
    Context context;
    List<String> listThongBao;

    public ThongBaoAdapter(Context context, List<String> listThongBao) {
        this.context = context;
        this.listThongBao = listThongBao;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tv_tieude, tv_thoigian;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_tieude = itemView.findViewById(R.id.tv_noidung_thongbao);
            tv_thoigian = itemView.findViewById(R.id.time_thongbao);
        }
    }

    @NonNull
    @Override
    public ThongBaoAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item1_thongbao, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoAdapter.ViewHoder holder, int position) {
        String item = listThongBao.get(position);
    }

    @Override
    public int getItemCount() {
        return listThongBao.size();
    }


}
