package com.example.fastcar.Activity.KhachHang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.CreateOrder;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;


public class ThanhToan_Activity extends AppCompatActivity {
    RadioButton rd_momo, rd_zalopay;
    AppCompatButton btn_thanhtoan;
    LinearLayout ln_zalopay, ln_momo;
    TextView tv_tienCoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        mapping();
        load();
    }

    private void mapping() {
        rd_momo = findViewById(R.id.radio_momo);
        rd_zalopay = findViewById(R.id.radio_zalopay);
        btn_thanhtoan = findViewById(R.id.btn_thanhtoan);
        ln_zalopay = findViewById(R.id.ln_zalopay);
        ln_momo = findViewById(R.id.ln_momo);
        tv_tienCoc = findViewById(R.id.tv_tienCoc_inThanhToan);
    }

    private void load() {
        Intent intent = getIntent();
        HoaDon hoaDon = intent.getParcelableExtra("hoadon");

        ln_zalopay.setOnClickListener(view -> {
            rd_zalopay.setChecked(true);
            rd_momo.setChecked(false);
        });

        ln_momo.setOnClickListener(view -> {
            rd_momo.setChecked(true);
            rd_zalopay.setChecked(false);
        });

        if (rd_zalopay.isChecked()) {
            rd_momo.setChecked(false);
        }
        if (rd_momo.isChecked()) {
            rd_zalopay.setChecked(false);
        }

        tv_tienCoc.setText(NumberFormatVND.format(hoaDon.getTienCoc()));

        btn_thanhtoan.setOnClickListener(view -> {
            if (!rd_zalopay.isChecked() && !rd_momo.isChecked()) {
                CustomDialogNotify.showToastCustom(ThanhToan_Activity.this, "Chưa chọn phương thức thanh toán");
            } else {
                if (rd_zalopay.isChecked()) {
                    requestZaloPay(hoaDon);
                } else {
                    CustomDialogNotify.showToastCustom(ThanhToan_Activity.this, "Chức năng thanh toán qua ví Momo đang phát tiển");
                }
            }
        });
    }

    private void requestZaloPay(HoaDon hoaDon) {
        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(String.valueOf(hoaDon.getTienCoc()));
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");

                ZaloPaySDK.getInstance().payOrder(ThanhToan_Activity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        hoaDon.setTrangThaiHD(3);
                        updateTrangThaiHD(hoaDon);
                        showDialog(hoaDon);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        CustomDialogNotify.showToastCustom(ThanhToan_Activity.this, "Bạn đã huỷ thanh toán");
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        CustomDialogNotify.showToastCustom(ThanhToan_Activity.this, "Thanh toán thất bại");
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showDialog(HoaDon hoaDon) {
        LayoutInflater inflater = LayoutInflater.from(ThanhToan_Activity.this);
        View custom = inflater.inflate(R.layout.dialog_thanhtoan_thanhcong, null);
        Dialog dialog = new Dialog(ThanhToan_Activity.this);
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

        TextView tv_content = dialog.findViewById(R.id.tv_content_dialog_tt_success);

        StringBuilder format_Money = NumberFormatVND.format(hoaDon.getTienCoc());
        String maHD = hoaDon.getMaHD();
        String tenXe = hoaDon.getXe().getMauXe();
        String st_html = "Quý khách đã thanh toán thành công <b>" + format_Money + "</b> tiền cọc cho xe "
                + tenXe + "(" + maHD + ")";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_content.setText(Html.fromHtml(st_html, Html.FROM_HTML_MODE_LEGACY));
        }
        AppCompatButton btn_close_dialog = custom.findViewById(R.id.btn_close_dialog_thanhtoanthanhcong);

        btn_close_dialog.setOnClickListener(view -> {
            dialog.dismiss();
            startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
            finish();
        });
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

    public void back_to_HoaDon(View view) {
        onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}