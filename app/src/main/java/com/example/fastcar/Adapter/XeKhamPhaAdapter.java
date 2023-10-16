package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.ChiTietXe_Activity;
import com.example.fastcar.Model.Car;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class XeKhamPhaAdapter extends PagerAdapter {
    Context context;
    List<Car> listXeKP;
    ShapeableImageView img;
    TextView tv_tenXe, tv_soSao, tv_soChuyen, tv_truyenDong;
    ConstraintLayout item;
    Long startTime, endTime;

    public XeKhamPhaAdapter(Context context, List<Car> listXeKP) {
        this.context = context;
        this.listXeKP = listXeKP;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_xekhampha, container, false);
        img = view.findViewById(R.id.img_xe_inKhamPha);
        tv_soChuyen = view.findViewById(R.id.tv_soChuyen_inKhamPha);
        tv_tenXe = view.findViewById(R.id.tv_tenxe_inKhamPha);
        tv_soSao = view.findViewById(R.id.tv_soSao_inKhamPha);
        tv_truyenDong = view.findViewById(R.id.tv_truyendong_inKhamPha);
        item = view.findViewById(R.id.item_xeKhamPha);

        Car car = listXeKP.get(position);

        if (listXeKP != null) {
            Glide.with(context)
                    .load(HostApi.URL_Image + car.getHinhAnh().get(0))
                    .into(img);

            tv_tenXe.setText(car.getMauXe());
            tv_truyenDong.setText(car.getChuyenDong());
            tv_soChuyen.setText(car.getSoChuyen() + " chuyến");

            item.setOnClickListener(view1 -> {
                Intent intent = new Intent(context, ChiTietXe_Activity.class);
                intent.putExtra("car", car);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                context.startActivity(intent);
            });

            container.addView(view);
        }
        return view;
    }

    @Override
    public int getCount() {
        if (listXeKP != null) {
            return listXeKP.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
