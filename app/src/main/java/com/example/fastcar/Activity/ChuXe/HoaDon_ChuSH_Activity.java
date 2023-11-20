package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.KhachHang.ChiTietXe_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.Dialog_BangGiaChiTiet;
import com.example.fastcar.Dialog.Dialog_Coc30Per;
import com.example.fastcar.Dialog.Dialog_TT70Per;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoaDon_ChuSH_Activity extends AppCompatActivity {
    AppCompatButton btn_dongychothue, btn_huychuyen;
    ImageView img_xe, img_viewXe, ic_in_4stt, btn_back;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    TextView btn_xemChiTietGia;
    TextView tv_tenxe, tv_maHD, tv_ngayNhan, tv_ngayTra, tv_diachiXe, tv_tongTien, tv_coc30Per, tv_tt70Per, tvContentInfo, tvSdtKhachHang;
    CircleImageView img_khachhang;
    TextView tv_tenKhachHang, tv_thoiGianThanhToan, stt1, stt2, stt3, stt4, tvGiaoXe, tvGoiChoKhach;
    LinearLayout ln_4stt, ln_view_thoiGianThanhToan, ln_view_huy_or_dongy, ln_sdtKhachHang, ln_giaoxe;
    HoaDon hoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_chu_sh);

        mapping();
        load();

        btn_back.setOnClickListener(view -> onBackPressed());
        btn_dongychothue.setOnClickListener(view -> showDialog_Confirm());
        btn_huychuyen.setOnClickListener(view -> showDialog_HuyChuyen(hoaDon));
    }

    private void mapping() {
        btn_dongychothue = findViewById(R.id.btn_dongy_chothuexe_inHD_ChuSH);
        btn_huychuyen = findViewById(R.id.btn_huychuyen_inHD_ChuSH);
        btn_xemChiTietGia = findViewById(R.id.btn_show_dialog_giachitiet_inHD_ChuSH);
        img_xe = findViewById(R.id.img_xe_inHD_ChuSH);
        img_viewXe = findViewById(R.id.btn_viewXe_fromHD_inHD_ChuSH);
        tv_tenxe = findViewById(R.id.tv_tenxe_inHD_ChuSH);
        tv_maHD = findViewById(R.id.tv_maHD_inHD_ChuSH);
        tv_ngayNhan = findViewById(R.id.tv_ngayNhanXe_inHD_ChuSH);
        tv_ngayTra = findViewById(R.id.tv_ngayTraXe_inHD_ChuSH);
        tv_diachiXe = findViewById(R.id.tv_diachi_nhanxe_inHD_ChuSH);
        tv_tongTien = findViewById(R.id.tv_thanhTien_inHD_ChuSH);
        tv_coc30Per = findViewById(R.id.tv_soTien30Per_inHD_ChuSH);
        tv_tt70Per = findViewById(R.id.tv_soTien70Per_inHD_ChuSH);
        tvContentInfo = findViewById(R.id.tv_contentInfo_inHD_ChuSH);
        img_khachhang = findViewById(R.id.img_avatar_khachhang_inHD_ChuSH);
        tv_tenKhachHang = findViewById(R.id.tv_tenkhachhang_Xe_inHD_ChuSH);
        tvSdtKhachHang = findViewById(R.id.tv_sdtKhachHang_inHD_ChuSH);
        ln_sdtKhachHang = findViewById(R.id.ln_sdtkhachhang_inHD_ChuSH);
        tv_thoiGianThanhToan = findViewById(R.id.tv_thoiGian_thanhtoan_conlai_inHD_ChuSH);
        ln_4stt = findViewById(R.id.ln_4stt_inHD_ChuSH);
        ln_view_thoiGianThanhToan = findViewById(R.id.ln_cho_thanhtoan_inHD_ChuSH);
        ln_view_huy_or_dongy = findViewById(R.id.ln_view_huy_or_dongy_inHD_ChuSH);
        ic_in_4stt = findViewById(R.id.icon_in_4sttHD_inHD_ChuSH);
        stt1 = findViewById(R.id.stt_1_inHD_ChuSH);
        stt2 = findViewById(R.id.stt_2_inHD_ChuSH);
        stt3 = findViewById(R.id.stt_3_inHD_ChuSH);
        stt4 = findViewById(R.id.stt_4_inHD_ChuSH);
        btn_back = findViewById(R.id.btn_back_inHD_ChuSH);
        data_view = findViewById(R.id.data_view_inHoaDon_ChuSH);
        shimmer_view = findViewById(R.id.shimmer_view_inHoaDon_ChuSH);
        tvGiaoXe = findViewById(R.id.btn_giaoxe_chokhach_inHD_ChuSH);
        tvGoiChoKhach = findViewById(R.id.btn_goi_cho_khachhang_inHD_ChuSH);
        ln_giaoxe = findViewById(R.id.ln_giaoxe_inHD_ChuSH);
    }

    private void load() {
        Intent intent = getIntent();
        HoaDon hoadon_intent = intent.getParcelableExtra("hoadon");

        data_view.setVisibility(View.GONE);
        shimmer_view.startShimmerAnimation();

        fetchHoaDon_byMaHD(hoadon_intent.getMaHD());
    }

    private void renderUI() {
        Glide.with(this)
                .load(HostApi.URL_Image + hoaDon.getXe().getHinhAnh().get(0))
                .into(img_xe);
        tv_tenxe.setText(hoaDon.getXe().getMauXe());
        tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe());
        img_viewXe.setOnClickListener(view -> {
            Intent intent1 = new Intent(HoaDon_ChuSH_Activity.this, ChiTietXe_Activity.class);
            intent1.putExtra("car", hoaDon.getXe());
            intent1.putExtra("isMyCar", true);
            startActivity(intent1);
        });
        tv_maHD.setText(hoaDon.getMaHD());
        tv_ngayNhan.setText(hoaDon.getNgayThue());
        tv_ngayTra.setText(hoaDon.getNgayTra());
        tv_tongTien.setText(NumberFormatVND.format(hoaDon.getTongTien()));
        tv_coc30Per.setText(NumberFormatVND.format(hoaDon.getTienCoc()));
        tv_tt70Per.setText(NumberFormatVND.format(hoaDon.getThanhToan()));

        // khách hàng
        if (hoaDon.getUser().getAvatar() != null) {
            Glide.with(this)
                    .load(hoaDon.getUser().getAvatar())
                    .into(img_khachhang);
        } else {
            Glide.with(this)
                    .load(R.drawable.img_avatar_user_v1)
                    .into(img_khachhang);
        }
        tv_tenKhachHang.setText(hoaDon.getUser().getUserName());

        int statusCode = hoaDon.getTrangThaiHD();
        // 0: bị huỷ
        //1: chờ chủ xe duyệt
        //2: duyệt thành công, chờ đặt cọc
        //3: đặt cọc thành công
        //4: chủ xe giao xe thành công
        //5: (hết time thuê, khách mang xe trả cho chủ) khách hàng trả xe thành công
        //6: chủ xe nhận xe thành công ( đối chiếu nếu cần thiết ) = hoàn thành chuyến

        if (statusCode == 0) {
            // bị huỷ
            ln_4stt.setVisibility(View.GONE);
            tv_thoiGianThanhToan.setText(hoaDon.getLyDo());
            ic_in_4stt.setImageResource(R.drawable.icon_car_cancel);
            tv_thoiGianThanhToan.setTextColor(Color.RED);
            ln_view_huy_or_dongy.setVisibility(View.GONE);
            ln_sdtKhachHang.setVisibility(View.GONE);
            ln_giaoxe.setVisibility(View.GONE);
        } else if (statusCode == 1) {
            // chờ chủ xe duyệt
            ic_in_4stt.setImageResource(R.drawable.icon_time_black);
            tv_thoiGianThanhToan.setText("Đang chờ bạn duyệt yêu cầu");
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ln_sdtKhachHang.setVisibility(View.GONE);
            ln_giaoxe.setVisibility(View.GONE);
        } else if (statusCode == 2) {
            // chờ khách hàng đặt cọc
            ic_in_4stt.setImageResource(R.drawable.icon_time_black);
            update_Time(hoaDon.getTimeChuXeXN());
            ln_view_huy_or_dongy.setVisibility(View.GONE);
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            ln_sdtKhachHang.setVisibility(View.GONE);
            ln_giaoxe.setVisibility(View.GONE);
        } else if (statusCode == 3) {
            // đặt cọc thành công
            ln_view_huy_or_dongy.setVisibility(View.GONE);
            tv_thoiGianThanhToan.setText("Khách hàng đã thanh toán thành công");
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ic_in_4stt.setImageResource(R.drawable.icon_dadatcoc);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            stt2.setTextColor(Color.WHITE);
            stt2.setBackgroundResource(R.drawable.custom_btn5);
            tvContentInfo.setVisibility(View.GONE);
            tvSdtKhachHang.setText(hoaDon.getUser().getSDT());
            tvGiaoXe.setText("Giao xe");
        } else if (statusCode == 4) {
            ln_view_huy_or_dongy.setVisibility(View.GONE);
            tv_thoiGianThanhToan.setText("Đã giao xe cho khách hàng");
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ic_in_4stt.setImageResource(R.drawable.icon_car);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            stt2.setTextColor(Color.WHITE);
            stt2.setBackgroundResource(R.drawable.custom_btn5);
            stt3.setTextColor(Color.WHITE);
            stt3.setBackgroundResource(R.drawable.custom_btn5);
            tvContentInfo.setVisibility(View.GONE);
            tvSdtKhachHang.setText(hoaDon.getUser().getSDT());
            tvGiaoXe.setText("Đã giao xe");
            tvGiaoXe.setEnabled(false);
        } else if (statusCode == 5) {
            tv_thoiGianThanhToan.setText("Khách hàng đã trả xe cho bạn");
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ic_in_4stt.setImageResource(R.drawable.icon_car);
            ln_view_huy_or_dongy.setVisibility(View.GONE);
            tvContentInfo.setVisibility(View.GONE);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            stt2.setTextColor(Color.WHITE);
            stt2.setBackgroundResource(R.drawable.custom_btn5);
            stt3.setTextColor(Color.WHITE);
            stt3.setBackgroundResource(R.drawable.custom_btn5);
            tvGiaoXe.setText("Đã nhận được xe?");
        }  else {
            // hoàn thành chuyến xe
            ln_view_huy_or_dongy.setVisibility(View.GONE);
            tv_thoiGianThanhToan.setText("Đã kết thúc");
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ic_in_4stt.setImageResource(R.drawable.icon_hoanthanh);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            stt2.setTextColor(Color.WHITE);
            stt2.setBackgroundResource(R.drawable.custom_btn5);
            stt3.setTextColor(Color.WHITE);
            stt3.setBackgroundResource(R.drawable.custom_btn5);
            stt4.setTextColor(Color.WHITE);
            stt4.setBackgroundResource(R.drawable.custom_btn5);
            tvContentInfo.setVisibility(View.GONE);
            tvSdtKhachHang.setText(hoaDon.getUser().getSDT());
            ln_giaoxe.setVisibility(View.GONE);
        }

        tvGiaoXe.setOnClickListener(view -> {
            if(statusCode == 3) {
                hoaDon.setTrangThaiHD(4);
                updateTrangThaiHD(hoaDon);
            } else if (statusCode == 5) {
                // nếu khách trả xe thành công -> done
                hoaDon.setTrangThaiHD(6);
                updateTrangThaiHD(hoaDon);
            }
        });

        btn_xemChiTietGia.setOnClickListener(view -> Dialog_BangGiaChiTiet.showDialog(this, hoaDon));
    }

    private void fetchHoaDon_byMaHD(String mahd) {
        RetrofitClient.FC_services().getHoaDonbyMaHD(mahd).enqueue(new Callback<List<HoaDon>>() {
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (response.body().size() != 0) {
                        hoaDon = response.body().get(0);
                        renderUI();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                System.out.println("Có lỗi khi getHoaDon: " + t);
            }
        });
    }

    private void updateTrangThaiHD(HoaDon hoaDon) {
        RetrofitClient.FC_services().updateTrangThaiHD(hoaDon.getMaHD(), hoaDon).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    System.out.println("Cập nhật trạng thái hoá đơn " + hoaDon.getMaHD() + " thành công.");
                    load();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Có lỗi khi cập nhật trạng thái hoá đơn " + hoaDon.getMaHD() + "  " + t);
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
            }
        });
    }

    private void update_TimeXacNhanHoaDon(HoaDon hoaDon) {
        RetrofitClient.FC_services().updateTimeXNHD(hoaDon.getMaHD(), hoaDon).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    System.out.println("Cập nhật trạng thái hoá đơn " + hoaDon.getMaHD() + " thành công.");
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Thành công");
                    load();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Có lỗi khi cập nhật trạng thái hoá đơn " + hoaDon.getMaHD() + "  " + t);
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
            }
        });
    }

    private void showDialog_HuyChuyen(HoaDon hoaDon) {
        LayoutInflater inflater = LayoutInflater.from(HoaDon_ChuSH_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_lydo_huychuyen_chush, null);
        Dialog dialog = new Dialog(HoaDon_ChuSH_Activity.this);
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

        ImageView ic_close = dialog.findViewById(R.id.icon_close_dialog_lydo_huychuyen_chush);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_huyChuyen_chush);
        CheckBox ckbox1 = dialog.findViewById(R.id.ckbox1_chush);
        CheckBox ckbox2 = dialog.findViewById(R.id.ckbox2_chush);
        CheckBox ckbox3 = dialog.findViewById(R.id.ckbox3_chush);
        CheckBox ckbox4 = dialog.findViewById(R.id.ckbox4_chush);
        CheckBox ckbox5 = dialog.findViewById(R.id.ckbox5_chush);
        EditText edt_lydoKhac = dialog.findViewById(R.id.edt_lydoKhac_chush);

        CompoundButton.OnCheckedChangeListener checker = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ckbox1.setChecked(compoundButton == ckbox1);
                    ckbox2.setChecked(compoundButton == ckbox2);
                    ckbox3.setChecked(compoundButton == ckbox3);
                    ckbox4.setChecked(compoundButton == ckbox4);
                    ckbox5.setChecked(compoundButton == ckbox5);
                }
            }
        };

        ckbox1.setOnCheckedChangeListener(checker);
        ckbox2.setOnCheckedChangeListener(checker);
        ckbox3.setOnCheckedChangeListener(checker);
        ckbox4.setOnCheckedChangeListener(checker);
        ckbox5.setOnCheckedChangeListener(checker);


        ic_close.setOnClickListener(view -> dialog.dismiss());
        String lydoStr = "Chuyến xe đã bị huỷ bởi chủ xe.\n";
        btn_confirm.setOnClickListener(view -> {
            if (ckbox1.isChecked() || ckbox2.isChecked() || ckbox3.isChecked() || ckbox4.isChecked() || ckbox5.isChecked()) {
                if (ckbox1.isChecked()) {
                    hoaDon.setLyDo(lydoStr + "Lý do: " + ckbox1.getText().toString());
                } else if (ckbox2.isChecked()) {
                    hoaDon.setLyDo(lydoStr + "Lý do: " + ckbox2.getText().toString());
                } else if (ckbox3.isChecked()) {
                    hoaDon.setLyDo(lydoStr + "Lý do: " + ckbox3.getText().toString());
                } else if (ckbox4.isChecked()) {
                    hoaDon.setLyDo(lydoStr + "Lý do: " + ckbox4.getText().toString());
                } else {
//                    edt_lydoKhac.setVisibility(View.VISIBLE);
                    String edt = edt_lydoKhac.getText().toString();
                    String str;
                    if (edt.isEmpty()) {
                        str = "Lý do: " + ckbox5.getText().toString();
                    } else {
                        str = "Lý do khác: " + edt;
                    }
                    hoaDon.setLyDo(lydoStr + str);
                }
                hoaDon.setTrangThaiHD(0);
                updateTrangThaiHD(hoaDon);
                dialog.dismiss();
            } else {
                CustomDialogNotify.showToastCustom(HoaDon_ChuSH_Activity.this, "Vui lòng chọn lý do");
            }
        });
    }

    private void showDialog_Confirm() {
        LayoutInflater inflater = LayoutInflater.from(HoaDon_ChuSH_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_confirm_chothuexe, null);
        Dialog dialog = new Dialog(HoaDon_ChuSH_Activity.this);
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

        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel_chothuexe);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_chothuexe);

        btn_cancel.setOnClickListener(view -> dialog.dismiss());

        btn_confirm.setOnClickListener(view -> {
            // 2 = đồng ý cho thuê
            dialog.dismiss();
            Date getTimeNow = new Date();
            hoaDon.setTrangThaiHD(2);
            hoaDon.setTimeChuXeXN(getTimeNow);
            update_TimeXacNhanHoaDon(hoaDon);
        });
    }

    private void update_Time(Date time) {
        long oneHour = time.getTime() + 3600000; // 1 tiếng

        new CountDownTimer(oneHour - System.currentTimeMillis(), 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                if (hoaDon.getTrangThaiHD() == 3) {
                    cancel();
                    return;
                }
                long seconds = 1000;
                long minutes = seconds * 60;

                long elapsedMinutes = millisUntilFinished / minutes;
                millisUntilFinished = millisUntilFinished % minutes;

                long elapsedSeconds = millisUntilFinished / seconds;

                if (elapsedMinutes < 1) {
                    tv_thoiGianThanhToan.setText("Đang chờ khách hàng thanh toán trong " + elapsedSeconds + " giây");
                } else {
                    tv_thoiGianThanhToan.setText("Đang chờ khách hàng thanh toán trong " + elapsedMinutes + " phút " + elapsedSeconds + " giây");
                }
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                ic_in_4stt.setImageResource(R.drawable.icon_time_red);
                tv_thoiGianThanhToan.setText("Khách hàng đã hết thời gian thanh toán");
                tv_thoiGianThanhToan.setTextColor(Color.RED);
            }
        }.start();

    }

    public void showDialog_DatCoc30Per_inHD_ChuSH(View view) {
        Dialog_Coc30Per.showDialog(this);
    }

    public void showDialog_ThanhToan70Per_inHD_ChuSH(View view) {
        Dialog_TT70Per.showDialog(this);
    }
}