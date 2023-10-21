package com.example.fastcar.Dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastcar.R;

public class CustomDialogNotify {

    public static void showToastCustom(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.layout_custom_dialog_notify, null);
        TextView tv_noidungthongnbao = view.findViewById(R.id.tv_noidung_thongbao);

        // build toast
        Toast customToast = new Toast(context);
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(view);

        tv_noidungthongnbao.setText(message);

        // set vị trí hiển thị
        customToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);

        customToast.show();
    }
}
