package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fastcar.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CapNhatThongTinUser_Activity extends AppCompatActivity {
    TextInputLayout edt_hoten, edt_email, edt_sdt, edt_ngaysinh;
    Spinner spinner;
    AppCompatButton btnUpdate;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_thong_tin_user);

        mapping();
        calendar = Calendar.getInstance();
    }

    private void mapping() {
        edt_hoten = findViewById(R.id.edt_hoten_in_ud);
        edt_email = findViewById(R.id.edt_email_in_ud);
        edt_sdt = findViewById(R.id.edt_sdt_in_ud);
        edt_ngaysinh = findViewById(R.id.edt_ngaysinh_in_ud);
        spinner = findViewById(R.id.spinner_gioittinh_in_ud);
//        btnUpdate = findViewById(R.id.btn_update_infoUser);
    }

    public void backto_ThongTinUserACT(View view) {
        onBackPressed();
    }

    public void showDatePicker_inUpdateInfoUser(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate =  selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        edt_ngaysinh.getEditText().setText(selectedDate);
                    }
                }, year, month, day);

        // Giới hạn ngày tối đa
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_NEGATIVE) {
                    datePickerDialog.dismiss();
                }
            }
        });

        datePickerDialog.show();
    }
}