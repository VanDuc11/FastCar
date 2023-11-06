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

import com.example.fastcar.Model.FeedBack;
import com.example.fastcar.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NhanXetAdapter extends RecyclerView.Adapter<NhanXetAdapter.ViewHoder>{
    Context context;
    List<FeedBack> list;

    public NhanXetAdapter(Context context, List<FeedBack> list){
        this.context = context;
        this.list = list;
    }

    public  class  ViewHoder extends RecyclerView.ViewHolder{
        TextView tvTen,tvSao,tvDate, tvContent;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_nhanxet,parent,false);
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
        if(feedBack.getNoiDung() != null) {
            holder.tvContent.setText(feedBack.getNoiDung());
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
