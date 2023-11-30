package com.example.fastcar.Activity.ChuXe.QuanLyChuyenXe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastcar.R;


public class VehicleWorksFragment extends Fragment {
    public static VehicleWorksFragment newInstance(String data) {
        VehicleWorksFragment fragment = new VehicleWorksFragment();
        Bundle args = new Bundle();
        args.putString("key", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Lấy dữ liệu từ Bundle
        Bundle args = getArguments();
        if (args != null) {
            String data = args.getString("key", "");;
            System.out.println(data+"1111111111111111");
            // Xử lý dữ liệu
            // ...
        }


        // Xử lý dữ liệu
        // ...

        return inflater.inflate(R.layout.fragment_vehicle_works, container, false);
    }
}