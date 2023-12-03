package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
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
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LichSuThueXeAdapter extends RecyclerView.Adapter<LichSuThueXeAdapter.ViewHolder> {
    List<HoaDon> listHoaDon;
    Context context;
    public LichSuThueXeAdapter(Context context, List<HoaDon> listHoaDon) {
        this.listHoaDon = listHoaDon;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView img_xe;
        TextView tv_tenxe, tv_ngayNhan, tv_ngayTra, tvTongTien, tvStatus, tvTime;
        ImageView imgStatus;

        public ViewHolder(@NonNull View view) {
            super(view);
            img_xe = view.findViewById(R.id.img_xe_inLSThueXe);
            tv_tenxe = view.findViewById(R.id.tv_tenxe_inLSThueXe);
            tv_ngayNhan = view.findViewById(R.id.tv_ngayNhanXe_inLSThueXe);
            tv_ngayTra = view.findViewById(R.id.tv_ngayTraXe_inLSThueXe);
            tvTongTien = view.findViewById(R.id.tv_tongTien_inLSThueXe);
            imgStatus = view.findViewById(R.id.icon_status_inLSThueXe);
            tvStatus = view.findViewById(R.id.tv_status_inLSThueXe);
            tvTime = view.findViewById(R.id.tv_time_inLSThueXe);
        }
    }

    @NonNull
    @Override
    public LichSuThueXeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item1_lichsuthuexe, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LichSuThueXeAdapter.ViewHolder holder, int position) {
        HoaDon hoaDon = listHoaDon.get(position);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, HoaDon_Activity.class);
            intent.putExtra("hoadon", hoaDon);
            view.getContext().startActivity(intent);
        });

        Glide.with(context)
                .load(HostApi.URL_Image + hoaDon.getXe().getHinhAnh().get(0))
                .into(holder.img_xe);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tv_tenxe.setText(hoaDon.getXe().getMauXe());
        holder.tv_ngayNhan.setText("Bắt đầu: " + sdf.format(hoaDon.getNgayThue()));
        holder.tv_ngayTra.setText("Kết thúc: " + sdf.format(hoaDon.getNgayTra()));
        holder.tvTongTien.setText(NumberFormatVND.format(hoaDon.getTongTien()));

        holder.tvTime.setText(getTimeDifference(hoaDon.getGioTaoHD()));

        int statusCode = hoaDon.getTrangThaiHD();
        // 0: đã huỷ
        // 1: chưa cọc
        // 2: đã cọc = chưa nhận xe
        // 3: đang vận hành
        // 4: hoàn thành đơn
        if (statusCode == 0) {
            holder.imgStatus.setImageResource(R.drawable.icon_huychuyen);
            holder.tvStatus.setText("Đã bị huỷ");
        } else {
            holder.imgStatus.setImageResource(R.drawable.icon_hoanthanh);
            holder.tvStatus.setText("Đã kết thúc");
        }
    }

    @Override
    public int getItemCount() {
        return listHoaDon.size();
    }

    private static String getTimeDifference(Date endDate) {
        Date startDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        long duration = Math.abs(startDate.getTime() - endDate.getTime());
        long minutes = duration / (60 * 1000);
        long hours = 0;
        if (minutes > 60) {
            hours = minutes / 60;
            minutes = minutes % 60;
        }

        int day = (int) (hours / 24);
        int week = (int) (day / 7);

        if (hours <= 1) {
            if (minutes < 1) {
                return duration / 1000 + " giây trước";
            } else {
                return minutes + " phút trước";
            }
        }
        if (hours < 24) {
            return hours + " giờ trước";
        } else if (day <= 7) {
            return (hours >= 48) ? day + " ngày trước" : "hôm qua";
        } else {
            return (week <= 4) ? week + " tuần trước" : sdf.format(endDate);
        }
    }

}
