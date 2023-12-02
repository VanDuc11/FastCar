package com.example.fastcar.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.fastcar.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Dialog_PhuPhi_PhatSinh {
    public static void showDialog(Context context) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_phuphi_phatsinh, null);
        dialog.setContentView(v);
        dialog.show();


        ImageView btn_close = dialog.findViewById(R.id.icon_close_dialog_phuphi_phatsinh);
        btn_close.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }
}
