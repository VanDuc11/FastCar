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
import android.widget.TextView;

import com.example.fastcar.Activity.ChiTietXe_Activity;
import com.example.fastcar.Activity.DanhSachXe_Activity;
import com.example.fastcar.R;

public class KhamPha_Fragment extends Fragment {
    TextView goDS;
    LinearLayout item1, item2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kham_pha, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapping(view);

        goDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DanhSachXe_Activity.class);
                startActivity(i);
            }
        });

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate_();
            }
        });

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate_();
            }
        });
    }

    void mapping(View view) {
        goDS = view.findViewById(R.id.txt_ds);
        item1 = view.findViewById(R.id.item1_dsxe);
        item2 = view.findViewById(R.id.item2_dsxe);
    }

    void navigate_() {
        startActivity(new Intent(getContext(), DanhSachXe_Activity.class));
    }
}