package com.example.fastcar.Activity.ThemXe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fastcar.Activity.act_bottom.CaNhan_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.R;

public class Upload_ImageXe_Activity extends AppCompatActivity {
    RelativeLayout ic_back, ic_close;
    ImageView img_truoc, img_sau, img_trai, img_phai, img_dangky, img_dangkiem, img_baohiem;
    TextView btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_xe);

        mapping();
        load();

        ic_back.setOnClickListener(view -> onBackPressed());

        btn_confirm.setOnClickListener(view -> {
            CustomDialogNotify.showToastCustom(this, "Chức năng đang phát triển");
            startActivity(new Intent(this, CaNhan_Activity.class));
            finish();
        });
    }

    private void mapping() {
        btn_confirm = findViewById(R.id.btn_confirm_ThemXe);
        ic_back = findViewById(R.id.icon_back_in_upload_imageCar);
        ic_close = findViewById(R.id.icon_close_in_upload_imageCar);

    }

    private void load() {

    }
}