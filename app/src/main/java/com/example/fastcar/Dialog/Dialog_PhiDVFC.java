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

public class Dialog_PhiDVFC {
    public static void showDialog(Context context) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_phidv_fastcar, null);
        dialog.setContentView(v);
        dialog.show();


        ImageView btn_close = dialog.findViewById(R.id.icon_close_dialog_phiDV_FastCar);
        btn_close.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }
}
