package com.example.fastcar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

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
        RetrofitClient.FC_services().getListVoucher().enqueue(new Callback<List<Voucher>>() {
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                if (response.code() == 200) {
                    listVoucher = response.body();
                    adapter = new VoucherAdapter(MaGiamGia_Activity.this, listVoucher);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                System.out.println("Có lỗi xảy ra " + t);
            }
        });
    }

    public void backTo_ThongTinThueACT(View view) {
        onBackPressed();
    }

}