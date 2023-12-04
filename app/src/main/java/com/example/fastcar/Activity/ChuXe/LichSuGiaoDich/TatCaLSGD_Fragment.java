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
import android.widget.TextView;

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

public class TatCaLSGD_Fragment extends Fragment {
    LichSuGiaoDichApdater apdater;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    ShimmerFrameLayout shimmer_view;
    LinearLayout dataView;
    LinearLayout ln_noresult;
    String idUser;

    public static TatCaLSGD_Fragment newInstance(String data) {
        TatCaLSGD_Fragment fragment = new TatCaLSGD_Fragment();
        Bundle args = new Bundle();
        args.putString("data_lsgd", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        idUser = getArguments().getString("data_lsgd");
        return inflater.inflate(R.layout.fragment_tat_ca_lsgd, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView_all_lich_su_giao_dich);
        refreshLayout = view.findViewById(R.id.refresh_data_inAll_LSGD);
        dataView = view.findViewById(R.id.data_view_all_lichSuGiaoDich);
        shimmer_view = view.findViewById(R.id.shimmer_view_all_lichSuGiaoDich);
        ln_noresult = view.findViewById(R.id.ln_no_result_inallLSGD);
        fetchData_All_LSGD(idUser);
        refreshLayout.setOnRefreshListener(() -> {
            fetchData_All_LSGD(idUser);
            refreshLayout.setRefreshing(false);
        });
    }

    private void fetchData_All_LSGD(String idUser) {
        ln_noresult.setVisibility(View.GONE);
        shimmer_view.setVisibility(View.VISIBLE);
        shimmer_view.startShimmerAnimation();

        RetrofitClient.FC_services().getLSGD_ofUser(idUser, null).enqueue(new Callback<List<LichSuGiaoDich>>() {
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
                        System.out.println(response.code() + response.message());
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
            fetchData_All_LSGD(idUser);
        }
    }
}