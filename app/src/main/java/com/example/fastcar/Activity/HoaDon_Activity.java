package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.Dialog_BangGiaChiTiet;
import com.example.fastcar.Dialog.Dialog_Coc30Per;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Dialog.Dialog_PhuPhi_PhatSinh;
import com.example.fastcar.Dialog.Dialog_TS_TheChap;
import com.example.fastcar.Dialog.Dialog_TT70Per;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;

import java.text.ParseException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            showDialog_HuyChuyen(hoaDon);
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
                tv_thoiGianThanhToan.setText("Chuyến xe đã bị huỷ\nLý do: " + hoaDon.getLyDo());
                ic_in_4stt.setImageResource(R.drawable.icon_car_cancel);
                tv_thoiGianThanhToan.setTextColor(Color.RED);
                tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe().substring(indexDC + 2));
            } else if (statusCode == 1) {
                ln_4stt.setVisibility(View.VISIBLE);
                update_Time(hoaDon.getGioTaoHD());
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                stt2.setTextColor(Color.WHITE);
                stt2.setBackgroundResource(R.drawable.custom_btn5);
                tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe().substring(indexDC + 2));
            } else if (statusCode == 2) {
                ln_4stt.setVisibility(View.VISIBLE);
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

                // hết time = huỷ chuyến
                // setTrangThaiHD = 0
                hoaDon.setTrangThaiHD(0);
                hoaDon.setLyDo("Hết thời gian thanh toán");
                updateTrangThaiHD(hoaDon);
            }
        }.start();

    }

    private void updateTrangThaiHD(HoaDon hoaDon) {
        RetrofitClient.FC_services().updateTrangThaiHD(hoaDon.getMaHD(), hoaDon).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Cập nhật trạng thái hoá đơn " + hoaDon.getMaHD() + " thành công.");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Có lỗi khi cập nhật trạng thái hoá đơn " + hoaDon.getMaHD() + "  " + t);
            }
        });
    }

    private void showDialog_HuyChuyen(HoaDon hoaDon) {
        LayoutInflater inflater = LayoutInflater.from(HoaDon_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_lydo_huychuyen, null);
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

        ImageView ic_close = dialog.findViewById(R.id.icon_close_dialog_lydo_huychuyen);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_huyChuyen);
        CheckBox ckbox1 = dialog.findViewById(R.id.ckbox1);
        CheckBox ckbox2 = dialog.findViewById(R.id.ckbox2);
        CheckBox ckbox3 = dialog.findViewById(R.id.ckbox3);
        CheckBox ckbox4 = dialog.findViewById(R.id.ckbox4);
        CheckBox ckbox5 = dialog.findViewById(R.id.ckbox5);
        EditText edt_lydoKhac = dialog.findViewById(R.id.edt_lydoKhac);

        CompoundButton.OnCheckedChangeListener checker = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
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

        btn_confirm.setOnClickListener(view -> {
            if(ckbox1.isChecked() || ckbox2.isChecked() || ckbox3.isChecked() || ckbox4.isChecked() || ckbox5.isChecked()) {
                if(ckbox1.isChecked()) {
                    hoaDon.setLyDo(ckbox1.getText().toString());
                } else if(ckbox2.isChecked()) {
                    hoaDon.setLyDo(ckbox2.getText().toString());
                } else if(ckbox3.isChecked()) {
                    hoaDon.setLyDo(ckbox3.getText().toString());
                } else if(ckbox4.isChecked()) {
                    hoaDon.setLyDo(ckbox4.getText().toString());
                } else {
//                    edt_lydoKhac.setVisibility(View.VISIBLE);
                    String edt = edt_lydoKhac.getText().toString();
                    String str;
                    if(edt.isEmpty()) {
                        str = ckbox5.getText().toString();
                    } else {
                        str = ckbox5.getText().toString() + ": " + edt;
                    }
                    hoaDon.setLyDo(str);
                }
                hoaDon.setTrangThaiHD(0);
                updateTrangThaiHD(hoaDon);

                startActivity(new Intent(HoaDon_Activity.this, KhamPha_Activity.class));
                finish();
            } else {
                CustomDialogNotify.showToastCustom(HoaDon_Activity.this, "Vui lòng chọn lý do");
            }
        });
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