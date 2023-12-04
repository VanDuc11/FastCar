package com.example.fastcar.Activity.ChuXe.LichSuGiaoDich;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class PagerAdapter_LSGD extends FragmentPagerAdapter {
    private String idUser;
    public PagerAdapter_LSGD(@NonNull FragmentManager fm, String idUser) {
        super(fm);
        this.idUser = idUser;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TatCaLSGD_Fragment().newInstance(idUser);
            case 1:
                return new RutTien_Fragment().newInstance(idUser);
            case 2:
                return new ThanhToan20Per_Fragment().newInstance(idUser);
            case 3:
                return new HoanTien_Fragment().newInstance(idUser);
            default:
                return new TatCaLSGD_Fragment().newInstance(idUser);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
