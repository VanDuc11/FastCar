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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.TaiKhoanNganHang_Activity;
import com.example.fastcar.Activity.ThemXe.ThemXe_Activity;
import com.example.fastcar.Activity.MaGiamGia_Activity;
import com.example.fastcar.Activity.ChuXe.XeCuaToi_Activity;
import com.example.fastcar.Activity.XeYeuThich_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Activity.LichSu_ThueXe_Activity;
import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.Activity.ThongTin_User_Activity;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.login.LoginManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaNhan_Activity extends AppCompatActivity {
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    TextView btnInfoUser, btnVoucher, btnLichSuThueXe, btnXeYeuThich, btnDoiMK, btnTaiKhoanNH;
    LinearLayout btnXeCuaToi, btnThemXe;
    AppCompatButton btnLogout;
    CircleImageView avt_user;
    TextView tv_name_user;
    private FirebaseAuth auth;
    private FirebaseUser fBaseuser;
    FirebaseDatabase database;
    String username, phone, email, url;
    Uri uri;
    User user;
    List<Car> listCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ca_nhan);

        mapping();
        load();
        getCar_ofUserLogin();

        // Thông tin cá nhân
        btnInfoUser.setOnClickListener(view -> {
            Intent intent = new Intent(CaNhan_Activity.this, ThongTin_User_Activity.class);
            intent.putExtra("emailUser", email);
            startActivity(intent);
        });

        // Xe yêu thích
        btnXeYeuThich.setOnClickListener(
                view -> startActivity(new Intent(CaNhan_Activity.this, XeYeuThich_Activity.class)));

        // Voucher
        btnVoucher.setOnClickListener(
                view -> {
                    Intent intent = new Intent(CaNhan_Activity.this, MaGiamGia_Activity.class);
                    intent.putExtra("SHOW_ICON_ADD", false);
                    startActivity(intent);
                });

        // Thêm xe
        btnThemXe.setOnClickListener(
                view -> startActivity(new Intent(this, ThemXe_Activity.class)));

        // Tài khoản ngân hàng
        btnTaiKhoanNH.setOnClickListener(view -> {
            Intent intent = new Intent(CaNhan_Activity.this, TaiKhoanNganHang_Activity.class);
            intent.putExtra("emailUser", email);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        // Xe của tôi
        btnXeCuaToi.setOnClickListener(view -> {
            Intent intent = new Intent(this, XeCuaToi_Activity.class);
            intent.putExtra("emailUser", email);
            startActivity(intent);
        });

        // Lịch sử thuê xe
        btnLichSuThueXe.setOnClickListener(view -> startActivity(new Intent(this, LichSu_ThueXe_Activity.class)));

        // Đổi mật khẩu
        btnDoiMK.setOnClickListener(view -> showDialog_DoiMK());

        // Đăng xuất
        btnLogout.setOnClickListener(view -> showDialog_logOut());
    }

    private void mapping() {
        data_view = findViewById(R.id.data_view_inCaNhan);
        shimmer_view = findViewById(R.id.shimmer_view_inCaNhan);
        btnInfoUser = findViewById(R.id.btn_thongtin_canhan_in_tabUser);
        btnVoucher = findViewById(R.id.btn_magiamgia_in_tabUser);
        btnLichSuThueXe = findViewById(R.id.btn_lichsu_ThueXe_in_tabUser);
        btnLogout = findViewById(R.id.btn_logout);
        btnXeYeuThich = findViewById(R.id.btn_xeyeuthich_in_tabUser);
        btnDoiMK = findViewById(R.id.btn_doiMK_in_tabUser);
        avt_user = findViewById(R.id.avt_user_in_user);
        tv_name_user = findViewById(R.id.tv_name_user_in_user);
        btnThemXe = findViewById(R.id.btn_ThemXe_in_tabUser);
        btnXeCuaToi = findViewById(R.id.btn_XeCuaToi_in_tabUser);
        btnTaiKhoanNH = findViewById(R.id.btn_nganhang_in_tabUser);
    }

    private void load() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fBaseuser = auth.getCurrentUser();
        email = fBaseuser.getEmail();

        fetchData_UserLogin(email);
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
        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

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
            logout(email);
            startActivity(new Intent(getBaseContext(), Login_Activity.class));
            finish();

            editor.clear();
            editor.apply();
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
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AppCompatButton btn_confirm_dialog = custom.findViewById(R.id.btn_confirm_DoiMK);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView img_close_dialog = custom.findViewById(R.id.icon_back_in_doiMK);

        img_close_dialog.setOnClickListener(view -> dialog.dismiss());

        btn_confirm_dialog.setOnClickListener(view -> {
            dialog.dismiss();
            CustomDialogNotify.showToastCustom(CaNhan_Activity.this, "Chức năng đang phát triển");
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void fetchData_UserLogin(String emailUser) {
        RetrofitClient.FC_services().getListUser(emailUser).enqueue(new Callback<List<User>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                List<User> list = response.body();
                user = list.get(0);
                username = user.getUserName();
                phone = user.getSDT();
                url = user.getAvatar();

                tv_name_user.setText(username);

                if (url != null) {
                    Glide.with(getBaseContext()).load(url).into(avt_user);
                } else {
                    Glide.with(getBaseContext()).load(R.drawable.img_avatar_user_v1).into(avt_user);
                }

                getCar_ofUserLogin();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("Có lỗi khi fetch user có email: " + emailUser + " --- " + t);
            }
        });
    }

    private void getCar_ofUserLogin() {
        data_view.setVisibility(View.GONE);
        shimmer_view.startShimmerAnimation();

        // get data
        RetrofitClient.FC_services().getListCar_ofUser(email, "0,1,2,3").enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        listCars = response.body();
                        btnThemXe.setVisibility(View.GONE);
                        btnXeCuaToi.setVisibility(View.VISIBLE);
                    }
                } else {
                    listCars = null;
                    btnThemXe.setVisibility(View.VISIBLE);
                    btnXeCuaToi.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                System.out.println("Có lỗi khi getListCar_ofUser(): " + email + " --- " + t);
                listCars = null;
                btnThemXe.setVisibility(View.VISIBLE);
                btnXeCuaToi.setVisibility(View.GONE);
            }
        });
    }

    private void logout(String email) {
        RetrofitClient.FC_services().logout(email).enqueue(new Callback<ResMessage>() {
            @Override
            public void onResponse(Call<ResMessage> call, Response<ResMessage> response) {

            }

            @Override
            public void onFailure(Call<ResMessage> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }
}