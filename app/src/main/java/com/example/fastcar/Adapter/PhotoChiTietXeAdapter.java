package com.example.fastcar.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.act_bottom.CaNhan_Activity;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class PhotoChiTietXeAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<String> listPhoto;

    public PhotoChiTietXeAdapter(Context context, ArrayList<String> listPhoto) {
        this.context = context;
        this.listPhoto = listPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        try {
            view = LayoutInflater.from(context).inflate(R.layout.layout_item_photo_inchitietxe, container, false);

        }catch (Exception e){
            Log.e("TAG","onCreateView",e);
            throw e;
        }
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView img = view.findViewById(R.id.img_xe_inChiTietXe);

        String photo = listPhoto.get(position);
        if(listPhoto != null) {
            Glide.with(context)
                    .load(HostApi.URL_Image + photo)
                    .into(img);

            container.addView(view);
        }
        return view;
    }

    @Override
    public int getCount() {
        if(listPhoto != null) {
            return listPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
