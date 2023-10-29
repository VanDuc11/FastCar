package com.example.fastcar.Activity.act_bottom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.XeYeuThich_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Activity.LichSu_ThueXe_Activity;
import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.Activity.ThongTin_User_Activity;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaNhan_Activity extends AppCompatActivity {
    TextView btnInfoUser, btnVoucher, btnLichSuThueXe, btnDeleteAccount, btnXeYeuThich, btnThemXe, btnDoiMK;
    AppCompatButton btnLogout;
    CircleImageView avt_user;
    TextView tv_name_user;
    private FirebaseAuth auth;
    private FirebaseUser fBaseuser;
    FirebaseDatabase database;
    String username, phone, email;
    Uri uri;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ca_nhan);

        mapping();
        load();

        // Thông tin cá nhân
        btnInfoUser.setOnClickListener(view -> {
            Intent intent = new Intent(CaNhan_Activity.this, ThongTin_User_Activity.class);
            intent.putExtra("image", String.valueOf(uri));
            intent.putExtra("user", user);
            startActivity(intent);
        });

        // Xe yêu thích
        btnXeYeuThich.setOnClickListener(
                view -> startActivity(new Intent(CaNhan_Activity.this, XeYeuThich_Activity.class)));

        // Voucher
        btnVoucher.setOnClickListener(
                view -> CustomDialogNotify.showToastCustom(CaNhan_Activity.this, "Chức năng đang phát triển"));

        // Thêm xe
        btnThemXe.setOnClickListener(
                view -> CustomDialogNotify.showToastCustom(CaNhan_Activity.this, "Chức năng đang phát triển"));

        // Lịch sử thuê xe
        btnLichSuThueXe.setOnClickListener(
                view -> startActivity(new Intent(getBaseContext(), LichSu_ThueXe_Activity.class)));

        // Đổi mật khẩu
        btnDoiMK.setOnClickListener(view -> showDialog_DoiMK());

        // Đăng xuất
        btnLogout.setOnClickListener(view -> showDialog_logOut());

        // Xoá tài khoản
        btnDeleteAccount.setOnClickListener(view -> showDialog_deleteAccount());
    }

    private void mapping() {
        btnInfoUser = findViewById(R.id.btn_thongtin_canhan_in_tabUser);
        btnVoucher = findViewById(R.id.btn_magiamgia_in_tabUser);
        btnLichSuThueXe = findViewById(R.id.btn_lichsu_ThueXe_in_tabUser);
        btnLogout = findViewById(R.id.btn_logout);
        btnDeleteAccount = findViewById(R.id.btn_deleteAccount_in_tabUser);
        btnXeYeuThich = findViewById(R.id.btn_xeyeuthich_in_tabUser);
        btnDoiMK = findViewById(R.id.btn_doiMK_in_tabUser);
        avt_user = findViewById(R.id.avt_user_in_user);
        tv_name_user = findViewById(R.id.tv_name_user_in_user);
        btnThemXe = findViewById(R.id.btn_ThemXe_in_tabUser);
    }

    private void load() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fBaseuser = auth.getCurrentUser();

        String personName = fBaseuser.getDisplayName();
        username = personName == "" ? "UserName" : personName;
        tv_name_user.setText(username);
        uri = fBaseuser.getPhotoUrl();
        email = fBaseuser.getEmail();
        phone = fBaseuser.getPhoneNumber();

        if (uri != null) {
            Glide.with(getBaseContext())
                    .load(uri)
                    .into(avt_user);
        } else {
            Glide.with(getBaseContext())
                    .load(R.drawable.img_avatar_user_v1)
                    .into(avt_user);
        }
    }

    public void tab4_to_tab1(View view) {
        startActivity(new Intent(getBaseContext(), KhamPha_Activity.class));
    }

    public void tab4_to_tab2(View view) {
        startActivity(new Intent(getBaseContext(), ChuyenXe_Activity.class));
    }

    public void tab4_to_tab3(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    private void showDialog_logOut() {
        LayoutInflater inflater = LayoutInflater.from(CaNhan_Activity.this);
        View custom = inflater.inflate(R.layout.dialog_logout, null);
        Dialog dialog = new Dialog(CaNhan_Activity.this);
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

        AppCompatButton btn_confirm_dialog = custom.findViewById(R.id.btn_confirm_logout);
        ImageView img_close_dialog = custom.findViewById(R.id.btn_cancel_dialog_logout);

        img_close_dialog.setOnClickListener(view -> dialog.dismiss());

        btn_confirm_dialog.setOnClickListener(view -> {
            dialog.dismiss();
            auth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getBaseContext(), Login_Activity.class));
            finish();
        });

    }

    private void showDialog_deleteAccount() {
        LayoutInflater inflater = LayoutInflater.from(CaNhan_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_delete_account, null);
        Dialog dialog = new Dialog(CaNhan_Activity.this);
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

        AppCompatButton btn_confirm_dialog = custom.findViewById(R.id.btn_confirm_delete_account);
        ImageView img_close_dialog = custom.findViewById(R.id.btn_cancel_dialog_delete_account);

        img_close_dialog.setOnClickListener(view -> dialog.dismiss());

        btn_confirm_dialog.setOnClickListener(view -> {
            dialog.dismiss();
            CustomDialogNotify.showToastCustom(CaNhan_Activity.this, "Chức năng đang phát triển");
        });
    }

    private void showDialog_DoiMK() {
        LayoutInflater inflater = LayoutInflater.from(CaNhan_Activity.this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_doi_matkhau, null);
        Dialog dialog = new Dialog(CaNhan_Activity.this);
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

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton btn_confirm_dialog = custom.findViewById(R.id.btn_confirm_DoiMK);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView img_close_dialog = custom.findViewById(R.id.btn_close_dialog_doiMK);

        img_close_dialog.setOnClickListener(view -> dialog.dismiss());

        btn_confirm_dialog.setOnClickListener(view -> {
            dialog.dismiss();
            CustomDialogNotify.showToastCustom(CaNhan_Activity.this, "Chức năng đang phát triển");
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void fetchData_UserLogin() {
        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        User userModel = gson.fromJson(userStr, User.class);

        RetrofitClient.FC_services().getListUser(userModel.getEmail()).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> list = response.body();
                user = list.get(0);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch user có email: " + user.getEmail() + " --- " + t);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData_UserLogin();
    }
}