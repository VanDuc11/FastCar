package com.example.fastcar.Activity.KhachHang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.ThemXe.Upload_ImageXe_Activity;
import com.example.fastcar.Activity.act_bottom.HoTro_Activity;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.Dialog_BangGiaChiTiet;
import com.example.fastcar.Dialog.Dialog_Coc30Per;
import com.example.fastcar.Dialog.Dialog_GiayToThueXe;
import com.example.fastcar.Dialog.Dialog_PhuPhi_PhatSinh;
import com.example.fastcar.Dialog.Dialog_TS_TheChap;
import com.example.fastcar.Dialog.Dialog_TT70Per;
import com.example.fastcar.Dialog.Dialog_View_Image;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoaDon_Activity extends AppCompatActivity {
    AppCompatButton btn_datcoc, btn_huychuyen;
    ImageView img_xe, img_viewXe, ic_in_4stt;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    TextView btn_xemChiTietGia, tv_TheChap;
    TextView tv_tenxe, tv_maHD, tv_ngayNhan, tv_ngayTra, tv_diachiXe, tv_tongTien, tv_coc30Per, tv_tt70Per, tvContentInfo, tvSdtChuSH;
    CircleImageView img_chuSH;
    TextView tv_tenChuSH, tv_soSao_ofChuSH, tv_soChuyen_ofChuSH, tv_thoiGianThanhToan, stt1, stt2, stt3, stt4, tvGoiChoChuSH, tvTraXe, tvXemHinhAnh;
    LinearLayout ln_4stt, ln_view_thoiGianThanhToan, ln_view_huy_or_coc, ln_sdtChuSH, ln_traxe;
    HoaDon hoaDon;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;
    float TrungBinhSao;
    private int totalChuyen_ofChuSH;
    private float totalStar_ofChuSH;
    int index = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CALL_PERMISSION = 123;
    private Uri cameraImageUri;
    ImageView img1, img2, ic_add1, ic_add2;
    String pathImage1, pathImage2;
    int finalStar = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);

        mapping();
        load();

        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });

        btn_datcoc.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), ThanhToan_Activity.class);
            intent.putExtra("hoadon", hoaDon);
            startActivity(intent);
        });

        btn_huychuyen.setOnClickListener(view -> showDialog_HuyChuyen(hoaDon));
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
        tv_TheChap = findViewById(R.id.tv_thechap_inHD);
        tvXemHinhAnh = findViewById(R.id.tv_xemhinhanh_inHD);
        tv_tt70Per = findViewById(R.id.tv_soTien70Per_inHD);
        tvContentInfo = findViewById(R.id.tv_contentInfo_inHD);
        img_chuSH = findViewById(R.id.img_avatar_chuSHXe_inHD);
        tv_tenChuSH = findViewById(R.id.tv_tenChuSH_Xe_inHD);
        tv_soSao_ofChuSH = findViewById(R.id.tv_soSao_ofChuSH_Xe_inHD);
        tvSdtChuSH = findViewById(R.id.tv_sdtChuSH_inHD);
        ln_sdtChuSH = findViewById(R.id.ln_sdtChuSH_inHD);
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
        data_view = findViewById(R.id.data_view_inHoaDon);
        refreshLayout = findViewById(R.id.refreshData_inHoaDon);
        shimmer_view = findViewById(R.id.shimmer_view_inHoaDon);
        tvTraXe = findViewById(R.id.btn_traxe_cho_chuSH_inHD);
        tvGoiChoChuSH = findViewById(R.id.btn_goi_cho_chuSH_inHD);
        ln_traxe = findViewById(R.id.ln_traxe_inHD);
        progressBar = findViewById(R.id.progressBar_inHoaDon);
    }

    private void load() {
        Intent intent = getIntent();
        HoaDon hoadon_intent = intent.getParcelableExtra("hoadon");

        progressBar.setVisibility(View.GONE);
        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        getListCar_ofChuSH(hoadon_intent.getXe().getChuSH().getEmail());
        fetchHoaDon_byMaHD(hoadon_intent.getMaHD());
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void renderUI() {
        if (hoaDon != null) {
            Glide.with(this)
                    .load(HostApi.URL_Image + hoaDon.getXe().getHinhAnh().get(0))
                    .into(img_xe);
            tv_tenxe.setText(hoaDon.getXe().getMauXe());
            img_viewXe.setOnClickListener(view -> {
                Intent intent1 = new Intent(HoaDon_Activity.this, ChiTietXe_Activity.class);
                intent1.putExtra("car", hoaDon.getXe());
                intent1.putExtra("isMyCar", false);
                startActivity(intent1);
            });
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tv_maHD.setText(hoaDon.getMaHD());
            tv_ngayNhan.setText(sdf.format(hoaDon.getNgayThue()));
            tv_ngayTra.setText(sdf.format(hoaDon.getNgayTra()));
            tv_tongTien.setText(NumberFormatVND.format(hoaDon.getTongTien()));
            tv_coc30Per.setText(NumberFormatVND.format(hoaDon.getTienCoc()));
            tv_tt70Per.setText(NumberFormatVND.format(hoaDon.getThanhToan()));

            // chủ SH
            if (hoaDon.getXe().getChuSH().getAvatar() != null) {
                Glide.with(this)
                        .load(hoaDon.getXe().getChuSH().getAvatar())
                        .into(img_chuSH);
            } else {
                Glide.with(this)
                        .load(R.drawable.img_avatar_user_v1)
                        .into(img_chuSH);
            }
            tv_tenChuSH.setText(hoaDon.getXe().getChuSH().getUserName());

            int statusCode = hoaDon.getTrangThaiHD();
            // 0: bị huỷ
            //1: chờ chủ xe duyệt
            //2: duyệt thành công, chờ đặt cọc
            //3: đặt cọc thành công
            //4: chủ xe giao xe thành công
            //5: (hết time thuê, khách mang xe trả cho chủ) khách hàng trả xe thành công
            //6: chủ xe nhận xe thành công ( đối chiếu nếu cần thiết ) = hoàn thành chuyến

            // dùng subString để ẩn đi địa chỉ chi tiết
            String diaChiXe = hoaDon.getXe().getDiaChiXe();
            String[] parts = diaChiXe.split(",");
            int lastIndex = parts.length - 1;
            String diachi = null;
            if (lastIndex >= 2) {
                String quanHuyen = parts[lastIndex - 2].trim();
                String thanhPhoTinh = parts[lastIndex - 1].trim();

                diachi = quanHuyen + ", " + thanhPhoTinh;
            }

            if (statusCode == 0) {
                // bị huỷ
                ln_4stt.setVisibility(View.GONE);
                tv_thoiGianThanhToan.setText(hoaDon.getLyDo());
                ic_in_4stt.setImageResource(R.drawable.icon_car_cancel);
                tv_thoiGianThanhToan.setTextColor(Color.RED);
                tv_diachiXe.setText(diachi);
                ln_view_huy_or_coc.setVisibility(View.GONE);
                ln_sdtChuSH.setVisibility(View.GONE);
                ln_traxe.setVisibility(View.GONE);
                tvXemHinhAnh.setVisibility(View.GONE);
            } else if (statusCode == 1) {
                // chờ chủ xe duyệt
                ic_in_4stt.setImageResource(R.drawable.icon_time_black);
                ln_4stt.setVisibility(View.VISIBLE);
                tv_thoiGianThanhToan.setText("Đang chờ chủ xe duyệt yêu cầu");
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                tv_diachiXe.setText(diachi);
                ln_sdtChuSH.setVisibility(View.GONE);
                // vô hiệu hoá button đặt cọc
                btn_datcoc.setBackgroundResource(R.drawable.disable_custom_btn3);
                btn_datcoc.setEnabled(false);
                ln_traxe.setVisibility(View.GONE);
                tvXemHinhAnh.setVisibility(View.GONE);
            } else if (statusCode == 2) {
                // chờ đặt cọc
                ic_in_4stt.setImageResource(R.drawable.icon_time_black);
                ln_4stt.setVisibility(View.VISIBLE);
                update_Time(hoaDon.getTimeChuXeXN());
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                stt1.setTextColor(Color.WHITE);
                stt1.setBackgroundResource(R.drawable.custom_btn5);
                tv_diachiXe.setText(diachi);
                ln_sdtChuSH.setVisibility(View.GONE);
                ln_traxe.setVisibility(View.GONE);
                tvXemHinhAnh.setVisibility(View.GONE);
            } else if (statusCode == 3) {
                // đặt cọc thành công
                ln_4stt.setVisibility(View.VISIBLE);
                btn_datcoc.setVisibility(View.GONE);
                btn_huychuyen.setBackgroundResource(R.drawable.custom_btn7_v1);
                tv_thoiGianThanhToan.setText("Quý khách đã đặt cọc thành công. Vui lòng liên hệ chủ xe theo thông tin bên dưới để tiến hành nhận xe");
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                ic_in_4stt.setImageResource(R.drawable.icon_dadatcoc);
                stt1.setTextColor(Color.WHITE);
                stt1.setBackgroundResource(R.drawable.custom_btn5);
                stt2.setTextColor(Color.WHITE);
                stt2.setBackgroundResource(R.drawable.custom_btn5);
                tv_diachiXe.setText(diaChiXe);
                tvContentInfo.setVisibility(View.GONE);
                tvSdtChuSH.setText(hoaDon.getXe().getChuSH().getSDT());
                ln_traxe.setVisibility(View.VISIBLE);
                tvGoiChoChuSH.setBackgroundResource(R.drawable.custom_btn4);
                tvTraXe.setVisibility(View.GONE);
                tvXemHinhAnh.setVisibility(View.GONE);
            } else if (statusCode == 4) {
                ln_4stt.setVisibility(View.VISIBLE);
                ln_view_huy_or_coc.setVisibility(View.GONE);
                tv_thoiGianThanhToan.setText("Đã nhận xe thành công");
                tvXemHinhAnh.setText("Xem hình ảnh");
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                ic_in_4stt.setImageResource(R.drawable.icon_car);
                stt1.setTextColor(Color.WHITE);
                stt1.setBackgroundResource(R.drawable.custom_btn5);
                stt2.setTextColor(Color.WHITE);
                stt2.setBackgroundResource(R.drawable.custom_btn5);
                stt3.setTextColor(Color.WHITE);
                stt3.setBackgroundResource(R.drawable.custom_btn5);
                tv_diachiXe.setText(diaChiXe);
                tvContentInfo.setVisibility(View.GONE);
                tvSdtChuSH.setText(hoaDon.getXe().getChuSH().getSDT());
                tvTraXe.setText("Trả xe");
            } else if (statusCode == 5) {
                tv_thoiGianThanhToan.setText("Đã trả xe thành công");
                tv_thoiGianThanhToan.setTextColor(Color.BLACK);
                ic_in_4stt.setImageResource(R.drawable.icon_car);
                ln_view_huy_or_coc.setVisibility(View.GONE);
                tvTraXe.setText("Đã trả xe");
                tvXemHinhAnh.setText("Xem hình ảnh");
                tvTraXe.setEnabled(false);
                tvContentInfo.setVisibility(View.GONE);
                stt1.setTextColor(Color.WHITE);
                stt1.setBackgroundResource(R.drawable.custom_btn5);
                stt2.setTextColor(Color.WHITE);
                stt2.setBackgroundResource(R.drawable.custom_btn5);
                stt3.setTextColor(Color.WHITE);
                stt3.setBackgroundResource(R.drawable.custom_btn5);
            } else {
                // hoàn thành chuyến xe
                ln_4stt.setVisibility(View.VISIBLE);
                ln_view_huy_or_coc.setVisibility(View.GONE);
                tv_thoiGianThanhToan.setText("Đã kết thúc");
                tvXemHinhAnh.setVisibility(View.GONE);
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
                tv_diachiXe.setText(diaChiXe);
                tvContentInfo.setVisibility(View.GONE);
                tvSdtChuSH.setText(hoaDon.getXe().getChuSH().getSDT());
                tvGoiChoChuSH.setVisibility(View.GONE);
                tvTraXe.setBackgroundResource(R.drawable.custom_btn4);
                if (!hoaDon.isHaveFeedback()) {
                    tvTraXe.setText("Thêm đánh giá");
                } else {
                    tvTraXe.setText("Xem đánh giá");
                }
            }

            if (hoaDon.getXe().getTheChap()) {
                int number;
                if (hoaDon.getXe().getGiaThue1Ngay() < 1500000) {
                    number = 20;
                } else if (hoaDon.getXe().getGiaThue1Ngay() < 3000000) {
                    number = 30;
                } else {
                    number = 50;
                }
                String text = number + " triệu (tiền mặt/chuyển khoản cho chủ xe khi nhận xe) hoặc Xe máy (kèm giấy tờ gốc) có giá trị tương đương " + number + " triệu.";
                tv_TheChap.setText(text);
            } else {
                tv_TheChap.setText("Miễn thế chấp");
            }

            tvTraXe.setOnClickListener(view -> {
                if (statusCode == 4) {
                    showDialog_UploadImage_TraXe();
                } else if (statusCode == 6) {
                    // user đăng nhận xét, update lại TrungBinhSao
                    if (hoaDon.isHaveFeedback() == false) {
                        // chưa đăng nhận xét
                        showDialog_PostFeedback(false);
                    } else {
                        showDialog_PostFeedback(true);
                    }
                }
            });

            tvXemHinhAnh.setOnClickListener(view -> {
                List<String> listURL = new ArrayList<>();
                if (statusCode == 4) {
                    for (String item : hoaDon.getHinhAnhChuXeGiaoXe()) {
                        listURL.add(HostApi.URL_Image + item);
                    }
                } else if (statusCode == 5) {
                    for (String item : hoaDon.getHinhAnhKhachHangTraXe()) {
                        listURL.add(HostApi.URL_Image + item);
                    }
                }
                Dialog_View_Image.showDialog(this, listURL);
            });

            tvGoiChoChuSH.setOnClickListener(view -> showDialogCallPhoneNumber(hoaDon.getXe().getChuSH().getSDT()));

            btn_xemChiTietGia.setOnClickListener(view -> Dialog_BangGiaChiTiet.showDialog(this, hoaDon));
        }
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
                    tv_thoiGianThanhToan.setText("Đang chờ thanh toán trong " + elapsedSeconds + " giây");
                } else {
                    tv_thoiGianThanhToan.setText("Đang chờ thanh toán trong " + elapsedMinutes + " phút " + elapsedSeconds + " giây");
                }
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                if (hoaDon.getTrangThaiHD() == 2) {
                    ic_in_4stt.setImageResource(R.drawable.icon_time_red);
                    tv_thoiGianThanhToan.setText("Đã hết thời gian thanh toán");
                    tv_thoiGianThanhToan.setTextColor(Color.RED);
                    ln_view_huy_or_coc.setVisibility(View.GONE);

                    // hết time = huỷ chuyến
                    // setTrangThaiHD = 0
                    hoaDon.setTrangThaiHD(0);
                    hoaDon.setLyDo("Chuyến xe đã bị huỷ bởi khách hàng.\nLý do: Hết thời gian thanh toán");
                    updateTrangThaiHD(hoaDon);
                }
            }
        }.start();

    }

    private void updateTrangThaiHD(HoaDon hoaDon) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.FC_services().updateTrangThaiHD(hoaDon.getMaHD(), hoaDon).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
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
        LayoutInflater inflater = LayoutInflater.from(HoaDon_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_lydo_huychuyen, null);
        Dialog dialog = new Dialog(HoaDon_Activity.this);
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
        String lydoStr = "Chuyến xe đã bị huỷ bởi khách hàng.\n";
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
                    String edt = edt_lydoKhac.getText().toString();
                    String str;
                    if (edt.isEmpty()) {
                        str = "Lý do: " + ckbox5.getText().toString();
                    } else {
                        str = "Lý do khác: " + edt;
                    }
                    hoaDon.setLyDo(lydoStr + str);
                }
                if (hoaDon.getTrangThaiHD() == 3) {
                    // đã cọc => huỷ
                    hoaDon.setTrangThaiHD(0);
                    showDialog_HuyChuyen_MatCoc(hoaDon, dialog);
                } else {
                    hoaDon.setTrangThaiHD(0);
                    updateTrangThaiHD(hoaDon);
                    dialog.dismiss();
//                    startActivity(new Intent(HoaDon_Activity.this, KhamPha_Activity.class));
//                    finish();
                }
            } else {
                CustomDialogNotify.showToastCustom(HoaDon_Activity.this, "Vui lòng chọn lý do");
            }
        });
    }

    private void showDialog_PostFeedback(boolean check) {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_post_feedback, null);
        Dialog dialog = new Dialog(HoaDon_Activity.this);
        dialog.setContentView(custom);

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

        ImageView btn_cancel = dialog.findViewById(R.id.btn_close_post_feedback);
        TextView btn_confirm = dialog.findViewById(R.id.btnSave_inDialog_PostFeedBack);
        TextView tv_content = dialog.findViewById(R.id.tv_content_feedback_indialog);
        EditText edt_content = dialog.findViewById(R.id.edt_content_in_dialog_post_feedback);
        ImageView star1 = dialog.findViewById(R.id.star1);
        ImageView star2 = dialog.findViewById(R.id.star2);
        ImageView star3 = dialog.findViewById(R.id.star3);
        ImageView star4 = dialog.findViewById(R.id.star4);
        ImageView star5 = dialog.findViewById(R.id.star5);

        if (check) {
            SharedPreferences preferences = getSharedPreferences("feedback", MODE_PRIVATE);
            int numberStar = preferences.getInt("star_number" + hoaDon.getMaHD(), 0);
            String content = preferences.getString("content" + hoaDon.getMaHD(), "");
            if (numberStar > 0) {
                loadUI_star(numberStar, star1, star2, star3, star4, star5);
            }

            tv_content.setText(content);
            edt_content.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.GONE);

            star1.setEnabled(false);
            star2.setEnabled(false);
            star3.setEnabled(false);
            star4.setEnabled(false);
            star5.setEnabled(false);
        } else {
            tv_content.setVisibility(View.GONE);
        }

        // gán giá trị = 5 sao

        star1.setOnClickListener(view -> {
            finalStar = 1;
            loadUI_star(1, star1, star2, star3, star4, star5);
        });

        star2.setOnClickListener(view -> {
            finalStar = 2;
            loadUI_star(2, star1, star2, star3, star4, star5);
        });

        star3.setOnClickListener(view -> {
            finalStar = 3;
            loadUI_star(3, star1, star2, star3, star4, star5);
        });

        star4.setOnClickListener(view -> {
            finalStar = 4;
            loadUI_star(4, star1, star2, star3, star4, star5);
        });

        star5.setOnClickListener(view -> {
            finalStar = 5;
            loadUI_star(5, star1, star2, star3, star4, star5);
        });

        btn_confirm.setOnClickListener(view -> {
            String content = edt_content.getText().toString().trim();
            Date getTimeNow = new Date();
            FeedBack feedBack = new FeedBack(hoaDon.getUser(), hoaDon.getXe(), content, finalStar, getTimeNow);
            createFeedback(feedBack, dialog);
        });

        btn_cancel.setOnClickListener(view -> dialog.dismiss());
    }

    private void loadUI_star(int numberStar, ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5) {
        if (numberStar == 1) {
            star1.setImageResource(R.drawable.icon_star_v1);
            star2.setImageResource(R.drawable.icon_unstar_v1);
            star3.setImageResource(R.drawable.icon_unstar_v1);
            star4.setImageResource(R.drawable.icon_unstar_v1);
            star5.setImageResource(R.drawable.icon_unstar_v1);
        } else if (numberStar == 2) {
            star1.setImageResource(R.drawable.icon_star_v1);
            star2.setImageResource(R.drawable.icon_star_v1);
            star3.setImageResource(R.drawable.icon_unstar_v1);
            star4.setImageResource(R.drawable.icon_unstar_v1);
            star5.setImageResource(R.drawable.icon_unstar_v1);
        } else if (numberStar == 3) {
            star1.setImageResource(R.drawable.icon_star_v1);
            star2.setImageResource(R.drawable.icon_star_v1);
            star3.setImageResource(R.drawable.icon_star_v1);
            star4.setImageResource(R.drawable.icon_unstar_v1);
            star5.setImageResource(R.drawable.icon_unstar_v1);
        } else if (numberStar == 4) {
            star1.setImageResource(R.drawable.icon_star_v1);
            star2.setImageResource(R.drawable.icon_star_v1);
            star3.setImageResource(R.drawable.icon_star_v1);
            star4.setImageResource(R.drawable.icon_star_v1);
            star5.setImageResource(R.drawable.icon_unstar_v1);
        } else {
            star1.setImageResource(R.drawable.icon_star_v1);
            star2.setImageResource(R.drawable.icon_star_v1);
            star3.setImageResource(R.drawable.icon_star_v1);
            star4.setImageResource(R.drawable.icon_star_v1);
            star5.setImageResource(R.drawable.icon_star_v1);
        }
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
        Dialog_TS_TheChap.showDialog(this, hoaDon.getXe().getTheChap());
    }

    public void showDialog_PhuPhiPhatSinh_inHD(View view) {
        Dialog_PhuPhi_PhatSinh.showDialog(this);
    }

    public void back_inHoaDon(View view) {
        onBackPressed();
    }

    private void getListFeedBack(Car car) {
        TrungBinhSao = 0;
        RetrofitClient.FC_services().getListFeedBack(car.get_id()).enqueue(new Callback<List<FeedBack>>() {
            @Override
            public void onResponse(Call<List<FeedBack>> call, Response<List<FeedBack>> response) {
                List<FeedBack> feedBackList = response.body();
                if (response.code() == 200) {
                    if (feedBackList.size() != 0) {
                        float totalStar = 0;
                        for (FeedBack feedBack : feedBackList) {
                            totalStar += feedBack.getSoSao();
                        }
                        TrungBinhSao = totalStar / feedBackList.size();
                        car.setTrungBinhSao(TrungBinhSao);
                        updateRateXe(car);
                    }

                } else {
                    System.out.println("Có lỗi khi get feedback id: " + car.get_id() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FeedBack>> call, Throwable t) {
                System.out.println("Có lỗi khi get feedback id: " + car.get_id() + " --- " + t);
            }
        });
    }

    private void createFeedback(FeedBack feedBack, Dialog dialog) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.FC_services().createFeedBack(feedBack).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 201) {
                    hoaDon.setHaveFeedback(true);
                    updateBoolean_Feedback_inHoaDon(hoaDon, dialog);
                    getListFeedBack(hoaDon.getXe());
                    SharedPreferences preferences = getSharedPreferences("feedback", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("content" + hoaDon.getMaHD(), feedBack.getNoiDung());
                    editor.putInt("star_number" + hoaDon.getMaHD(), feedBack.getSoSao());
                    editor.apply();
                } else {
                    System.out.println("Có lỗi khi createFeedback(): " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi createFeedback(): " + t);
            }
        });
    }

    private void updateRateXe(Car car) {
        RetrofitClient.FC_services().updateXe(car.get_id(), car).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                if (response.code() == 200) {
                    System.out.println("updateRateXe() success");
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi updateRateXe(): " + t);
            }
        });
    }

    private void getListCar_ofChuSH(String email) {
        totalChuyen_ofChuSH = 0;
        totalStar_ofChuSH = 0;
        RetrofitClient.FC_services().getListCar_ofUser(email, "1").enqueue(new Callback<List<Car>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
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
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi getListCar_ofChuSH: " + t);
            }
        });
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

    private void updateBoolean_Feedback_inHoaDon(HoaDon hoaDon, Dialog dialog) {
        RetrofitClient.FC_services().updateTrangThaiHD(hoaDon.getMaHD(), hoaDon).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    CustomDialogNotify.showToastCustom(HoaDon_Activity.this, "Đã đăng nhận xét");
                    dialog.dismiss();
                    load();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Có lỗi khi updateBoolean_Feedback_inHoaDon: " + t);
            }
        });
    }

    private void showDialog_UploadImage_TraXe() {
        Dialog dialog = new Dialog(HoaDon_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_upload_image_traxe);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        ImageView btnBack = dialog.findViewById(R.id.btn_close_dialog_image_traxe);
        img1 = dialog.findViewById(R.id.img1_traxe_thanhcong);
        img2 = dialog.findViewById(R.id.img2_traxe_thanhcong);
        ic_add1 = dialog.findViewById(R.id.ic_add_img1_traxe_thanhcong);
        ic_add2 = dialog.findViewById(R.id.ic_add_img2_traxe_thanhcong);
        TextView btnSave = dialog.findViewById(R.id.btnSave_Image_traxe);

        btnBack.setOnClickListener(view -> dialog.dismiss());
        btnSave.setOnClickListener(view -> {
            if (pathImage1 == null && pathImage2 == null) {
                CustomDialogNotify.showToastCustom(getBaseContext(), "Vui lòng đăng ít nhất 1 ảnh");
            } else {
                uploadImage_TraXeThanhCong(dialog);
            }
        });

        img1.setOnClickListener(view -> {
            index = 0;
            showImageDialog();
        });

        img2.setOnClickListener(view -> {
            index = 1;
            showImageDialog();
        });
    }

    private void uploadImage_TraXeThanhCong(Dialog dialog) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.FC_services().updateHinhAnh_KhachHangTraXe(hoaDon.getMaHD(), OutImagePaths()).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    CustomDialogNotify.showToastCustom(HoaDon_Activity.this, "Thành công");
                    dialog.dismiss();
                    load();
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi uploadImage_TraXeThanhCong" + t);
            }
        });
    }

    private List<MultipartBody.Part> OutImagePaths() {
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        List<String> listPath = new ArrayList<>();

        if (pathImage1 == null) {
            listPath.add(pathImage2);
        } else if (pathImage2 == null) {
            listPath.add(pathImage1);
        } else {
            listPath.add(pathImage1);
            listPath.add(pathImage2);
        }

        for (String path : listPath) {
            File image = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), image);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("HinhAnhKhachHangTraXe", image.getName(), requestBody);
            imageParts.add(imagePart);
        }
        return imageParts;
    }

    private void showImageDialog() {
        Dialog dialog = new Dialog(HoaDon_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_select_camera_or_library);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView cameraButton = dialog.findViewById(R.id.btn_open_camera);
        TextView libraryButton = dialog.findViewById(R.id.btn_open_library);

        cameraButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkCameraPermission()) {
                    openCamera();
                }
                dialog.dismiss();
            }
        });

        libraryButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkGalleryPermission()) {
                    openGallery();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImageUri = cameraImageUri;
                // Xử lý ảnh được chọn từ thư viện
                if (index == 0) {
                    if (img1 != null) {
                        img1.setImageURI(selectedImageUri);
                        ic_add1.setVisibility(View.GONE);
                        pathImage1 = getImagePath(selectedImageUri);
                    }
                } else {
                    if (img2 != null) {
                        img2.setImageURI(selectedImageUri);
                        ic_add2.setVisibility(View.GONE);
                        pathImage2 = getImagePath(selectedImageUri);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // xử lý hoạt động bị huỷ bỏ
            }
        } else if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImageUri = data.getData();
                if (index == 0) {
                    if (img1 != null) {
                        img1.setImageURI(selectedImageUri);
                        ic_add1.setVisibility(View.GONE);
                        pathImage1 = getImagePath(selectedImageUri);
                    }
                } else {
                    if (img2 != null) {
                        img2.setImageURI(selectedImageUri);
                        ic_add2.setVisibility(View.GONE);
                        pathImage2 = getImagePath(selectedImageUri);
                    }
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            // Xử lý khi hoạt động bị hủy bỏ
        }
    }

    private Uri createImageFile() {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageFile != null) {
            return FileProvider.getUriForFile(this, getPackageName() + ".provider", imageFile);
        } else {
            return null;
        }
    }

    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        } else {
            return uri.getPath();
        }
    }

    private void openCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            cameraImageUri = createImageFile();
            if (cameraImageUri != null) {
                List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(takePicture, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;
                    grantUriPermission(packageName, cameraImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                startActivityForResult(takePicture, REQUEST_CAMERA);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                // Kiểm tra quyền camera
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    CustomDialogNotify.showToastCustom(HoaDon_Activity.this, "Vui lòng cấp quyền cho ứng dụng");
                }
                break;

            case REQUEST_GALLERY:
                // Kiểm tra quyền đọc bộ nhớ ngoại
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    CustomDialogNotify.showToastCustom(HoaDon_Activity.this, "Vui lòng cấp quyền cho ứng dụng");
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
            return false;
        }
        return true;
    }

    private void showDialogCallPhoneNumber(String phone_number) {
        Dialog dialog = new Dialog(HoaDon_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_call_phonenumber);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        TextView tvCall = dialog.findViewById(R.id.btn_call_in_dialog);
        TextView btnback = dialog.findViewById(R.id.btn_close_dialog_call);
        btnback.setOnClickListener(view -> dialog.dismiss());

        tvCall.setText("Gọi " + phone_number);
        tvCall.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                requestCall(phone_number);
            } else {
                // chưa có quyền, yêu cầu quyền CALL_PHONE từ người dùng
                ActivityCompat.requestPermissions(HoaDon_Activity.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            }
        });
    }

    private void requestCall(String phone_number) {
        String phoneNumberWithDialer = "tel:" + phone_number;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(phoneNumberWithDialer));

        if (intent.resolveActivity(HoaDon_Activity.this.getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(HoaDon_Activity.this, "Không có ứng dụng gọi điện thoại", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog_HuyChuyen_MatCoc(HoaDon hoaDon, Dialog dialogOld) {
        LayoutInflater inflater = LayoutInflater.from(HoaDon_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_huychuyen_khidacoc, null);
        Dialog dialog = new Dialog(HoaDon_Activity.this);
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

        TextView tv_nd = dialog.findViewById(R.id.tv_nd_inDialog_HuyChuyenMatCoc);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel_closeDialog_HuyChuyenMatCoc);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_closeDialog_HuyChuyenMatCoc);

        tv_nd.setText("Bạn đã đặt cọc thành công cho chuyến đi này. Nếu bạn đồng ý huỷ, bạn sẽ không được hoàn lại số tiền đã thanh toán");
        btn_cancel.setOnClickListener(view -> dialog.dismiss());
        btn_confirm.setOnClickListener(view -> {
            updateTrangThaiHD(hoaDon);
            dialogOld.dismiss();
            dialog.dismiss();
        });
    }
}