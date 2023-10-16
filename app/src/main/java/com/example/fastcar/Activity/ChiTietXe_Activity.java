package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fastcar.Adapter.NhanXetAdapter;
import com.example.fastcar.Adapter.PhotoAdapter;
import com.example.fastcar.Adapter.PhotoChiTietXeAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;

public class ChiTietXe_Activity extends AppCompatActivity {
    TextView btnThueXe, tv_tenxe_actionbar, tv_tenxe, tv_soSao, tv_soChuyen, tv_ngayNhanXe, tv_ngayTraXe;
    TextView tv_truyendong, tv_soGhe, tv_nhienlieu, tv_tieuhao, tv_mota, tv_soNgayThueXe, tv_tongTien;
    ImageView ic_back;
    RecyclerView reyNhanXet;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    PhotoChiTietXeAdapter photoAdapter;
    Long startTime, endTime;
    MaterialDatePicker<Pair<Long, Long>> datePicker;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);

        mapping();
        load();

        build_DatePicker();

        btnThueXe.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(), ThongTinThue_Activity.class);
            startActivity(i);
        });

        ic_back.setOnClickListener(view -> onBackPressed());
        func_TinhTongTien(startTime, endTime);
    }

    void mapping() {
        btnThueXe = findViewById(R.id.btn_thuexe);
        ic_back = findViewById(R.id.icon_back_in_dsxe);
        reyNhanXet = findViewById(R.id.act_chitietxe_reyNhanXet);
        viewPager = findViewById(R.id.viewPager_Photo_inChiTietXe);
        circleIndicator = findViewById(R.id.circle_indicator_inChiTietXe);
        tv_tenxe = findViewById(R.id.tv_tenxe_inChiTietXe);
        tv_tenxe_actionbar = findViewById(R.id.tv_tenxe_actionBar_inChiTietXe);
        tv_soSao = findViewById(R.id.tv_soSao_inChiTietXe);
        tv_soChuyen = findViewById(R.id.tv_soChuyen_inChiTietXe);
        tv_ngayNhanXe = findViewById(R.id.tv_ngayNhanXe_inChiTietXe);
        tv_ngayTraXe = findViewById(R.id.tv_ngayTraXe_inChiTietXe);
        tv_truyendong = findViewById(R.id.tv_truyendong_inChiTietXe);
        tv_soGhe = findViewById(R.id.tv_soGhe_inChiTietXe);
        tv_tieuhao = findViewById(R.id.tv_tieuHao_inChiTietXe);
        tv_nhienlieu = findViewById(R.id.tv_loaiNhienLieu_inChiTietXe);
        tv_mota = findViewById(R.id.tv_moTa_inChiTietXe);
        tv_soNgayThueXe = findViewById(R.id.tv_soNgayThueXe_inChiTietXe);
        tv_tongTien = findViewById(R.id.tv_tongTien_inChiTietXe);
    }

    @SuppressLint("SetTextI18n")
    void load() {
        List<String> itemList = new ArrayList<>();
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");

        NhanXetAdapter adapter2 = new NhanXetAdapter(this, itemList);
        reyNhanXet.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        reyNhanXet.setAdapter(adapter2);

        Intent intent = getIntent();
        Car car = intent.getParcelableExtra("car");

        SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);

        boolean check = preferences.getBoolean("check", false);

        if (check) {
            startTime = preferences.getLong("startTime2", 0);
            endTime = preferences.getLong("endTime2", 0);
        } else {
            startTime = preferences.getLong("startTime1", 0);
            endTime = preferences.getLong("endTime1", 0);
        }

        tv_tenxe_actionbar.setText(car.getMauXe());
        tv_tenxe.setText(car.getMauXe());
        tv_soSao.setText("5.0");
        tv_soChuyen.setText(car.getSoChuyen() + " chuyến");
        tv_mota.setText(car.getMoTa());
        tv_truyendong.setText(car.getChuyenDong());
        tv_soGhe.setText(car.getSoGhe() + " chỗ");
        tv_nhienlieu.setText(car.getLoaiNhienLieu());
        tv_tieuhao.setText(car.getTieuHao() + "l/100km");

        photoAdapter = new PhotoChiTietXeAdapter(ChiTietXe_Activity.this, car.getHinhAnh());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    @SuppressLint("SetTextI18n")
    private void build_DatePicker() {
        datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(startTime, endTime))
                .setCalendarConstraints(buildCalendarConstraints())
                .build();

        setValue_forDate(startTime, endTime);
    }

    public void showDatePicker_inChiTietXe(View view) {
        datePicker.show(getSupportFragmentManager(), "Material_Range");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                // Lấy ngày bắt đầu và ngày kết thúc từ Pair
                Long startDate = selection.first;
                Long endDate = selection.second;

                setValue_forDate(startDate, endDate);
                func_TinhTongTien(startDate, endDate);
            }
        });
    }

    private CalendarConstraints buildCalendarConstraints() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Chỉ cho phép chọn từ ngày hiện tại trở đi
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        constraintsBuilder.setStart(today);

        // Vô hiệu hóa ngày trong quá khứ bằng cách sử dụng setValidator
        // Các ngày không hợp lệ (trong quá khứ) sẽ không thể chọn được
        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                return date >= today;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
            }
        });

        return constraintsBuilder.build();
    }

    private void setValue_forDate(Long startTime, Long endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedStartDate = sdf.format(new Date(startTime));
        String formattedEndDate = sdf.format(new Date(endTime));

        tv_ngayNhanXe.setText(formattedStartDate);
        tv_ngayTraXe.setText(formattedEndDate);
    }

    @SuppressLint("SetTextI18n")
    private void func_TinhTongTien(Long startTime, Long endTime) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(startTime);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(endTime);

        // Tính toán số ngày giữa hai ngày
        long diffInMillis = Math.abs(calendar2.getTimeInMillis() - calendar1.getTimeInMillis());
        long numberOfDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        tv_soNgayThueXe.setText(numberOfDays + " ngày");

        StringBuilder sb = new StringBuilder(String.valueOf(numberOfDays * 500000));
        int count = 0;
        for (int i = sb.length() - 1; i >= 0; i--) {
            count++;
            if (count % 3 == 0 && i != 0) {
                sb.insert(i, '.');
            }
        }
        sb.append(" đ");
        tv_tongTien.setText(sb);
    }
}