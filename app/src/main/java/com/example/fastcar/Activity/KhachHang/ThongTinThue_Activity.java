package com.example.fastcar.Activity.KhachHang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.DanhSachXe_Activity;
import com.example.fastcar.Activity.MaGiamGia_Activity;
import com.example.fastcar.Activity.ThongTinGPLX_Activity;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.Dialog_Coc30Per;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Dialog.Dialog_GoiYLoiNhan;
import com.example.fastcar.Dialog.Dialog_PhiDVFC;
import com.example.fastcar.Dialog.Dialog_TS_TheChap;
import com.example.fastcar.Dialog.Dialog_TT70Per;
import com.example.fastcar.Dialog.Dialog_Thoat_DangKy;
import com.example.fastcar.Dialog.Dialog_TienThue_1ngay;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.FormatString.RandomMaHD;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.Model.Voucher;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ThongTinThue_Activity extends AppCompatActivity {
    ImageView ic_back, ic_close_voucher;
    TextView btn_xacnhanThueXe, tv_tenXe, tv_soSao, tv_soChuyen, tv_ngayNhanXe, tv_ngayTraXe, tv_diaChiNhanXe, tv_tienThue1ngay;
    TextView tv_tongTienxSoNgay, tv_tenVoucher, tv_giatriVoucher, btn_Select_Voucher, tv_thanhTien, tv_coc30Per, tv_tt70Per;
    TextView tv_tenChuSH_Xe, tv_soSao_ofChuSH, tv_soChuyen_ofChuSH, tv_maSoXe, tv_phiDV;
    EditText edt_loiNhan;
    CircleImageView img_chuSH_Xe;
    CheckBox ckbox_dieuKhoan;
    LinearLayout ln_view_voucher;
    ShapeableImageView img_Xe;
    Long startTime, endTime;
    long soNgayThueXe;
    Car car;
    User user;
    int giaThue1ngay, phiDVFastCar1ngay, tongPhiDV, tongTien, coc30Per, thanhToan70Per, tongTienGiamGia;
    private int totalChuyen_ofChuSH;
    private float totalStar_ofChuSH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_thue);

        mapping();
        load();

        ic_back.setOnClickListener(view -> onBackPressed());

        btn_xacnhanThueXe.setOnClickListener(view -> createHD_and_showDialog());
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
        tv_coc30Per = findViewById(R.id.tv_soTien30Per_inThongTinThue);
        tv_tt70Per = findViewById(R.id.tv_soTien70Per_inThongTinThue);
        tv_thanhTien = findViewById(R.id.tv_thanhTien_inThongTinThue);
        tv_maSoXe = findViewById(R.id.tv_masoXe_inThongTinThue);
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
    }

    @SuppressLint("SetTextI18n")
    private void load() {
        Intent intent = getIntent();
        car = intent.getParcelableExtra("car");
        soNgayThueXe = intent.getLongExtra("soNgayThueXe", 0);
        totalChuyen_ofChuSH = intent.getIntExtra("chuyen", 0);
        totalStar_ofChuSH = intent.getFloatExtra("stars", 0);

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
        user = gson.fromJson(userStr, User.class);

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

        if(trungbinhSao > 0) {
            DecimalFormat df = new DecimalFormat("0.0");
            String formattedNumber = df.format(trungbinhSao);

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
        if(car.getChuSH().getAvatar() == null ) {
            Glide.with(this)
                    .load(R.drawable.img_avatar_user_v1)
                    .into(img_chuSH_Xe);
        } else {
            Glide.with(this)
                    .load(car.getChuSH().getAvatar())
                    .into(img_chuSH_Xe);
        }

        tv_tenChuSH_Xe.setText(car.getChuSH().getUserName());
        DecimalFormat df = new DecimalFormat("0.0");
        String formattedNumber = df.format(totalStar_ofChuSH);
        tv_soSao_ofChuSH.setText(formattedNumber);
        tv_soChuyen_ofChuSH.setText(totalChuyen_ofChuSH + " chuyến");

        setValue_forDate(startTime, endTime);

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

        ckbox_dieuKhoan.setChecked(true);

    }

    @SuppressLint("SetTextI18n")
    private void tinhTien(int giatriPercent, int giatriMax) {

        giaThue1ngay = car.getGiaThue1Ngay();
        // Phí DV = 5% giá thuê 1 ngày của xe
        phiDVFastCar1ngay = giaThue1ngay * 5 / 100;
        tongTien = (int) ((giaThue1ngay + phiDVFastCar1ngay) * soNgayThueXe);
        tongPhiDV = phiDVFastCar1ngay * (int) soNgayThueXe;

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

    void createHD_and_showDialog() {
        Date getTimeNow = new Date();

        if(user.getTrangThai_GPLX() == 2) {
            // đã xác minh gplx
            String ngayNhan = tv_ngayNhanXe.getText().toString().trim();
            String ngayTra = tv_ngayTraXe.getText().toString().trim();
            String voucher = tv_tenVoucher.getText().toString().trim();
            String maHD = "FCAR" + RandomMaHD.random(5);
            String loiNhan = edt_loiNhan.getText().toString();

            HoaDon hoaDon = new HoaDon(maHD, user, car, ngayNhan, ngayTra, (int) soNgayThueXe, tongPhiDV,
                    voucher, tongTienGiamGia, 0, tongTien, coc30Per, thanhToan70Per, loiNhan, getTimeNow, null, 1, "");

            RetrofitClient.FC_services().createHoaDon(hoaDon).enqueue(new Callback<ResMessage>() {
                @Override
                public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
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
            showDialog_VerifyGPLX();
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

    private void setValue_forDate(Long startTime, Long endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedStartDate = sdf.format(new Date(startTime));
        String formattedEndDate = sdf.format(new Date(endTime));

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
        Dialog_TS_TheChap.showDialog(this);
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

}