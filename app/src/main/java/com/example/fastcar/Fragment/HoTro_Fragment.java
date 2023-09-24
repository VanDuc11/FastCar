package com.example.fastcar.Fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fastcar.R;

public class HoTro_Fragment extends Fragment {
    TextView tv_hotline_html;
    Button btn_call, btn_send_message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ho_tro, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapping(view);

        String tv_format = "Cần hỗ trợ nhanh vui lòng gọi: <b>0222 666 888 22</b> hoặc gửi tin nhắn cho chúng tôi.";
        tv_hotline_html.setText(Html.fromHtml(tv_format, Html.FROM_HTML_MODE_LEGACY));

        // gọi điện
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // gửi tin nhắn
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void mapping(View view) {
        tv_hotline_html = view.findViewById(R.id.tv_hotline_html);
        btn_call = view.findViewById(R.id.btn_call_to_hotline);
        btn_send_message = view.findViewById(R.id.btn_send_message_to_hotline);
    }
}