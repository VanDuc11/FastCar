package com.example.fastcar.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Model.Car;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;

public class DialogGiayToXe {
    public static void showDialog(Context context, Car car) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_giaytoxe, null);
        Dialog dialog = new Dialog(context);
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

        ImageView btn_back = dialog.findViewById(R.id.icon_back_inDialog_giaytoxe);
        ImageView img_DangKy = dialog.findViewById(R.id.img_dangky_xe_inDialog_giaytoxe);
        ImageView img_DangKiem = dialog.findViewById(R.id.img_dangkiem_xe_inDialog_giaytoxe);
        ImageView img_BaoHiem = dialog.findViewById(R.id.img_baohiem_xe_inDialog_giaytoxe);

        Glide.with(context).load(HostApi.URL_Image + car.getDangKyXe()).into(img_DangKy);
        Glide.with(context).load(HostApi.URL_Image + car.getDangKiem()).into(img_DangKiem);
        Glide.with(context).load(HostApi.URL_Image + car.getBaoHiem()).into(img_BaoHiem);

        btn_back.setOnClickListener(view -> dialog.dismiss());
    }
}
