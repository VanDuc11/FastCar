package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fastcar.Adapter.VoucherAdapter;
import com.example.fastcar.Model.Voucher;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaGiamGia_Activity extends AppCompatActivity {
    List<Voucher> listVoucher;
    AppCompatButton btn_save;
    RecyclerView recyclerView;
    VoucherAdapter adapter;
    LinearLayout ln_noResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_giam_gia);

        mapping();

        getData();

        btn_save.setOnClickListener(view -> {
            Voucher selectedVoucher = getSelectedVoucher();
            if (selectedVoucher != null) {
                Intent intent = new Intent();
                intent.putExtra("voucher", selectedVoucher);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Intent intent = new Intent();
                intent.putExtra("voucher", (android.os.Parcelable) null);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    void mapping() {
        recyclerView = findViewById(R.id.recyclerView_voucher);
        btn_save = findViewById(R.id.btn_Save_Voucher);
        ln_noResult = findViewById(R.id.ln_no_result_inVoucher);
    }

    public Voucher getSelectedVoucher() {
        for (Voucher voucher : listVoucher) {
            if (voucher.isSelected()) {
                return voucher;
            }
        }
        return null;
    }


    private void getData() {
        ln_noResult.setVisibility(View.GONE);

        Intent intent = getIntent();
        boolean isIcon = intent.getBooleanExtra("SHOW_ICON_ADD", false);

        if(isIcon) {
            btn_save.setVisibility(View.VISIBLE);
        } else {
            btn_save.setVisibility(View.GONE);
        }

        RetrofitClient.FC_services().getListVoucher().enqueue(new Callback<List<Voucher>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                if (response.code() == 200) {
                    if(!response.body().isEmpty()) {
                        ln_noResult.setVisibility(View.GONE);
                        listVoucher = response.body();
                        adapter = new VoucherAdapter(MaGiamGia_Activity.this, listVoucher, isIcon);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        ln_noResult.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    System.out.println("Có lỗi khi get list voucher: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                System.out.println("Có lỗi khi get list voucher: " + t);
            }
        });
    }

    public void backTo_ThongTinThueACT(View view) {
        onBackPressed();
    }

}