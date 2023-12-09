package com.example.fastcar.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Adapter.NhanXetAdapter;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.R;

import java.util.List;

public class Dialog_FullNhanXet {
    @SuppressLint("NotifyDataSetChanged")
    public static void showDialog(Context context, List<FeedBack> listFeedbacks) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_showfull_nhanxet, null);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(custom);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        ImageView btnBack = dialog.findViewById(R.id.btn_close_dialog_fullnhanxet);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_full_nhanxet);

        btnBack.setOnClickListener(view -> dialog.dismiss());

        NhanXetAdapter nhanXetAdapter = new NhanXetAdapter(context, listFeedbacks, true);
        recyclerView.setAdapter(nhanXetAdapter);
        nhanXetAdapter.notifyDataSetChanged();
    }
}
