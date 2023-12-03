package com.example.fastcar.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.KhachHang.HoaDon_Activity;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.ThongBao;
import com.example.fastcar.Model.Voucher;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHoder> {
    Context context;
    List<ThongBao> listThongBao;

    public ThongBaoAdapter(Context context, List<ThongBao> listThongBao) {
        this.context = context;
        this.listThongBao = listThongBao;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tv_tieude, tv_thoigian, tv_noidung;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_tieude = itemView.findViewById(R.id.tv_tieude_thongbao);
            tv_noidung = itemView.findViewById(R.id.tv_noidung_thongbao);
            tv_thoigian = itemView.findViewById(R.id.time_thongbao);
        }
    }

    @NonNull
    @Override
    public ThongBaoAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item1_thongbao, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoAdapter.ViewHoder holder, int position) {
        ThongBao item = listThongBao.get(position);
        holder.tv_tieude.setText(item.getTieuDe());
        holder.tv_noidung.setText(item.getNoiDung());
        holder.tv_thoigian.setText(getTimeDifference(item.getCreatedAt()));
        holder.itemView.setOnClickListener(view -> showDialog_DetailNotify(item));
    }

    @Override
    public int getItemCount() {
        return listThongBao.size();
    }

    private static String getTimeDifference(Date endDate) {
        Date startDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        long duration = Math.abs(startDate.getTime() - endDate.getTime());
        long minutes = duration / (60 * 1000);
        long hours = 0;
        if (minutes > 60) {
            hours = minutes / 60;
            minutes = minutes % 60;
        }

        int day = (int) (hours / 24);
        int week = (int) (day / 7);

        if (hours <= 1) {
            if (minutes < 1) {
                return duration / 1000 + " giây trước";
            } else {
                return minutes + " phút trước";
            }
        }
        if (hours < 24) {
            return hours + " giờ trước";
        } else if (day <= 7) {
            return (hours >= 48) ? day + " ngày trước" : "hôm qua";
        } else {
            return (week <= 4) ? week + " tuần trước" : sdf.format(endDate);
        }
    }

    private void showDialog_DetailNotify(ThongBao thongBao) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View custom = inflater.inflate(R.layout.dialog_detail_notify, null);
        Dialog dialog = new Dialog(context);
        dialog.setContentView(custom);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();

        TextView tvNoidung, tvTieude;
        RelativeLayout ic_close = dialog.findViewById(R.id.icon_close_dialog_detail_notify);
        ImageView img = dialog.findViewById(R.id.img_inDetail_notify);
        tvTieude = dialog.findViewById(R.id.tv_tieude_thongbao_inDialog_detail);
        tvNoidung = dialog.findViewById(R.id.tv_noidung_thongbao_inDialog_detail);

        ic_close.setOnClickListener(view -> dialog.dismiss());

        Glide.with(context).load(HostApi.URL_Image + thongBao.getHinhAnh()).into(img);

        tvTieude.setText(thongBao.getTieuDe());
        tvNoidung.setText(thongBao.getNoiDung());
    }

}