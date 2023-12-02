package com.example.fastcar.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fastcar.R;

import java.util.List;

public class Dialog_View_Image {
    public static void showDialog(Context context, List<String> listURL) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_zoom_image);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        ImageView btn_back = dialog.findViewById(R.id.btn_close_imagezoom);
        ImageView img1 = dialog.findViewById(R.id.img_1_inDialogZoom);
        ImageView img2 = dialog.findViewById(R.id.img_2_inDialogZoom);

        btn_back.setOnClickListener(view -> dialog.dismiss());
        if (listURL.size() == 1) {
            Glide.with(context).load(listURL.get(0)).into(img1);
            img2.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(listURL.get(0)).into(img1);
            Glide.with(context).load(listURL.get(1)).into(img2);
        }
    }
}
