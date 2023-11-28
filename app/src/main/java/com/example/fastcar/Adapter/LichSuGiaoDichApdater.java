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


import com.example.fastcar.Activity.ChuXe.HoaDon_ChuSH_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
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

        TextView tv_ngay_giao_dich,tv_trang_thai,tv_noiDung,tv_so_tien_giao_dich;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ngay_giao_dich = itemView.findViewById(R.id.tv_date);
            tv_trang_thai = itemView.findViewById(R.id.tv_trang_thai_giao_dich);
            tv_noiDung = itemView.findViewById(R.id.tv_noidung_chuyen_khoan);
            tv_so_tien_giao_dich = itemView.findViewById(R.id.tv_so_tien_giao_dich);

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
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(lichSuGiaoDich.getNoiDung().equals("Yêu cầu rút tiền")){
               showImageDialog(lichSuGiaoDich);
               }
               else {
                   Intent intent = new Intent(context, HoaDon_ChuSH_Activity.class);
                   intent.putExtra("hoadon",lichSuGiaoDich.getHoaDon() );
                   v.getContext().startActivity(intent);
               }

           }
       });
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String formattedDateTime = dateTimeFormat.format(lichSuGiaoDich.getThoiGian());
        SpannableString spannableString = new SpannableString(formattedDateTime);
        spannableString.setSpan(new RelativeSizeSpan(1f), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), 11, formattedDateTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       holder.tv_ngay_giao_dich.setText(spannableString);

       holder.tv_trang_thai.setText(lichSuGiaoDich.getNoiDung().equals("Yêu cầu rút tiền")?((lichSuGiaoDich.getTrangThai()==0)?"Đang chờ duyệt":"Giao dịch thành công"):"Giao dịch thành công");
       holder.tv_noiDung.setText("Nội dung :\n\n- "+lichSuGiaoDich.getNoiDung());
       holder.tv_so_tien_giao_dich.setText("Số tiền giao dịch : "+ NumberFormatVND.format(lichSuGiaoDich.getSoTienGD()));
    }
    @Override
    public int getItemCount() {
        return listGiaoDich.size();
    }

    public void showImageDialog(LichSuGiaoDich list) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(" HH:mm dd/MM/yyyy");
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_chi_tiet_hoa_don);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView close = dialog.findViewById(R.id.btn_close_hoadonchitiet);
        TextView tv_tenuser = dialog.findViewById(R.id.tv_ten_user);
        TextView tvsdt = dialog.findViewById(R.id.sdt_user);
        TextView datecreate = dialog.findViewById(R.id.date_create);
        TextView noiDung = dialog.findViewById(R.id.tv_noidung);
        TextView sotien = dialog.findViewById(R.id.tv_so_tien);
        TextView trangthai = dialog.findViewById(R.id.tv_trang_thai);

        tv_tenuser.setText(list.getUser().getUserName());
        tvsdt.setText(list.getUser().getSDT());
        datecreate.setText(dateTimeFormat.format(list.getThoiGian()));
        noiDung.setText(list.getNoiDung());
        sotien.setText(NumberFormatVND.format(list.getSoTienGD()));
        trangthai.setText(list.getTrangThai()==0?"Đang chờ duyệt":"Giao dịch thành công");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

}

