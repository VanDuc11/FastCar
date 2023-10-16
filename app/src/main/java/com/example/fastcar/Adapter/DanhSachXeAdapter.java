package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.ChiTietXe_Activity;
import com.example.fastcar.Model.Car;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.net.URL;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class DanhSachXeAdapter extends RecyclerView.Adapter<DanhSachXeAdapter.ViewHolder> {
    private List<Car> listXe;
    private Context context;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    PhotoAdapter photoAdapter;


    public DanhSachXeAdapter(Context context, List<Car> listXe) {
        this.listXe = listXe;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenXe, tv_soSao, tv_soChuyen, tv_truyenDong;
        ConstraintLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_soChuyen = itemView.findViewById(R.id.tv_soChuyen_inItem);
            tv_tenXe = itemView.findViewById(R.id.tv_tenxe_inItem);
            tv_soSao = itemView.findViewById(R.id.tv_soSao_inItem);
            tv_truyenDong = itemView.findViewById(R.id.tv_truyendong_inItem);
            viewPager = itemView.findViewById(R.id.viewPager_Photo);
            circleIndicator = itemView.findViewById(R.id.circle_indicator);
            item = itemView.findViewById(R.id.item_inDSxe);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item1_danhsachxe, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DanhSachXeAdapter.ViewHolder holder, int position) {
        Car car = listXe.get(position);

        holder.tv_tenXe.setText(car.getMauXe());
        holder.tv_truyenDong.setText(car.getChuyenDong());
        holder.tv_soChuyen.setText(car.getSoChuyen() + " chuyến");

        photoAdapter = new PhotoAdapter(context, car.getHinhAnh());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        holder.item.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChiTietXe_Activity.class);
            intent.putExtra("car", car);
            view.getContext().startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return listXe.size();
    }


}
