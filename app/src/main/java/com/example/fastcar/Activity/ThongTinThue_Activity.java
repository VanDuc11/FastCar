package com.example.fastcar.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fastcar.R;


public class ThongTinThue_Activity extends AppCompatActivity {
    ImageView ic_back;
    TextView btn_xacnhandatcoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_thue);

        mapping();

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_xacnhandatcoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    void mapping() {
        ic_back = findViewById(R.id.icon_back_in_thongtin_xethue);
        btn_xacnhandatcoc = findViewById(R.id.btn_xacnhan_datcoc);
    }

    void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(ThongTinThue_Activity.this);
        View custom = inflater.inflate(R.layout.dialog_xac_nhan_thue_xe, null);
        Dialog dialog = new Dialog(this);
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
//        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.show();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton btn_datcoc = custom.findViewById(R.id.btn_datcoc_in_dialog);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton btn_thuexekhac = custom.findViewById(R.id.btn_thuexekhac_in_dialog);
        btn_datcoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(getBaseContext(), ThongTinTruocDatCoc_Activity.class));
            }
        });

        btn_thuexekhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(getBaseContext(), DanhSachXe_Activity.class));
            }
        });
    }
}