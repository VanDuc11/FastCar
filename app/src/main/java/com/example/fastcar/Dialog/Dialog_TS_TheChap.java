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
import android.widget.TextView;

import com.example.fastcar.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Dialog_TS_TheChap {
    private static String ktc_text1 = "- Chủ xe hỗ trợ chính sách miễn thế chấp. Khách hàng không cần để lại tài sản (xe máy hoặc 15 triệu tiền mặt) khi thuê xe của chủ xe.";
    private static String ktc_text2 = "- Các phụ phí phát sinh (nếu có) trong quá trình thuê xe, khách hàng vui lòng thanh toán trực tiếp cho chủ xe khi làm thủ tục trả xe.";
    private static String ctc_text1 = "- Bạn sẽ để lại tài sản thế chấp (tiền mặt/chuyển khoản hoặc xe máy kèm giấy tờ gốc) cho chủ xe khi làm thủ tục nhận xe.";
    private static String ctc_text2 = "- Chủ xe sẽ gửi lại tài sản thế chấp khi bạn hoàn trả xe theo như nguyên trạng ban đầu lúc nhận xe.";

    public static void showDialog(Context context, boolean isTheChap) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_taisan_thechap, null);
        dialog.setContentView(v);
        dialog.show();

        ImageView btn_close = dialog.findViewById(R.id.icon_close_dialog_taisan_thechap);
        TextView tv1 = dialog.findViewById(R.id.tv1_dialog_taisan_thechap);
        TextView tv2 = dialog.findViewById(R.id.tv2_dialog_taisan_thechap);
        if (isTheChap) {
            // có thế chấp
            tv1.setText(ctc_text1);
            tv2.setText(ctc_text2);
        } else {
            tv1.setText(ktc_text1);
            tv2.setText(ktc_text2);
        }
        btn_close.setOnClickListener(view -> dialog.dismiss());
    }
}
