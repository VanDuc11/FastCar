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
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.fastcar.R;

import java.util.List;

public class Dialog_View_Image {
    public static void showDialog(Context context, List<String> listURL) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View custom = inflater.inflate(R.layout.layout_dialog_zoom_image, null);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(custom);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

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
