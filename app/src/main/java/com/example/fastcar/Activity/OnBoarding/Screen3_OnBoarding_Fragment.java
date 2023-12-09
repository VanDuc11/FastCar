package com.example.fastcar.Activity.OnBoarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.R;

public class Screen3_OnBoarding_Fragment extends Fragment {
    TextView btnStart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_screen3__on_boarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart = view.findViewById(R.id.btn_start);
        SharedPreferences preferences = getContext().getSharedPreferences("onboaring", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        btnStart.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), Login_Activity.class));
            editor.putBoolean("started", true);
            editor.apply();
        });
    }
}