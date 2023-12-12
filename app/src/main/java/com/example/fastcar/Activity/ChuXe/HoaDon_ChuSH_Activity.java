package com.example.fastcar.Activity.ChuXe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.KhachHang.ChiTietXe_Activity;
import com.example.fastcar.Activity.KhachHang.HoaDon_Activity;
import com.example.fastcar.Activity.TaiKhoanNganHang_Activity;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Adapter.NganHangAdapter;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.Dialog_BangGiaChiTiet;
import com.example.fastcar.Dialog.Dialog_Coc30Per;
import com.example.fastcar.Dialog.Dialog_TT70Per;
import com.example.fastcar.Dialog.Dialog_View_Image;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.FormatString.RandomMaHD;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.NganHang;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.example.fastcar.Socket.SocketManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoaDon_ChuSH_Activity extends AppCompatActivity {
    AppCompatButton btn_dongychothue, btn_huychuyen;
    ImageView img_xe, img_viewXe, ic_in_4stt, btn_back;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    TextView btn_xemChiTietGia, tvXemHinhAnh;
    TextView tv_tenxe, tv_maHD, tv_ngayNhan, tv_ngayTra, tv_diachiXe, tv_tongTien, tv_coc30Per, tv_tt70Per, tvContentInfo, tvSdtKhachHang;
    CircleImageView img_khachhang;
    SwipeRefreshLayout refreshLayout;
    TextView tv_tenKhachHang, tv_thoiGianThanhToan, stt1, stt2, stt3, stt4, tvGiaoXe, tvGoiChoKhach;
    LinearLayout ln_4stt, ln_view_thoiGianThanhToan, ln_view_huy_or_dongy, ln_sdtKhachHang, ln_giaoxe;
    HoaDon hoaDon, hoadon_intent;
    int index = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CALL_PERMISSION = 123;
    private Uri cameraImageUri;
    ImageView img1, img2, ic_add1, ic_add2;
    String pathImage1, pathImage2;
    private ProgressBar progressBar;
    private WebView webView_loadMap;
    private SocketManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_chu_sh);

        socketManager = KhamPha_Activity.getSocketManager();
        mapping();
        load();

        refreshLayout.setOnRefreshListener(() -> {
            load();
            refreshLayout.setRefreshing(false);
        });

        btn_back.setOnClickListener(view -> onBackPressed());
        btn_dongychothue.setOnClickListener(view -> showDialog_Confirm());
        btn_huychuyen.setOnClickListener(view -> showDialog_HuyChuyen(hoaDon));

        socketManager.on("updateSTT_HD", requestLoadUI_fromSocket);
    }

    private final Emitter.Listener requestLoadUI_fromSocket = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String dataFromSocket = (String) args[0];
            if(dataFromSocket.equals(hoadon_intent.getMaHD())) {
                runOnUiThread(() -> load());
            }
        }
    };

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
        tvXemHinhAnh = findViewById(R.id.tv_xemhinhanh_inHD_ChuSH);
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
        refreshLayout = findViewById(R.id.refresh_data_inHoaDon_ChuSH);
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
        progressBar = findViewById(R.id.progressBar_inHoaDon_chuSH);
        webView_loadMap = findViewById(R.id.webView_loadMap_inHD_ChuSH);
    }

    private void load() {
        Intent intent = getIntent();
        hoadon_intent = intent.getParcelableExtra("hoadon");

        progressBar.setVisibility(View.GONE);
        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        fetchHoaDon_byMaHD(hoadon_intent.getMaHD());
    }

    private void renderUI() {
        if(!isFinishing()) {
            Glide.with(this)
                    .load(HostApi.URL_Image + hoaDon.getXe().getHinhAnh().get(0))
                    .into(img_xe);
            if (hoaDon.getUser().getAvatar() != null) {
                Glide.with(this)
                        .load(hoaDon.getUser().getAvatar())
                        .into(img_khachhang);
            } else {
                Glide.with(this)
                        .load(R.drawable.img_avatar_user_v1)
                        .into(img_khachhang);
            }
        }
        tv_tenxe.setText(hoaDon.getXe().getMauXe());
        tv_diachiXe.setText(hoaDon.getXe().getDiaChiXe());
        img_viewXe.setOnClickListener(view -> {
            Intent intent1 = new Intent(HoaDon_ChuSH_Activity.this, ChiTietXe_Activity.class);
            intent1.putExtra("car", hoaDon.getXe());
            intent1.putExtra("isMyCar", true);
            startActivity(intent1);
        });
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        tv_maHD.setText(hoaDon.getMaHD());
        tv_ngayNhan.setText(sdf.format(hoaDon.getNgayThue()));
        tv_ngayTra.setText(sdf.format(hoaDon.getNgayTra()));
        tv_tongTien.setText(NumberFormatVND.format(hoaDon.getTongTien()));
        tv_coc30Per.setText(NumberFormatVND.format(hoaDon.getTienCoc()));
        tv_tt70Per.setText(NumberFormatVND.format(hoaDon.getThanhToan()));

        // khách hàng
        tv_tenKhachHang.setText(hoaDon.getUser().getUserName());

        // map
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
        loadMap_fromHTML_withMarker(hoaDon.getXe().getLongitude(), hoaDon.getXe().getLatitude());

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
            tvXemHinhAnh.setVisibility(View.GONE);
        } else if (statusCode == 1) {
            // chờ chủ xe duyệt
            ic_in_4stt.setImageResource(R.drawable.icon_time_black);
            tv_thoiGianThanhToan.setText("Đang chờ bạn duyệt yêu cầu");
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ln_sdtKhachHang.setVisibility(View.GONE);
            ln_giaoxe.setVisibility(View.GONE);
            tvXemHinhAnh.setVisibility(View.GONE);
        } else if (statusCode == 2) {
            // chờ khách hàng đặt cọc
            ic_in_4stt.setImageResource(R.drawable.icon_time_black);
            update_Time(hoaDon.getTimeChuXeXN());
            btn_dongychothue.setVisibility(View.GONE);
            btn_huychuyen.setVisibility(View.VISIBLE);
            btn_huychuyen.setBackgroundResource(R.drawable.custom_btn7_v1);
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            ln_sdtKhachHang.setVisibility(View.GONE);
            ln_giaoxe.setVisibility(View.GONE);
            tvXemHinhAnh.setVisibility(View.GONE);
        } else if (statusCode == 3) {
            // đặt cọc thành công
//            ln_view_huy_or_dongy.setVisibility(View.GONE);
            btn_dongychothue.setVisibility(View.GONE);
            btn_huychuyen.setVisibility(View.VISIBLE);
            btn_huychuyen.setBackgroundResource(R.drawable.custom_btn7_v1);
            tv_thoiGianThanhToan.setText("Khách hàng đã thanh toán thành công");
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ic_in_4stt.setImageResource(R.drawable.icon_dadatcoc);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            stt2.setTextColor(Color.WHITE);
            stt2.setBackgroundResource(R.drawable.custom_btn5);
            tvContentInfo.setVisibility(View.GONE);
            ln_sdtKhachHang.setVisibility(View.VISIBLE);
            tvSdtKhachHang.setText(hoaDon.getUser().getSDT());
            ln_giaoxe.setVisibility(View.VISIBLE);
            tvGiaoXe.setText("Giao xe");
            tvXemHinhAnh.setVisibility(View.GONE);
        } else if (statusCode == 4) {
            ln_view_huy_or_dongy.setVisibility(View.GONE);
            tv_thoiGianThanhToan.setText("Đã giao xe cho khách hàng");
            tvXemHinhAnh.setText("Xem hình ảnh");
            tvXemHinhAnh.setVisibility(View.VISIBLE);
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ic_in_4stt.setImageResource(R.drawable.icon_car);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            stt2.setTextColor(Color.WHITE);
            stt2.setBackgroundResource(R.drawable.custom_btn5);
            stt3.setTextColor(Color.WHITE);
            stt3.setBackgroundResource(R.drawable.custom_btn5);
            tvContentInfo.setVisibility(View.GONE);
            ln_sdtKhachHang.setVisibility(View.VISIBLE);
            tvSdtKhachHang.setText(hoaDon.getUser().getSDT());
            tvGiaoXe.setText("Đã giao xe");
            tvGiaoXe.setEnabled(false);
        } else if (statusCode == 5) {
            tv_thoiGianThanhToan.setText("Khách hàng đã trả xe cho bạn");
            tvXemHinhAnh.setText("Xem hình ảnh");
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
            tvGiaoXe.setEnabled(true);
            tvGiaoXe.setText("Đã nhận được xe?");
            ln_sdtKhachHang.setVisibility(View.VISIBLE);
            tvSdtKhachHang.setText(hoaDon.getUser().getSDT());
        } else {
            // hoàn thành chuyến xe
            ln_view_huy_or_dongy.setVisibility(View.GONE);
            tv_thoiGianThanhToan.setText("Đã kết thúc");
            tv_thoiGianThanhToan.setTextColor(Color.BLACK);
            ic_in_4stt.setImageResource(R.drawable.icon_hoanthanh);
            tvXemHinhAnh.setVisibility(View.GONE);
            stt1.setTextColor(Color.WHITE);
            stt1.setBackgroundResource(R.drawable.custom_btn5);
            stt2.setTextColor(Color.WHITE);
            stt2.setBackgroundResource(R.drawable.custom_btn5);
            stt3.setTextColor(Color.WHITE);
            stt3.setBackgroundResource(R.drawable.custom_btn5);
            stt4.setTextColor(Color.WHITE);
            stt4.setBackgroundResource(R.drawable.custom_btn5);
            tvContentInfo.setVisibility(View.GONE);
            ln_sdtKhachHang.setVisibility(View.VISIBLE);
            tvSdtKhachHang.setText(hoaDon.getUser().getSDT());
            ln_giaoxe.setVisibility(View.GONE);
        }

        tvGiaoXe.setOnClickListener(view -> {
            if (statusCode == 3) {
                showDialog_UploadImage_TraXe();
            } else if (statusCode == 5) {
                // nếu khách trả xe thành công -> done
                hoaDon.setTrangThaiHD(6);
                updateTrangThaiHD(hoaDon);
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

        tvGoiChoKhach.setOnClickListener(view -> showDialogCallPhoneNumber(hoaDon.getUser().getSDT()));

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
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.FC_services().updateTrangThaiHD(hoaDon.getMaHD(), hoaDon).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    socketManager.emit("updateSTT_HD", hoaDon.getMaHD());
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

    private void update_TimeXacNhanHoaDon(HoaDon hoaDon) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.FC_services().updateTimeXNHD(hoaDon.getMaHD(), hoaDon).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    socketManager.emit("updateSTT_HD", hoaDon.getMaHD());
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Thành công");
                    load();
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
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
//                    finish();
                }
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
                if (hoaDon.getTrangThaiHD() != 2) {
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

    private void showDialog_UploadImage_TraXe() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View custom = inflater.inflate(R.layout.layout_dialog_upload_image_giaoxe, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(custom);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        ImageView btnBack = dialog.findViewById(R.id.btn_close_dialog_image_giaoxe);
        img1 = dialog.findViewById(R.id.img1_giaoxe_thanhcong);
        img2 = dialog.findViewById(R.id.img2_giaoxe_thanhcong);
        ic_add1 = dialog.findViewById(R.id.ic_add_img1_giaoxe_thanhcong);
        ic_add2 = dialog.findViewById(R.id.ic_add_img2_giaoxe_thanhcong);
        TextView btnSave = dialog.findViewById(R.id.btnSave_Image_GiaoXe);

        btnBack.setOnClickListener(view -> dialog.dismiss());
        btnSave.setOnClickListener(view -> {
            if (pathImage1 == null && pathImage2 == null) {
                CustomDialogNotify.showToastCustom(getBaseContext(), "Vui lòng đăng ít nhất 1 ảnh");
            } else {
                uploadImage_GiaoXeThanhCong(dialog);
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

    private void uploadImage_GiaoXeThanhCong(Dialog dialog) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.FC_services().updateHinhAnh_ChuXeGiaoXe(hoaDon.getMaHD(), OutImagePaths()).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    socketManager.emit("updateSTT_HD", hoaDon.getMaHD());
                    CustomDialogNotify.showToastCustom(HoaDon_ChuSH_Activity.this, "Thành công");
                    dialog.dismiss();
                    load();
                }
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi uploadImage_GiaoXeThanhCong" + t);
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
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("HinhAnhChuXeGiaoXe", image.getName(), requestBody);
            imageParts.add(imagePart);
        }
        return imageParts;
    }

    private void showImageDialog() {
        Dialog dialog = new Dialog(HoaDon_ChuSH_Activity.this);
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
                    CustomDialogNotify.showToastCustom(HoaDon_ChuSH_Activity.this, "Vui lòng cấp quyền cho ứng dụng");
                }
                break;

            case REQUEST_GALLERY:
                // Kiểm tra quyền đọc bộ nhớ ngoại
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    CustomDialogNotify.showToastCustom(HoaDon_ChuSH_Activity.this, "Vui lòng cấp quyền cho ứng dụng");
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
        Dialog dialog = new Dialog(HoaDon_ChuSH_Activity.this);
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
                ActivityCompat.requestPermissions(HoaDon_ChuSH_Activity.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            }
        });
    }

    private void requestCall(String phone_number) {
        String phoneNumberWithDialer = "tel:" + phone_number;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(phoneNumberWithDialer));

        if (intent.resolveActivity(HoaDon_ChuSH_Activity.this.getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(HoaDon_ChuSH_Activity.this, "Không có ứng dụng gọi điện thoại", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog_HuyChuyen_MatCoc(HoaDon hoaDon, Dialog dialogOld) {
        LayoutInflater inflater = LayoutInflater.from(HoaDon_ChuSH_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_huychuyen_khidacoc, null);
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

        TextView tv_nd = dialog.findViewById(R.id.tv_nd_inDialog_HuyChuyenMatCoc);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel_closeDialog_HuyChuyenMatCoc);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_closeDialog_HuyChuyenMatCoc);

        tv_nd.setText("Vì khách hàng đã đặt cọc thành công, nên nếu bạn huỷ chuyến (đồng thời tạo yêu cầu hoàn tiền), khách hàng sẽ được hoàn lại toàn bộ số tiền đã thanh toán");
        btn_cancel.setOnClickListener(view -> dialog.dismiss());
        btn_confirm.setOnClickListener(view -> {
            updateTrangThaiHD(hoaDon);
            dialogOld.dismiss();
            dialog.dismiss();
        });
    }

    private void loadMap_fromHTML_withMarker(String longitude, String latitude) {
        String html = "<html lang=\"en\" style=\"height: 100%;\">\n" +
                "\n" +
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
                "\n" +
                "    </style>\n" +
                "\n" +
                "    <script src='https://cdn.jsdelivr.net/npm/@goongmaps/goong-js@1.0.9/dist/goong-js.js'></script>\n" +
                "    <link href='https://cdn.jsdelivr.net/npm/@goongmaps/goong-js@1.0.9/dist/goong-js.css' rel='stylesheet' />\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"height: 100%; margin: 0;\">\n" +
                "    <div id='map'></div>\n" +
                "\n" +
                "    <script>\n" +
                "        goongjs.accessToken = '" + HostApi.api_key_load_map + "';\n" +
                "        var map = new goongjs.Map({\n" +
                "            container: 'map',\n" +
                "            style: 'https://tiles.goong.io/assets/goong_light_v2.json',\n" +
                "            center: [" + longitude + "," + latitude + "],\n" +
                "            zoom: 15, // starting zoom\n" +
                "            maxZoom: 20,\n" +
                "            minZoom: 15,\n" +
                "            dragRotate: false,\n" +
                "            // dragPan: false\n" +
                "        });\n" +
                "\n" +
                "        // Tạo một Marker và thêm vào bản đồ\n" +
                "        var marker = new goongjs.Marker()\n" +
                "            .setLngLat([" + longitude + "," + latitude + "])\n" +
                "            .addTo(map);\n" +
                "    </script>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        webView_loadMap.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
}