package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.fastcar.Adapter.NhanXetAdapter;
import com.example.fastcar.Adapter.PhotoChiTietXeAdapter;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Dialog.Dialog_PhuPhi_PhatSinh;
import com.example.fastcar.Dialog.Dialog_TS_TheChap;
import com.example.fastcar.FavoriteCar_Method;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.FormatString.NumberFormatK;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietXe_Activity extends AppCompatActivity {
    TextView btnThueXe, tv_tenxe_actionbar, tv_tenxe, tv_soSao, tv_soChuyen, tv_ngayNhanXe, tv_ngayTraXe;
    TextView tv_truyendong, tv_soGhe, tv_nhienlieu, tv_tieuhao, tv_mota, tv_soNgayThueXe, tv_tongTien, tv_xeDaThue;
    TextView tv_400km, tv_dieuKhoan, btn_see_more, tv_tenChuSH_Xe, tv_soSao_ofChuSH, tv_soChuyen_ofChuSH, tv_noResult_inFB, tv_soTien1ngay;
    TextView tv_diaChiXe;
    boolean isSeeMore_inDieuKhoan = false;
    CardView selection1, selection2;
    RadioButton rd_selection1, rd_selection2;
    ImageView ic_back, ic_favorite;
    boolean isFavorite;
    RecyclerView reyNhanXet;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    CircleImageView img_chuSH_Xe;
    PhotoChiTietXeAdapter photoAdapter;
    NhanXetAdapter nhanXetAdapter;
    Long startTime, endTime;
    MaterialDatePicker<Pair<Long, Long>> datePicker;
    long soNgayThueXe;
    Car car;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);

        mapping();
        load();

        build_DatePicker();
        func_TinhTongTien(startTime, endTime);

        btnThueXe.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThongTinThue_Activity.class);
            intent.putExtra("car", car);
            intent.putExtra("soNgayThueXe", soNgayThueXe);
            startActivity(intent);
        });

        ic_back.setOnClickListener(view -> onBackPressed());

    }

    void mapping() {
        btnThueXe = findViewById(R.id.btn_thuexe);
        ic_back = findViewById(R.id.icon_back_in_dsxe);
        ic_favorite = findViewById(R.id.icon_favorite_car_inCTX);
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
        tv_diaChiXe = findViewById(R.id.tv_diachiXe_inCTX);
        tv_tongTien = findViewById(R.id.tv_tongTien_inChiTietXe);
        tv_xeDaThue = findViewById(R.id.tv_xedaThue_inChiTietXe);
        tv_soTien1ngay = findViewById(R.id.tv_soTienThue_1ngay_inChiTietXe);
        selection1 = findViewById(R.id.card_selection_1);
        selection2 = findViewById(R.id.card_selection_2);
        rd_selection1 = findViewById(R.id.rd_selection1);
        rd_selection2 = findViewById(R.id.rd_selection2);
        tv_400km = findViewById(R.id.tv_400km_inChiTietXe);
        tv_dieuKhoan = findViewById(R.id.tv_dieukhoan_inChiTietXe);
        btn_see_more = findViewById(R.id.btn_see_more);
        img_chuSH_Xe = findViewById(R.id.img_avatar_chuSHXe_inCTX);
        tv_tenChuSH_Xe = findViewById(R.id.tv_tenChuSH_Xe_inCTX);
        tv_soSao_ofChuSH = findViewById(R.id.tv_soSao_ofChuSH_Xe_inCTX);
        tv_soChuyen_ofChuSH = findViewById(R.id.tv_soChuyen_ofChuSH_Xe_inCTX);
        tv_noResult_inFB = findViewById(R.id.tv_noResult_inFB);
    }

    @SuppressLint("SetTextI18n")
    void load() {
        Intent intent = getIntent();
        car = intent.getParcelableExtra("car");

        SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);

        boolean check = preferences.getBoolean("check", false);

        if (check) {
            startTime = preferences.getLong("startTime2", 0);
            endTime = preferences.getLong("endTime2", 0);
        } else {
            startTime = preferences.getLong("startTime1", 0);
            endTime = preferences.getLong("endTime1", 0);
        }

        SharedPreferences preferences1 = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences1.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        RetrofitClient.FC_services().findFavoriteCar(user.get_id(), car.get_id()).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.body().isEmpty()) {
                    ic_favorite.setImageResource(R.drawable.icon_nolove);
                    isFavorite = false;
                } else {
                    ic_favorite.setImageResource(R.drawable.icon_love);
                    isFavorite = true;
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi find xe yêu thích: " + t);
            }
        });

        // xe yêu thích
        ic_favorite.setOnClickListener(view -> {
            if (isFavorite) {
                ic_favorite.setImageResource(R.drawable.icon_nolove);
                isFavorite = false;

                // xoá xe yêu thích
                FavoriteCar_Method.deleteFavoriteCar(user.get_id(), car.get_id());
            } else {
                ic_favorite.setImageResource(R.drawable.icon_love);
                isFavorite = true;

                // thêm xe yêu thích
                FavoriteCar_Method.addFavoriteCar(ChiTietXe_Activity.this, new FavoriteCar(user, car));
            }
        });

        tv_tenxe_actionbar.setText(car.getMauXe());
        tv_tenxe.setText(car.getMauXe());
        tv_soSao.setText("5.0");
        tv_soChuyen.setText(car.getSoChuyen() + " chuyến");
        tv_soTien1ngay.setText(NumberFormatK.format(car.getGiaThue1Ngay()));
        tv_mota.setText(car.getMoTa());
        tv_truyendong.setText(car.getChuyenDong());
        tv_soGhe.setText(car.getSoGhe() + " chỗ");
        tv_nhienlieu.setText(car.getLoaiNhienLieu());
        tv_tieuhao.setText(car.getTieuHao() + "l/100km");
        tv_xeDaThue.setVisibility(View.GONE);


        // dùng subString để ẩn đi địa chỉ chi tiết
        int indexDC = car.getDiaChiXe().indexOf(",");
        tv_diaChiXe.setText(car.getDiaChiXe().substring(indexDC + 2));

        // chủ SH
//        Glide.with(this)
//                .load(HostApi.URL_Image + photo)
//                .into(img_chuSH_Xe);

        tv_tenChuSH_Xe.setText(car.getChuSH().getUserName());
        tv_soSao_ofChuSH.setText("5.0");
        tv_soChuyen_ofChuSH.setText("22 chuyến");

        rd_selection1.setChecked(true);

        selection2.setCardBackgroundColor(Color.parseColor("#DCCBCB"));

        btn_see_more.setOnClickListener(view -> {
            if (isSeeMore_inDieuKhoan) {
                tv_dieuKhoan.setMaxLines(5);
                btn_see_more.setText("Xem thêm");
                isSeeMore_inDieuKhoan = false;
            } else {
                tv_dieuKhoan.setMaxLines(Integer.MAX_VALUE);
                btn_see_more.setText("Thu gọn");
                isSeeMore_inDieuKhoan = true;
            }
        });

        String tv_format = "Phụ phí phát sinh nếu lộ trình di chuyển vượt quá <b>400km</b> khi thuê <b>1 ngày</b>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_400km.setText(Html.fromHtml(tv_format, Html.FROM_HTML_MODE_LEGACY));
        }

        photoAdapter = new PhotoChiTietXeAdapter(ChiTietXe_Activity.this, car.getHinhAnh());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        // feedback
        getFeedBack(car.get_id());
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

                SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("startTime2", startDate);
                editor.putLong("endTime2", endDate);
                editor.putBoolean("check", true);
                editor.apply();

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
        soNgayThueXe = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        tv_soNgayThueXe.setText(soNgayThueXe + " ngày");

        tv_tongTien.setText(NumberFormatK.format((int) (car.getGiaThue1Ngay() * soNgayThueXe)));
    }

    private void getFeedBack(String id_xe) {
        RetrofitClient.FC_services().getListFeedBack(id_xe).enqueue(new Callback<List<FeedBack>>() {
            @Override
            public void onResponse(Call<List<FeedBack>> call, Response<List<FeedBack>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        nhanXetAdapter = new NhanXetAdapter(ChiTietXe_Activity.this, response.body());
                        reyNhanXet.setAdapter(nhanXetAdapter);
                        nhanXetAdapter.notifyDataSetChanged();
                        tv_noResult_inFB.setVisibility(View.GONE);
                    } else {
                        tv_noResult_inFB.setVisibility(View.VISIBLE);
                        reyNhanXet.setVisibility(View.GONE);
                    }
                } else {
                    System.out.println("Có lỗi khi get feedback id: " + id_xe);
                }
            }

            @Override
            public void onFailure(Call<List<FeedBack>> call, Throwable t) {
                System.out.println("Có lỗi khi get feedback id: " + id_xe + " --- " + t);
            }
        });
    }

    public void showDialog_GiayToThueXe_inCTX(View view) {
        Dialog_GiayToThueXe.showDialog(this);
    }

    public void showDialog_TSTheChap_inCTX(View view) {
        Dialog_TS_TheChap.showDialog(this);
    }

    public void showDialog_PhuPhiPhatSinh_inCTX(View view) {
        Dialog_PhuPhi_PhatSinh.showDialog(this);
    }
}