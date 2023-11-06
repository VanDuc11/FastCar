package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.LinearLayout;
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
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.ParseException;
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
    TextView tv_diaChiXe, tv_diaChiXe2;
    boolean isSeeMore_inDieuKhoan = false;
    CardView selection1, selection2, cardview_thoigianThueXe, cardview_DieuKhoan_PhuPhi;
    RadioButton rd_selection1, rd_selection2;
    ConstraintLayout ln_view_buttonThueXe_inCTX;
    LinearLayout btn_showDatePicker;
    ImageView ic_back, ic_favorite;
    boolean isFavorite;
    RecyclerView reyNhanXet;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    CircleImageView img_chuSH_Xe;
    PhotoChiTietXeAdapter photoAdapter;
    NhanXetAdapter nhanXetAdapter;
    Long startTimeLong, endTimeLong;
    Date startDate, endDate;
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

        func_TinhTongTien(startTimeLong, endTimeLong);

        btn_showDatePicker.setOnClickListener(view -> showDatePicker_inChiTietXe(car));

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
        tv_diaChiXe2 = findViewById(R.id.tv_diachiXe2_inCTX);
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
        btn_showDatePicker = findViewById(R.id.btn_showDatePicker_inCTX);
        img_chuSH_Xe = findViewById(R.id.img_avatar_chuSHXe_inCTX);
        tv_tenChuSH_Xe = findViewById(R.id.tv_tenChuSH_Xe_inCTX);
        tv_soSao_ofChuSH = findViewById(R.id.tv_soSao_ofChuSH_Xe_inCTX);
        tv_soChuyen_ofChuSH = findViewById(R.id.tv_soChuyen_ofChuSH_Xe_inCTX);
        tv_noResult_inFB = findViewById(R.id.tv_noResult_inFB);
        cardview_DieuKhoan_PhuPhi = findViewById(R.id.cardview_DieuKhoan_PhuPhi);
        cardview_thoigianThueXe = findViewById(R.id.cardview_thoigianThueXe);
        ln_view_buttonThueXe_inCTX = findViewById(R.id.ln_view_buttonThueXe_inCTX);
    }

    @SuppressLint("SetTextI18n")
    void load() {
        Intent intent = getIntent();
        car = intent.getParcelableExtra("car");
        boolean isMyCar = intent.getBooleanExtra("isMyCar", false);

        // check điều kiện, nếu xe của user đang login thì disable 1 số chức năng ( thuê xe, chọn thời gian,... )
        if (isMyCar) {
            cardview_thoigianThueXe.setVisibility(View.GONE);
            cardview_DieuKhoan_PhuPhi.setVisibility(View.GONE);
            ln_view_buttonThueXe_inCTX.setVisibility(View.GONE);
            ic_favorite.setVisibility(View.GONE);
        } else {
            cardview_thoigianThueXe.setVisibility(View.VISIBLE);
            cardview_DieuKhoan_PhuPhi.setVisibility(View.VISIBLE);
            ln_view_buttonThueXe_inCTX.setVisibility(View.VISIBLE);
            ic_favorite.setVisibility(View.VISIBLE);
        }

        SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);

        boolean check = preferences.getBoolean("check", false);

        if (check) {
            startTimeLong = preferences.getLong("startTime2", 0);
            endTimeLong = preferences.getLong("endTime2", 0);
        } else {
            startTimeLong = preferences.getLong("startTime1", 0);
            endTimeLong = preferences.getLong("endTime1", 0);
        }
        tv_xeDaThue.setVisibility(View.GONE);
        build_DatePicker();

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

        int soChuyen = car.getSoChuyen();
        float trungbinhSao = car.getTrungBinhSao();

        if (soChuyen == 0) {
            tv_soChuyen.setText("Chưa có chuyến");
            tv_soSao.setVisibility(View.GONE);
        } else {
            tv_soChuyen.setText(soChuyen + " chuyến");
            tv_soSao.setVisibility(View.VISIBLE);
        }

        if (trungbinhSao > 0) {
            DecimalFormat df = new DecimalFormat("0.0");
            String formattedNumber = df.format(trungbinhSao);

            tv_soSao.setVisibility(View.VISIBLE);
            tv_soSao.setText(formattedNumber);
        } else {
            tv_soSao.setVisibility(View.GONE);
        }

        tv_soTien1ngay.setText(NumberFormatK.format(car.getGiaThue1Ngay()));
        tv_mota.setText(car.getMoTa());
        tv_truyendong.setText(car.getChuyenDong());
        tv_soGhe.setText(car.getSoGhe() + " chỗ");
        tv_nhienlieu.setText(car.getLoaiNhienLieu());
        tv_tieuhao.setText(car.getTieuHao() + "l/100km");

        String diaChiXe = car.getDiaChiXe();
        String[] parts = diaChiXe.split(",");
        int lastIndex = parts.length - 1;
        String diachi = null;
        if (lastIndex >= 2) {
            String quanHuyen = parts[lastIndex - 2].trim();
            String thanhPhoTinh = parts[lastIndex - 1].trim();

            diachi = quanHuyen + ", " + thanhPhoTinh;
        }
        tv_diaChiXe.setText(diachi);
        tv_diaChiXe2.setText(diachi);

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
        datePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startTimeLong, endTimeLong)).setCalendarConstraints(buildCalendarConstraints()).build();

        setValue_forDate(startTimeLong, endTimeLong);
        getListHoaDon_hasTrangThai2a3(car.get_id());
    }


    private void showDatePicker_inChiTietXe(Car car) {
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
                getListHoaDon_hasTrangThai2a3(car.get_id());
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

    private void getListHoaDon_hasTrangThai2a3(String id_xe) {
        // convert kiểu dữ liệu của ngày đã chọn từ String sang Date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        startDate = null;
        endDate = null;

        try {
            startDate = sdf.parse(tv_ngayNhanXe.getText().toString());
            endDate = sdf.parse(tv_ngayTraXe.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // lấy list hoá đơn đã đặt cọc và đang vận hành của xe để check
        // 2,3 = đã cọc, đang vận hành
        RetrofitClient.FC_services().getListHoaDon(id_xe, "2,3").enqueue(new Callback<List<HoaDon>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                List<HoaDon> hoaDonList = response.body();
                if (response.code() == 200) {
                    if (hoaDonList != null) {
                        StringBuilder valid = new StringBuilder();
                        for (HoaDon hoaDon : hoaDonList) {
                            // convert kiểu dữ liệu của ngày bd, kt trong HĐ từ String sang Date
                            Date ngayBD = null;
                            Date ngayKT = null;
                            try {
                                ngayBD = sdf.parse(hoaDon.getNgayThue());
                                ngayKT = sdf.parse(hoaDon.getNgayTra());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if ((startDate.equals(ngayBD) || startDate.equals(ngayKT) ||
                                    endDate.equals(ngayBD) || endDate.equals(ngayKT)) ||
                                    (startDate.before(ngayKT) && endDate.after(ngayBD))) {
                                // Khoảng ngày đã chọn trùng với khoảng ngày trong HoaDon
                                tv_xeDaThue.setVisibility(View.VISIBLE);
                                btnThueXe.setEnabled(false);
                                btnThueXe.setBackgroundResource(R.drawable.disable_custom_btn3);
                                String text = "- Xe đã được thuê từ ngày " + hoaDon.getNgayThue() + " đến hết ngày " + hoaDon.getNgayTra() + "\n";
                                valid.append(text);
                            } else {
                                tv_xeDaThue.setVisibility(View.GONE);
                                btnThueXe.setEnabled(true);
                                btnThueXe.setBackgroundResource(R.drawable.custom_btn3);
                            }
                        }
                        tv_xeDaThue.setText(valid);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
                tv_xeDaThue.setVisibility(View.GONE);
                btnThueXe.setEnabled(true);
            }
        });
    }
}