package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.fastcar.Adapter.DSHangXeFilterAdapter;
import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.Distance.DistanceMatrix;
import com.example.fastcar.Model.HangXeFilter;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.RangeSlider;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachXe_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout ic_back;
    List<Car> listCarFilter = new ArrayList<>();
    LinearLayout ln_noResult, data_view, data_view_filter, btnResetFilter, btnFilterHangXe, btnFilterChiTiet, btnRate5Star, btnFilterMienTheChap;
    ShimmerFrameLayout shimmer_view, shimmer_view_filter;
    private User user;
    private String longitude, latitude;
    private boolean isSelectedRate5Star, isSelectedMienTheChap = false;
    private String rate5star, mienthechap;
    RangeSlider rangeSlider_soghe, rangeSlider_tienThue1ngay, rangeSlider_namSX;
    TextView tv_khoangcach_seekbar, tv_mucGiaFrom, tv_mucGiaTo, tvSoGheFrom, tvSoGheTo, tvNSXFrom, tvNSXTo, btnResetFilterChiTiet, tvDiaChi, tvThoiGian;
    LinearLayout btn_search;
    SeekBar seekBar_KhoangCach;
    CheckBox ckbox_sosan, ckbox_std, ckbox_xang, ckbox_dau, ckbox_dien;
    int khoangcach, mucgiaFrom, mucgiaTo, sogheFrom, sogheTo, nsxFrom, nsxTo;
    String truyendongFilter, nhienlieuFilter, queryHangXeFilter = "", mucgiaToStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_xe);

        mapping();
        loadUI();
        getData(null, null, null, null, null, null, null, null, null, null, null, khoangcach);
        getData_ofCarFilter();

        ic_back.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("data_filter", Context.MODE_PRIVATE);
            preferences.edit().clear().apply();
            onBackPressed();
        });

        btnResetFilter.setOnClickListener(view -> {
            // reset
            SharedPreferences preferences = getSharedPreferences("data_filter", Context.MODE_PRIVATE);
            preferences.edit().clear().apply();

            // fetch lại list
            loadUI();
            getData(null, null, null, null, null, null, null, null, null, null, null, khoangcach);
        });

        btnFilterHangXe.setOnClickListener(view -> {
            showDialog_Filter_HangXe();
        });

        btnFilterChiTiet.setOnClickListener(view -> {
            showDialog_Filter_ChiTiet();
        });

        btnFilterMienTheChap.setOnClickListener(view -> {
            String finalTheChap;
            if (isSelectedMienTheChap) {
                isSelectedMienTheChap = false;
                mienthechap = "";
                finalTheChap = null;
                btnFilterMienTheChap.setBackgroundResource(R.drawable.custom_border_item_filter_non_selected);

            } else {
                isSelectedMienTheChap = true;
                mienthechap = "false";
                finalTheChap = "false";
                btnFilterMienTheChap.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
            }

            SharedPreferences preferences = getSharedPreferences("data_filter", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("mienthechap", mienthechap);
            editor.apply();
            if (queryHangXeFilter.equals("")) {
                getData(null, finalTheChap, truyendongFilter, nhienlieuFilter, rate5star, String.valueOf(mucgiaFrom), mucgiaToStr, String.valueOf(sogheFrom), String.valueOf(sogheTo), String.valueOf(nsxFrom), String.valueOf(nsxTo), khoangcach);
            } else {
                getData(queryHangXeFilter, finalTheChap, truyendongFilter, nhienlieuFilter, rate5star, String.valueOf(mucgiaFrom), mucgiaToStr, String.valueOf(sogheFrom), String.valueOf(sogheTo), String.valueOf(nsxFrom), String.valueOf(nsxTo), khoangcach);
            }
            loadUI();
        });

        btnRate5Star.setOnClickListener(view -> {
            String number;
            if (isSelectedRate5Star) {
                isSelectedRate5Star = false;
                rate5star = "";
                number = null;
                btnRate5Star.setBackgroundResource(R.drawable.custom_border_item_filter_non_selected);
            } else {
                isSelectedRate5Star = true;
                rate5star = "5";
                number = "5";
                btnRate5Star.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
            }

            SharedPreferences preferences = getSharedPreferences("data_filter", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("rate5Star", rate5star);
            editor.apply();
            if (queryHangXeFilter.equals("")) {
                getData(null, mienthechap, truyendongFilter, nhienlieuFilter, number, String.valueOf(mucgiaFrom), mucgiaToStr, String.valueOf(sogheFrom), String.valueOf(sogheTo), String.valueOf(nsxFrom), String.valueOf(nsxTo), khoangcach);
            } else {
                getData(queryHangXeFilter, mienthechap, truyendongFilter, nhienlieuFilter, number, String.valueOf(mucgiaFrom), mucgiaToStr, String.valueOf(sogheFrom), String.valueOf(sogheTo), String.valueOf(nsxFrom), String.valueOf(nsxTo), khoangcach);
            }
            loadUI();
        });
    }

    private void mapping() {
        recyclerView = findViewById(R.id.recyclerView_listXe);
        ic_back = findViewById(R.id.icon_back_in_dsxe);
        ln_noResult = findViewById(R.id.ln_no_result_inListXe);
        data_view = findViewById(R.id.data_view_inDSXe);
        shimmer_view = findViewById(R.id.shimmer_view_inDSXe);
        data_view_filter = findViewById(R.id.data_view_filter_inDSXe);
        shimmer_view_filter = findViewById(R.id.shimmer_view_filter_inDSXe);
        btnResetFilter = findViewById(R.id.btn_reset_filter);
        btnFilterHangXe = findViewById(R.id.btn_filter_hangxe);
        btnFilterChiTiet = findViewById(R.id.btn_filter_chitiet);
        btnRate5Star = findViewById(R.id.btn_filter_rate5Star);
        btnFilterMienTheChap = findViewById(R.id.btn_filter_thechap);
        tvDiaChi = findViewById(R.id.tv_diachi_timkiem_inItemDSX);
        tvThoiGian = findViewById(R.id.tv_thoigian_timkiem_inItemDSX);
        data_view_filter.setVisibility(View.GONE);
        shimmer_view_filter.startShimmerAnimation();

        // lấy địa chỉ từ shared
        SharedPreferences preferences = getSharedPreferences("diachiXe", Context.MODE_PRIVATE);
        tvDiaChi.setText(preferences.getString("diachi", ""));
        tvThoiGian.setText(preferences.getString("time", ""));
        longitude = preferences.getString("long", "");
        latitude = preferences.getString("lat", "");

        // lấy user login từ sharedpreferences
        SharedPreferences preferences1 = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences1.getString("user", "");
        Gson gson = new Gson();
        user = gson.fromJson(userStr, User.class);
    }

    private void loadUI() {
        SharedPreferences preferences = getSharedPreferences("data_filter", Context.MODE_PRIVATE);
        rate5star = preferences.getString("rate5Star", "");
        boolean isSelectedHangXeFilter = preferences.getBoolean("isSelectedHangXeFilter", false);
        mucgiaFrom = preferences.getInt("mucgiaFrom", 0);
        mucgiaTo = preferences.getInt("mucgiaTo", 0);
        khoangcach = preferences.getInt("khoangcach", 0);
        truyendongFilter = preferences.getString("truyendong", "");
        nhienlieuFilter = preferences.getString("nhienlieu", "");
        mienthechap = preferences.getString("mienthechap", "");
        sogheFrom = preferences.getInt("sogheFrom", 0);
        sogheTo = preferences.getInt("sogheTo", 0);
        nsxFrom = preferences.getInt("nsxFrom", 0);
        nsxTo = preferences.getInt("nsxTo", 0);
        Date getYear = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        if (khoangcach == 0) {
            khoangcach = 20000;
        }

        if (truyendongFilter.equals("")) {
            truyendongFilter = null;
        }
        if (nhienlieuFilter.equals("")) {
            nhienlieuFilter = null;
        }
        if (sogheFrom == 0 && sogheTo == 0) {
            sogheFrom = 2;
            sogheTo = 20;
        }
        if (nsxFrom == 0 && nsxTo == 0) {
            nsxFrom = 2000;
            nsxTo = Integer.parseInt(sdf.format(getYear));
        }

        if (rate5star.equals("")) {
            btnRate5Star.setBackgroundResource(R.drawable.custom_border_item_filter_non_selected);
            rate5star = null;
        } else {
            btnRate5Star.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
            btnResetFilter.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
        }

        if (mienthechap.equals("")) {
            btnFilterMienTheChap.setBackgroundResource(R.drawable.custom_border_item_filter_non_selected);
            mienthechap = null;
        } else {
            btnFilterMienTheChap.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
            btnResetFilter.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
        }

        if (isSelectedHangXeFilter) {
            btnFilterHangXe.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
            btnResetFilter.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
        } else {
            // all
            btnFilterHangXe.setBackgroundResource(R.drawable.custom_border_item_filter_non_selected);
        }

        if (mucgiaTo == 0 && mucgiaFrom == 0) {
            mucgiaFrom = 100000;
            mucgiaToStr = null;
            btnFilterChiTiet.setBackgroundResource(R.drawable.custom_border_item_filter_non_selected);
        } else if (mucgiaFrom != 0 && mucgiaTo == 0) {
            mucgiaToStr = null;
            btnFilterChiTiet.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
            btnResetFilter.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
        } else {
            mucgiaToStr = String.valueOf(mucgiaTo);
            btnFilterChiTiet.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
            btnResetFilter.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
        }

        if (rate5star == null && !isSelectedHangXeFilter && mienthechap == null && mucgiaFrom == 100000 && khoangcach == 20000 &&
                truyendongFilter == null && nhienlieuFilter == null && sogheFrom == 2 && sogheTo == 20 && nsxFrom == 2000 && nsxTo == Integer.parseInt(sdf.format(getYear))) {
            btnResetFilter.setBackgroundResource(R.drawable.custom_border_item_filter_non_selected);
        } else {
            btnResetFilter.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
        }
    }

    private void getData(String HangXe, String TheChap, String ChuyenDong, String NhienLieu, String Rate5Sao, String giaThueFrom, String giaThueTo, String soGheFrom, String soGheTo, String yearFrom, String yearTo, int distance) {
        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        RetrofitClient.FC_services().getListCar_NotUser(user.getEmail(), 1, HangXe, TheChap, ChuyenDong, NhienLieu, Rate5Sao, giaThueFrom, giaThueTo, soGheFrom, soGheTo, yearFrom, yearTo).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.code() == 200) {
                    List<Car> listCar = response.body();
                    if (listCar.size() > 0) {
                        getDistanceFromCars(response.body(), distance);
                    } else {
                        ln_noResult.setVisibility(View.VISIBLE);
                        data_view.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        shimmer_view.stopShimmerAnimation();
                        shimmer_view.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi thực hiện: " + t);
            }
        });
    }

    private void getData_ofCarFilter() {
        RetrofitClient.FC_services().getListCar_NotUser(user.getEmail(), 1, null, null, null, null, null, null, null, null, null, null, null).enqueue(new Callback<List<Car>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                data_view_filter.setVisibility(View.VISIBLE);
                shimmer_view_filter.stopShimmerAnimation();
                shimmer_view_filter.setVisibility(View.GONE);
                if (response.code() == 200) {
                    listCarFilter = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi thực hiện: " + t);
            }
        });
    }

    private void getDistanceFromCars(List<Car> carList, int distance) {
        // địa chỉ tìm kiếm
        String origins = latitude + "," + longitude;
        // địa chỉ của xe ( nhiều xe )
        StringBuilder destinations = new StringBuilder();
        // loại phương tiện: car, bike, taxi
        String vehicle = "car";

        for (int i = 0; i < carList.size(); i++) {
            String lat_and_long = carList.get(i).getLatitude() + "," + carList.get(i).getLongitude();
            destinations.append(lat_and_long);

            // Nếu danh sách có nhiều hơn 1 item và không phải là item cuối cùng
            if (carList.size() > 1 && i < carList.size() - 1) {
                destinations.append("|");
            }
        }

        if (destinations.length() > 0) {
            // Loại bỏ "|" cuối cùng nếu có
            destinations.deleteCharAt(destinations.length() - 1);
        }

        RetrofitClient.GoongIO_Services().getDistance(origins, String.valueOf(destinations), vehicle, HostApi.api_key_goong).enqueue(new Callback<DistanceMatrix>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<DistanceMatrix> call, Response<DistanceMatrix> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    ln_noResult.setVisibility(View.GONE);
                    List<DistanceMatrix.Element> elementList = response.body().getRows().get(0).getElements();
                    // Cập nhật thông tin khoảng cách cho từng xe
                    for (int i = 0; i < carList.size(); i++) {
                        double distanceValue = elementList.get(i).getDistance().getValue();
                        carList.get(i).setDistance(distanceValue);
                    }

                    // Sắp xếp danh sách xe theo khoảng cách
                    Collections.sort(carList, new Comparator<Car>() {
                        @Override
                        public int compare(Car car1, Car car2) {
                            return Double.compare(car1.getDistance(), car2.getDistance());
                        }
                    });

                    Collections.sort(elementList, new Comparator<DistanceMatrix.Element>() {
                        @Override
                        public int compare(DistanceMatrix.Element element1, DistanceMatrix.Element element2) {
                            return Double.compare(element1.distance.value, element2.distance.value);
                        }
                    });

                    DanhSachXeAdapter adapter = new DanhSachXeAdapter(getBaseContext(), carList, elementList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    boolean check = adapter.filterByDistance(distance);
                    if (!check) {
                        recyclerView.setVisibility(View.GONE);
                        ln_noResult.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        ln_noResult.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceMatrix> call, Throwable t) {
                System.out.println("Có lỗi khi getDistance: " + t);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDialog_Filter_HangXe() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_filter_hangxe, findViewById(R.id.dialog_filter_hangxe));
        dialog.setContentView(v);
        dialog.show();

        RecyclerView recyclerView_filter_hangXe = dialog.findViewById(R.id.recyclerView_filter_hangXe);
        LinearLayout btn_search = dialog.findViewById(R.id.btn_search_inDialog_HangXe);
        List<HangXeFilter> hangXeFilterList = new ArrayList<>();
        hangXeFilterList.add(new HangXeFilter("Tất cả", listCarFilter.size()));

        hangXeFilterList.addAll(genderListTypeCar_fromListCar(listCarFilter));

        DSHangXeFilterAdapter hangXeFilterAdapter = new DSHangXeFilterAdapter(getBaseContext(), hangXeFilterList);
        recyclerView_filter_hangXe.setAdapter(hangXeFilterAdapter);
        hangXeFilterAdapter.notifyDataSetChanged();

        btn_search.setOnClickListener(view -> {
            List<HangXeFilter> selectedHangXeList = hangXeFilterAdapter.getSelectedHangXeList();
            StringBuilder queryHangXe = new StringBuilder();
            for (HangXeFilter selectedHangXe : selectedHangXeList) {
                if (selectedHangXe.getSoluong() != listCarFilter.size()) {
                    queryHangXe.append(selectedHangXe.getTenHangXe()).append(",");
                }
            }

            if (queryHangXe.length() > 0) {
                queryHangXe.deleteCharAt(queryHangXe.length() - 1);
            }
            queryHangXeFilter = queryHangXe.toString();

            // Lưu trạng thái các mục đã chọn vào SharedPreferences
            boolean check = hangXeFilterAdapter.saveSelectedItemsToSharedPreferences();

            if (check) {
                // all
                getData(null, mienthechap, truyendongFilter, nhienlieuFilter, rate5star, String.valueOf(mucgiaFrom), mucgiaToStr, String.valueOf(sogheFrom), String.valueOf(sogheTo), String.valueOf(nsxFrom), String.valueOf(nsxTo), khoangcach);
            } else {
                getData(queryHangXeFilter, mienthechap, truyendongFilter, nhienlieuFilter, rate5star, String.valueOf(mucgiaFrom), mucgiaToStr, String.valueOf(sogheFrom), String.valueOf(sogheTo), String.valueOf(nsxFrom), String.valueOf(nsxTo), khoangcach);
            }
            loadUI();
            dialog.dismiss();
        });
    }

    @SuppressLint("SetTextI18n")
    private void showDialog_Filter_ChiTiet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_filter_chitiet, null);
        dialog.setContentView(v);
        dialog.show();

        seekBar_KhoangCach = dialog.findViewById(R.id.seekBar_khoangcach);
        rangeSlider_soghe = dialog.findViewById(R.id.rangeSlider_soghe);
        rangeSlider_tienThue1ngay = dialog.findViewById(R.id.rangeSlider_giaThue1ngay);
        rangeSlider_namSX = dialog.findViewById(R.id.rangeSlider_nsx);
        tvSoGheFrom = dialog.findViewById(R.id.tv_soghe_from_rangeSlider);
        tvSoGheTo = dialog.findViewById(R.id.tv_soghe_to_rangeSlider);
        tv_khoangcach_seekbar = dialog.findViewById(R.id.tv_khoangcach_from_seekBar);
        tv_mucGiaFrom = dialog.findViewById(R.id.tv_giaThue_from_rangeSlider);
        tv_mucGiaTo = dialog.findViewById(R.id.tv_giaThue_to_rangeSlider);
        tvNSXFrom = dialog.findViewById(R.id.tv_nsx_from_rangeSlider);
        tvNSXTo = dialog.findViewById(R.id.tv_nsx_to_rangeSlider);
        btnResetFilterChiTiet = dialog.findViewById(R.id.btn_reset_filter_chitiet);
        btn_search = dialog.findViewById(R.id.btn_search_inDialog_ChiTiet);
        ckbox_sosan = dialog.findViewById(R.id.ckbox_sosan_infilter_chitiet);
        ckbox_std = dialog.findViewById(R.id.ckbox_sotudong_infilter_chitiet);
        ckbox_xang = dialog.findViewById(R.id.ckbox_xang_infilter_chitiet);
        ckbox_dau = dialog.findViewById(R.id.ckbox_dau_infilter_chitiet);
        ckbox_dien = dialog.findViewById(R.id.ckbox_dien_infilter_chitiet);

        genderUI_DialogFilterChiTiet();
        btnResetFilterChiTiet.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("data_filter", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("khoangcach", 0);
            editor.putInt("mucgiaFrom", 0);
            editor.putInt("mucgiaTo", 0);
            editor.putString("truyendong", "");
            editor.putString("nhienlieu", "");
            editor.putInt("sogheFrom", 0);
            editor.putInt("sogheTo", 0);
            editor.putInt("nsxFrom", 0);
            editor.putInt("nsxTo", 0);
            editor.apply();
            loadUI();
            genderUI_DialogFilterChiTiet();
        });

        btn_search.setOnClickListener(view -> {
            if (ckbox_sosan.isChecked() || ckbox_std.isChecked()) {
                if (ckbox_sosan.isChecked()) {
                    truyendongFilter = ckbox_sosan.getText().toString();
                } else if (ckbox_std.isChecked()) {
                    truyendongFilter = ckbox_std.getText().toString();
                }
            } else {
                truyendongFilter = "";
            }

            if (ckbox_xang.isChecked() || ckbox_dau.isChecked() || ckbox_dien.isChecked()) {
                if (ckbox_xang.isChecked()) {
                    nhienlieuFilter = ckbox_xang.getText().toString();
                } else if (ckbox_dau.isChecked()) {
                    nhienlieuFilter = ckbox_dau.getText().toString();
                } else if (ckbox_dien.isChecked()) {
                    nhienlieuFilter = ckbox_dien.getText().toString();
                }
            } else {
                nhienlieuFilter = "";
            }

            // save to sharedpreferences
            SharedPreferences preferences = getSharedPreferences("data_filter", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("khoangcach", khoangcach);
            editor.putInt("mucgiaFrom", mucgiaFrom);
            editor.putInt("mucgiaTo", mucgiaTo);
            editor.putString("truyendong", truyendongFilter);
            editor.putString("nhienlieu", nhienlieuFilter);
            editor.putInt("sogheFrom", sogheFrom);
            editor.putInt("sogheTo", sogheTo);
            editor.putInt("nsxFrom", nsxFrom);
            editor.putInt("nsxTo", nsxTo);
            editor.apply();

            if (mucgiaTo == 0) {
                mucgiaToStr = null;
            } else {
                mucgiaToStr = String.valueOf(mucgiaTo);
            }
            if (truyendongFilter.length() == 0) {
                truyendongFilter = null;
            }
            if (nhienlieuFilter.length() == 0) {
                nhienlieuFilter = null;
            }
            if (queryHangXeFilter.equals("")) {
                getData(null, mienthechap, truyendongFilter, nhienlieuFilter, rate5star, String.valueOf(mucgiaFrom), mucgiaToStr,
                        String.valueOf(sogheFrom), String.valueOf(sogheTo), String.valueOf(nsxFrom), String.valueOf(nsxTo), khoangcach);
            } else {
                getData(queryHangXeFilter, mienthechap, truyendongFilter, nhienlieuFilter, rate5star, String.valueOf(mucgiaFrom), mucgiaToStr,
                        String.valueOf(sogheFrom), String.valueOf(sogheTo), String.valueOf(nsxFrom), String.valueOf(nsxTo), khoangcach);
            }

            loadUI();
            btnFilterChiTiet.setBackgroundResource(R.drawable.custom_border_item_filter_selected);
            dialog.dismiss();
        });
    }

    private void genderUI_DialogFilterChiTiet() {
        if (khoangcach == 0) {
            tv_khoangcach_seekbar.setText("20 km trở lại");
            seekBar_KhoangCach.setProgress(19);
            khoangcach = 20000;
        } else {
            tv_khoangcach_seekbar.setText((khoangcach / 1000) + " km trở lại");
            seekBar_KhoangCach.setProgress((khoangcach / 1000) - 1);
        }
        seekBar_KhoangCach.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 49) {
                    tv_khoangcach_seekbar.setText("Tất cả");
                    khoangcach = 50000; // đơn vị: mét
                } else {
                    tv_khoangcach_seekbar.setText((i + 1) + " km trở lại");
                    khoangcach = (i + 1) * 1000;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (truyendongFilter == null) {
            ckbox_sosan.setChecked(false);
            ckbox_std.setChecked(false);
        } else {
            if (truyendongFilter.equals("Số sàn")) {
                ckbox_sosan.setChecked(true);
            } else {
                ckbox_std.setChecked(true);
            }
        }

        if (nhienlieuFilter == null) {
            ckbox_xang.setChecked(false);
            ckbox_dau.setChecked(false);
            ckbox_dien.setChecked(false);
        } else {
            if (nhienlieuFilter.equals("Điện")) {
                ckbox_dien.setChecked(true);
            } else if (nhienlieuFilter.equals("Xăng")) {
                ckbox_xang.setChecked(true);
            } else {
                ckbox_dau.setChecked(true);
            }
        }

        // range slider giá tiền thuê
        rangeSlider_tienThue1ngay.setValueFrom(100);
        rangeSlider_tienThue1ngay.setValueTo(3000);
        if (mucgiaFrom == 0 && mucgiaTo == 0) {
            rangeSlider_tienThue1ngay.setValues(100f, 3000f);
            tv_mucGiaFrom.setText("100 K");
            tv_mucGiaTo.setText("Trên 3000 K");
//            mucgiaFrom = 100000;
        } else if (mucgiaFrom != 0 && mucgiaTo == 0) {
            rangeSlider_tienThue1ngay.setValues(Float.parseFloat(String.valueOf(mucgiaFrom / 1000)), 3000f);
            tv_mucGiaFrom.setText(mucgiaFrom / 1000 + " K");
            tv_mucGiaTo.setText("Trên 3000 K");
//            mucgiaFrom = 100000;
        } else {
            rangeSlider_tienThue1ngay.setValues(Float.parseFloat(String.valueOf(mucgiaFrom / 1000)), Float.parseFloat(String.valueOf(mucgiaTo / 1000)));
            tv_mucGiaFrom.setText(mucgiaFrom / 1000 + " K");
            tv_mucGiaTo.setText(mucgiaTo / 1000 + " K");
        }
        rangeSlider_tienThue1ngay.setStepSize(100f);
        rangeSlider_tienThue1ngay.setCustomThumbDrawable(R.drawable.icon_dollar_v1);
        rangeSlider_tienThue1ngay.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                tv_mucGiaFrom.setText(Math.round(slider.getValues().get(0)) + " K");
                mucgiaFrom = Math.round(slider.getValues().get(0)) * 1000;
                if (slider.getValues().get(1) == 3000) {
                    tv_mucGiaTo.setText("Trên " + Math.round(slider.getValues().get(1)) + " K");
                    mucgiaTo = 0;
                } else {
                    tv_mucGiaTo.setText(Math.round(slider.getValues().get(1)) + " K");
                    mucgiaTo = Math.round(slider.getValues().get(1)) * 1000;
                }
            }
        });

        // range slider số ghế
        rangeSlider_soghe.setValueFrom(2);
        rangeSlider_soghe.setValueTo(20);
        if (sogheFrom == 0 && sogheTo == 0) {
            rangeSlider_soghe.setValues(2f, 20f);
            tvSoGheFrom.setText("2 ghế");
            tvSoGheTo.setText("20 ghế");
//            sogheFrom = 2;
//            sogheTo = 20;
        } else {
            rangeSlider_soghe.setValues(Float.parseFloat(String.valueOf(sogheFrom)), Float.parseFloat(String.valueOf(sogheTo)));
            tvSoGheFrom.setText(sogheFrom + " ghế");
            tvSoGheTo.setText(sogheTo + " ghế");
        }
        rangeSlider_soghe.setStepSize(1f);
        rangeSlider_soghe.setCustomThumbDrawable(R.drawable.icon_car_seekbar_v1);
        rangeSlider_soghe.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                // Xử lý khi giá trị thay đổi
                tvSoGheFrom.setText(Math.round(slider.getValues().get(0)) + " ghế");
                tvSoGheTo.setText(Math.round(slider.getValues().get(1)) + " ghế");
                sogheFrom = Math.round(slider.getValues().get(0));
                sogheTo = Math.round(slider.getValues().get(1));
            }
        });

        // range slider năm sản xuất
        Date getYear = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        rangeSlider_namSX.setValueFrom(2000);
        rangeSlider_namSX.setValueTo(Float.parseFloat(sdf.format(getYear)));
        if (nsxFrom == 0 && nsxTo == 0) {
            rangeSlider_namSX.setValues(2000f, Float.parseFloat(sdf.format(getYear)));
            tvNSXFrom.setText("năm 2000");
            tvNSXTo.setText("năm " + sdf.format(getYear));
//            nsxFrom = 2000;
//            nsxTo = Integer.parseInt(sdf.format(getYear));
        } else {
            rangeSlider_namSX.setValues(Float.parseFloat(String.valueOf(nsxFrom)), Float.parseFloat(String.valueOf(nsxTo)));
            tvNSXFrom.setText("năm " + nsxFrom);
            tvNSXTo.setText("năm " + nsxTo);
        }
        rangeSlider_namSX.setStepSize(1f);
        rangeSlider_namSX.setCustomThumbDrawable(R.drawable.icon_car_seekbar_v1);
        rangeSlider_namSX.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                // Xử lý khi giá trị thay đổi
                tvNSXFrom.setText("năm " + Math.round(slider.getValues().get(0)));
                tvNSXTo.setText("năm " + Math.round(slider.getValues().get(1)));
                nsxFrom = Math.round(slider.getValues().get(0));
                nsxTo = Math.round(slider.getValues().get(1));
            }
        });

        CompoundButton.OnCheckedChangeListener checker1 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ckbox_sosan.setChecked(compoundButton == ckbox_sosan);
                    ckbox_std.setChecked(compoundButton == ckbox_std);
                }
            }
        };
        ckbox_sosan.setOnCheckedChangeListener(checker1);
        ckbox_std.setOnCheckedChangeListener(checker1);

        CompoundButton.OnCheckedChangeListener checker2 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ckbox_xang.setChecked(compoundButton == ckbox_xang);
                    ckbox_dau.setChecked(compoundButton == ckbox_dau);
                    ckbox_dien.setChecked(compoundButton == ckbox_dien);
                }
            }
        };
        ckbox_xang.setOnCheckedChangeListener(checker2);
        ckbox_dau.setOnCheckedChangeListener(checker2);
        ckbox_dien.setOnCheckedChangeListener(checker2);
    }

    private List<HangXeFilter> genderListTypeCar_fromListCar(List<Car> listCars) {
        Map<String, Integer> typeCountMap = new HashMap<>();

        for (Car car : listCars) {
            String tenHangXe = car.getHangXe();

            // Kiểm tra xem tenHangXe đã có trong typeCountMap chưa
            if (typeCountMap.containsKey(tenHangXe)) {
                // Nếu đã có, tăng giá trị số lượng xe
                int currentQuantity = typeCountMap.get(tenHangXe);
                typeCountMap.put(tenHangXe, currentQuantity + 1);
            } else {
                // Nếu chưa có, thêm mới vào typeCountMap với số lượng xe là 1
                typeCountMap.put(tenHangXe, 1);
            }
        }

        // Chuyển đổi typeCountMap thành danh sách listTypeCar
        List<HangXeFilter> listHangXeFilter = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : typeCountMap.entrySet()) {
            listHangXeFilter.add(new HangXeFilter(entry.getKey(), entry.getValue()));
        }
        return listHangXeFilter;
    }
}