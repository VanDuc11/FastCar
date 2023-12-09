package com.example.fastcar.Activity.OnBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastcar.Adapter.ViewPager_OnBoardingAdapter;
import com.example.fastcar.R;

import me.relex.circleindicator.CircleIndicator;

public class OnBoaring_Activity extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private TextView btnSkip;
    private ViewPager_OnBoardingAdapter viewPager_onBoardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boaring);

        mapping();
        viewPager_onBoardingAdapter = new ViewPager_OnBoardingAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPager_onBoardingAdapter);
        circleIndicator.setViewPager(viewPager);
        btnSkip.setOnClickListener(v -> viewPager.setCurrentItem(2));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    btnSkip.setVisibility(View.GONE);
                } else {
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SharedPreferences preferences = getSharedPreferences("onboaring", Context.MODE_PRIVATE);
        if(preferences.getBoolean("started", false)) {
            finish();
        }
    }

    private void mapping() {
        viewPager = findViewById(R.id.pager_onboard);
        circleIndicator = findViewById(R.id.circle_ic_onboarding);
        btnSkip = findViewById(R.id.tv_skip_in_onboarding);
    }
}