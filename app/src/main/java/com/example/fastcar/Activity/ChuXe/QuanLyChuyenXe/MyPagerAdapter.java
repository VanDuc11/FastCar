package com.example.fastcar.Activity.ChuXe.QuanLyChuyenXe;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.fastcar.Activity.MaGiamGia_Activity;
import com.example.fastcar.Activity.ThongBao_Activity;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private String id_xe;
    private boolean isLoaded;
    public MyPagerAdapter(FragmentManager fm,String id_xe) {
        super(fm);
        this.id_xe = id_xe;
    }

    @Override
    public Fragment getItem(int position) {
        // Tạo và trả về fragment tương ứng với vị trí
        switch (position) {
            case 0:
                return new VehicleWorksFragment().newInstance(id_xe);
            case 1:
                return new Upcoming_CarFragment().newInstance(id_xe);
            default:
                return new VehicleWorksFragment().newInstance(id_xe);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
