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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.example.fastcar.Activity.KhachHang.ChiTietXe_Activity;
import com.example.fastcar.FavoriteCar_Method;
import com.example.fastcar.Model.Car;
import com.example.fastcar.FormatString.NumberFormatK;
import com.example.fastcar.Model.Distance.DistanceMatrix;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachXeAdapter extends RecyclerView.Adapter<DanhSachXeAdapter.ViewHolder> {
    private List<Car> listXe;
    private List<DistanceMatrix.Element> listElements;
    private Context context;

    public DanhSachXeAdapter(Context context, List<Car> listXe, List<DistanceMatrix.Element> listElements) {
        this.listXe = listXe;
        this.listElements = listElements;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenXe, tv_soSao, tv_soChuyen, tv_truyenDong, tv_soTien1ngay, tvMienTheChap, tvDiaChiXe, tvKhoangCach;
        CardView item;
        ImageView img_favorite;
        RelativeLayout button_favorite;
        ViewPager viewPager;
        CircleIndicator circleIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_soChuyen = itemView.findViewById(R.id.tv_soChuyen_inItem);
            tv_tenXe = itemView.findViewById(R.id.tv_tenxe_inItem);
            tv_soSao = itemView.findViewById(R.id.tv_soSao_inItem);
            tv_soTien1ngay = itemView.findViewById(R.id.tv_soTienThue_1ngay_inDSxe);
            tv_truyenDong = itemView.findViewById(R.id.tv_truyendong_inItem);
            tvMienTheChap = itemView.findViewById(R.id.tv_mienthechap);
            viewPager = itemView.findViewById(R.id.viewPager_Photo);
            circleIndicator = itemView.findViewById(R.id.circle_indicator);
            item = itemView.findViewById(R.id.item_inDSxe);
            img_favorite = itemView.findViewById(R.id.icon_favorite_car_inListXe);
            button_favorite = itemView.findViewById(R.id.button_favorite_car_inListXe);
            tvDiaChiXe = itemView.findViewById(R.id.tv_diachixe_inItem);
            tvKhoangCach = itemView.findViewById(R.id.tv_khoangcachxe_inItem);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Car car = listXe.get(position);

        if (listElements == null) {
            holder.tvKhoangCach.setVisibility(View.GONE);
        } else {
            DistanceMatrix.Element element = listElements.get(position);
            holder.tvKhoangCach.setText("~ " + element.getDistance().getText());
        }

        boolean[] isFavorite = {false};

        holder.tv_tenXe.setText(car.getMauXe());
        holder.tv_truyenDong.setText(car.getChuyenDong());
        if (car.getTheChap() == true) {
            holder.tvMienTheChap.setVisibility(View.GONE);
        } else {
            holder.tvMienTheChap.setVisibility(View.VISIBLE);
            holder.tvMienTheChap.setText("Miễn thế chấp");
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
        holder.tvDiaChiXe.setText(diachi);

        int soChuyen = car.getSoChuyen();
        float trungbinhSao = car.getTrungBinhSao();

        if (soChuyen == 0) {
            holder.tv_soChuyen.setText("Chưa có chuyến");
            holder.tv_soSao.setVisibility(View.GONE);
        } else {
            holder.tv_soChuyen.setText(soChuyen + " chuyến");
            holder.tv_soSao.setVisibility(View.VISIBLE);
        }

        if (trungbinhSao > 0) {
            DecimalFormat df = new DecimalFormat("0.#");
            String formattedNumber = df.format(trungbinhSao);
            formattedNumber = formattedNumber.replace(",", ".");

            holder.tv_soSao.setVisibility(View.VISIBLE);
            holder.tv_soSao.setText(formattedNumber);
        } else {
            holder.tv_soSao.setVisibility(View.GONE);
        }

        holder.tv_soTien1ngay.setText(NumberFormatK.format(car.getGiaThue1Ngay()));

        PhotoAdapter photoAdapter = new PhotoAdapter(context, car.getHinhAnh());
        holder.viewPager.setAdapter(photoAdapter);

        holder.circleIndicator.setViewPager(holder.viewPager);
        photoAdapter.registerDataSetObserver(holder.circleIndicator.getDataSetObserver());

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

    @SuppressLint("NotifyDataSetChanged")
    public boolean filterByDistance(int distanceMeters) {
        // distanceMeters: tham số có dạng mét (mét).
        List<Car> filteredList = new ArrayList<>();
        List<DistanceMatrix.Element> filteredListElement = new ArrayList<>();

        for (int i = 0; i < getItemCount(); i++) {
            double distanceValue = listElements.get(i).getDistance().getValue();
            if (distanceMeters < 50000) {
                if (distanceValue < distanceMeters) {
                    filteredList.add(listXe.get(i));
                    filteredListElement.add(listElements.get(i));
                }
            } else {
                filteredList.add(listXe.get(i));
                filteredListElement.add(listElements.get(i));
            }
        }
        listXe = filteredList;
        listElements = filteredListElement;
        notifyDataSetChanged();
        if (listXe.size() != 0 && listElements.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

}
