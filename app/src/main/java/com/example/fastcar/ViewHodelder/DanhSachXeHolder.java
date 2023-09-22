package com.example.fastcar.ViewHodelder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.R;

public class DanhSachXeHolder extends RecyclerView.ViewHolder{
    public ImageView img_xe;
    public TextView name_xe;

    public LinearLayout layout;

    public DanhSachXeHolder(@NonNull View itemView) {
        super(itemView);
        img_xe =itemView.findViewById(R.id.img_xe);
        name_xe = itemView.findViewById(R.id.name_xe);
        layout = itemView.findViewById(R.id.card_xe);
    }
}
