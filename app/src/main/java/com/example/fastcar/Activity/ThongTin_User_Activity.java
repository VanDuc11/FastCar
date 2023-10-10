package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastcar.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThongTin_User_Activity extends AppCompatActivity {
    ImageView btn_edit_info;
    TextView tv_hoten, tv_email, tv_sdt, tv_gioitinh, tv_ngaysinh, tv_ten_hienthi;
    CircleImageView img_avt_user;
    String name, email, phone, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_user);

        mapping();
        load();

        // button edit info user
        btn_edit_info.setOnClickListener( view -> {
            startActivity(new Intent(getBaseContext(), CapNhatThongTinUser_Activity.class));
        });
    }

    private void mapping() {
        tv_hoten = findViewById(R.id.tv_hoten_user_in_thongtinchitiet);
        tv_ngaysinh = findViewById(R.id.tv_ngaysinh_user_in_thongtinchitiet);
        tv_email = findViewById(R.id.tv_email_user_in_thongtinchitiet);
        tv_sdt = findViewById(R.id.tv_sdt_user_in_thongtinchitiet);
        tv_gioitinh = findViewById(R.id.tv_gioitinh_user_in_thongtinchitiet);
        tv_ten_hienthi = findViewById(R.id.tv_name_user_in_thongtinchitiet);
        btn_edit_info = findViewById(R.id.btn_edit_info_user);
        img_avt_user = findViewById(R.id.avt_user_in_thongtinchitiet);
    }

    void load() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        url = intent.getStringExtra("image");

        tv_ten_hienthi.setText(name);
        tv_email.setText(email);

        if(phone == null) {
            tv_sdt.setText("0987654321");
        }
        tv_hoten.setText(name);
        tv_gioitinh.setText("Nam");
        tv_ngaysinh.setText("11/11/2011");


        if (url != null) {
            Glide.with(getBaseContext())
                    .load(url)
                    .into(img_avt_user);
        } else {
            Glide.with(getBaseContext())
                    .load(R.drawable.img_avatar_user)
                    .into(img_avt_user);
        }
    }

    public void backTo_CaNhanACT(View view) {
        onBackPressed();
    }

    public void update_Avatar_User(View view) {

    }
}