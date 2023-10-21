package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.example.fastcar.Activity.ChiTietXe_Activity;
import com.example.fastcar.FavoriteCar_Method;
import com.example.fastcar.Model.Car;
import com.example.fastcar.FormatString.NumberFormatK;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        TextView tv_tenXe, tv_soSao, tv_soChuyen, tv_truyenDong, tv_soTien1ngay;
        ConstraintLayout item;
        ImageView img_favorite;
        RelativeLayout button_favorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_soChuyen = itemView.findViewById(R.id.tv_soChuyen_inItem);
            tv_tenXe = itemView.findViewById(R.id.tv_tenxe_inItem);
            tv_soSao = itemView.findViewById(R.id.tv_soSao_inItem);
            tv_soTien1ngay = itemView.findViewById(R.id.tv_soTienThue_1ngay_inDSxe);
            tv_truyenDong = itemView.findViewById(R.id.tv_truyendong_inItem);
            viewPager = itemView.findViewById(R.id.viewPager_Photo);
            circleIndicator = itemView.findViewById(R.id.circle_indicator);
            item = itemView.findViewById(R.id.item_inDSxe);
            img_favorite = itemView.findViewById(R.id.icon_favorite_car_inListXe);
            button_favorite = itemView.findViewById(R.id.button_favorite_car_inListXe);
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
        final boolean[] isFavorite = {false};

        holder.tv_tenXe.setText(car.getMauXe());
        holder.tv_truyenDong.setText(car.getChuyenDong());
        holder.tv_soChuyen.setText(car.getSoChuyen() + " chuyến");

        holder.tv_soTien1ngay.setText(NumberFormatK.format(car.getGiaThue1Ngay()));

        photoAdapter = new PhotoAdapter(context, car.getHinhAnh());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        SharedPreferences preferences = context.getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        RetrofitClient.FC_services().findFavoriteCar(user.get_id(), car.get_id()).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if(response.body().isEmpty()) {
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
        holder.button_favorite.setOnClickListener(view -> {
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
