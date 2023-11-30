package com.example.fastcar.Activity.ChuXe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastcar.Activity.TaiKhoanNganHang_Activity;
import com.example.fastcar.Adapter.NganHangAdapter;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.FormatString.RandomMaHD;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.NganHang;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViChuXe_Activity extends AppCompatActivity {
    RelativeLayout btnBack;
    String emailUser;
    User user;
    TextView tvSoDu, btnRutTien, btnLSGD;
    List<NganHang> nganHangList = new ArrayList<>();
    LichSuGiaoDich lichSuGiaoDich ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_chu_xe);

        mapping();
        load();

        btnBack.setOnClickListener(view -> onBackPressed());
        btnRutTien.setOnClickListener(view -> {});
        btnLSGD.setOnClickListener(view -> {});

        Intent intent = getIntent();
        fetch_ListNH_ofUser(intent.getStringExtra("emailUser"));

        btnRutTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if (user.getSoDu()>10000){
                 showImageDialog();
             }else {
                 CustomDialogNotify.showToastCustom(getBaseContext(), "Số dư của bạn không đủ để rút");
             }
            }
        });

        btnLSGD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),LichSuGD_Activity.class);
                startActivity(i);
            }
        });
    }

    private void mapping() {
        btnBack = findViewById(R.id.icon_back_in_vichuxe);
        tvSoDu = findViewById(R.id.tv_sodu_vichuxe);
        btnRutTien = findViewById(R.id.btn_RutTien_vichuxe);
        btnLSGD = findViewById(R.id.btn_LSGD_vichuxe);
    }

    private void load() {
        Intent intent = getIntent();
        emailUser = intent.getStringExtra("emailUser");
        fetchData_UserLogin(emailUser);

    }
    private void fetchData_UserLogin(String emailUser) {
        RetrofitClient.FC_services().getListUser(emailUser).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                List<User> list = response.body();
                user = list.get(0);
                tvSoDu.setText(NumberFormatVND.format(user.getSoDu()));
                if(user.getSoDu() == 0) {
                    btnRutTien.setEnabled(false);
                    btnRutTien.setBackgroundResource(R.drawable.disable_custom_btn4);
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch user có email: " + emailUser + " --- " + t);
            }
        });
    }

    public void showImageDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yeu_cau_rut);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        EditText ed_sotien = dialog.findViewById(R.id.ed_soTien);

        TextView tenNH = dialog.findViewById(R.id.tv_tenNganHang);
        TextView tenChu = dialog.findViewById(R.id.tv_tenChuTK_NganHang);
        TextView stk = dialog.findViewById(R.id.tv_STK_NganHang);
        TextView btn_creat = dialog.findViewById(R.id.create);

        tenNH.setText(nganHangList.get(0).getTenNH());
        tenChu.setText(nganHangList.get(0).getTenChuTK());
        stk.setText(formatString(nganHangList.get(0).getSoTK()+""));

        ImageView close = dialog.findViewById(R.id.close_dilog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
        btn_creat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cleanString = ed_sotien.getText().toString().replaceAll("\\.", "");
                int sotiengd = Integer.parseInt(cleanString);
                if(sotiengd>user.getSoDu()){
                    CustomDialogNotify.showToastCustom(getBaseContext(), "Số dư của bạn không đủ để rút");
                }else {
                    String maLSGD = RandomMaHD.random(8)+"";
                    lichSuGiaoDich = new LichSuGiaoDich(null,maLSGD,user,sotiengd,date,"Yêu cầu rút tiền",0,null,nganHangList.get(0));
                    RetrofitClient.FC_services().createLSGD(lichSuGiaoDich).enqueue(new Callback<ResMessage>() {
                        @Override
                        public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {
                            if(response.code()==201){
                                CustomDialogNotify.showToastCustom(getBaseContext(), "Tạo Yêu cầu thành công");
                            }else {
                                System.out.println(response.message());
                            }
                        }
                        @Override
                        public void onFailure(Call<ResMessage> call, Throwable t) {
                            System.out.println("Có lỗi khi fetch user có email: "+ t);
                        }
                    });
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
                    if (!response.body().isEmpty()) {
                        nganHangList = response.body();
                    } else {

                    }
                }
            }
            @Override
            public void onFailure(Call<List<NganHang>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch_ListNH_ofUser(): " + t);

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
}