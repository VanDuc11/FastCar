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
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.Model.User;
import com.example.fastcar.Model.Voucher;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

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
    private User user;
    private final StringBuilder allVoucherHasBeenUsedOfUser = new StringBuilder();
    private boolean isIcon;
    private ShimmerFrameLayout shimmer_view;
    private LinearLayout data_view;

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
        shimmer_view = findViewById(R.id.shimmer_view_inVoucher);
        data_view = findViewById(R.id.data_view_inVoucher);
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
        btn_save.setVisibility(View.GONE);
        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        SharedPreferences preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        String userStr = preferences.getString("user", "");
        Gson gson = new Gson();
        user = gson.fromJson(userStr, User.class);

        Intent intent = getIntent();
        isIcon = intent.getBooleanExtra("SHOW_ICON_ADD", false);

        getListHoaDonHaveVoucherOfUser();
    }

    private void getListHoaDonHaveVoucherOfUser() {
        // hoá đơn bị huỷ sẽ không tính là đã sử dụng voucher đó
        RetrofitClient.FC_services().getListHoaDonUser(user.get_id(), "1,2,3,4,5,6").enqueue(new Callback<List<HoaDon>>() {
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                if (response.code() == 200) {
                    if (response.body().size() != 0) {
                        List<HoaDon> listHoaDonHaveVoucher = response.body();
                        for (HoaDon hd : listHoaDonHaveVoucher) {
                            if(hd.getMaGiamGia().length() > 0) {
                                allVoucherHasBeenUsedOfUser.append(hd.getMaGiamGia()).append(",");
                            }
                        }
                        if (allVoucherHasBeenUsedOfUser.length() > 0) {
                            allVoucherHasBeenUsedOfUser.deleteCharAt(allVoucherHasBeenUsedOfUser.length() - 1);
                        }
                        getListVoucher(String.valueOf(allVoucherHasBeenUsedOfUser));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                System.out.println("Có lỗi khi getListHoaDonHaveVoucherOfUser(): " + t);
            }
        });
    }

    private void getListVoucher(String vouchers) {
        RetrofitClient.FC_services().getListVoucher(vouchers).enqueue(new Callback<List<Voucher>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);
                if (isIcon) {
                    btn_save.setVisibility(View.VISIBLE);
                }

                if (response.code() == 200) {
                    if (!response.body().isEmpty()) {
                        ln_noResult.setVisibility(View.GONE);
                        listVoucher = response.body();
                        adapter = new VoucherAdapter(MaGiamGia_Activity.this, listVoucher, isIcon, 1);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        ln_noResult.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        btn_save.setVisibility(View.GONE);
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