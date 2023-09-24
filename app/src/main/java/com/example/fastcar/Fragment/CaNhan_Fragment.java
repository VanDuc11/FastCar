package com.example.fastcar.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastcar.Activity.LichSu_ThueXe_Activity;
import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.Activity.ThongTin_User_Activity;
import com.example.fastcar.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class CaNhan_Fragment extends Fragment {
    LinearLayout btnInfoUser, btnLogout, btnVoucher, btnLichSuThueXe, btnDeleteAccount;
    CircleImageView avt_user;
    TextView tv_name_user;

    private FirebaseAuth auth;
    private FirebaseUser fBaseuser ;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ca_nhan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapping(view);

        // Thông tin cá nhân
        btnInfoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ThongTin_User_Activity.class));
            }
        });

        // Voucher

        // Lịch sử thuê xe
        btnLichSuThueXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LichSu_ThueXe_Activity.class));
            }
        });

        // Đăng xuất
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fBaseuser = auth.getCurrentUser();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        // xoá tài khoản
    }

    void mapping(View view) {
        btnInfoUser = view.findViewById(R.id.btn_info_user);
        btnVoucher = view.findViewById(R.id.btn_voucher);
        btnLichSuThueXe = view.findViewById(R.id.btn_lichsu_thuexe);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnDeleteAccount = view.findViewById(R.id.btn_delete_account);
        avt_user = view.findViewById(R.id.avt_user_in_user);
        tv_name_user = view.findViewById(R.id.tv_name_user_in_user);
    }

    void signOut(){
        auth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(getContext(), Login_Activity.class));
        getActivity().finish();
    }
}