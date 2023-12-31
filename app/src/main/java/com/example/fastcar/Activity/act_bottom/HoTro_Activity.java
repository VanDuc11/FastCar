package com.example.fastcar.Activity.act_bottom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastcar.Activity.KhachHang.HoaDon_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.R;

public class HoTro_Activity extends AppCompatActivity {
    TextView tv_hotline_html;
    AppCompatButton btn_call, btn_send_message;
    String phone_number = "022266688822";
    private static final int REQUEST_CALL_PERMISSION = 123;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_tro);

        mapping();

        String tv_format = "Cần hỗ trợ nhanh vui lòng gọi: <b>0222 666 888 22</b> hoặc gửi tin nhắn cho chúng tôi.";
        tv_hotline_html.setText(Html.fromHtml(tv_format, Html.FROM_HTML_MODE_LEGACY));

        // gọi điện
        btn_call.setOnClickListener(view -> showDialog());

        // gửi tin nhắn
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_message();
            }
        });
    }

    void mapping() {
        tv_hotline_html = findViewById(R.id.tv_hotline_html);
        btn_call = findViewById(R.id.btn_call_to_hotline);
        btn_send_message = findViewById(R.id.btn_send_message_to_hotline);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // đã cấp quyền
                requestCall();
            } else {
                CustomDialogNotify.showToastCustom(HoTro_Activity.this, "Chưa cấp quyền thực hiện cuộc gọi");
            }
        }
    }

    private void requestCall() {
        String phoneNumberWithDialer = "tel:" + phone_number;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(phoneNumberWithDialer));

        if (intent.resolveActivity(HoTro_Activity.this.getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(HoTro_Activity.this, "Không có ứng dụng gọi điện thoại", Toast.LENGTH_SHORT).show();
        }
    }

    private void request_message() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + phone_number));

        // Kiểm tra xem có ứng dụng tin nhắn nào để xử lý Intent này
        if (intent.resolveActivity(HoTro_Activity.this.getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(HoTro_Activity.this, "Không có ứng dụng tin nhắn", Toast.LENGTH_SHORT).show();
        }

    }

    public void tab3_to_tab1(View view) {
        startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
    }

    public void tab3_to_tab2(View view) {
        startActivity(new Intent(getBaseContext(), ChuyenXe_Activity.class));
    }

    public void tab3_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
    }

    private void showDialog() {
        Dialog dialog = new Dialog(HoTro_Activity.this);
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
                requestCall();
            } else {
                // chưa có quyền, yêu cầu quyền CALL_PHONE từ người dùng
                ActivityCompat.requestPermissions(HoTro_Activity.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}