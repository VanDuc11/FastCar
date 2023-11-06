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
import com.example.fastcar.Activity.HoaDon_Activity;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.List;

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

        holder.tv_tenxe.setText(hoaDon.getXe().getMauXe());
        holder.tv_ngayNhan.setText("Bắt đầu: " + hoaDon.getNgayThue());
        holder.tv_ngayTra.setText("Kết thúc: " + hoaDon.getNgayTra());
        holder.tvTongTien.setText(NumberFormatVND.format(hoaDon.getTongTien()));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String formattedDate = dateFormat.format(hoaDon.getGioTaoHD());
        holder.tvTime.setText(formattedDate);

        int statusCode = hoaDon.getTrangThaiHD();
        // 0: đã huỷ
        // 1: chưa cọc
        // 2: đã cọc = chưa nhận xe
        // 3: đang vận hành
        // 4: hoàn thành đơn
        if (statusCode == 0) {
            holder.imgStatus.setImageResource(R.drawable.icon_huychuyen);
            holder.tvStatus.setText("Đã huỷ");
        } else {
            holder.imgStatus.setImageResource(R.drawable.icon_hoanthanh);
            holder.tvStatus.setText("Đã hoàn thành");
        }
    }

    @Override
    public int getItemCount() {
        return listHoaDon.size();
    }

}
