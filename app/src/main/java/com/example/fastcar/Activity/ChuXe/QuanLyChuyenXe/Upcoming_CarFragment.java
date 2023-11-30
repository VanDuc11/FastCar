package com.example.fastcar.Activity.ChuXe.QuanLyChuyenXe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastcar.R;

public class Upcoming_CarFragment extends Fragment {
    public static Upcoming_CarFragment newInstance(String data) {
        Upcoming_CarFragment fragment = new Upcoming_CarFragment();
        Bundle args = new Bundle();
        args.putString("key", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Lấy dữ liệu từ Bundle
        String data = getArguments().getString("key");
        System.out.println(data+"1111112");
        // Xử lý dữ liệu
        // ...

        return inflater.inflate(R.layout.fragment_upcoming__car, container, false);
    }
}