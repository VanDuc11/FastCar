package com.example.fastcar.Activity.ChuXe.QuanLyChuyenXe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastcar.Adapter.ChuyenXeChuSHAdapter;
import com.example.fastcar.Model.HoaDon;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VehicleWorksFragment extends Fragment {
    public static VehicleWorksFragment newInstance(String data) {
        VehicleWorksFragment fragment = new VehicleWorksFragment();
        Bundle args = new Bundle();
        args.putString("key", data);
        fragment.setArguments(args);
        return fragment;
    }
    RecyclerView recyclerView;
    ChuyenXeChuSHAdapter adapter;
    String id;
    private boolean isFragmentVisible = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_vehicle_works, container, false);
        String data = getArguments().getString("key");
        id=data;
        recyclerView = v.findViewById(R.id.rc_chuyenxe_user);
        fechData(data);

        return v;
    }
    private void fechData(String data){
        RetrofitClient.FC_services().getListHoaDon(data,"1").enqueue(new Callback<List<HoaDon>>() {
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                if (response.code()==200){
                    adapter = new ChuyenXeChuSHAdapter(getActivity(), response.body());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    System.out.println(response.code()+" : "+ response.message());
                }
            }
            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                System.out.println("lỗi " + t);
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisible = isVisibleToUser;
        if (isVisibleToUser && isAdded() && getView() != null) {
            fechData(id);
        }
    }
}