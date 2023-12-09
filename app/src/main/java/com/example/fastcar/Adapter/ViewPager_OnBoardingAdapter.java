package com.example.fastcar.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fastcar.Activity.OnBoarding.Screen1_OnBoarding_Fragment;
import com.example.fastcar.Activity.OnBoarding.Screen2_OnBoarding_Fragment;
import com.example.fastcar.Activity.OnBoarding.Screen3_OnBoarding_Fragment;

public class ViewPager_OnBoardingAdapter extends FragmentStatePagerAdapter {
    public ViewPager_OnBoardingAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Screen1_OnBoarding_Fragment();
            case 1:
                return new Screen2_OnBoarding_Fragment();
            case 2:
                return new Screen3_OnBoarding_Fragment();
            default:
                return new Screen1_OnBoarding_Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
