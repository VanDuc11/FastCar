package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.Model.Voucher;
import com.example.fastcar.R;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHoder> {
    Context context;
    List<Voucher> list;

    public VoucherAdapter(Context context, List<Voucher> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tv_tenVC, tv_giatriVC, tv_checkDate;
        ImageView ic_add;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_tenVC = itemView.findViewById(R.id.tv_ten_voucher_inItem);
            tv_giatriVC = itemView.findViewById(R.id.tv_giatri_voucher_inItem);
            tv_checkDate = itemView.findViewById(R.id.tv_checkHSD_voucher);
            ic_add = itemView.findViewById(R.id.icon_add_inItem_voucher);

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
            holder.tv_giatriVC.setText("Giảm " + format_Money_to_VND(voucher.getGiaTriMax()));
        } else if (voucher.getGiaTri() != 0 && voucher.getGiaTriMax() == 0) {
            holder.tv_giatriVC.setText("Giảm " + voucher.getGiaTri() + "%");
        } else {
            holder.tv_giatriVC.setText("Giảm " + voucher.getGiaTri() + "% (tối đa " + format_Money_to_VND(voucher.getGiaTriMax()) + ")");
        }

        if (voucher.isSelected()) {
            holder.ic_add.setImageResource(R.drawable.icon_tick);
        } else {
            holder.ic_add.setImageResource(R.drawable.icon_add);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private StringBuilder format_Money_to_VND(int money) {
        StringBuilder sb = new StringBuilder(String.valueOf(money));
        int count = 0;
        for (int i = sb.length() - 1; i >= 0; i--) {
            count++;
            if (count % 3 == 0 && i != 0) {
                sb.insert(i, '.');
            }
        }
        sb.append(" đ");
        return sb;
    }
}
