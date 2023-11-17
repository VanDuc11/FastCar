package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fastcar.Activity.ThemXe.ThemXe_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.User_Method;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class XeCuaToi_Activity extends AppCompatActivity {
    TextView btn_dsXe, btn_ThemXe, btnThongTinBoSung, btn_ViChuXe, tv_soDu;
    Calendar calendar;
    EditText edt_soCCCD, edt_noicap;
    TextView edt_ngaycap;
    String socccd, ngaycap, noicap, emailUser;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xe_cua_toi);

        mapping();
        load();

        // danh sách xe
        btn_dsXe.setOnClickListener(view -> startActivity(new Intent(this, ThemXe_Activity.class)));

        // thêm xe
        btn_ThemXe.setOnClickListener(view -> startActivity(new Intent(this, ThemXe_Activity.class)));

        // ví chủ xe
        btn_ViChuXe.setOnClickListener(view -> startActivity(new Intent(this, ViChuXe_Activity.class)));

        // bổ sung thông tin
        btnThongTinBoSung.setOnClickListener(view -> showDialog_UpdateTTBS());
    }

    private void mapping() {
        tv_soDu = findViewById(R.id.tv_soDu_inXeCuaToi);
        btn_ThemXe = findViewById(R.id.btn_themXe_inXeCuaToi);
        btn_ViChuXe = findViewById(R.id.btn_viChuXe_inXeCuaToi);
        btn_dsXe = findViewById(R.id.btn_dsXe_inXeCuaToi);
        btnThongTinBoSung = findViewById(R.id.btn_thongtinbosung_inXeCuaToi);
    }

    private void load() {
        calendar = Calendar.getInstance();
        Intent intent = getIntent();
        emailUser = intent.getStringExtra("emailUser");
        fetchData_UserLogin(emailUser);
    }

    public void backFromXeCuaToi_ToCaNhanACT(View view) {
        onBackPressed();
    }

    private void showDialog_UpdateTTBS() {
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_them_cccd, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(custom);
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        ImageView btn_back = dialog.findViewById(R.id.icon_back_in_ttbs);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_inTTBS);
        edt_soCCCD = dialog.findViewById(R.id.edt_soCCCD_inTTBS);
        edt_ngaycap = dialog.findViewById(R.id.edt_ngaycap_inTTBS);
        edt_noicap = dialog.findViewById(R.id.edt_noicap_inTTBS);

        edt_soCCCD.setText(user.getSo_CCCD());
        edt_ngaycap.setText(user.getNgayCap_CCCD());
        edt_noicap.setText(user.getNoiCap_CCCD());

        // back
        btn_back.setOnClickListener(view -> dialog.dismiss());

        edt_ngaycap.setOnClickListener(view -> showDatePicker());
        // confirm
        btn_confirm.setOnClickListener(view -> {
            socccd = edt_soCCCD.getText().toString().trim();
            ngaycap = edt_ngaycap.getText().toString().trim();
            noicap = edt_noicap.getText().toString();

            if (validate_FormCCCD()) {
                // gọi hàm update info user
                User userModel = new User(socccd, ngaycap, noicap);
                User_Method.func_updateUser(this, emailUser, userModel, true);
                load();
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable myRunnable = dialog::dismiss;
                handler.postDelayed(() -> handler.post(myRunnable), 1000);
            }
        });
    }

    private void fetchData_UserLogin(String emailUser) {
        RetrofitClient.FC_services().getListUser(emailUser).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                List<User> list = response.body();
                user = list.get(0);
                tv_soDu.setText(NumberFormatVND.format(user.getSoDu()));
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch user có email: " + emailUser + " --- " + t);
            }
        });
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        edt_ngaycap.setText(selectedDate);
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

    private boolean validate_FormCCCD() {
        if (socccd.equals("")) {
            CustomDialogNotify.showToastCustom(this, "Chưa nhập số CCCD/CMND");
            edt_soCCCD.requestFocus();
            return false;
        } else {
            if (socccd.length() != 12) {
                CustomDialogNotify.showToastCustom(this, "Số CCCD phải đủ 12 số");
                edt_soCCCD.requestFocus();
                return false;
            }
        }

        if (ngaycap.equals("")) {
            CustomDialogNotify.showToastCustom(this, "Chưa chọn ngày cấp");
            return false;
        }

        if (noicap.equals("")) {
            CustomDialogNotify.showToastCustom(this, "Chưa nhập nơi cấp");
            edt_noicap.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData_UserLogin(emailUser);
    }
}