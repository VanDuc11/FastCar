package com.example.fastcar.Activity.ChuXe.QuanLyChuyenXe;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fastcar.Adapter.ChuyenXeChuSHAdapter;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VehicleWorksFragment extends Fragment {
    String allCarID;
    RecyclerView recyclerView;
    ChuyenXeChuSHAdapter adapter;
    LinearLayout ln_noResult;
    ShimmerFrameLayout shimmer_view;
    LinearLayout data_view;
    SwipeRefreshLayout refreshLayout;

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
        allCarID = getArguments().getString("key");
        return inflater.inflate(R.layout.fragment_vehicle_works, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView_chuyenxe2345_chush);
        ln_noResult = view.findViewById(R.id.ln_no_result_inChuyenXe2345_chuSH);
        data_view = view.findViewById(R.id.data_view_inChuyenXe2345_chush);
        shimmer_view = view.findViewById(R.id.shimmer_view_inChuyenXe2345_chush);
        refreshLayout = view.findViewById(R.id.refresh_data_inChuyenXe2345_ChuSH);
        fetchListHoaDon_2345(allCarID);
        refreshLayout.setOnRefreshListener(() -> {
            fetchListHoaDon_2345(allCarID);
            refreshLayout.setRefreshing(false);
        });
    }

    private void fetchListHoaDon_2345(String carIDs) {
        ln_noResult.setVisibility(View.GONE);
        data_view.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();
        RetrofitClient.FC_services().getListHoaDon(carIDs, "2,3,4,5", null, null).enqueue(new Callback<List<HoaDon>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                data_view.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);

                if (response.code() == 200) {
                    if (response.body().size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new ChuyenXeChuSHAdapter(getContext(), response.body());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        ln_noResult.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                System.out.println("Có lỗi khi fetchListHoaDon_2345: " + t);
                CustomDialogNotify.showToastCustom(getContext(), "Có lỗi xảy ra");
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && getView() != null) {
            fetchListHoaDon_2345(allCarID);
        }
    }
}