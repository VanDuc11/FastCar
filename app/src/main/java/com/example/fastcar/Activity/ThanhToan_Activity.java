package com.example.fastcar.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.fastcar.R;

public class ThanhToan_Activity extends AppCompatActivity {
    RadioGroup rd_group;
    RadioButton rd_momo, rd_zalopay;
    AppCompatButton btn_thanhtoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        mapping();

        rd_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                if(checkedID == R.id.radio_zalopay) {
                    Toast.makeText(ThanhToan_Activity.this, "Bạn chọn thanh toán qua ZaloPay", Toast.LENGTH_SHORT).show();
                } else if (checkedID == R.id.radio_momo) {
                    Toast.makeText(ThanhToan_Activity.this, "Bạn chọn thanh toán qua Momo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ThanhToan_Activity.this, "Bạn chưa chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    void mapping() {
        rd_group = findViewById(R.id.radio_group);
        rd_momo = findViewById(R.id.radio_momo);
        rd_zalopay = findViewById(R.id.radio_zalopay);
        btn_thanhtoan = findViewById(R.id.btn_thanhtoan);
    }

    void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(ThanhToan_Activity.this);
        View custom = inflater.inflate(R.layout.dialog_thanhtoan_thanhcong, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ThanhToan_Activity.this);
        builder.setView(custom);
        builder.setInverseBackgroundForced(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        AppCompatButton btn_close_dialog = custom.findViewById(R.id.btn_close_dialog_thanhtoanthanhcong);

        btn_close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startActivity(new Intent(getBaseContext(), BottomNavigation_Activity.class));
            }
        });
    }
}