package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.FormatString.NumberFormatVND;
import com.example.fastcar.Model.Voucher;
import com.example.fastcar.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHoder> {
    Context context;
    List<Voucher> list;
    boolean isIconAddVisible;

    public VoucherAdapter(Context context, List<Voucher> list, boolean isIconAddVisible) {
        this.context = context;
        this.list = list;
        this.isIconAddVisible = isIconAddVisible;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tv_tenVC, tv_giatriVC, tv_checkDate;
        ImageView ic_add;
        CardView item;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_tenVC = itemView.findViewById(R.id.tv_ten_voucher_inItem);
            tv_giatriVC = itemView.findViewById(R.id.tv_giatri_voucher_inItem);
            tv_checkDate = itemView.findViewById(R.id.tv_checkHSD_voucher);
            ic_add = itemView.findViewById(R.id.icon_add_inItem_voucher);
            item = itemView.findViewById(R.id.item_voucher);

            if(isIconAddVisible) {
                ic_add.setVisibility(View.VISIBLE);
            } else {
                ic_add.setVisibility(View.GONE);
            }

            ic_add.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Voucher selectedVoucher = list.get(position);
                    if (selectedVoucher.isSelected()) {
                        selectedVoucher.setSelected(false);
                        notifyDataSetChanged();
                    } else {
                        // Chỉ cho phép chọn duy nhất một voucher
                        for (Voucher voucher : list) {
                            voucher.setSelected(false);
                        }
                        selectedVoucher.setSelected(true);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public VoucherAdapter.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_voucher, parent, false);
        return new ViewHoder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.ViewHoder holder, @SuppressLint("RecyclerView") int position) {
        Voucher voucher = list.get(position);

        holder.tv_tenVC.setText(voucher.getMaGiamGia());

        if (voucher.getGiaTri() == 0) {
            holder.tv_giatriVC.setText("Giảm " + NumberFormatVND.format(voucher.getGiaTriMax()));
        } else if (voucher.getGiaTri() != 0 && voucher.getGiaTriMax() == 0) {
            holder.tv_giatriVC.setText("Giảm " + voucher.getGiaTri() + "%");
        } else {
            holder.tv_giatriVC.setText("Giảm " + voucher.getGiaTri() + "% (tối đa " + NumberFormatVND.format(voucher.getGiaTriMax()) + ")");
        }

        // Lấy ngày hiện tại
        Date today = new Date();
        Date hsd = voucher.getHSD();

        // tính toán thời gian còn lại của voucher
        long diffInMillies = hsd.getTime() - today.getTime();
        long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;

        if(daysBetween < 0) {
            holder.tv_checkDate.setText("Mã khuyến mãi không khả dụng");
            holder.item.setCardBackgroundColor(Color.parseColor("#EDE4E4"));
            holder.ic_add.setVisibility(View.GONE);
        } else if(daysBetween == 1) {
            holder.tv_checkDate.setText("Mã khuyến mãi sẽ hết hạn trong hôm nay");
        } else {
            if(daysBetween <= 5) {
                holder.tv_checkDate.setText("Hết hạn sau " + daysBetween + " ngày");
            } else {
                holder.tv_checkDate.setVisibility(View.GONE);
            }
        }


        if (voucher.isSelected()) {
            holder.ic_add.setImageResource(R.drawable.icon_tick);
        } else {
            holder.ic_add.setImageResource(R.drawable.icon_add);
        }

        holder.item.setOnClickListener(view -> showDialog_DetailVoucher(voucher) );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("SetTextI18n")
    private void showDialog_DetailVoucher(Voucher voucher) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formatNgayBD = dateFormat.format(voucher.getNgayBD());
        String formatHSD = dateFormat.format(voucher.getHSD());

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.dialog_detail_voucher, null);
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

        TextView tv_tenvoucher, tv_hsd, tv_ngaybd_to_ngaykt, tv_soTienGiamGia;
        ImageView ic_close = dialog.findViewById(R.id.icon_close_dialog_detail_voucher);
        tv_tenvoucher = dialog.findViewById(R.id.tv_ten_voucher_inDetail_voucher);
        tv_ngaybd_to_ngaykt = dialog.findViewById(R.id.tv_ngaybd_to_ngaykt_inDetail_voucher);
        tv_hsd = dialog.findViewById(R.id.tv_ngayHetHan_voucher_inDetail_voucher);
        tv_soTienGiamGia = dialog.findViewById(R.id.tv_soTien_giamgia_inDetail_voucher);

        ic_close.setOnClickListener(view -> dialog.dismiss());

        if (voucher.getGiaTri() == 0) {
            tv_soTienGiamGia.setText(NumberFormatVND.format(voucher.getGiaTriMax()));
        } else if (voucher.getGiaTri() != 0 && voucher.getGiaTriMax() == 0) {
            tv_soTienGiamGia.setText(voucher.getGiaTri() + "%");
        } else {
            tv_soTienGiamGia.setText(voucher.getGiaTri() + "% (tối đa " + NumberFormatVND.format(voucher.getGiaTriMax()) + ")");
        }

        tv_tenvoucher.setText(voucher.getMaGiamGia());
        tv_ngaybd_to_ngaykt.setText("Áp dụng từ " + formatNgayBD + " đến " + formatHSD);
        tv_hsd.setText(formatHSD);
    }
}
