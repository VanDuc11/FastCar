package com.example.fastcar.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.fastcar.Activity.ThemXe.ThemXe_Activity;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.R;

public class Dialog_Thoat_DangKy {

    @SuppressLint("SetTextI18n")
    public static void showDialog(Context context, boolean check) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_thoat_dangky, null);
        Dialog dialog = new Dialog(context);
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

        TextView tv_nd = dialog.findViewById(R.id.tv_nd_inDialog_DangKy);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel_closeDialog_DangKy);
        TextView btn_confirm = dialog.findViewById(R.id.btn_confirm_closeDialog_DangKy);

        btn_cancel.setOnClickListener(view -> dialog.dismiss());

        if (check) {
            tv_nd.setText("Bạn muốn về màn hình chính");
            btn_confirm.setOnClickListener(view -> {
                Intent intent = new Intent(context, KhamPha_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            });

        } else {
            tv_nd.setText("Bạn muốn thoát đăng ký");
            btn_confirm.setOnClickListener(view -> {
                Intent intent = new Intent(context, ThemXe_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            });
        }
    }
}
