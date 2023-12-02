package com.example.fastcar.Dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fastcar.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Dialog_GoiYLoiNhan {

    public static void showDialog(Context context) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_goiyloinhan, null);
        dialog.setContentView(v);
        dialog.show();

        ImageView btn_close = dialog.findViewById(R.id.icon_close_dialog_goiy);
        btn_close.setOnClickListener(view -> {
            dialog.dismiss();
        });

        TextView btn_copy = dialog.findViewById(R.id.btn_copy_goiy);
        TextView tv_goiyloinhan = dialog.findViewById(R.id.tv_goiyloinhan);

        btn_copy.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("data", tv_goiyloinhan.getText().toString());
            clipboardManager.setPrimaryClip(clipData);

            CustomDialogNotify.showToastCustom(context, "Đã sao chép vào bảng nhớ tạm");
            dialog.dismiss();
        });

    }
}

