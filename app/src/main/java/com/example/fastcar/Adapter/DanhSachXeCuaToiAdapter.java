package com.example.fastcar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.ChuXe.ChiTietXeCuaToi_Activity;
import com.example.fastcar.Activity.KhachHang.ChiTietXe_Activity;
import com.example.fastcar.FormatString.NumberFormatK;
import com.example.fastcar.Model.Car;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.DecimalFormat;
import java.util.List;

public class DanhSachXeCuaToiAdapter extends RecyclerView.Adapter<DanhSachXeCuaToiAdapter.ViewHolder> {
    Context context;
    List<Car> listCars;

    public DanhSachXeCuaToiAdapter(Context context, List<Car> listCars) {
        this.context = context;
        this.listCars = listCars;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imgXe;
        TextView tvTenXe, tvGiaXe, tvSoChuyen, tvSoSao, tvdiachixe, tvTrangThaiXe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgXe = itemView.findViewById(R.id.img_xe_inItem_XeCuaToi);
            tvTenXe = itemView.findViewById(R.id.tv_tenxe_inItem_XeCuaToi);
            tvSoSao = itemView.findViewById(R.id.tv_soSao_inItem_XeCuaToi);
            tvSoChuyen = itemView.findViewById(R.id.tv_soChuyen_inItem_XeCuaToi);
            tvdiachixe = itemView.findViewById(R.id.tv_diachixe_inItem_XeCuaToi);
            tvGiaXe = itemView.findViewById(R.id.tv_giaThue_inItem_XeCuaToi);
            tvTrangThaiXe = itemView.findViewById(R.id.tv_trangthaixe_inItem_XeCuaToi);
        }
    }


    @NonNull
    @Override
    public DanhSachXeCuaToiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_dsxe_cuatoi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachXeCuaToiAdapter.ViewHolder holder, int position) {
        Car car = listCars.get(position);
        int status = car.getTrangThai();
        //0: chờ duyệt
        //1: đang hoạt động = duyệt thành công
        //2: từ chối
        //3: không hoạt động
        //4: vô hiệu hoá

        if (status == 0) {
            holder.tvTrangThaiXe.setText("Chờ duyệt");
        } else if (status == 1) {
            holder.tvTrangThaiXe.setText("Đang hoạt động");
        } else if (status == 2) {
            holder.tvTrangThaiXe.setText("Bị từ chối");
        } else if(status == 3){
            holder.tvTrangThaiXe.setText("Không hoạt động");
        } else {
            holder.tvTrangThaiXe.setText("Vô hiệu hoá");
        }


        Glide.with(context).load(HostApi.URL_Image + car.getHinhAnh().get(0)).into(holder.imgXe);

        holder.tvTenXe.setText(car.getMauXe());
        holder.tvdiachixe.setText(car.getDiaChiXe());

        int soChuyen = car.getSoChuyen();
        float trungbinhSao = car.getTrungBinhSao();

        if (soChuyen == 0) {
            holder.tvSoChuyen.setText("Chưa có chuyến");
            holder.tvSoSao.setVisibility(View.GONE);
        } else {
            holder.tvSoChuyen.setText(soChuyen + " chuyến");
            holder.tvSoSao.setVisibility(View.VISIBLE);
        }

        if (trungbinhSao > 0) {
            DecimalFormat df = new DecimalFormat("0.#");
            String formattedNumber = df.format(trungbinhSao);
            formattedNumber = formattedNumber.replace(",", ".");

            holder.tvSoSao.setVisibility(View.VISIBLE);
            holder.tvSoSao.setText(formattedNumber);
        } else {
            holder.tvSoSao.setVisibility(View.GONE);
        }

        holder.tvGiaXe.setText(NumberFormatK.format(car.getGiaThue1Ngay()));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChiTietXeCuaToi_Activity.class);
            intent.putExtra("car", car);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listCars.size();
    }


}
