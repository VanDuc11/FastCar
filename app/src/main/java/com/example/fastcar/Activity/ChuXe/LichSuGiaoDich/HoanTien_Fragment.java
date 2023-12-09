package com.example.fastcar.Activity.ChuXe.LichSuGiaoDich;

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

import com.example.fastcar.Activity.ChuXe.QuanLyChuyenXe.VehicleWorksFragment;
import com.example.fastcar.Adapter.LichSuGiaoDichApdater;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.LichSuGiaoDich;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoanTien_Fragment extends Fragment {
    private LichSuGiaoDichApdater apdater;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private ShimmerFrameLayout shimmer_view;
    private LinearLayout dataView;
    private LinearLayout ln_noresult;
    private String emailUser;

    public static HoanTien_Fragment newInstance(String data) {
        HoanTien_Fragment fragment = new HoanTien_Fragment();
        Bundle args = new Bundle();
        args.putString("data_lsgd", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        emailUser = getArguments().getString("data_lsgd");
        return inflater.inflate(R.layout.fragment_hoan_tien, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView_lich_su_giao_dich_hoantien);
        refreshLayout = view.findViewById(R.id.refresh_data_LSGD_hoantien);
        dataView = view.findViewById(R.id.data_view_lichSuGiaoDich_hoantien);
        shimmer_view = view.findViewById(R.id.shimmer_view_lichSuGiaoDich_hoantien);
        ln_noresult = view.findViewById(R.id.ln_no_result_hoantien);
        fetchData_LSGD_HoanTien(emailUser);
        refreshLayout.setOnRefreshListener(() -> {
            fetchData_LSGD_HoanTien(emailUser);
            refreshLayout.setRefreshing(false);
        });
    }

    private void fetchData_LSGD_HoanTien(String idUser) {
        ln_noresult.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        RetrofitClient.FC_services().getLSGD_ofUser(idUser, "2", null).enqueue(new Callback<List<LichSuGiaoDich>>() {
            @Override
            public void onResponse(Call<List<LichSuGiaoDich>> call, Response<List<LichSuGiaoDich>> response) {
                dataView.setVisibility(View.VISIBLE);
                shimmer_view.stopShimmerAnimation();
                shimmer_view.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if(response.body().size() > 0) {
                        apdater = new LichSuGiaoDichApdater(response.body(), getContext());
                        recyclerView.setAdapter(apdater);
                        apdater.notifyDataSetChanged();
                    } else {
                        ln_noresult.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LichSuGiaoDich>> call, Throwable t) {
                System.out.println("Có lỗi xảy ra: " + t);
                CustomDialogNotify.showToastCustom(getActivity().getBaseContext(), "Có lỗi xảy ra");
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && getView() != null) {
            fetchData_LSGD_HoanTien(emailUser);
        }
    }
}