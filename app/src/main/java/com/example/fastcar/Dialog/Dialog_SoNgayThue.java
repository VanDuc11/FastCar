package com.example.fastcar.Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.fastcar.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Dialog_SoNgayThue {
    public static void showDialog(Context context) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_songaythue, null);
        dialog.setContentView(v);
        dialog.show();

        ImageView btn_close = dialog.findViewById(R.id.icon_close_dialog_songaythue);
        btn_close.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }
}
