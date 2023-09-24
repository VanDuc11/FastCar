package com.example.fastcar.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fastcar.Activity.ChiTietHoaDon_Activity;
import com.example.fastcar.R;


public class ChuyenXe_Fragment extends Fragment {
    LinearLayout item1, item2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chuyen_xe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapping(view);

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChiTietHoaDon_Activity.class));
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChiTietHoaDon_Activity.class));
            }
        });
    }

    void mapping(View view) {
        item1 = view.findViewById(R.id.item1_chuyenxe1);
        item2 = view.findViewById(R.id.item1_chuyenxe2);
    }
}