package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.KhachHang.ChiTietXe_Activity;
import com.example.fastcar.Activity.TaiKhoanNganHang_Activity;
import com.example.fastcar.CustomTimePickerDialog;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.DialogGiayToXe;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietXeCuaToi_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    ImageView btn_back, btn_more;
    ShapeableImageView imgXe;
    TextView tvTenXe, btnThongTinXe, btnGiayToXe, btnChuyenXe, btnThemLichBan, tvTrangThaiXe, tvBKS, btnTaiSanTheChap, btnSetupTime;
    Car car;
    private TextView tvTime_start1, tvTime_end1, tvTime_start2, tvTime_end2;
    private int index11, index12, index21, index22;
    private CustomTimePickerDialog timePickerDialog1, timePickerDialog2;
    private int state;

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

        btnSetupTime.setOnClickListener(view -> showDialogSetupTime());
        btnTaiSanTheChap.setOnClickListener(view -> showDialogTaiSanTheChap(car));
        btnThemLichBan.setOnClickListener(view -> {
            Intent intent = new Intent(this, LichBan_CuaXe_Activity.class);
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
        tvBKS = findViewById(R.id.tv_bks_inCTXCT);
        tvTrangThaiXe = findViewById(R.id.tv_trangthaixe_inCTXCT);
        btnThongTinXe = findViewById(R.id.btn_thongtin_xe_inCTXCT);
        btnGiayToXe = findViewById(R.id.btn_giaytoxe_inCTXCT);
        btnChuyenXe = findViewById(R.id.btn_danhsachchuyen_inCTXCT);
        btnThemLichBan = findViewById(R.id.btn_themlichban_inCTXCT);
        btnTaiSanTheChap = findViewById(R.id.btn_taisanthechap_inCTXCT);
        btnSetupTime = findViewById(R.id.btn_time_nhanxe_or_giaoxe_inCTXCT);
        data_view = findViewById(R.id.data_view_inChiTietXeCuaToi);
        shimmer_view = findViewById(R.id.shimmer_view_inChiTietXeCuaToi);
    }

    private void load() {
        Intent intent = getIntent();
        Car carIntent = intent.getParcelableExtra("car");

        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
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
                        tvBKS.setText(car.getBKS());
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

    private void showDialogSetupTime() {
        LayoutInflater inflater = LayoutInflater.from(ChiTietXeCuaToi_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_thoigian_giaoxe_and_nhanxe, null);
        Dialog dialog = new Dialog(ChiTietXeCuaToi_Activity.this);
        dialog.setContentView(custom);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        TextView btnSave = dialog.findViewById(R.id.btnSave_thoigian_giaonhanxe);
        ImageView btnBack = dialog.findViewById(R.id.btn_close_dialog_thoigian_giaonhanxe);
        tvTime_start1 = dialog.findViewById(R.id.tv_startTime_giaoxe);
        tvTime_end1 = dialog.findViewById(R.id.tv_endTime_giaoxe);
        tvTime_start2 = dialog.findViewById(R.id.tv_startTime_nhanxe);
        tvTime_end2 = dialog.findViewById(R.id.tv_endTime_nhanxe);
        btnBack.setOnClickListener(view -> dialog.dismiss());

        String[] parts1 = car.getThoiGianGiaoXe().split(" - ");
        String[] parts2 = car.getThoiGianNhanXe().split(" - ");
        String startTime1 = parts1[0];
        String endTime1 = parts1[1];
        String startTime2 = parts2[0];
        String endTime2 = parts2[1];

        tvTime_start1.setText(startTime1);
        tvTime_end1.setText(endTime1);
        tvTime_start2.setText(startTime2);
        tvTime_end2.setText(endTime2);

        tvTime_start1.setOnClickListener(view -> initTimePicker(0));
        tvTime_end1.setOnClickListener(view -> initTimePicker(0));
        tvTime_start2.setOnClickListener(view -> initTimePicker(1));
        tvTime_end2.setOnClickListener(view -> initTimePicker(1));

        btnSave.setOnClickListener(view -> {
            String time_giaoxe = tvTime_start1.getText().toString() + " - " + tvTime_end1.getText().toString();
            String time_nhanxe = tvTime_start2.getText().toString() + " - " + tvTime_end2.getText().toString();
            if (checkTime_GiaoNhanXe(tvTime_start1.getText().toString(), tvTime_end1.getText().toString(), 0)) {
                if (checkTime_GiaoNhanXe(tvTime_start2.getText().toString(), tvTime_end2.getText().toString(), 1)) {
                    car.setThoiGianGiaoXe(time_giaoxe);
                    car.setThoiGianNhanXe(time_nhanxe);
                    updateCar(car.get_id(), car);
                }
            }
        });
    }

    private void initTimePicker(int index) {
        if (index == 0) {
            state = 0;
            timePickerDialog1 = new CustomTimePickerDialog();
            timePickerDialog1.setListener(this, index11, index12, 1);
            timePickerDialog1.show(getSupportFragmentManager(), "timepicker1");
        } else {
            state = 1;
            timePickerDialog2 = new CustomTimePickerDialog();
            timePickerDialog2.setListener(this, index21, index22, 1);
            timePickerDialog2.show(getSupportFragmentManager(), "timepicker2");
        }
    }

    private void showDialogTaiSanTheChap(Car car) {
        LayoutInflater inflater = LayoutInflater.from(ChiTietXeCuaToi_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_taisanthechap, null);
        Dialog dialog = new Dialog(ChiTietXeCuaToi_Activity.this);
        dialog.setContentView(custom);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        TextView tv_tenxe = dialog.findViewById(R.id.tv_tenxe_inDialog_taisanthechap);
        TextView btnSave = dialog.findViewById(R.id.btnSave_taisanthechap);
        SwitchCompat switchTheChap = dialog.findViewById(R.id.switch_tstc);
        ImageView btnBack = dialog.findViewById(R.id.btn_close_dialog_taisanthechap);
        switchTheChap.setChecked(car.getTheChap());

        tv_tenxe.setText(car.getMauXe());
        btnSave.setOnClickListener(view -> {
            if (switchTheChap.isChecked()) {
                car.setTheChap(true);
            } else {
                car.setTheChap(false);
            }
            updateCar(car.get_id(), car);
        });

        btnBack.setOnClickListener(view -> dialog.dismiss());
    }

    private void updateCar(String idCar, Car car) {
        RetrofitClient.FC_services().updateXe(idCar, car).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Cập nhật thành công");
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String timeSelected = "";
        if (state == 0) {
            timeSelected = timePickerDialog1.getSelectedTime();
            int indexOfDash = timeSelected.indexOf(" - ");
            tvTime_start1.setText(timeSelected.substring(0, indexOfDash).trim());
            tvTime_end1.setText(timeSelected.substring(indexOfDash + 3).trim());
            index11 = i;
            index12 = i1;
            timePickerDialog1.dismissAllowingStateLoss();
        } else {
            timeSelected = timePickerDialog2.getSelectedTime();
            int indexOfDash = timeSelected.indexOf(" - ");
            tvTime_start2.setText(timeSelected.substring(0, indexOfDash).trim());
            tvTime_end2.setText(timeSelected.substring(indexOfDash + 3).trim());
            index21 = i;
            index22 = i1;
            timePickerDialog2.dismissAllowingStateLoss();
        }
    }

    private boolean checkTime_GiaoNhanXe(String startTime, String endTime, int stateInt) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            Date startTime1 = timeFormat.parse(startTime);
            Date endTime1 = timeFormat.parse(endTime);
            // giờ kết thúc phải lớn hơn giờ bắt đầu
            boolean isTimeInRange = endTime1.after(startTime1);
            if (!isTimeInRange) {
                if (stateInt == 0) {
                    CustomDialogNotify.showToastCustom(this, "Thời gian giao xe không hợp lệ");
                } else {
                    CustomDialogNotify.showToastCustom(this, "Thời gian nhận xe không hợp lệ");
                }
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

}