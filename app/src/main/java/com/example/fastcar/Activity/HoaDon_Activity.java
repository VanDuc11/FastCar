package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.Dialog_BangGiaChiTiet;
import com.example.fastcar.Dialog.Dialog_Coc30Per;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Dialog.Dialog_PhuPhi_PhatSinh;
import com.example.fastcar.Dialog.Dialog_TS_TheChap;
import com.example.fastcar.Dialog.Dialog_TT70Per;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;

import java.text.ParseException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class HoaDon_Activity extends AppCompatActivity {
    AppCompatButton btn_datcoc, btn_huychuyen;
    ImageView img_xe, img_viewXe, ic_in_4stt;
    TextView btn_xemChiTietGia;
    TextView tv_tenxe, tv_maHD, tv_ngayNhan, tv_ngayTra, tv_diachiXe, tv_tongTien, tv_coc30Per, tv_tt70Per;
    CircleImageView img_chuSH;
    TextView tv_tenChuSH, tv_soSao_ofChuSH, tv_soChuyen_ofChuSH, tv_thoiGianThanhToan, stt1, stt2, stt3, stt4;
    LinearLayout ln_4stt, ln_view_thoiGianThanhToan, ln_view_huy_or_coc;
    HoaDon hoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_truoc_dat_coc);

        mapping();
        load();

        btn_datcoc.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), ThanhToan_Activity.class);
            intent.putExtra("hoadon", hoaDon);
            startActivity(intent);
        });

        btn_huychuyen.setOnClickListener(view -> {
            startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
            finish();
        });
    }

    private void mapping() {
        btn_datcoc = findViewById(R.id.btn_datcoc);
        btn_huychuyen = findViewById(R.id.btn_huychuyen);
        btn_xemChiTietGia = findViewById(R.id.btn_show_dialog_giachitiet_inHD);
        img_xe = findViewById(R.id.img_xe_inHD);
        img_viewXe = findViewById(R.id.btn_viewXe_fromHD);
        tv_tenxe = findViewById(R.id.tv_tenxe_inHD);
        tv_maHD = findViewById(R.id.tv_maHD);
        tv_ngayNhan = findViewById(R.id.tv_ngayNhanXe_inHD);
        tv_ngayTra = findViewById(R.id.tv_ngayTraXe_inHD);
        tv_diachiXe = findViewById(R.id.tv_diachi_nhanxe_inHD);
        tv_tongTien = findViewById(R.id.tv_thanhTien_inHD);
        tv_coc30Per = findViewById(R.id.tv_soTien30Per_inHD);
        tv_tt70Per = findViewById(R.id.tv_soTien70Per_inHD);
        img_chuSH = findViewById(R.id.img_avatar_chuSHXe_inHD);
        tv_tenChuSH = findViewById(R.id.tv_tenChuSH_Xe_inHD);
        tv_soSao_ofChuSH = findViewById(R.id.tv_soSao_ofChuSH_Xe_inHD);
        tv_soChuyen_ofChuSH = findViewById(R.id.tv_soChuyen_ofChuSH_Xe_inHD);
        tv_thoiGianThanhToan = findViewById(R.id.tv_thoiGian_thanhtoan_conlai_inHD);
        ln_4stt = findViewById(R.id.ln_4stt_inHD);
        ln_view_thoiGianThanhToan = findViewById(R.id.ln_cho_thanhtoan_inHD);
        ln_view_huy_or_coc = findViewById(R.id.ln_view_huy_or_coc_inHD);
        ic_in_4stt = findViewById(R.id.icon_in_4sttHD);
        stt1 = findViewById(R.id.stt_1);
        stt2 = findViewById(R.id.stt_2);
        stt3 = findViewById(R.id.stt_3);
        stt4 = findViewById(R.id.stt_4);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void load() {
        Intent intent = getIntent();
        hoaDon = intent.getParcelableExtra("hoadon");
        if (hoaDon != null) {
            Glide.with(this)
                    .load(HostApi.URL_Image + hoaDon.getXe().getHinhAnh().get(0))
                    .into(img_xe);
            tv_tenxe.setText(hoaDon.getXe().getMauXe());
            img_viewXe.setOnClickListener(view -> {
                Intent intent1 = new Intent(HoaDon_Activity.this, ChiTietXe_Activity.class);
                intent1.putExtra("car", hoaDon.getXe());
                startActivity(intent1);
            });
            tv_maHD.setText(hoaDon.getMaHD());
            tv_ngayNhan.setText(hoaDon.getNgayThue());
            tv_ngayTra.setText(hoaDon.getNgayTra());
            tv_tongTien.setText(NumberFormatVND.format(hoaDon.getTongTien()));
            tv_coc30Per.setText(NumberFormatVND.format(hoaDon.getTienCoc()));
            tv_tt70Per.setText(NumberFormatVND.format(hoaDon.getThanhToan()));

            // chủ SH
            tv_tenChuSH.setText(hoaDon.getXe().getChuSH().getUserName());
            tv_soSao_ofChuSH.setText("5.0");
            tv_soChuyen_ofChuSH.setText("22 chuyến");

            int statusCode = hoaDon.getTrangThaiHD();
            // 0: đã huỷ
            // 1: chưa cọc
            // 2: đã cọc
            // 3: đang vận hành
            // 4: hoàn thành đơn

            // dùng subString để ẩn đi địa chỉ chi tiết
            int indexDC = hoaDon.getXe().getDiaChiXe().indexOf(",");

            if (statusCode == 0) {
                ln_4stt.setVisibility(View.GONE);
                ln_view_huy_or_coc.setVisibility(View.GONE);
                tv_thoiGianThanhToan.setText("Quý khách đã huỷ chuyến");
                ic_in_4stt.setVisibility(View.GONE);
                tv_thoiGianThanhToan.setTextColor(Color.RED);
                tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe().substring(indexDC + 2));
            } else if (statusCode == 1) {
                ln_4stt.setVisibility(View.VISIBLE);
                update_Time(hoaDon.getGioTaoHD());
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                stt2.setTextColor(Color.WHITE);
                stt2.setBackgroundResource(R.drawable.custom_btn5);
                tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe());
            } else if (statusCode == 2) {
                ln_4stt.setVisibility(View.VISIBLE);
                ln_view_huy_or_coc.setVisibility(View.VISIBLE);
                ln_view_huy_or_coc.setVisibility(View.GONE);
                tv_thoiGianThanhToan.setText("Quý khách đã đặt cọc thành công. Vui lòng liên hệ chủ xe theo thông tin bên dưới để tiến hành nhận xe");
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                ic_in_4stt.setImageResource(R.drawable.icon_dadatcoc);
                stt3.setTextColor(Color.WHITE);
                stt3.setBackgroundResource(R.drawable.custom_btn5);
                tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe());
            } else if (statusCode == 3) {
                ln_4stt.setVisibility(View.VISIBLE);
                ln_view_huy_or_coc.setVisibility(View.GONE);
                tv_thoiGianThanhToan.setText("Chuyến xe của quý khách đang khởi hành");
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                ic_in_4stt.setImageResource(R.drawable.icon_car);
                stt3.setTextColor(Color.WHITE);
                stt3.setBackgroundResource(R.drawable.custom_btn5);
                tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe());
            } else {
                ln_4stt.setVisibility(View.VISIBLE);
                ln_view_huy_or_coc.setVisibility(View.GONE);
                tv_thoiGianThanhToan.setText("Đã kết thúc");
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                ic_in_4stt.setImageResource(R.drawable.icon_hoanthanh);
                stt4.setTextColor(Color.WHITE);
                stt4.setBackgroundResource(R.drawable.custom_btn5);
                tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe());
            }

            btn_xemChiTietGia.setOnClickListener(view -> Dialog_BangGiaChiTiet.showDialog(this, hoaDon));
        }
    }

    private void update_Time(Date time) {
        long oneHour = time.getTime() + 3600000; // 1 tiếng

        new CountDownTimer(oneHour - System.currentTimeMillis(), 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                long seconds = 1000;
                long minutes = seconds * 60;

                long elapsedMinutes = millisUntilFinished / minutes;
                millisUntilFinished = millisUntilFinished % minutes;

                long elapsedSeconds = millisUntilFinished / seconds;

                if(elapsedMinutes < 1) {
                    tv_thoiGianThanhToan.setText("Đang chờ thanh toán trong " + elapsedSeconds + " giây");
                } else {
                    tv_thoiGianThanhToan.setText("Đang chờ thanh toán trong " + elapsedMinutes + " phút " + elapsedSeconds + " giây");
                }
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                ic_in_4stt.setImageResource(R.drawable.icon_time_red);
                tv_thoiGianThanhToan.setText("Đã hết thời gian thanh toán");
                tv_thoiGianThanhToan.setTextColor(Color.RED);
                ln_view_huy_or_coc.setVisibility(View.GONE);

                int indexDC = hoaDon.getXe().getDiaChiXe().indexOf(",");
                tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe().substring(indexDC + 2));

                // hết time = huỷ chuyến
                // setTrangThaiHD = 0
            }
        }.start();

    }

    public void showDialog_DatCoc30Per_inHD(View view) {
        Dialog_Coc30Per.showDialog(this);
    }

    public void showDialog_ThanhToan70Per_inHD(View view) {
        Dialog_TT70Per.showDialog(this);
    }

    public void showDialog_GiayToThueXe_inHD(View view) {
        Dialog_GiayToThueXe.showDialog(this);
    }

    public void showDialog_TSTheChap_inHD(View view) {
        Dialog_TS_TheChap.showDialog(this);
    }

    public void showDialog_PhuPhiPhatSinh_inHD(View view) {
        Dialog_PhuPhi_PhatSinh.showDialog(this);
    }

    public void back_inHoaDon(View view) {
        onBackPressed();
    }
}