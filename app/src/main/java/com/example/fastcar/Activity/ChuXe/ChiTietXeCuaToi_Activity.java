package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.KhachHang.ChiTietXe_Activity;
import com.example.fastcar.Activity.TaiKhoanNganHang_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.DialogGiayToXe;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.imageview.ShapeableImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietXeCuaToi_Activity extends AppCompatActivity {
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    ImageView btn_back, btn_more;
    ShapeableImageView imgXe;
    TextView tvTenXe, btnThongTinXe, btnGiayToXe, btnChuyenXe, btnThemLichBan, tvTrangThaiXe;
    Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe_cua_toi);

        mapping();
        load();

        btnThongTinXe.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChiTietXe_Activity.class);
            intent.putExtra("car", car);
            intent.putExtra("isMyCar", true);
            startActivity(intent);
        });

        btnGiayToXe.setOnClickListener(view -> DialogGiayToXe.showDialog(this, car));
        btnChuyenXe.setOnClickListener(view -> {
            Intent intent = new Intent(ChiTietXeCuaToi_Activity.this, ChuyenXe_ChuSH_Activity.class);
            intent.putExtra("car", car);
            startActivity(intent);
        });

        btn_back.setOnClickListener(view -> onBackPressed());
        btn_more.setOnClickListener(view -> showDialog_XoaXe_orTatHD(car));
    }

    private void mapping() {
        btn_back = findViewById(R.id.icon_back_inCTXCT);
        btn_more = findViewById(R.id.icon_more_inCTXCT);
        imgXe = findViewById(R.id.img_xe_inCTXCT);
        tvTenXe = findViewById(R.id.tv_tenxe_inCTXCT);
        tvTrangThaiXe = findViewById(R.id.tv_trangthaixe_inCTXCT);
        btnThongTinXe = findViewById(R.id.btn_thongtin_xe_inCTXCT);
        btnGiayToXe = findViewById(R.id.btn_giaytoxe_inCTXCT);
        btnChuyenXe = findViewById(R.id.btn_danhsachchuyen_inCTXCT);
        btnThemLichBan = findViewById(R.id.btn_themlichban_inCTXCT);
        data_view = findViewById(R.id.data_view_inChiTietXeCuaToi);
        shimmer_view = findViewById(R.id.shimmer_view_inChiTietXeCuaToi);
    }

    private void load() {
        Intent intent = getIntent();
        Car carIntent = intent.getParcelableExtra("car");

        data_view.setVisibility(View.GONE);
        shimmer_view.startShimmerAnimation();

        fetchData_ofCar(carIntent.get_id());
    }

    @SuppressLint("SetTextI18n")
    private void showDialog_XoaXe_orTatHD(Car car) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_xoaxe);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        LinearLayout btnTat_orBat_TTHDXe = dialog.findViewById(R.id.btn_bat_tat_tt_xe);
        ImageView img_stt = dialog.findViewById(R.id.icon_bat_tat_tt_xe);
        TextView tvTT = dialog.findViewById(R.id.tv_bat_tat_tt_xe);
        LinearLayout btnXoaXe = dialog.findViewById(R.id.btn_xoa_xe);

        int trangthaiXe = car.getTrangThai();
        // 0: chờ duyệt
        //1: đang hoạt động = duyệt thành công
        //2: từ chối
        //3: không hoạt động

        if (trangthaiXe != 1 && trangthaiXe != 3) {
            // xe chưa duyệt hoặc duyệt thất bại mới được xoá
            btnTat_orBat_TTHDXe.setVisibility(View.GONE);
        } else if (trangthaiXe == 1) {
            // tắt hoạt động
            btnXoaXe.setVisibility(View.GONE);
            img_stt.setImageResource(R.drawable.icon_car_cancel);
            tvTT.setText("Tắt hoạt động");
        } else {
            // bật hoạt động
            btnXoaXe.setVisibility(View.GONE);
            img_stt.setImageResource(R.drawable.icon_car_tick);
            tvTT.setText("Bật hoạt động");
        }

        btnXoaXe.setOnClickListener(view -> {
            showDialogConfirmDelete(car.get_id());
            dialog.dismiss();
        });
        btnTat_orBat_TTHDXe.setOnClickListener(view -> {
            if (trangthaiXe == 1) {
                Car carModel = new Car();
                carModel.setTrangThai(3);
                updateTrangThaiXe(car.get_id(), carModel, "Đã tắt hoạt động");
                dialog.dismiss();
            } else {
                Car carModel = new Car();
                carModel.setTrangThai(1);
                updateTrangThaiXe(car.get_id(), carModel, "Đã bật hoạt động");
                dialog.dismiss();
            }
        });
    }

    private void showDialogConfirmDelete(String idXe) {
        LayoutInflater inflater = LayoutInflater.from(ChiTietXeCuaToi_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_confirm_delete_item, null);
        Dialog dialog = new Dialog(ChiTietXeCuaToi_Activity.this);
        dialog.setContentView(custom);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel_delete_item);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_delete_item);

        btn_cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        btn_confirm.setOnClickListener(view -> {
            // xoá item
            deleteCar(idXe, dialog);
        });
    }

    private void deleteCar(String idXe, Dialog dialog) {
        RetrofitClient.FC_services().deleteXe(idXe).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Xoá thành công");
                    Handler handler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = () -> {
                        dialog.dismiss();
                        onBackPressed();
                        finish();
                    };
                    handler.postDelayed(() -> handler.post(myRunnable), 1000);
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
                System.out.println("Có lỗi khi xoá xe: " + t);
            }
        });
    }

    private void updateTrangThaiXe(String idXe, Car car, String message) {
        RetrofitClient.FC_services().updateTrangThaiXe(idXe, car).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    load();
                    CustomDialogNotify.showToastCustom(getBaseContext(), message);
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi updateTrangThaiXe: " + t);
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
            }
        });
    }

    private void fetchData_ofCar(String idXe) {
        RetrofitClient.FC_services().getCarByID(idXe).enqueue(new Callback<Car>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (response.body() != null) {
                        car = response.body();
                        Glide.with(getBaseContext()).load(HostApi.URL_Image + car.getHinhAnh().get(0)).into(imgXe);
                        tvTenXe.setText(car.getMauXe());
                        int status = car.getTrangThai();
                        if (status == 0) {
                            tvTrangThaiXe.setText("Chờ duyệt");
                        } else if (status == 1) {
                            tvTrangThaiXe.setText("Đang hoạt động");
                        } else if (status == 2) {
                            tvTrangThaiXe.setText("Bị từ chối");
                        } else {
                            tvTrangThaiXe.setText("Không hoạt động");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
            }
        });
    }
}