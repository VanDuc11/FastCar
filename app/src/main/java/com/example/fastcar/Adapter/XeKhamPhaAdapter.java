package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.KhachHang.ChiTietXe_Activity;
import com.example.fastcar.FavoriteCar_Method;
import com.example.fastcar.Model.Car;
import com.example.fastcar.FormatString.NumberFormatK;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XeKhamPhaAdapter extends RecyclerView.Adapter<XeKhamPhaAdapter.ViewHolder> {
    Context context;
    List<Car> listXeKP;
    Long startTime, endTime;

    public XeKhamPhaAdapter(Context context, List<Car> listXeKP) {
        this.context = context;
        this.listXeKP = listXeKP;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenXe, tv_soSao, tv_soChuyen, tv_truyenDong, tv_soTien1ngay, tvDiachiXe, tvTheChap;
        ShapeableImageView img;
        ImageView img_favorite;
        RelativeLayout button_favorite;

        public ViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img_xe_inKhamPha);
            tv_soChuyen = view.findViewById(R.id.tv_soChuyen_inKhamPha);
            tv_tenXe = view.findViewById(R.id.tv_tenxe_inKhamPha);
            tv_soSao = view.findViewById(R.id.tv_soSao_inKhamPha);
            tv_truyenDong = view.findViewById(R.id.tv_truyendong_inKhamPha);
            tv_soTien1ngay = view.findViewById(R.id.tv_soTienThue_1ngay_inXeKhamPha);
            img_favorite = view.findViewById(R.id.icon_favorite_car_inXeKhamPha);
            button_favorite = view.findViewById(R.id.button_favorite_car_inXeKhamPha);
            tvDiachiXe = view.findViewById(R.id.tv_diachixe_inKhamPha);
            tvTheChap = view.findViewById(R.id.tv_mienthechap_inKhamPha);
        }
    }

    @NonNull
    @Override
    public XeKhamPhaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        try {
            view = LayoutInflater.from(context).inflate(R.layout.layout_item_xekhampha, parent, false);
        }catch (Exception e){
            Log.e("TAG","onCreateView",e);
            throw e;
        }
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull XeKhamPhaAdapter.ViewHolder holder, int position) {
        final boolean[] isFavorite = {false};

        Car car = listXeKP.get(position);
        Glide.with(context)
                .load(HostApi.URL_Image + car.getHinhAnh().get(0))
                .into(holder.img);

        holder.tv_tenXe.setText(car.getMauXe());
        holder.tv_truyenDong.setText(car.getChuyenDong());

        holder.tv_truyenDong.setText(car.getChuyenDong());
        if(car.getTheChap() == true) {
            holder.tvTheChap.setVisibility(View.GONE);
        } else {
            holder.tvTheChap.setText("Miễn thế chấp");
        }

        String diaChiXe = car.getDiaChiXe();
        String[] parts = diaChiXe.split(",");
        int lastIndex = parts.length - 1;
        String diachi = null;
        if (lastIndex >= 2) {
            String quanHuyen = parts[lastIndex - 2].trim();
            String thanhPhoTinh = parts[lastIndex - 1].trim();

            diachi = quanHuyen + ", " + thanhPhoTinh;
        }
        holder.tvDiachiXe.setText(diachi);

        int soChuyen = car.getSoChuyen();
        float trungbinhSao = car.getTrungBinhSao();

        if (soChuyen == 0) {
            holder.tv_soChuyen.setText("Chưa có chuyến");
            holder.tv_soSao.setVisibility(View.GONE);
        } else {
            holder.tv_soChuyen.setText(soChuyen + " chuyến");
            holder.tv_soSao.setVisibility(View.VISIBLE);
        }

        if(trungbinhSao > 0) {
            DecimalFormat df = new DecimalFormat("0.0");
            String formattedNumber = df.format(trungbinhSao);

            holder.tv_soSao.setVisibility(View.VISIBLE);
            holder.tv_soSao.setText(formattedNumber);
        } else {
            holder.tv_soSao.setVisibility(View.GONE);
        }

        holder.tv_soTien1ngay.setText(NumberFormatK.format(car.getGiaThue1Ngay()));

        SharedPreferences preferences = context.getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        RetrofitClient.FC_services().findFavoriteCar(user.get_id(), car.get_id()).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.body().isEmpty()) {
                    holder.img_favorite.setImageResource(R.drawable.icon_nolove);
                    isFavorite[0] = false;
                } else {
                    holder.img_favorite.setImageResource(R.drawable.icon_love);
                    isFavorite[0] = true;
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi find xe yêu thích: " + t);
            }
        });

        // xe yêu thích
        holder.button_favorite.setOnClickListener(view2 -> {
            if (isFavorite[0]) {
                holder.img_favorite.setImageResource(R.drawable.icon_nolove);
                isFavorite[0] = false;

                // xoá xe yêu thích
                FavoriteCar_Method.deleteFavoriteCar(user.get_id(), car.get_id());
            } else {
                holder.img_favorite.setImageResource(R.drawable.icon_love);
                isFavorite[0] = true;

                // thêm xe yêu thích
                FavoriteCar_Method.addFavoriteCar(context, new FavoriteCar(user, car));
            }
        });

        holder.itemView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChiTietXe_Activity.class);
            intent.putExtra("car", car);
            intent.putExtra("startTime", startTime);
            intent.putExtra("endTime", endTime);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listXeKP.size();
    }

}
