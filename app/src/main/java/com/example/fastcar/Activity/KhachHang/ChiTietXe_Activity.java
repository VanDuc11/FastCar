package com.example.fastcar.Activity.KhachHang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.DanhSachXe_Activity;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Adapter.NhanXetAdapter;
import com.example.fastcar.Adapter.PhotoChiTietXeAdapter;
import com.example.fastcar.CustomTimePickerDialog;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.Dialog_FullNhanXet;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Dialog.Dialog_InfoChuSH;
import com.example.fastcar.Dialog.Dialog_PhuPhi_PhatSinh;
import com.example.fastcar.Dialog.Dialog_SoNgayThue;
import com.example.fastcar.Dialog.Dialog_TS_TheChap;
import com.example.fastcar.FavoriteCar_Method;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FavoriteCar;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.FormatString.NumberFormatK;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.User;
import com.example.fastcar.MyApplication;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.example.fastcar.Socket.SocketManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietXe_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextView btnThueXe, tv_tenxe_actionbar, tv_tenxe, tv_soSao, tv_soChuyen, tv_ngayNhanXe, tv_ngayTraXe;
    TextView tv_truyendong, tv_soGhe, tv_nhienlieu, tv_tieuhao, tv_mota, tv_soNgayThueXe, tv_tongTien, tv_xeDaThue;
    TextView tv_400km, tv_dieuKhoan, btn_see_more, tv_tenChuSH_Xe, tv_soSao_ofChuSH, tv_soChuyen_ofChuSH, tv_soTien1ngay;
    TextView tv_diaChiXe, tv_diaChiXe2, tv_thechap, tvTimeGiaoXe, tvTimeNhanXe, tvXemThemNhanXet, tvSoluongNX;
    private boolean isSeeMore_inDieuKhoan = false;
    CardView selection1, selection2, cardview_thoigianThueXe, cardview_DieuKhoan_PhuPhi, cardview_danhgiaXe, cardview_chuxe;
    RadioButton rd_selection1, rd_selection2;
    private ConstraintLayout ln_view_buttonThueXe_inCTX;
    private LinearLayout btn_showDatePicker, ln_giaonhanxe;
    private ImageView ic_back, ic_favorite, icon_redflag_xedathue;
    boolean isFavorite;
    private RecyclerView reyNhanXet;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private CircleImageView img_chuSH_Xe;
    private PhotoChiTietXeAdapter photoAdapter;
    private NhanXetAdapter nhanXetAdapter;
    private List<FeedBack> listFeedbacks = new ArrayList<>();
    private List<FeedBack> allFeedbacks_ofAllCars = new ArrayList<>();
    private List<Car> listXe_ofChuSH = new ArrayList<>();
    private Long startTimeLong, endTimeLong;
    private MaterialDatePicker<Pair<Long, Long>> datePicker;
    private long soNgayThueXe;
    private Car carIntent, car;
    private boolean isMyCar;
    private WebView webView_loadMap;
    private int totalChuyen_ofChuSH;
    private float totalStar_ofChuSH;
    private CustomTimePickerDialog timePickerDialog;
    private String formattedStartDate, formattedEndDate, timestr1, timestr2;
    private int time1, time2;
    private List<String> listsLichBan;
    private SocketManager socketManager;
    private ShimmerFrameLayout shimmer_view;
    private NestedScrollView data_view;
    private boolean isDatePickerDialogShowing = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_xe);

        if (socketManager == null) {
            socketManager = MyApplication.getSocketManager();
        }
        mapping();
        load();

        btn_showDatePicker.setOnClickListener(view -> showDatePicker_inChiTietXe());

        btnThueXe.setOnClickListener(v -> {
            Intent intent = new Intent(this, ThongTinThue_Activity.class);
            intent.putExtra("car", car);
            intent.putExtra("soNgayThueXe", soNgayThueXe);
            intent.putExtra("stars", totalStar_ofChuSH);
            intent.putExtra("chuyen", totalChuyen_ofChuSH);
            startActivity(intent);
        });

        tv_soNgayThueXe.setOnClickListener(view -> Dialog_SoNgayThue.showDialog(this));
        ic_back.setOnClickListener(view -> onBackPressed());
        tvXemThemNhanXet.setOnClickListener(view -> Dialog_FullNhanXet.showDialog(this, listFeedbacks));
        cardview_chuxe.setOnClickListener(view -> Dialog_InfoChuSH.showDialog(this, car, listXe_ofChuSH, allFeedbacks_ofAllCars, totalChuyen_ofChuSH));

        socketManager.on("updateCar", requestLoadUI_fromSocket);
    }

    private final Emitter.Listener requestLoadUI_fromSocket = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String dataFromSocket = (String) args[0];
            if (dataFromSocket.equals(car.get_id())) {
                runOnUiThread(() -> load());
            }
        }
    };

    private void mapping() {
        shimmer_view = findViewById(R.id.shimmer_view_inCTX);
        data_view = findViewById(R.id.data_view_inCTX);
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
        cardview_DieuKhoan_PhuPhi = findViewById(R.id.cardview_DieuKhoan_PhuPhi);
        cardview_thoigianThueXe = findViewById(R.id.cardview_thoigianThueXe);
        cardview_chuxe = findViewById(R.id.cardview_chuxe);
        tvTimeGiaoXe = findViewById(R.id.tv_thoigian_giaoxe_inChiTietXe);
        tvTimeNhanXe = findViewById(R.id.tv_thoigian_nhanxe_inChiTietXe);
        ln_giaonhanxe = findViewById(R.id.ln_thoigian_giaonhanxe_inCTX);
        cardview_danhgiaXe = findViewById(R.id.cardview_danhgiaXe);
        ln_view_buttonThueXe_inCTX = findViewById(R.id.ln_view_buttonThueXe_inCTX);
        webView_loadMap = findViewById(R.id.webView_loadMap);
        tvXemThemNhanXet = findViewById(R.id.tv_xemthem_NhanXet_inCTX);
        tvSoluongNX = findViewById(R.id.tv_sl_nhanxet_inCTX);
    }

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    private void load() {
        Intent intent = getIntent();
        carIntent = intent.getParcelableExtra("car");
        isMyCar = intent.getBooleanExtra("isMyCar", false);

        data_view.setVisibility(View.GONE);
        ln_view_buttonThueXe_inCTX.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();
        cardview_chuxe.setVisibility(View.GONE);

        fetchData_ofCar(carIntent.get_id());
    }

    @SuppressLint("SetTextI18n")
    private void loadUI() {
        isNotContinue();
        listsLichBan = car.getLichBan();

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
            time1 = preferences.getInt("startHour2", 0);
            time2 = preferences.getInt("endHour2", 0);
            timestr1 = preferences.getString("s2", "");
            timestr2 = preferences.getString("e2", "");
        } else {
            startTimeLong = preferences.getLong("startTime1", 0);
            endTimeLong = preferences.getLong("endTime1", 0);
            time1 = preferences.getInt("startHour1", 0);
            time2 = preferences.getInt("endHour1", 0);
            timestr1 = preferences.getString("s1", "");
            timestr2 = preferences.getString("e1", "");
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
            DecimalFormat df = new DecimalFormat("0.#");
            String formattedNumber = df.format(trungbinhSao);
            formattedNumber = formattedNumber.replace(",", ".");
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
        if (car.getTheChap()) {
            int number;
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
        timePickerDialog = new CustomTimePickerDialog();
        timePickerDialog.setListener(this, time1, time2, 0);
        setValue_forDate(startTimeLong, endTimeLong, timestr1, timestr2);
    }

    private void fetchData_ofCar(String idXe) {
        RetrofitClient.FC_services().getCarByID(idXe).enqueue(new Callback<Car>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                data_view.setVisibility(View.VISIBLE);
                ln_view_buttonThueXe_inCTX.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (response.body() != null) {
                        car = response.body();
                        loadUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
            }
        });
    }


    private void showDatePicker_inChiTietXe() {
        if (!isDatePickerDialogShowing) {
            isDatePickerDialogShowing = true;
            timePickerDialog = new CustomTimePickerDialog();
            timePickerDialog.setListener(ChiTietXe_Activity.this, time1, time2, 0);
            timePickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("Material_Range");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            datePicker.show(getSupportFragmentManager(), "Material_Range");
            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                @Override
                public void onPositiveButtonClick(Pair<Long, Long> selection) {
                    Long startDate = selection.first;
                    Long endDate = selection.second;
                    SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("startTime2", startDate);
                    editor.putLong("endTime2", endDate);
                    editor.putBoolean("check", true);
                    editor.apply();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    formattedStartDate = sdf.format(new Date(startDate));
                    formattedEndDate = sdf.format(new Date(endDate));
                }
            });

            datePicker.addOnDismissListener(dialog -> isDatePickerDialogShowing = false);
        }
    }

    private CalendarConstraints buildCalendarConstraints() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Chỉ cho phép chọn từ ngày hiện tại trở đi
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        constraintsBuilder.setStart(today);

        // Tính toán ngày kết thúc tối đa (max 4 tháng)
        Calendar maxEndDate = Calendar.getInstance();
        maxEndDate.set(Calendar.DAY_OF_MONTH, maxEndDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        maxEndDate.add(Calendar.MONTH, 3);
        long maxEndDateMillis = maxEndDate.getTimeInMillis();
        constraintsBuilder.setEnd(maxEndDateMillis);

        // test
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                if (car.getTrangThai() == 1) {
                    for (String dateRange : listsLichBan) {
                        if (isDateInRange(dateRange, date)) {
                            return false;
                        }
                    }
                    return date >= today && date <= maxEndDateMillis;
                } else {
                    return false;
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
            }

            private boolean isDateInRange(String dateRange, long date) {
                try {
                    String[] dates = dateRange.split(" - ");
                    long startDate = dateFormat.parse(dates[0]).getTime();
                    long endDate = dateFormat.parse(dates[1]).getTime() + (24 * 60 * 60 * 1000);
                    if (date >= startDate && date <= endDate) {
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        return constraintsBuilder.build();
    }

    private void setValue_forDate(Long startTime, Long endTime, String s1, String e1) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formattedStartDate = s1 + " " + sdf.format(new Date(startTime));
        formattedEndDate = e1 + " " + sdf.format(new Date(endTime));
        tv_ngayNhanXe.setText(formattedStartDate);
        tv_ngayTraXe.setText(formattedEndDate);
        func_TinhTongTien(formattedStartDate, formattedEndDate);
        if (checkTTXe(car)) {
            if (checkLichBan(sdf.format(new Date(startTime)), sdf.format(new Date(endTime)))) {
                if (checkTimeThueXe(formattedStartDate, formattedEndDate)) {
                    if (checkTimeNhanXe_GiaoXe(formattedStartDate, formattedEndDate)) {
                        icon_redflag_xedathue.setVisibility(View.GONE);
                        tv_xeDaThue.setVisibility(View.GONE);
                        isContinue();
                        getListHoaDon_hasTrangThai_2345(car.get_id());
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void func_TinhTongTien(String startTime, String endTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            Date date1 = dateFormat.parse(startTime);
            Date date2 = dateFormat.parse(endTime);

            long timeDifferenceMillis = date2.getTime() - date1.getTime();
            long hours = timeDifferenceMillis / (60 * 60 * 1000);
            if (hours % 24 == 0) {
                soNgayThueXe = hours / 24;
            } else {
                soNgayThueXe = (hours / 24) + 1; // làm tròn ngày
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_soNgayThueXe.setText(soNgayThueXe + " ngày");
        tv_tongTien.setText(NumberFormatK.format((int) (car.getGiaThue1Ngay() * soNgayThueXe)));
    }

    private void getFeedBack(String id_xe) {
        RetrofitClient.FC_services().getListFeedBack(id_xe).enqueue(new Callback<List<FeedBack>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(retrofit2.Call<List<FeedBack>> call, Response<List<FeedBack>> response) {
                if (response.code() == 200) {
                    listFeedbacks = response.body();
                    if (listFeedbacks.size() != 0) {
                        boolean isShowMore;
                        if (listFeedbacks.size() > 2) {
                            isShowMore = false;
                        } else {
                            tvXemThemNhanXet.setVisibility(View.GONE);
                            isShowMore = true;
                        }
                        nhanXetAdapter = new NhanXetAdapter(ChiTietXe_Activity.this, response.body(), isShowMore);
                        reyNhanXet.setAdapter(nhanXetAdapter);
                        nhanXetAdapter.notifyDataSetChanged();
                        tvSoluongNX.setText(response.body().size() + " nhận xét");

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
        SimpleDateFormat sdf_dmy = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        sdf_dmy.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        try {
            startDate = sdf_dmy.parse(startDateStr);
            endDate = sdf_dmy.parse(endDateStr);

            SimpleDateFormat gmtSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            gmtSdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            startDateStr = gmtSdf.format(startDate);
            endDateStr = gmtSdf.format(endDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // lấy list hoá đơn đã đặt, đã đặt cọc và đang vận hành của xe để check
        // 2: duyệt thành công, chờ đặt cọc
        // 3: đặt cọc thành công
        // 4: chủ xe giao xe thành công
        // 5: (hết time thuê, khách mang xe trả cho chủ) khách hàng trả xe thành công

        RetrofitClient.FC_services().getListHoaDon(id_xe, "1,2,3,4,5", startDateStr, endDateStr).enqueue(new Callback<List<HoaDon>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                isContinue();
                if (response.code() == 200) {
                    List<HoaDon> hoaDonList = response.body();
                    if (hoaDonList.size() != 0) {
                        // bị trùng lịch
                        StringBuilder valid = new StringBuilder();
                        valid.append("Xe đã được thuê:\n");
                        for (HoaDon hoaDon : hoaDonList) {
                            String text = "+ Từ " + sdf_dmy.format(hoaDon.getNgayThue()) + " đến hết " + sdf_dmy.format(hoaDon.getNgayTra()) + "\n";
                            valid.append(text);
                        }
                        if (valid.length() > 0) {
                            valid.deleteCharAt(valid.length() - 1);
                        }
                        icon_redflag_xedathue.setVisibility(View.VISIBLE);
                        tv_xeDaThue.setVisibility(View.VISIBLE);
                        isNotContinue();
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
                    listXe_ofChuSH = response.body();
                    float numberStar = 0;
                    int count = 0;
                    StringBuilder all_idCars = new StringBuilder();
                    assert listXe_ofChuSH != null;
                    for (Car car : listXe_ofChuSH) {
                        if (car.getTrungBinhSao() > 0) {
                            numberStar += car.getTrungBinhSao();
                            count++;
                        }
                        totalChuyen_ofChuSH += car.getSoChuyen();
                        all_idCars.append(car.get_id()).append(",");
                    }
                    totalStar_ofChuSH = numberStar / count;
                    DecimalFormat df = new DecimalFormat("0.#");
                    String formattedNumber = df.format(totalStar_ofChuSH);
                    formattedNumber = formattedNumber.replace(",", ".");
                    tv_soSao_ofChuSH.setText(formattedNumber);
                    tv_soChuyen_ofChuSH.setText(totalChuyen_ofChuSH + " chuyến");
                    if (all_idCars.length() > 0) {
                        all_idCars.deleteCharAt(all_idCars.length() - 1);
                    }
                    getAllFeedBack_ofAllCars_ChuSH(String.valueOf(all_idCars));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi getListCar_ofChuSH: " + t);
            }
        });
    }

    private void getAllFeedBack_ofAllCars_ChuSH(String all_idCars) {
        RetrofitClient.FC_services().getListFeedBack(all_idCars).enqueue(new Callback<List<FeedBack>>() {
            @Override
            public void onResponse(Call<List<FeedBack>> call, Response<List<FeedBack>> response) {
                if (response.code() == 200) {
                    if (response.body().size() > 0) {
                        allFeedbacks_ofAllCars = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FeedBack>> call, Throwable t) {
                System.out.println("Có lỗi khi getAllFeedBack_ofAllCars_ChuSH: " + t);
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

    private boolean checkLichBan(String startTime, String endTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        StringBuilder valid = new StringBuilder();
        for (String lichban : listsLichBan) {
            Date ngayBD = null;
            Date ngayKT = null;
            String[] dates = lichban.split(" - ");
            try {
                ngayBD = sdf.parse(dates[0]);
                ngayKT = sdf.parse(dates[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((startDate.equals(ngayBD) || startDate.equals(ngayKT) ||
                    endDate.equals(ngayBD) || endDate.equals(ngayKT)) ||
                    (startDate.before(ngayKT) && endDate.after(ngayBD))) {
                String text = "Xe bận từ 00:00 ngày " + dates[0] + " đến 23:59 ngày " + dates[1] + "\n";
                valid.append(text);
            }
        }
        if (valid.length() > 0) {
            valid.deleteCharAt(valid.length() - 1);
            tv_xeDaThue.setVisibility(View.VISIBLE);
            icon_redflag_xedathue.setVisibility(View.VISIBLE);
            tv_xeDaThue.setText(valid);
            isNotContinue();
            return false;
        } else {
            tv_xeDaThue.setVisibility(View.GONE);
            icon_redflag_xedathue.setVisibility(View.GONE);
            isContinue();
            return true;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("startHour2", i);
        editor.putInt("endHour2", i1);
        time1 = i;
        time2 = i1;
        String ddMM1 = formattedStartDate;
        String ddMM2 = formattedEndDate;
        String selectedTimeRange = timePickerDialog.getSelectedTime();
        int indexOfDash = selectedTimeRange.indexOf(" - ");
        formattedStartDate = selectedTimeRange.substring(0, indexOfDash).trim() + " " + formattedStartDate;
        formattedEndDate = selectedTimeRange.substring(indexOfDash + 3).trim() + " " + formattedEndDate;
        tv_ngayNhanXe.setText(formattedStartDate);
        tv_ngayTraXe.setText(formattedEndDate);
        editor.putString("s2", selectedTimeRange.substring(0, indexOfDash).trim());
        editor.putString("e2", selectedTimeRange.substring(indexOfDash + 3).trim());
        editor.apply();

        func_TinhTongTien(formattedStartDate, formattedEndDate);
        if (checkTTXe(car)) {
            if (checkLichBan(ddMM1, ddMM2)) {
                if (checkTimeThueXe(formattedStartDate, formattedEndDate)) {
                    if (checkTimeNhanXe_GiaoXe(formattedStartDate, formattedEndDate)) {
                        icon_redflag_xedathue.setVisibility(View.GONE);
                        tv_xeDaThue.setVisibility(View.GONE);
                        isContinue();
                        getListHoaDon_hasTrangThai_2345(car.get_id());
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private boolean checkTimeThueXe(String startTime, String endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            Date date1 = dateFormat.parse(startTime);
            Date date2 = dateFormat.parse(endTime);
            Date timeNow = new Date();
            long timeThree = date1.getTime() - timeNow.getTime();
            long threeHoursInMillis = 3 * 60 * 60 * 1000;

            if (date1.after(timeNow)) {
                if (timeThree > threeHoursInMillis) {
                    if (date2.after(date1)) {
                        long timeDifference = date2.getTime() - date1.getTime();
                        long hoursDifference = timeDifference / (60 * 60 * 1000);
                        if (hoursDifference < 3) {
                            isNotContinue();
                            icon_redflag_xedathue.setVisibility(View.VISIBLE);
                            tv_xeDaThue.setVisibility(View.VISIBLE);
                            tv_xeDaThue.setText("Thời gian cách nhau tối thiểu 3 tiếng khi thuê xe trong ngày");
                            return false;
                        }
                        return true;
                    } else {
                        isNotContinue();
                        icon_redflag_xedathue.setVisibility(View.VISIBLE);
                        tv_xeDaThue.setVisibility(View.VISIBLE);
                        tv_xeDaThue.setText("Thời gian không hợp lệ");
                        return false;
                    }
                } else {
                    isNotContinue();
                    icon_redflag_xedathue.setVisibility(View.VISIBLE);
                    tv_xeDaThue.setVisibility(View.VISIBLE);
                    tv_xeDaThue.setText("Thời gian nhận xe cần cách thời gian hiện tại 3 tiếng");
                    return false;
                }
            } else {
                isNotContinue();
                icon_redflag_xedathue.setVisibility(View.VISIBLE);
                tv_xeDaThue.setVisibility(View.VISIBLE);
                tv_xeDaThue.setText("Thời gian không hợp lệ");
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkTimeNhanXe_GiaoXe(String startTime, String endTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try {
            String[] timeRangeParts1 = car.getThoiGianGiaoXe().split(" - ");
            Date startTime1 = timeFormat.parse(timeRangeParts1[0]);
            Date endTime1 = timeFormat.parse(timeRangeParts1[1]);

            String[] timeRangeParts2 = car.getThoiGianNhanXe().split(" - ");
            Date startTime2 = timeFormat.parse(timeRangeParts2[0]);
            Date endTime2 = timeFormat.parse(timeRangeParts2[1]);

            Date datetime1 = timeFormat.parse(startTime);
            Date datetime2 = timeFormat.parse(endTime);

            // Kiểm tra xem thời gian từ String2 có nằm trong khoảng giờ của String1 không
            boolean isTimeInRange1 = datetime1.compareTo(startTime1) >= 0 && datetime1.compareTo(endTime1) <= 0;
            boolean isTimeInRange2 = datetime2.compareTo(startTime2) >= 0 && datetime2.compareTo(endTime2) <= 0;

            StringBuilder builder = new StringBuilder();
            if (isTimeInRange1 && isTimeInRange2) {
                isContinue();
                icon_redflag_xedathue.setVisibility(View.GONE);
                tv_xeDaThue.setVisibility(View.GONE);
                return true;
            } else if (!isTimeInRange1 && isTimeInRange2) {
                isNotContinue();
                icon_redflag_xedathue.setVisibility(View.VISIBLE);
                builder.append("Thời gian nhận xe không hợp lệ");
                tv_xeDaThue.setVisibility(View.VISIBLE);
                tv_xeDaThue.setText(builder + "");
                return false;
            } else if (isTimeInRange1) {
                isNotContinue();
                icon_redflag_xedathue.setVisibility(View.VISIBLE);
                builder.append("Thời gian trả xe không hợp lệ");
                tv_xeDaThue.setVisibility(View.VISIBLE);
                tv_xeDaThue.setText(builder + "");
                return false;
            } else {
                isNotContinue();
                icon_redflag_xedathue.setVisibility(View.VISIBLE);
                builder.append("Thời gian nhận xe không hợp lệ\n");
                builder.append("Thời gian trả xe không hợp lệ");
                tv_xeDaThue.setVisibility(View.VISIBLE);
                tv_xeDaThue.setText(builder + "");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkTTXe(Car car) {
        if (car.getTrangThai() == 3) {
            // xe không hoạt động
            isNotContinue();
            tv_xeDaThue.setVisibility(View.VISIBLE);
            tv_xeDaThue.setText("Xe không hoạt động");
            icon_redflag_xedathue.setVisibility(View.VISIBLE);
            return false;
        } else if (car.getTrangThai() == 4) {
            // xe bị vô hiệu hoá
            isNotContinue();
            tv_xeDaThue.setVisibility(View.VISIBLE);
            tv_xeDaThue.setText("Xe đã bị vô hiệu hoá");
            icon_redflag_xedathue.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            socketManager.off("updateCar", requestLoadUI_fromSocket);
        }
    }
}