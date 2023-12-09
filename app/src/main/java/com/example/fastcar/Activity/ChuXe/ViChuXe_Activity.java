package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fastcar.Activity.ChuXe.LichSuGiaoDich.LichSuGD_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.FormatString.RandomMaHD;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.NganHang;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.MonthYearPickerDialog;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViChuXe_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RelativeLayout btnBack;
    String emailUser;
    User user;
    TextView tvSoDu, btnRutTien, btnLSGD, btnSelectMonth, tvSLRate, tvSLChuyen, tvTotal, tvRutTien, tvHoanTien, tvNhanTien;
    List<NganHang> nganHangList = new ArrayList<>();
    LichSuGiaoDich lichSuGiaoDich;
    private List<Car> listCars_ofChuSH = new ArrayList<>();
    private Calendar calendar;
    private int year = 0;
    private int month = 0;
    private StringBuilder builder;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_chu_xe);

        mapping();
        load();

        btnBack.setOnClickListener(view -> onBackPressed());

        btnRutTien.setOnClickListener(v -> {
            if (nganHangList.size() == 0) {
                CustomDialogNotify.showToastCustom(getBaseContext(), "Bạn chưa có tài khoản ngân hàng");
            } else {
                if (user.getSoDu() >= 10000) {
                    showImageDialog();
                } else {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Số dư của bạn không đủ");
                }
            }
        });

        btnLSGD.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(), LichSuGD_Activity.class);
            startActivity(i);
        });

        btnSelectMonth.setOnClickListener(view -> {
            MonthYearPickerDialog pd = new MonthYearPickerDialog();
            pd.setListener(this, month, year);
            pd.show(getSupportFragmentManager(), "MonthYearPickerDialog");
        });
    }

    private void mapping() {
        btnBack = findViewById(R.id.icon_back_in_vichuxe);
        tvSoDu = findViewById(R.id.tv_sodu_vichuxe);
        btnRutTien = findViewById(R.id.btn_RutTien_vichuxe);
        btnLSGD = findViewById(R.id.btn_LSGD_vichuxe);
        btnSelectMonth = findViewById(R.id.btn_SelectMonth_filter);
        tvSLRate = findViewById(R.id.tv_sl_nhanxet_vichuxe);
        tvSLChuyen = findViewById(R.id.tv_chuyenhoanthanh_vichuxe);
        tvTotal = findViewById(R.id.tv_tongthaydoi_trongthang);
        tvRutTien = findViewById(R.id.tv_sotien_ruttien);
        tvNhanTien = findViewById(R.id.tv_sotien_tu_chuyenhoanthanh);
        tvHoanTien = findViewById(R.id.tv_sotien_duoc_hoantien);
        progressBar = findViewById(R.id.progressBar_inViChuXe);
    }

    private void load() {
        Intent intent = getIntent();
        emailUser = intent.getStringExtra("emailUser");
        fetchData_UserLogin(emailUser);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        progressBar.setVisibility(View.GONE);
        getListCar_ofChuSH();
    }

    private void fetchData_UserLogin(String emailUser) {
        btnRutTien.setEnabled(false);
        btnRutTien.setBackgroundResource(R.drawable.disable_custom_btn4);

        RetrofitClient.FC_services().getListUser(emailUser).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                if (response.code() == 200) {
                    user = response.body().get(0);
                    tvSoDu.setText(NumberFormatVND.format(user.getSoDu()));
                    if (user.getSoDu() > 0) {
                        btnRutTien.setEnabled(false);
                        btnRutTien.setBackgroundResource(R.drawable.custom_btn4);
                        fetch_ListNH_ofUser(emailUser);
                    } else {
                        btnRutTien.setEnabled(false);
                        btnRutTien.setBackgroundResource(R.drawable.disable_custom_btn4);
                    }
                } else {
                    System.out.println("Có lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch user có email: " + emailUser + " --- " + t);
            }
        });
    }

    private void showImageDialog() {
        LayoutInflater inflater = LayoutInflater.from(ViChuXe_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_yeu_cau_rut, null);
        Dialog dialog = new Dialog(ViChuXe_Activity.this);
        dialog.setContentView(custom);

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

        EditText ed_sotien = dialog.findViewById(R.id.ed_soTien);

        TextView tenNH = dialog.findViewById(R.id.tv_tenNganHang_inDialogRutTien);
        TextView tenChu = dialog.findViewById(R.id.tv_tenChuTK_NganHang_inDialogRutTien);
        TextView stk = dialog.findViewById(R.id.tv_STK_NganHang_inDialogRutTien);
        TextView btn_create = dialog.findViewById(R.id.btn_ruttien);
        ImageView close = dialog.findViewById(R.id.close_dilog);

        tenNH.setText(nganHangList.get(0).getTenNH());
        tenChu.setText(nganHangList.get(0).getTenChuTK());
        stk.setText(formatString(nganHangList.get(0).getSoTK() + ""));

        close.setOnClickListener(v -> dialog.dismiss());

        ed_sotien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String originalText = s.toString().replaceAll("[^\\d]", ""); // Lọc chỉ giữ lại chữ số
                String formattedText = formatText(originalText);
                ed_sotien.removeTextChangedListener(this);
                ed_sotien.setText(formattedText);
                ed_sotien.setSelection(formattedText.length());
                ed_sotien.addTextChangedListener(this);
            }
        });

        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        Date date = new Date();

        btn_create.setOnClickListener(v -> {
            String cleanString = ed_sotien.getText().toString().replaceAll("\\.", "");
            if (cleanString.length() == 0) {
                CustomDialogNotify.showToastCustom(getBaseContext(), "Chưa nhập số tiền");
            } else {
                int sotiengd = Integer.parseInt(cleanString);
                if (sotiengd < 10000) {
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Rút tối thiểu 10.000 VNĐ");
                } else {
                    if (sotiengd > user.getSoDu()) {
                        CustomDialogNotify.showToastCustom(getBaseContext(), "Số dư của bạn không đủ");
                    } else {
                        String maLSGD = RandomMaHD.random(8) + "";
                        lichSuGiaoDich = new LichSuGiaoDich(null, maLSGD, user, sotiengd, date, "Yêu cầu rút tiền", 0, null, nganHangList.get(0), 0, "");
                        RetrofitClient.FC_services().createLSGD(lichSuGiaoDich).enqueue(new Callback<ResMessage>() {
                            @Override
                            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                                if (response.code() == 201) {
                                    CustomDialogNotify.showToastCustom(getBaseContext(), "Tạo yêu cầu rút tiền thành công");
                                    user.setSoDu(user.getSoDu() - sotiengd);
                                    updateSoDu(user, dialog);
                                } else {
                                    System.out.println(response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResMessage> call, Throwable t) {
                                System.out.println("Có lỗi khi createLSGD: " + t);
                            }
                        });
                    }
                }
            }
        });
    }

    private void fetch_ListNH_ofUser(String email) {
        RetrofitClient.FC_services().getListNH_ofUser(email).enqueue(new Callback<List<NganHang>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<NganHang>> call, Response<List<NganHang>> response) {
                if (response.code() == 200) {
                    nganHangList = response.body();
                    btnRutTien.setEnabled(true);
                    btnRutTien.setBackgroundResource(R.drawable.custom_btn4);
                }
            }

            @Override
            public void onFailure(Call<List<NganHang>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch_ListNH_ofUser(): " + t);
                CustomDialogNotify.showToastCustom(getBaseContext(), "Có lỗi xảy ra");
            }
        });
    }

    private String formatText(String originalText) {
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < originalText.length(); i++) {
            formattedText.append(originalText.charAt(i));
            if ((i + 1) % 3 == 0 && i != originalText.length() - 1) {
                formattedText.append(".");
            }
        }
        return formattedText.toString();
    }

    private static String formatString(String input) {
        StringBuilder formatted = new StringBuilder();
        int length = input.length();
        for (int i = 0; i < length; i += 4) {
            int endIndex = Math.min(i + 4, length);
            formatted.append(input, i, endIndex);
            if (endIndex < length) {
                formatted.append(" ");
            }
        }
        return formatted.toString();
    }

    private void updateSoDu(User user, Dialog dialog) {
        RetrofitClient.FC_services().updateSoDu(emailUser, user).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                dialog.dismiss();
                fetchData_UserLogin(emailUser);
            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {
                System.out.println("Có lỗi khi updateSoDu: " + t);
            }
        });
    }

    private void getListCar_ofChuSH() {
        RetrofitClient.FC_services().getListCar_ofUser(emailUser, "0,1,2,3").enqueue(new Callback<List<Car>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.code() == 200) {
                    listCars_ofChuSH = response.body();
                    builder = new StringBuilder();
                    for (Car car : listCars_ofChuSH) {
                        builder.append(car.get_id()).append(",");
                    }

                    if (builder.length() > 0) {
                        builder.deleteCharAt(builder.length() - 1);
                    }

                    getListHoaDon_haveTT6(String.valueOf(builder), month + 1 + "/" + year);
                    getListLSGD_ofUser_inMonth(emailUser, month + 1 + "/" + year);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi getListCar_ofUser(): " + user.getEmail() + " --- " + t);
            }
        });
    }

    private void getListHoaDon_haveTT6(String idCars, String starDate) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.FC_services().getListHoaDon(idCars, "6", starDate, null).enqueue(new Callback<List<HoaDon>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                if (response.code() == 200) {
                    int totalChuyen = 0;
                    int totalRate = 0;
                    for (HoaDon hoaDon : response.body()) {
                        totalChuyen++;
                        if (hoaDon.isHaveFeedback()) {
                            totalRate++;
                        }
                    }

                    tvSLChuyen.setText(totalChuyen + "");
                    tvSLRate.setText(totalRate + "");
                }
            }

            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                System.out.println("Có lỗi khi getListHoaDon_haveTT6: " + t);
            }
        });
    }

    private void getListLSGD_ofUser_inMonth(String email, String startDate) {
        RetrofitClient.FC_services().getLSGD_ofUser(email, null, startDate).enqueue(new Callback<List<LichSuGiaoDich>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<LichSuGiaoDich>> call, Response<List<LichSuGiaoDich>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    int totalRutTien = 0;
                    int totalHoanTien = 0;
                    int totalNhanTien = 0;
                    for (LichSuGiaoDich lsgd : response.body()) {
                        if (lsgd.getTitle() == 1) {
                            // 20%
                            totalNhanTien += lsgd.getSoTienGD();
                        } else if (lsgd.getTitle() == 0) {
                            if (lsgd.getTrangThai() == 1) {
                                // rút tiền + đã duyệt
                                totalRutTien += lsgd.getSoTienGD();
                            }
                        } else {
                            if (lsgd.getTrangThai() == 1) {
                                // hoàn tiền + đã duyệt
                                totalHoanTien += lsgd.getSoTienGD();
                            }
                        }
                    }

                    tvNhanTien.setText(NumberFormatVND.format(totalNhanTien));
                    tvHoanTien.setText(NumberFormatVND.format(totalHoanTien));
                    tvRutTien.setText(NumberFormatVND.format(totalRutTien));
                    if (totalNhanTien - totalRutTien >= 0) {
                        tvTotal.setText("+ " + NumberFormatVND.format(totalNhanTien - totalRutTien));
                    } else {
                        tvTotal.setText("- " + NumberFormatVND.format(Math.abs(totalNhanTien - totalRutTien)));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LichSuGiaoDich>> call, Throwable t) {
                CustomDialogNotify.showToastCustom(ViChuXe_Activity.this, "Có lỗi xảy ra");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int yearF, int monthF, int dayF) {
        btnSelectMonth.setText("Tháng " + monthF + "/" + yearF);
        year = yearF;
        month = monthF - 1;
        String monthStr = "";
        if (monthF < 10) {
            monthStr = "0" + monthF;
        } else {
            monthStr = String.valueOf(monthF);
        }
        getListHoaDon_haveTT6(String.valueOf(builder), monthStr + "/" + yearF);
        getListLSGD_ofUser_inMonth(emailUser, monthStr + "/" + yearF);
        System.out.println(monthF + yearF);
    }
}