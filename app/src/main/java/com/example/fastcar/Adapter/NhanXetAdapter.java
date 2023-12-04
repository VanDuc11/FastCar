package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NhanXetAdapter extends RecyclerView.Adapter<NhanXetAdapter.ViewHoder> {
    Context context;
    List<FeedBack> list;
    private boolean isShowMore;

    public NhanXetAdapter(Context context, List<FeedBack> list, boolean isShowMore) {
        this.context = context;
        this.list = list;
        this.isShowMore = isShowMore;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tvTen, tvSao, tvDate, tvContent;
        ImageView imgUser;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tv_nameUser_feedback);
            tvSao = itemView.findViewById(R.id.tv_soSao_ofFeedBackUser);
            tvDate = itemView.findViewById(R.id.tv_feedBack_date);
            imgUser = itemView.findViewById(R.id.img_feedback_ofUser);
            tvContent = itemView.findViewById(R.id.tv_feedBack_content);
        }
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nhanxet, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        FeedBack feedBack = list.get(position);
        holder.tvTen.setText(feedBack.getUser().getUserName());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 'lúc' HH:mm");
        String formattedDate = sdf.format(feedBack.getThoiGian());
        holder.tvDate.setText(formattedDate);
        holder.tvSao.setText(String.valueOf(feedBack.getSoSao()));
        if (feedBack.getNoiDung() != null) {
            holder.tvContent.setText(feedBack.getNoiDung());
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }

        if (feedBack.getUser().getAvatar() == null || feedBack.getUser().getAvatar().equals("")) {
            Glide.with(context)
                    .load(R.drawable.img_avatar_user_v1)
                    .into(holder.imgUser);
        } else {
            Glide.with(context)
                    .load(feedBack.getUser().getAvatar())
                    .into(holder.imgUser);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() <= 2) {
            return list.size();
        } else {
            if (isShowMore) {
                return list.size();
            } else {
                return 2;
            }
        }
    }

}
