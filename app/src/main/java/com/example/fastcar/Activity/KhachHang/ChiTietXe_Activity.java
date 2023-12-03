package com.example.fastcar.Activity.KhachHang;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.example.fastcar.Server.HostApi;
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
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietXe_Activity extends AppCompatActivity {
    TextView btnThueXe, tv_tenxe_actionbar, tv_tenxe, tv_soSao, tv_soChuyen, tv_ngayNhanXe, tv_ngayTraXe;
    TextView tv_truyendong, tv_soGhe, tv_nhienlieu, tv_tieuhao, tv_mota, tv_soNgayThueXe, tv_tongTien, tv_xeDaThue;
    TextView tv_400km, tv_dieuKhoan, btn_see_more, tv_tenChuSH_Xe, tv_soSao_ofChuSH, tv_soChuyen_ofChuSH, tv_noResult_inFB, tv_soTien1ngay;
    TextView tv_diaChiXe, tv_diaChiXe2, tv_thechap, tvTimeGiaoXe, tvTimeNhanXe;
    boolean isSeeMore_inDieuKhoan = false;
    CardView selection1, selection2, cardview_thoigianThueXe, cardview_DieuKhoan_PhuPhi, cardview_danhgiaXe, cardview_chuxe;
    RadioButton rd_selection1, rd_selection2;
    ConstraintLayout ln_view_buttonThueXe_inCTX;
    LinearLayout btn_showDatePicker, ln_giaonhanxe;
    ImageView ic_back, ic_favorite, icon_redflag_xedathue;
    boolean isFavorite;
    RecyclerView reyNhanXet;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    CircleImageView img_chuSH_Xe;
    PhotoChiTietXeAdapter photoAdapter;
    NhanXetAdapter nhanXetAdapter;
    Long startTimeLong, endTimeLong;
    MaterialDatePicker<Pair<Long, Long>> datePicker;
    long soNgayThueXe;
    Car car;
    WebView webView_loadMap;
    private int totalChuyen_ofChuSH;
    private float totalStar_ofChuSH;

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
            intent.putExtra("stars", totalStar_ofChuSH);
            intent.putExtra("chuyen", totalChuyen_ofChuSH);
            startActivity(intent);
        });

        ic_back.setOnClickListener(view -> onBackPressed());

    }

    private void mapping() {
        btnThueXe = findViewById(R.id.btn_thuexe);
        ic_back = findViewById(R.id.icon_back_in_CTX);
        ic_favorite = findViewById(R.id.icon_favorite_car_inCTX);
        icon_redflag_xedathue = findViewById(R.id.icon_redflag_xedathue);
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
        tv_thechap = findViewById(R.id.tv_thechap_inCTX);
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
        cardview_chuxe = findViewById(R.id.cardview_chuxe);
        tvTimeGiaoXe = findViewById(R.id.tv_thoigian_giaoxe_inChiTietXe);
        tvTimeNhanXe = tv_diaChiXe.findViewById(R.id.tv_thoigian_nhanxe_inChiTietXe);
        ln_giaonhanxe = findViewById(R.id.ln_thoigian_giaonhanxe_inCTX);
        cardview_danhgiaXe = findViewById(R.id.cardview_danhgiaXe);
        ln_view_buttonThueXe_inCTX = findViewById(R.id.ln_view_buttonThueXe_inCTX);
        webView_loadMap = findViewById(R.id.webView_loadMap);
    }

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    private void load() {
        Intent intent = getIntent();
        car = intent.getParcelableExtra("car");
        boolean isMyCar = intent.getBooleanExtra("isMyCar", false);
        isNotContinue();

        String diaChiXe = car.getDiaChiXe();
        String[] parts = diaChiXe.split(",");
        int lastIndex = parts.length - 1;
        String diachi = null;
        if (lastIndex >= 2) {
            String quanHuyen = parts[lastIndex - 2].trim();
            String thanhPhoTinh = parts[lastIndex - 1].trim();

            diachi = quanHuyen + ", " + thanhPhoTinh;
        }

        // check điều kiện, nếu xe của user đang login thì disable 1 số chức năng ( thuê xe, chọn thời gian,... )
        if (isMyCar) {
            cardview_thoigianThueXe.setVisibility(View.GONE);
            cardview_chuxe.setVisibility(View.GONE);
            ln_view_buttonThueXe_inCTX.setVisibility(View.GONE);
            ic_favorite.setVisibility(View.GONE);
            tv_diaChiXe2.setText(diaChiXe);
            ln_giaonhanxe.setVisibility(View.GONE);
        } else {
            cardview_thoigianThueXe.setVisibility(View.VISIBLE);
            cardview_chuxe.setVisibility(View.VISIBLE);
            ln_view_buttonThueXe_inCTX.setVisibility(View.VISIBLE);
            ic_favorite.setVisibility(View.VISIBLE);
            tv_diaChiXe.setText(diachi);
            tv_diaChiXe2.setText(diachi);
            tvTimeGiaoXe.setText("Thời gian giao xe: " + car.getThoiGianGiaoXe());
            tvTimeNhanXe.setText("Thời gian nhận xe: " + car.getThoiGianNhanXe());
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
        icon_redflag_xedathue.setVisibility(View.GONE);
        build_DatePicker();

        SharedPreferences preferences1 = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences1.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);

        RetrofitClient.FC_services().findFavoriteCar(user.get_id(), car.get_id()).enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Car>> call, Response<List<Car>> response) {
                if (response.body().isEmpty()) {
                    ic_favorite.setImageResource(R.drawable.icon_nolove);
                    isFavorite = false;
                } else {
                    ic_favorite.setImageResource(R.drawable.icon_love);
                    isFavorite = true;
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Car>> call, Throwable t) {
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
        if (car.getLoaiNhienLieu().equals("Điện")) {
            tv_tieuhao.setText(car.getTieuHao() + " km/lần sạc");
        } else {
            tv_tieuhao.setText(car.getTieuHao() + " lít/100km");
        }

        WebSettings webSettings = webView_loadMap.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // bo tròn WebView = css
        String css = "body {" +
                "   border-radius: 20px;" +
                "}";
        String js = "var style = document.createElement('style');" +
                "style.innerHTML = '" + css + "';" +
                "document.head.appendChild(style);";
        webView_loadMap.evaluateJavascript(js, null);
        webView_loadMap.setWebViewClient(new WebViewClient());
        loadMap_fromHTML(car.getLongitude(), car.getLatitude());

        // chủ SH
        String url_image_chuSH = car.getChuSH().getAvatar();

        if (url_image_chuSH != null) {
            Glide.with(getBaseContext()).load(url_image_chuSH).into(img_chuSH_Xe);
        } else {
            Glide.with(getBaseContext()).load(R.drawable.img_avatar_user_v1).into(img_chuSH_Xe);
        }

        tv_tenChuSH_Xe.setText(car.getChuSH().getUserName());
        getListCar_ofChuSH(car.getChuSH().getEmail());

        rd_selection1.setChecked(true);
        selection2.setCardBackgroundColor(Color.parseColor("#DCCBCB"));
        if (car.getTheChap() == true) {
            int number = 0;
            if (car.getGiaThue1Ngay() < 1500000) {
                number = 20;
            } else if (car.getGiaThue1Ngay() < 3000000) {
                number = 30;
            } else {
                number = 50;
            }
            String text = number + " triệu (tiền mặt/chuyển khoản cho chủ xe khi nhận xe) hoặc Xe máy (kèm giấy tờ gốc) có giá trị tương đương " + number + " triệu.";
            tv_thechap.setText(text);
        } else {
            tv_thechap.setText("Miễn thế chấp");
        }

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
        getListHoaDon_hasTrangThai_2345(car.get_id());
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
                getListHoaDon_hasTrangThai_2345(car.get_id());
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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(retrofit2.Call<List<FeedBack>> call, Response<List<FeedBack>> response) {
                if (response.code() == 200) {
                    if (response.body().size() != 0) {
                        nhanXetAdapter = new NhanXetAdapter(ChiTietXe_Activity.this, response.body());
                        reyNhanXet.setAdapter(nhanXetAdapter);
                        nhanXetAdapter.notifyDataSetChanged();
                        tv_noResult_inFB.setVisibility(View.GONE);
                    } else {
                        cardview_danhgiaXe.setVisibility(View.GONE);
                    }
                } else {
                    System.out.println("Có lỗi khi get feedback id: " + id_xe);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<FeedBack>> call, Throwable t) {
                System.out.println("Có lỗi khi get feedback id: " + id_xe + " --- " + t);
            }
        });
    }

    public void showDialog_GiayToThueXe_inCTX(View view) {
        Dialog_GiayToThueXe.showDialog(this);
    }

    public void showDialog_TSTheChap_inCTX(View view) {
        Dialog_TS_TheChap.showDialog(this, car.getTheChap());
    }

    public void showDialog_PhuPhiPhatSinh_inCTX(View view) {
        Dialog_PhuPhi_PhatSinh.showDialog(this);
    }

    private void getListHoaDon_hasTrangThai_2345(String id_xe) {
        isNotContinue();

        String startDateStr = tv_ngayNhanXe.getText().toString();
        String endDateStr = tv_ngayTraXe.getText().toString();

        Date startDate, endDate;
        SimpleDateFormat sdf_dmy = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            startDate = sdf_dmy.parse(startDateStr);
            endDate = sdf_dmy.parse(endDateStr);
            startDateStr = sdf_ymd.format(startDate);
            endDateStr = sdf_ymd.format(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // lấy list hoá đơn đã đặt, đã đặt cọc và đang vận hành của xe để check
        // 2: duyệt thành công, chờ đặt cọc
        // 3: đặt cọc thành công
        // 4: chủ xe giao xe thành công
        // 5: (hết time thuê, khách mang xe trả cho chủ) khách hàng trả xe thành công

        RetrofitClient.FC_services().getListHoaDon(id_xe, "2,3,4,5", startDateStr, endDateStr).enqueue(new Callback<List<HoaDon>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                isContinue();
                if (response.code() == 200) {
                    List<HoaDon> hoaDonList = response.body();
                    if (hoaDonList.size() != 0) {
                        // bị trùng lịch
                        StringBuilder valid = new StringBuilder();
                        for (HoaDon hoaDon : hoaDonList) {
                            String text = "+ Xe đã được thuê từ ngày " + sdf_dmy.format(hoaDon.getNgayThue()) + " đến hết ngày " + sdf_dmy.format(hoaDon.getNgayTra()) + "\n";
                            valid.append(text);
                        }
                        tv_xeDaThue.setVisibility(View.VISIBLE);
                        isNotContinue();
                        icon_redflag_xedathue.setVisibility(View.VISIBLE);
                        tv_xeDaThue.setText(valid);
                    } else {
                        tv_xeDaThue.setVisibility(View.GONE);
                        icon_redflag_xedathue.setVisibility(View.GONE);
                        isContinue();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<HoaDon>> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
                tv_xeDaThue.setVisibility(View.GONE);
                icon_redflag_xedathue.setVisibility(View.GONE);
                btnThueXe.setEnabled(false);
            }
        });
    }

    private void isContinue() {
        btnThueXe.setBackgroundResource(R.drawable.custom_btn3);
        btnThueXe.setEnabled(true);
    }

    private void isNotContinue() {
        btnThueXe.setBackgroundResource(R.drawable.disable_custom_btn3);
        btnThueXe.setEnabled(false);
    }

    private void getListCar_ofChuSH(String email) {
        totalChuyen_ofChuSH = 0;
        totalStar_ofChuSH = 0;
        RetrofitClient.FC_services().getListCar_ofUser(email, "1").enqueue(new Callback<List<Car>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<List<Car>> call, Response<List<Car>> response) {
                if (response.code() == 200) {
                    float numberStar = 0;
                    int count = 0;
                    List<Car> listCar_ofChuSH = response.body();
                    for (Car car : listCar_ofChuSH) {
                        if (car.getTrungBinhSao() > 0) {
                            numberStar += car.getTrungBinhSao();
                            count++;
                        }
                        totalChuyen_ofChuSH += car.getSoChuyen();
                    }
                    totalStar_ofChuSH = numberStar / count;
                    DecimalFormat df = new DecimalFormat("0.0");
                    String formattedNumber = df.format(totalStar_ofChuSH);
                    tv_soSao_ofChuSH.setText(formattedNumber);
                    tv_soChuyen_ofChuSH.setText(String.valueOf(totalChuyen_ofChuSH) + " chuyến");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi getListCar_ofChuSH: " + t);
            }
        });
    }

    private void loadMap_fromHTML(String longitude, String latitude) {
        String html =
                "<html lang=\"en\" style=\"height: 100%;\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Map</title>\n" +
                        "    <style>\n" +
                        "        html,\n" +
                        "        body {\n" +
                        "            height: 100%;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "\n" +
                        "        #map {\n" +
                        "            width: 100%;\n" +
                        "            height: 100%;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "    <script src='https://cdn.jsdelivr.net/npm/@goongmaps/goong-js@1.0.9/dist/goong-js.js'></script>\n" +
                        "    <link href='https://cdn.jsdelivr.net/npm/@goongmaps/goong-js@1.0.9/dist/goong-js.css' rel='stylesheet' />\n" +
                        "</head>\n" +
                        "\n" +
                        "<body style=\"height: 100%; margin: 0;\">\n" +
                        "\n" +
                        "    <div id='map'></div>\n" +
                        "    <script>\n" +
                        "        goongjs.accessToken = '" + HostApi.api_key_load_map + "';\n" +
                        "        var map = new goongjs.Map({\n" +
                        "            container: 'map',\n" +
                        "            style: 'https://tiles.goong.io/assets/goong_light_v2.json', // stylesheet location\n" +
                        "            center: [" + longitude + "," + latitude + "], // starting position [lng, lat]\n" +
                        "            zoom: 12, // starting zoom\n" +
                        "            maxZoom: 12,\n" +
                        "            minZoom: 12,\n" +
                        "            dragRotate: false,\n" +
                        "            dragPan: false\n" +
                        "        });\n" +
                        "\n" +
                        "        map.on('load', function () {\n" +
                        "            map.addLayer({\n" +
                        "                'id': 'circle-layer',\n" +
                        "                'type': 'circle',\n" +
                        "                'source': {\n" +
                        "                    'type': 'geojson',\n" +
                        "                    'data': {\n" +
                        "                        'type': 'Feature',\n" +
                        "                        'properties': {},\n" +
                        "                        'geometry': {\n" +
                        "                            'type': 'Point',\n" +
                        "                            'coordinates': [" + longitude + "," + latitude + "]\n" +
                        "                        }\n" +
                        "                    }\n" +
                        "                },\n" +
                        "                'paint': {\n" +
                        "                    'circle-radius': 70, // Bán kính của vòng tròn (meters)\n" +
                        "                    'circle-color': '#808080',\n" +
                        "                    'circle-opacity': 0.35,\n" +
                        "                    'circle-stroke-width': 2,\n" +
                        "                    'circle-stroke-color': '#808080'\n" +
                        "                }\n" +
                        "            });\n" +
                        "        });\n" +
                        "    </script>\n" +
                        "</body>\n" +
                        "</html>";

        webView_loadMap.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    @Override
    public void onBackPressed() {
        if (webView_loadMap != null && webView_loadMap.canGoBack()) {
            webView_loadMap.goBack();
        } else {
            super.onBackPressed();
        }
    }
}