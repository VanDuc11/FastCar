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

import com.example.fastcar.Activity.KhachHang.ChiTietXe_Activity;
import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.R;

import java.util.List;

public class Dialog_FullXe_ofChuSH {
    @SuppressLint("NotifyDataSetChanged")
    public static void showDialog(Context context, List<Car> listCars) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_showfull_xe_chush, null);
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

        ImageView btnBack = dialog.findViewById(R.id.btn_close_dialog_full_listxe_chush);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_full_listxe_chush);
        btnBack.setOnClickListener(view -> dialog.dismiss());
        DanhSachXeAdapter danhSachXeAdapter = new DanhSachXeAdapter(context, listCars, null);
        recyclerView.setAdapter(danhSachXeAdapter);
        danhSachXeAdapter.notifyDataSetChanged();
    }
}
