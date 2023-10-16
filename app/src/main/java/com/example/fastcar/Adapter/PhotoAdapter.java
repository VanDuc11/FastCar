package com.example.fastcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class PhotoAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> listPhoto;

    public PhotoAdapter(Context context, ArrayList<String> listPhoto) {
        this.context = context;
        this.listPhoto = listPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_photo, container, false);
        ShapeableImageView img = view.findViewById(R.id.img_xe_inItem);

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
