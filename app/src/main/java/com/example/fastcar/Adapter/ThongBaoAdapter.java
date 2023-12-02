package com.example.fastcar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.KhachHang.HoaDon_Activity;
import com.example.fastcar.Model.ThongBao;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHoder>{
    Context context;
    List<ThongBao> listThongBao;
    public ThongBaoAdapter(Context context, List<ThongBao> listThongBao) {
        this.context = context;
        this.listThongBao = listThongBao;
    }
    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tv_tieude, tv_thoigian;
        ImageView img_thongbao; 
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_tieude = itemView.findViewById(R.id.tieude_thongbao);
            tv_thoigian = itemView.findViewById(R.id.time_thongbao);
            img_thongbao = itemView.findViewById(R.id.img_thongbao);
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
        ThongBao item = listThongBao.get(position);
        holder.tv_tieude.setText( item.getTieuDe());
        Glide.with(context)
                .load(HostApi.URL_Image + item.getHinhAnh())
                .into(holder.img_thongbao);
        holder.tv_thoigian.setText(date(item.getCreatedAt()));
        holder.itemView.setOnClickListener(view -> {

        });
    }
    @Override
    public int getItemCount() {
        return listThongBao.size();
    }
private String date(Date date)  {
    try {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strTargetTime = sdf.format(date);
        Date targetTime = sdf.parse(strTargetTime);
        return getTimeDifference(currentDate,targetTime)+" trước";
    } catch (ParseException e) {
        e.printStackTrace();
        return e.toString();
    }
}
   private static String getTimeDifference(Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long duration = startDate.getTime()-endDate.getTime() ;
        long minutes = duration / (60 * 1000);
        long hours = minutes / 60;
        if(hours>1&&hours<48){
            if(hours>24){
                return hours/24 + " ngày ";
            }else {
                return hours + " giờ " ;
            }
        } else if (hours>48) {
            return sdf.format(startDate);
        } else {
            return (minutes % 60) + " phút";
        }
    }
}