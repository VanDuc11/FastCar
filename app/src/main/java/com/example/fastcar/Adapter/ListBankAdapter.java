package com.example.fastcar.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Model.Bank.Bank;
import com.example.fastcar.R;

import java.util.List;

public class ListBankAdapter extends RecyclerView.Adapter<ListBankAdapter.ViewHolder> {
    List<Bank> bankList;
    Context context;
    TextView tvName;
    Dialog dialog;

    public ListBankAdapter(Context context, List<Bank> bankList, TextView tvName, Dialog dialog) {
        this.bankList = bankList;
        this.context = context;
        this.tvName = tvName;
        this.dialog = dialog;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView nameBank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.img_nganhang_inItem);
            nameBank = itemView.findViewById(R.id.tv_ten_nganhang_inItem);
        }
    }

    @NonNull
    @Override
    public ListBankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_ten_nganhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBankAdapter.ViewHolder holder, int position) {
        Bank bank = bankList.get(position);
        Glide.with(context)
                .load(bank.getLogo())
                .into(holder.logo);
        holder.nameBank.setText(bank.getShortName());
        holder.itemView.setOnClickListener(view -> {
            tvName.setText(bank.getShortName());
            dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }
}
