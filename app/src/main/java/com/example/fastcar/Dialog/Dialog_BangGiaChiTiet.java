package com.example.fastcar.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;

public class Dialog_BangGiaChiTiet {
    @SuppressLint("SetTextI18n")
    public static void showDialog(Context context, HoaDon hoaDon) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_giachitiet);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        ImageView btn_close = dialog.findViewById(R.id.icon_close_dialog_chitietgia_inHD);
        btn_close.setOnClickListener(view1 -> dialog.dismiss());

        TextView showDialog_giaThue1ngay, showDialog_phiDVFC, showDialog_coc30Per, showDialog_tt70Per;
        TextView tv_tien1ngay, tv_phiDV, tv_voucher, tv_tongTien, tv_per30, tv_per70, tv_tongPhi_x_soNgay;
        LinearLayout ln_show_voucher;

        showDialog_giaThue1ngay = dialog.findViewById(R.id.btn_show_dialog_dongia1ngay_inChiTietGia);
        showDialog_phiDVFC = dialog.findViewById(R.id.btn_show_dialog_phi_FC_inChiTietGia);
        showDialog_coc30Per = dialog.findViewById(R.id.btn_show_dialog_coc30Per_inChiTietGia);
        showDialog_tt70Per = dialog.findViewById(R.id.btn_show_dialog_tt70Per_inChiTietGia);
        tv_tien1ngay = dialog.findViewById(R.id.tv_tienThue_1ngay_inChiTietGia);
        tv_phiDV = dialog.findViewById(R.id.tv_phiDV_FC_inChiTietGia);
        tv_voucher = dialog.findViewById(R.id.tv_giatri_voucher_inChiTietGia);
        tv_tongTien = dialog.findViewById(R.id.tv_thanhTien_inChiTietGia);
        tv_per30 = dialog.findViewById(R.id.tv_soTien30Per_inChiTietGia);
        tv_per70 = dialog.findViewById(R.id.tv_soTien70Per_inChiTietGia);
        ln_show_voucher = dialog.findViewById(R.id.ln_view_voucher_inChiTietGia);
        tv_tongPhi_x_soNgay = dialog.findViewById(R.id.tv_tongTien_x_soNgay_inChiTietGia);

        tv_tien1ngay.setText(NumberFormatVND.format(hoaDon.getXe().getGiaThue1Ngay()) + "/ngày");
        tv_phiDV.setText(NumberFormatVND.format(hoaDon.getPhiDV() / hoaDon.getTongSoNgayThue()) + "/ngày");

        if (hoaDon.getTongSoNgayThue() == 1) {
            tv_tongPhi_x_soNgay.setText(NumberFormatVND.format(hoaDon.getXe().getGiaThue1Ngay() + hoaDon.getPhiDV()));
        } else {
            tv_tongPhi_x_soNgay.setText(NumberFormatVND.format(hoaDon.getXe().getGiaThue1Ngay() + hoaDon.getPhiDV()) + " x " + hoaDon.getTongSoNgayThue() + " ngày");
        }

        if (hoaDon.getGiamGia() == 0) {
            ln_show_voucher.setVisibility(View.GONE);
        } else {
            ln_show_voucher.setVisibility(View.VISIBLE);
            tv_voucher.setText("- " + NumberFormatVND.format(hoaDon.getGiamGia()));
        }
        tv_per30.setText(NumberFormatVND.format(hoaDon.getTienCoc()));
        tv_per70.setText(NumberFormatVND.format(hoaDon.getThanhToan()));
        tv_tongTien.setText(NumberFormatVND.format(hoaDon.getTongTien()));

        showDialog_giaThue1ngay.setOnClickListener(view -> GiaThueXe_1ngay(context));
        showDialog_phiDVFC.setOnClickListener(view -> PhiDichVuFastCar(context));
        showDialog_coc30Per.setOnClickListener(view -> DatCoc30Per(context));
        showDialog_tt70Per.setOnClickListener(view -> ThanhToan70Per(context));
    }

    public static void GiaThueXe_1ngay(Context context) {
        Dialog_TienThue_1ngay.showDialog(context);
    }

    public static void PhiDichVuFastCar(Context context) {
        Dialog_PhiDVFC.showDialog(context);
    }

    public static void DatCoc30Per(Context context) {
        Dialog_Coc30Per.showDialog(context);
    }

    public static void ThanhToan70Per(Context context) {
        Dialog_TT70Per.showDialog(context);
    }
}
