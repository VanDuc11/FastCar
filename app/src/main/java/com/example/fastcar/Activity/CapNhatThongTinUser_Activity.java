package com.example.fastcar.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.User_Method;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapNhatThongTinUser_Activity extends AppCompatActivity {
    EditText edt_hoten, edt_sdt;
    TextView edt_ngaysinh;
    Spinner spinner;
    AppCompatButton btnUpdate;
    Calendar calendar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_thong_tin_user);

        mapping();
        load();

        btnUpdate.setOnClickListener(view -> {
            String username = edt_hoten.getText().toString();
            String ngaysinh = edt_ngaysinh.getText().toString();
            String gioitinh = spinner.getSelectedItem().toString();
            String sdt = edt_sdt.getText().toString();
            if (validateForm(username, sdt, ngaysinh)) {
                User userModel = new User(username, sdt, ngaysinh, gioitinh);
                User_Method.func_updateUser(this, user.getEmail(), userModel, true);
                updateUser_inFirebase(username);
            }
        });
    }

    private void mapping() {
        edt_hoten = findViewById(R.id.edt_hoten_in_ud);
        edt_sdt = findViewById(R.id.edt_sdt_in_ud);
        edt_ngaysinh = findViewById(R.id.edt_ngaysinh_in_ud);
        spinner = findViewById(R.id.spinner_gioittinh_in_ud);
        btnUpdate = findViewById(R.id.btn_update_infoUser);
    }

    private void load() {
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        calendar = Calendar.getInstance();

        edt_hoten.setText(user.getUserName());
        edt_sdt.setText(user.getSDT());
        edt_ngaysinh.setText(user.getNgaySinh());

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Nam");
        listSpinner.add("Nữ");
        listSpinner.add("Khác");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (user.getGioiTinh().equals("")) {
            spinner.setSelection(0);
        } else {
            if (user.getGioiTinh().equals("Nam")) {
                spinner.setSelection(0);
            } else if (user.getGioiTinh().equals("Nữ")) {
                spinner.setSelection(1);
            } else {
                spinner.setSelection(2);
            }
        }
    }

    private void updateUser_inFirebase(String username) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Cập nhật thành công.
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = () -> {
                    onBackPressed();
                    finish();
                };
                handler.postDelayed(() -> handler.post(myRunnable), 1000);
            }
        });
    }

    private boolean validateForm(String username, String sdt, String ngaysinh) {
        if (username.length() == 0) {
            CustomDialogNotify.showToastCustom(CapNhatThongTinUser_Activity.this, "Chưa nhập tên người dùng");
            edt_hoten.requestFocus();
            return false;
        }
        if (sdt.length() == 0) {
            CustomDialogNotify.showToastCustom(CapNhatThongTinUser_Activity.this, "Chưa nhập số điện thoại");
            edt_sdt.requestFocus();
            return false;
        } else {
            if (sdt.length() == 10) {
                if (!validateNumberPhone(edt_sdt.getText().toString().trim())) {
                    CustomDialogNotify.showToastCustom(CapNhatThongTinUser_Activity.this, "Số điện thoại phải bắt đầu bằng 0");
                    edt_sdt.requestFocus();
                    return false;
                }
            } else {
                CustomDialogNotify.showToastCustom(CapNhatThongTinUser_Activity.this, "Số điện thoại phải có đủ 10 số");
                edt_sdt.requestFocus();
                return false;
            }
        }

        if (ngaysinh.length() == 0) {
            CustomDialogNotify.showToastCustom(CapNhatThongTinUser_Activity.this, "Chưa chọn ngày sinh");
            return false;
        }
        return true;
    }

    public void backto_ThongTinUserACT(View view) {
        onBackPressed();
    }

    public void showDatePicker_inUpdateInfoUser(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    edt_ngaysinh.setText(selectedDate);
                }, year, month, day);

        // Giới hạn ngày tối đa
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Hủy", (dialogInterface, i) -> {
            if (i == DialogInterface.BUTTON_NEGATIVE) {
                datePickerDialog.dismiss();
            }
        });
        datePickerDialog.show();
    }

    private boolean validateNumberPhone(String numberphone) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^0([0-9]{9})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(numberphone);
        return matcher.matches();
    }

}