package com.example.fastcar.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.fastcar.Fragment.CaNhan_Fragment;
import com.example.fastcar.Fragment.ChuyenXe_Fragment;
import com.example.fastcar.Fragment.HoTro_Fragment;
import com.example.fastcar.Fragment.KhamPha_Fragment;
import com.example.fastcar.Fragment.ThongBao_Fragment;
import com.example.fastcar.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fastcar.databinding.ActivityBottomNavigationBinding;

public class BottomNavigation_Activity extends AppCompatActivity {

    private ActivityBottomNavigationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_khampha, R.id.nav_thongbao, R.id.nav_chuyenxe, R.id.nav_hotro, R.id.nav_canhan)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public void onBackPressed() {

    }
}