package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.ChuXe.HoaDon_ChuSH_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.Dialog_View_Image;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.example.fastcar.Server.HostApi;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuGiaoDichApdater extends RecyclerView.Adapter<LichSuGiaoDichApdater.ViewHolder> {
    private List<LichSuGiaoDich> listGiaoDich;
    private Context context;

    public LichSuGiaoDichApdater(List<LichSuGiaoDich> listGiaoDich, Context context) {
        this.listGiaoDich = listGiaoDich;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ngay_giao_dich, tv_trang_thai, tv_noiDung, tv_so_tien_giao_dich;
        ImageView img_lsgd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ngay_giao_dich = itemView.findViewById(R.id.tv_date);
            tv_trang_thai = itemView.findViewById(R.id.tv_trang_thai_giao_dich);
            tv_noiDung = itemView.findViewById(R.id.tv_noidung_chuyen_khoan);
            tv_so_tien_giao_dich = itemView.findViewById(R.id.tv_so_tien_giao_dich);
            img_lsgd = itemView.findViewById(R.id.img_lsgd);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_lich_su_giao_dich, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LichSuGiaoDichApdater.ViewHolder holder, int position) {
        LichSuGiaoDich lichSuGiaoDich = listGiaoDich.get(position);
        if (lichSuGiaoDich.getNoiDung().equals("Yêu cầu rút tiền")) {
            holder.img_lsgd.setImageResource(R.drawable.icon_ruttien);
            holder.tv_so_tien_giao_dich.setText("- " + NumberFormatVND.format(lichSuGiaoDich.getSoTienGD()));
        } else {
            holder.img_lsgd.setImageResource(R.drawable.icon_nhantien);
            holder.tv_so_tien_giao_dich.setText("+ " + NumberFormatVND.format(lichSuGiaoDich.getSoTienGD()));
        }

        holder.itemView.setOnClickListener(v -> {
            showImageDialog(lichSuGiaoDich);
        });
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy");

        holder.tv_ngay_giao_dich.setText(dateTimeFormat.format(lichSuGiaoDich.getThoiGian()));
        holder.tv_trang_thai.setText(lichSuGiaoDich.getNoiDung().equals("Yêu cầu rút tiền") ? ((lichSuGiaoDich.getTrangThai() == 0) ? "Đang chờ duyệt" : "Thành công") : "Thành công");
        holder.tv_noiDung.setText(lichSuGiaoDich.getNoiDung());
    }

    @Override
    public int getItemCount() {
        return listGiaoDich.size();
    }

    @SuppressLint("CheckResult")
    public void showImageDialog(LichSuGiaoDich lichSuGiaoDich) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_chi_tiet_hoa_don);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView close = dialog.findViewById(R.id.btn_close_hoadonchitiet);
        TextView tv_mahd = dialog.findViewById(R.id.tv_mahd_inDialogChiTiet);
        TextView tv_tenuser = dialog.findViewById(R.id.tv_ten_user_inDialogChiTiet);
        TextView tvsdt = dialog.findViewById(R.id.tv_sdt_user_inDialogChiTiet);
        TextView datecreate = dialog.findViewById(R.id.tv_date_create_inDialogChiTiet);
        TextView noiDung = dialog.findViewById(R.id.tv_noidung_inDialogChiTiet);
        TextView sotien = dialog.findViewById(R.id.tv_so_tien_inDialogChiTiet);
        TextView trangthai = dialog.findViewById(R.id.tv_trangthai_lsgd_inDialogChiTiet);
        TextView tv_nguontien = dialog.findViewById(R.id.tv_nguonnhan_inDialogChiTiet);
        TextView tv_tennh = dialog.findViewById(R.id.tv_tenNganHang_inDialogChiTiet);
        TextView tv_tenchutk = dialog.findViewById(R.id.tv_tenChuTK_NganHang_inDialogChiTiet);
        TextView tv_stk = dialog.findViewById(R.id.tv_STK_NganHang_inDialogChiTiet);
        TextView btn_ViewHoaDon = dialog.findViewById(R.id.btn_view_hoadon_inDialogChiTiet);
        TextView tv_hinhanh = dialog.findViewById(R.id.tv_hinhanh_lsgd);
        ImageView img_lsgd = dialog.findViewById(R.id.img_hinhanh_lsgd);

        tv_mahd.setText(lichSuGiaoDich.getMaLSGD());
        tv_tenuser.setText(lichSuGiaoDich.getUser().getUserName());
        tvsdt.setText(lichSuGiaoDich.getUser().getSDT());
        datecreate.setText(dateTimeFormat.format(lichSuGiaoDich.getThoiGian()));
        noiDung.setText(lichSuGiaoDich.getNoiDung());
        sotien.setText(NumberFormatVND.format(lichSuGiaoDich.getSoTienGD()));

        if (lichSuGiaoDich.getNganHang() != null) {
            tv_nguontien.setText("Tài khoản ngân hàng");
            tv_tennh.setText("Tên NH: " + lichSuGiaoDich.getNganHang().getTenNH());
            tv_tenchutk.setText("Chủ TK: " + lichSuGiaoDich.getNganHang().getTenChuTK());
            tv_stk.setText("STK: " + lichSuGiaoDich.getNganHang().getSoTK());
        }

        if (lichSuGiaoDich.getTrangThai() == 0) {
            trangthai.setText("Đang chờ duyệt");
            tv_hinhanh.setVisibility(View.GONE);
            img_lsgd.setVisibility(View.GONE);
            btn_ViewHoaDon.setVisibility(View.GONE);
        } else {
            if (lichSuGiaoDich.getNoiDung().equals("Yêu cầu rút tiền")) {
                trangthai.setText("Thành công");
                Glide.with(context).load(HostApi.URL_Image + lichSuGiaoDich.getHinhAnh()).into(img_lsgd);
                btn_ViewHoaDon.setVisibility(View.GONE);
            } else {
                tv_hinhanh.setVisibility(View.GONE);
                img_lsgd.setVisibility(View.GONE);
                tv_nguontien.setText("Ví chủ xe");
                tv_stk.setVisibility(View.GONE);
                tv_tenchutk.setVisibility(View.GONE);
                tv_tennh.setVisibility(View.GONE);
                btn_ViewHoaDon.setVisibility(View.VISIBLE);
            }
        }
        List<String> listURL = new ArrayList<>();
        listURL.add(HostApi.URL_Image + lichSuGiaoDich.getHinhAnh());
        img_lsgd.setOnClickListener(view -> Dialog_View_Image.showDialog(context, listURL));

        btn_ViewHoaDon.setOnClickListener(view -> {
            if (lichSuGiaoDich.getHoaDon() != null) {
                Intent intent = new Intent(view.getContext(), HoaDon_ChuSH_Activity.class);
                intent.putExtra("hoadon", lichSuGiaoDich.getHoaDon());
                view.getContext().startActivity(intent);
            }
        });

        close.setOnClickListener(v -> dialog.dismiss());
    }

}

