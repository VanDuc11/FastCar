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
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.example.fastcar.Adapter.NhanXetAdapter;
import com.example.fastcar.Adapter.XeKhamPhaAdapter;
import com.example.fastcar.Model.Car;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.R;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dialog_InfoChuSH {
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    public static void showDialog(Context context, Car car, List<Car> listXe_ofChuSH, List<FeedBack> listFeedBack , int totalChuyen_ofChuSH) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_dialog_info_chush, null);
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

        ImageView btnBack = dialog.findViewById(R.id.btn_close_dialog_info_chush);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_listxe_indialog_info_chush);
        CircleImageView imgChuXe = dialog.findViewById(R.id.avt_chush_inDialog_info_chush);
        TextView tvTenChuXe = dialog.findViewById(R.id.tv_name_chush_inDialog_info_chush);
        TextView tvNgayThamGia = dialog.findViewById(R.id.tv_ngaythamgia_chush_inDialog_info_chush);
        TextView tvSoChuyen = dialog.findViewById(R.id.tv_sochuyen_ofChuSH_inDialog_info);
        TextView tvTotalXe = dialog.findViewById(R.id.btn_showmore_listxe_ofchush);
        RecyclerView recyclerView_nhanxet = dialog.findViewById(R.id.recyclerView_nhanxet_inDialog_infoChuSH);
        TextView btnViewMore_nhanxet = dialog.findViewById(R.id.tv_xemthem_NhanXet_inDialog_infoChuSH);
        TextView tv_sl_nhanxet = dialog.findViewById(R.id.tv_sl_nhanxet_inDialog_infoChuSH);

        btnBack.setOnClickListener(view -> dialog.dismiss());

        Glide.with(context).load(car.getChuSH().getAvatar()).into(imgChuXe);
        tvTenChuXe.setText(car.getChuSH().getUserName());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvNgayThamGia.setText(dateFormat.format(car.getChuSH().getNgayThamGia()));
        tvSoChuyen.setText(totalChuyen_ofChuSH + " chuyến");
        tvTotalXe.setText("Danh sách xe (" + listXe_ofChuSH.size() + " xe)");
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        XeKhamPhaAdapter adapter = new XeKhamPhaAdapter(context, listXe_ofChuSH);
        recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();
        tvTotalXe.setOnClickListener(view -> Dialog_FullXe_ofChuSH.showDialog(context, listXe_ofChuSH));

        tv_sl_nhanxet.setText(listFeedBack.size() + " nhận xét");
        boolean isShowMore;
        if (listFeedBack.size() > 2) {
            isShowMore = false;
        } else {
            btnViewMore_nhanxet.setVisibility(View.GONE);
            isShowMore = true;
        }
        NhanXetAdapter nhanXetAdapter = new NhanXetAdapter(context, listFeedBack, isShowMore);
        recyclerView_nhanxet.setAdapter(nhanXetAdapter);
        nhanXetAdapter.notifyDataSetChanged();
        btnViewMore_nhanxet.setOnClickListener(view -> Dialog_FullNhanXet.showDialog(context, listFeedBack));
    }
}
