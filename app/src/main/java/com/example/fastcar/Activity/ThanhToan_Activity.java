package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;

public class ThanhToan_Activity extends AppCompatActivity {
    RadioButton rd_momo, rd_zalopay;
    AppCompatButton btn_thanhtoan;
    LinearLayout ln_zalopay, ln_momo;
    TextView tv_tienCoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

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

        tv_tienCoc.setText(format_Money_to_VND(hoaDon.getTienCoc()));

        btn_thanhtoan.setOnClickListener(view -> {
            if (!rd_zalopay.isChecked() && !rd_momo.isChecked()) {
                CustomDialogNotify.showToastCustom(ThanhToan_Activity.this, "Chưa chọn phương thức thanh toán");
            } else {
                if (rd_zalopay.isChecked()) {
                    showDialog(hoaDon);
                } else {
                    CustomDialogNotify.showToastCustom(ThanhToan_Activity.this, "Chức năng thanh toán qua ví Momo đang phát tiển");
                }
            }
        });
    }

    void showDialog(HoaDon hoaDon) {
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

        StringBuilder format_Money = format_Money_to_VND(hoaDon.getTienCoc());
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

    private StringBuilder format_Money_to_VND(int money) {
        StringBuilder sb = new StringBuilder(String.valueOf(money));
        int count = 0;
        for (int i = sb.length() - 1; i >= 0; i--) {
            count++;
            if (count % 3 == 0 && i != 0) {
                sb.insert(i, '.');
            }
        }
        sb.append(" đ");
        return sb;
    }

    public void back_to_HoaDon(View view) {
        onBackPressed();
    }
}