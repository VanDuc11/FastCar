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
import com.example.fastcar.Activity.ChuXe.HoaDon_ChuSH_Activity;
import com.example.fastcar.Activity.KhachHang.HoaDon_Activity;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChuyenXeChuSHAdapter extends RecyclerView.Adapter<ChuyenXeChuSHAdapter.ViewHolder>  {
    List<HoaDon> listHoaDon;
    Context context;

    public ChuyenXeChuSHAdapter(Context context, List<HoaDon> listHoaDon) {
        this.listHoaDon = listHoaDon;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView img_xe;
        TextView tv_tenxe, tv_ngayNhan, tv_ngayTra, tvTongTien, tvStatus, tvTime;
        ImageView imgStatus;
        public ViewHolder(@NonNull View view) {
            super(view);
            img_xe = view.findViewById(R.id.img_xe_inChuyenXeChuSH);
            tv_tenxe = view.findViewById(R.id.tv_tenxe_inChuyenXeChuSH);
            tv_ngayNhan = view.findViewById(R.id.tv_ngayNhanXe_inChuyenXeChuSH);
            tv_ngayTra = view.findViewById(R.id.tv_ngayTraXe_inChuyenXeChuSH);
            tvTongTien = view.findViewById(R.id.tv_tongTien_inChuyenXeChuSH);
            imgStatus = view.findViewById(R.id.icon_status_inChuyenXeChuSH);
            tvStatus = view.findViewById(R.id.tv_status_inChuyenXeChuSH);
            tvTime = view.findViewById(R.id.tv_time_inChuyenXeChuSH);
        }
    }

    @NonNull
    @Override
    public ChuyenXeChuSHAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item1_chuyenxe_chush, parent, false);
        return new ChuyenXeChuSHAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChuyenXeChuSHAdapter.ViewHolder holder, int position) {
        HoaDon hoaDon = listHoaDon.get(position);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, HoaDon_ChuSH_Activity.class);
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
        // 0: bị huỷ
        // 1: chờ chủ xe duyệt
        // 2: duyệt thành công, chờ đặt cọc
        // 3: đặt cọc thành công
        // 4: chủ xe giao xe thành công
        // 5: (hết time thuê, khách mang xe trả cho chủ) khách hàng trả xe thành công
        // 6: chủ xe nhận xe thành công ( đối chiếu nếu cần thiết )
        // 7: hoàn thành chuyến

        if (statusCode == 0) {
            holder.imgStatus.setImageResource(R.drawable.icon_huychuyen);
            holder.tvStatus.setText("Đã bị huỷ");
        } else if (statusCode == 1) {
            holder.imgStatus.setImageResource(R.drawable.icon_time_green);
            holder.tvStatus.setText("Chờ bạn duyệt");
        } else if (statusCode == 2) {
            holder.imgStatus.setImageResource(R.drawable.icon_time_green);
            holder.tvStatus.setText("Chờ khách đặt cọc");
        } else if (statusCode == 3) {
            holder.imgStatus.setImageResource(R.drawable.icon_dadatcoc);
            holder.tvStatus.setText("Khách đã đặt cọc");
        } else if (statusCode == 7){
            holder.imgStatus.setImageResource(R.drawable.icon_hoanthanh);
            holder.tvStatus.setText("Đã kết thúc");
        } else {
            holder.imgStatus.setImageResource(R.drawable.icon_car_v1_green_25x25);
            holder.tvStatus.setText("Đang vận hành");
        }
    }

    @Override
    public int getItemCount() {
        return listHoaDon.size();
    }


}
