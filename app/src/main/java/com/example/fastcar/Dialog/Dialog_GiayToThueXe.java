package com.example.fastcar.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.fastcar.R;

public class Dialog_GiayToThueXe {
    public static void showDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_giayto_thuexe);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        ImageView btn_close = dialog.findViewById(R.id.icon_close_dialog_giayto_thuexe);
        btn_close.setOnClickListener(view -> dialog.dismiss());
    }
}
