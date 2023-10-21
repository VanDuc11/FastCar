package com.example.fastcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastcar.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class KhuyenMaiApdater extends RecyclerView.Adapter<KhuyenMaiApdater.ViewHoder> {
    private Context context;
    private List<String> list;

    public KhuyenMaiApdater(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_khuyenmai, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        ShapeableImageView img;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.layout_item_khuyenmai);
        }
    }
}
