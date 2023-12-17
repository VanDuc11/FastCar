package com.example.fastcar.Activity.KhachHang;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.DanhSachXe_Activity;
import com.example.fastcar.Activity.MaGiamGia_Activity;
import com.example.fastcar.Activity.TaiKhoanNganHang_Activity;
import com.example.fastcar.Activity.ThongTinGPLX_Activity;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.Dialog_Coc30Per;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Dialog.Dialog_GoiYLoiNhan;
import com.example.fastcar.Dialog.Dialog_InfoChuSH;
import com.example.fastcar.Dialog.Dialog_PhiDVFC;
import com.example.fastcar.Dialog.Dialog_TS_TheChap;
import com.example.fastcar.Dialog.Dialog_TT70Per;
import com.example.fastcar.Dialog.Dialog_Thoat_DangKy;
import com.example.fastcar.Dialog.Dialog_TienThue_1ngay;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.FormatString.RandomMaHD;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.NganHang;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.Model.Voucher;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ThongTinThue_Activity extends AppCompatActivity {
    ImageView ic_back, ic_close_voucher;
    TextView btn_xacnhanThueXe, tv_tenXe, tv_soSao, tv_soChuyen, tv_ngayNhanXe, tv_ngayTraXe, tv_diaChiNhanXe, tv_tienThue1ngay;
    TextView tv_tongTienxSoNgay, tv_tenVoucher, tv_giatriVoucher, btn_Select_Voucher, tv_thanhTien, tv_coc30Per, tv_tt70Per;
    TextView tv_tenChuSH_Xe, tv_soSao_ofChuSH, tv_soChuyen_ofChuSH, tv_maSoXe, tv_phiDV, tv_TheChap;
    EditText edt_loiNhan;
    CircleImageView img_chuSH_Xe;
    CheckBox ckbox_dieuKhoan;
    LinearLayout ln_view_voucher;
    ShapeableImageView img_Xe;
    Long startTime, endTime;
    ProgressBar progressBar;
    long soNgayThueXe;
    Car car;
    NganHang nganHang;
    CardView cardView_chuSH;
    List<Car> listCars_ofChuSH = new ArrayList<>();
    List<FeedBack> listFeedBack = new ArrayList<>();
    private int totalChuyen_ofChuSH;
    User user, userShared;
    int giaThue1ngay, phiDVFastCar1ngay, tongPhiDV, tongTien, tienCocGoc, coc30Per, thanhToan70Per, tongTienGiamGia;
    private String timestr1, timestr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_thue);

        mapping();
        load();

        ic_back.setOnClickListener(view -> onBackPressed());

        btn_xacnhanThueXe.setOnClickListener(view -> createHD_and_showDialog());
        cardView_chuSH.setOnClickListener(view -> Dialog_InfoChuSH.showDialog(this, car, listCars_ofChuSH, listFeedBack, totalChuyen_ofChuSH));
    }

    void mapping() {
        ic_back = findViewById(R.id.icon_back_in_thongtin_xethue);
        btn_xacnhanThueXe = findViewById(R.id.btn_xacnhan_thueXe_inThongTinThue);
        ckbox_dieuKhoan = findViewById(R.id.ckbox_dieuKhoan_inThongTinThue);
        img_Xe = findViewById(R.id.img_xe_inThongTinThue);
        tv_tenXe = findViewById(R.id.tv_tenxe_inThongTinThue);
        tv_soSao = findViewById(R.id.tv_soSao_inThongTinThue);
        tv_soChuyen = findViewById(R.id.tv_soChuyen_inThongTinThue);
        tv_ngayNhanXe = findViewById(R.id.tv_ngayNhanXe_inThongTinThue);
        tv_phiDV = findViewById(R.id.tv_phiDV_FC_inTTT);
        tv_ngayTraXe = findViewById(R.id.tv_ngayTraXe_inThongTinThue);
        tv_diaChiNhanXe = findViewById(R.id.tv_diachi_nhanxe_inThongTinThue);
        tv_tienThue1ngay = findViewById(R.id.tv_tienThue_1ngay);
        tv_tongTienxSoNgay = findViewById(R.id.tv_tongTien_x_soNgay_inThongTinThue);
        progressBar = findViewById(R.id.progressBar_inThongTinThue);
        tv_coc30Per = findViewById(R.id.tv_soTien30Per_inThongTinThue);
        tv_tt70Per = findViewById(R.id.tv_soTien70Per_inThongTinThue);
        tv_thanhTien = findViewById(R.id.tv_thanhTien_inThongTinThue);
        tv_maSoXe = findViewById(R.id.tv_masoXe_inThongTinThue);
        tv_TheChap = findViewById(R.id.tvv_thechap_inTTT);
        tv_tenVoucher = findViewById(R.id.tv_tenma_voucher);
        tv_giatriVoucher = findViewById(R.id.tv_giatri_voucher);
        btn_Select_Voucher = findViewById(R.id.btn_select_voucher);
        ln_view_voucher = findViewById(R.id.ln_view_voucher);
        ic_close_voucher = findViewById(R.id.ic_close_voucher);
        img_chuSH_Xe = findViewById(R.id.img_avatar_chuSHXe_inTTT);
        tv_tenChuSH_Xe = findViewById(R.id.tv_tenChuSH_Xe_inTTT);
        tv_soSao_ofChuSH = findViewById(R.id.tv_soSao_ofChuSH_Xe_inTTT);
        tv_soChuyen_ofChuSH = findViewById(R.id.tv_soChuyen_ofChuSH_Xe_inTTT);
        edt_loiNhan = findViewById(R.id.edt_loiNhanCuaKH);
        cardView_chuSH = findViewById(R.id.cardView_chuxe_inTTT);
    }

    @SuppressLint("SetTextI18n")
    private void load() {
        Intent intent = getIntent();
        car = intent.getParcelableExtra("car");
        soNgayThueXe = intent.getLongExtra("soNgayThueXe", 0);
        totalChuyen_ofChuSH = intent.getIntExtra("chuyen", 0);
        float totalStar_ofChuSH = intent.getFloatExtra("stars", 0);
        progressBar.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences("timePicker", Context.MODE_PRIVATE);

        boolean check = preferences.getBoolean("check", false);

        if (check) {
            startTime = preferences.getLong("startTime2", 0);
            endTime = preferences.getLong("endTime2", 0);
            timestr1 = preferences.getString("s2", "");
            timestr2 = preferences.getString("e2", "");
        } else {
            startTime = preferences.getLong("startTime1", 0);
            endTime = preferences.getLong("endTime1", 0);
            timestr1 = preferences.getString("s1", "");
            timestr2 = preferences.getString("e1", "");
        }

        SharedPreferences preferencesUser = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferencesUser.getString("user", "");
        Gson gson = new Gson();
        userShared = gson.fromJson(userStr, User.class);
        fetchData_ofUserLogin(userShared.getEmail());

        Glide.with(this)
                .load(HostApi.URL_Image + car.getHinhAnh().get(0))
                .into(img_Xe);

        tv_tenXe.setText(car.getMauXe());
        tv_maSoXe.setText(car.get_id().substring(car.get_id().length() - 6));

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

        // dùng subString để ẩn đi địa chỉ chi tiết
        String diaChiXe = car.getDiaChiXe();
        String[] parts = diaChiXe.split(",");
        int lastIndex = parts.length - 1;
        String diachi = null;
        if (lastIndex >= 2) {
            String quanHuyen = parts[lastIndex - 2].trim();
            String thanhPhoTinh = parts[lastIndex - 1].trim();

            diachi = quanHuyen + ", " + thanhPhoTinh;
        }
        tv_diaChiNhanXe.setText(diachi);

        // info chủ sở hữu
        if (car.getChuSH().getAvatar() == null) {
            Glide.with(this)
                    .load(R.drawable.img_avatar_user_v1)
                    .into(img_chuSH_Xe);
        } else {
            Glide.with(this)
                    .load(car.getChuSH().getAvatar())
                    .into(img_chuSH_Xe);
        }

        tv_tenChuSH_Xe.setText(car.getChuSH().getUserName());
        DecimalFormat df = new DecimalFormat("0.#");
        String formattedNumber = df.format(totalStar_ofChuSH);
        formattedNumber = formattedNumber.replace(",", ".");
        tv_soSao_ofChuSH.setText(formattedNumber);
        tv_soChuyen_ofChuSH.setText(totalChuyen_ofChuSH + " chuyến");
        getListCar_ofChuSH(car.getChuSH().getEmail());

        setValue_forDate(startTime, endTime, timestr1, timestr2);

        tinhTien(-1, -1);

        btn_Select_Voucher.setOnClickListener(
                view -> {
                    Intent intent1 = new Intent(ThongTinThue_Activity.this, MaGiamGia_Activity.class);
                    intent1.putExtra("SHOW_ICON_ADD", true);
                    startActivityForResult(intent1, 1);
                });

        ic_close_voucher.setOnClickListener(view -> {
            tinhTien(-1, -1);
            ln_view_voucher.setVisibility(View.GONE);
        });

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
            tv_TheChap.setText(text);
        } else {
            tv_TheChap.setText("Miễn thế chấp");
        }

        ckbox_dieuKhoan.setChecked(true);
    }

    @SuppressLint("SetTextI18n")
    private void tinhTien(int giatriPercent, int giatriMax) {
        giaThue1ngay = car.getGiaThue1Ngay();
        // Phí DV = 5% giá thuê 1 ngày của xe
        phiDVFastCar1ngay = giaThue1ngay * 5 / 100;
        tongTien = (int) ((giaThue1ngay + phiDVFastCar1ngay) * soNgayThueXe);
        tongPhiDV = phiDVFastCar1ngay * (int) soNgayThueXe;
        tienCocGoc = (int) (tongTien * 0.3);

        tv_tienThue1ngay.setText(NumberFormatVND.format(giaThue1ngay) + "/ngày");
        tv_phiDV.setText(NumberFormatVND.format(phiDVFastCar1ngay) + "/ngày");

        if (soNgayThueXe == 1) {
            tv_tongTienxSoNgay.setText(NumberFormatVND.format(tongTien));
        } else {
            tv_tongTienxSoNgay.setText(NumberFormatVND.format(giaThue1ngay + phiDVFastCar1ngay) + " x " + soNgayThueXe + " ngày");
        }

        if (giatriPercent >= 0 && giatriMax >= 0) {
            if (giatriPercent == 0 && giatriMax > 0) {
                // voucher không giảm theo %, chỉ giảm theo số tiền mặc định
                // Ex: giatriPercent = 0, giatriMax = 300000...
                ln_view_voucher.setVisibility(View.VISIBLE);
                tongTienGiamGia = giatriMax;
                tongTien -= tongTienGiamGia;
                tv_giatriVoucher.setText("- " + NumberFormatVND.format(tongTienGiamGia));
            } else if (giatriPercent > 0 && giatriMax == 0) {
                // voucher chỉ giảm theo %, không giới hạn số tiền
                // Ex: giatriPercent = 10%
                ln_view_voucher.setVisibility(View.VISIBLE);
                tongTienGiamGia = tongTien * giatriPercent / 100;
                tongTien -= tongTienGiamGia;
                tv_giatriVoucher.setText("- " + NumberFormatVND.format(tongTienGiamGia));
            } else if (giatriPercent > 0 && giatriMax > 0) {
                // voucher có giảm theo %, nhưng có giới hạn số tiền
                // Ex: giatriPercent = 10%, giatriMax = 300000...
                ln_view_voucher.setVisibility(View.VISIBLE);
                tongTienGiamGia = tongTien * giatriPercent / 100;
                if (tongTienGiamGia > giatriMax) {
                    tongTienGiamGia = giatriMax;
                }
                tongTien -= tongTienGiamGia;
                tv_giatriVoucher.setText("- " + NumberFormatVND.format(tongTienGiamGia));
            }
        } else {
            // không dùng voucher
            tv_tenVoucher.setText("");
            tongTienGiamGia = 0;
            ln_view_voucher.setVisibility(View.GONE);
        }

        coc30Per = (int) (tongTien * 0.3);
        thanhToan70Per = tongTien - coc30Per;

        tv_coc30Per.setText(NumberFormatVND.format(coc30Per));
        tv_tt70Per.setText(NumberFormatVND.format(thanhToan70Per));
        tv_thanhTien.setText(NumberFormatVND.format(tongTien));
    }

    private void createHD_and_showDialog() {
        if (user == null) {
            isNotContinue();
        } else {
            if (user.getTrangThai_GPLX() == 2) {
                // đã xác minh gplx
                if (nganHang != null) {
                    String ngayNhanStr = tv_ngayNhanXe.getText().toString().trim();
                    String ngayTraStr = tv_ngayTraXe.getText().toString().trim();
                    String voucher = tv_tenVoucher.getText().toString().trim();
                    String maHD = "FCAR" + RandomMaHD.random(5);
                    String loiNhan = edt_loiNhan.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    Date ngayNhanGMT, ngayTraGMT;
                    try {
                        ngayNhanGMT = sdf.parse(ngayNhanStr);
                        ngayTraGMT = sdf.parse(ngayTraStr);
                        ngayNhanGMT.setTime(ngayNhanGMT.getTime() - TimeZone.getTimeZone("GMT+7").getRawOffset());
                        ngayTraGMT.setTime(ngayTraGMT.getTime() - TimeZone.getTimeZone("GMT+7").getRawOffset());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    HoaDon hoaDon = new HoaDon(maHD, user, car, ngayNhanGMT, ngayTraGMT, (int) soNgayThueXe, tongPhiDV,
                            voucher, tongTienGiamGia, 0, tongTien, coc30Per, tienCocGoc, thanhToan70Per, loiNhan, null, null, null, null, 1, "", false);
                    progressBar.setVisibility(View.VISIBLE);
                    RetrofitClient.FC_services().createHoaDon(hoaDon).enqueue(new Callback<ResMessage>() {
                        @Override
                        public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.code() == 201) {
                                showDialog_DatCoc_or_ThueXeKhac(hoaDon);
                            } else {
                                System.out.println(response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResMessage> call, Throwable t) {
                            System.out.println("Có lỗi khi thực hiện createHoaDon: " + t);
                        }
                    });
                } else {
                    showDialog_VerifyBank();
                }

            } else {
                showDialog_VerifyGPLX();
            }
        }
    }

    private void showDialog_VerifyGPLX() {
        LayoutInflater inflater = LayoutInflater.from(ThongTinThue_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_verify_gplx, null);
        Dialog dialog = new Dialog(ThongTinThue_Activity.this);
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

        TextView tvNameUser = dialog.findViewById(R.id.tv_tenUser_inDialogVerifyGPLX);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel_inDialogVerifyGPLX);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_inDialogVerifyGPLX);

        tvNameUser.setText(user.getUserName());

        btn_cancel.setOnClickListener(view -> dialog.dismiss());
        btn_confirm.setOnClickListener(view -> {
            Intent intent = new Intent(ThongTinThue_Activity.this, ThongTinGPLX_Activity.class);
            intent.putExtra("emailUser", user.getEmail());
            intent.putExtra("onBackPrev", true);
            startActivity(intent);
            dialog.dismiss();
        });
    }

    private void showDialog_VerifyBank() {
        LayoutInflater inflater = LayoutInflater.from(ThongTinThue_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_verify_bank, null);
        Dialog dialog = new Dialog(ThongTinThue_Activity.this);
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

        TextView tvNameUser = dialog.findViewById(R.id.tv_tenUser_inDialogVerifyBank);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel_inDialogVerifyBank);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_inDialogVerifyBank);

        tvNameUser.setText(user.getUserName());

        btn_cancel.setOnClickListener(view -> dialog.dismiss());
        btn_confirm.setOnClickListener(view -> {
            Intent intent = new Intent(ThongTinThue_Activity.this, TaiKhoanNganHang_Activity.class);
            intent.putExtra("emailUser", user.getEmail());
            intent.putExtra("user", user);
            startActivity(intent);
            dialog.dismiss();
        });
    }

    private void showDialog_DatCoc_or_ThueXeKhac(HoaDon hoaDon) {
        LayoutInflater inflater = LayoutInflater.from(ThongTinThue_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_xac_nhan_thue_xe, null);
        Dialog dialog = new Dialog(this);
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

        AppCompatButton btn_datcoc = custom.findViewById(R.id.btn_datcoc_in_dialog);
        AppCompatButton btn_thuexekhac = custom.findViewById(R.id.btn_thuexekhac_in_dialog);
        TextView tv = dialog.findViewById(R.id.tv_content_dialog_xntx);

        tv.setText("Yêu cầu thuê xe " + hoaDon.getXe().getMauXe() + " của quý khách đã được chấp nhận.\nVui lòng chờ chủ xe xác nhận yêu cầu.");

        btn_datcoc.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("data_filter_hangxe", Context.MODE_PRIVATE);
            preferences.edit().clear().apply();

            dialog.dismiss();
            Intent intent = new Intent(getBaseContext(), KhamPha_Activity.class);
            startActivity(intent);
            finish();
        });

        btn_thuexekhac.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(getBaseContext(), DanhSachXe_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void setValue_forDate(Long startTime, Long endTime, String s1, String e1) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedStartDate = s1 + " " + sdf.format(new Date(startTime));
        String formattedEndDate = e1 + " " + sdf.format(new Date(endTime));

        tv_ngayNhanXe.setText(formattedStartDate);
        tv_ngayTraXe.setText(formattedEndDate);
    }

    public void showDialog_GiaThueXe_1ngay_inTTT(View view) {
        Dialog_TienThue_1ngay.showDialog(this);
    }


    public void showDialog_PhiDichVuFastCar_inTTT(View view) {
        Dialog_PhiDVFC.showDialog(this);
    }

    public void showDialog_DatCoc30Per_inTTT(View view) {
        Dialog_Coc30Per.showDialog(this);
    }

    public void showDialog_ThanhToan70Per_inTTT(View view) {
        Dialog_TT70Per.showDialog(this);
    }

    public void showDialog_GiayToThueXe_inTTT(View view) {
        Dialog_GiayToThueXe.showDialog(this);
    }

    public void showDialog_TSTheChap_inTTT(View view) {
        Dialog_TS_TheChap.showDialog(this, car.getTheChap());
    }

    public void showDialog_GoiYLoiNhan(View view) {
        Dialog_GoiYLoiNhan.showDialog(this);
    }

    public void showDialog_DangKy(View view) {
        Dialog_Thoat_DangKy.showDialog(this, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Voucher selectedVoucher = data.getParcelableExtra("voucher");
                if (selectedVoucher != null) {
                    tv_tenVoucher.setText(selectedVoucher.getMaGiamGia());

                    int giatriPercent = selectedVoucher.getGiaTri();
                    int giatriMax = selectedVoucher.getGiaTriMax();

                    tinhTien(giatriPercent, giatriMax);
                } else {
                    tinhTien(-1, -1);
                    tv_tenVoucher.setText("");
                }
            }
        }
    }

    private void fetchData_ofUserLogin(String email) {
        isNotContinue();
        RetrofitClient.FC_services().getListUser(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().isEmpty()) {
                        user = response.body().get(0);
                        fetch_ListNH_ofUser(user.getEmail());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi: " + t);
            }
        });
    }

    private void fetch_ListNH_ofUser(String email) {
        RetrofitClient.FC_services().getListNH_ofUser(email).enqueue(new Callback<List<NganHang>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<NganHang>> call, Response<List<NganHang>> response) {
                isContinue();
                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        nganHang = response.body().get(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NganHang>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch_NH_ofUser(): " + t);
            }
        });
    }

    private void getListCar_ofChuSH(String email) {
        RetrofitClient.FC_services().getListCar_ofUser(email, "1").enqueue(new Callback<List<Car>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<List<Car>> call, Response<List<Car>> response) {
                if (response.code() == 200) {
                    listCars_ofChuSH = response.body();
                    StringBuilder all_idCars = new StringBuilder();
                    for (Car car : listCars_ofChuSH) {
                        all_idCars.append(car.get_id()).append(",");
                    }
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
                        listFeedBack = response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FeedBack>> call, Throwable t) {
                System.out.println("Có lỗi khi getAllFeedBack_ofAllCars_ChuSH: " + t);
            }
        });
    }

    private void isContinue() {
        btn_xacnhanThueXe.setBackgroundResource(R.drawable.custom_btn4);
        btn_xacnhanThueXe.setEnabled(true);
    }

    private void isNotContinue() {
        btn_xacnhanThueXe.setBackgroundResource(R.drawable.disable_custom_btn4);
        btn_xacnhanThueXe.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData_ofUserLogin(userShared.getEmail());
    }
}