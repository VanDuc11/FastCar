package com.example.fastcar.Activity.ThemXe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fastcar.Adapter.TinhNangXeAdpater;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Dialog.Dialog_Thoat_DangKy;
import com.example.fastcar.Model.TinhNangXe;
import com.example.fastcar.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ThongTinChiTiet_Activity extends AppCompatActivity {
    TextView btn_tieptuc, btn_chonDiaChiXe, tv_diachi, tv_tieuHao ;
    EditText edt_mota, edt_TieuHao;
    RelativeLayout btn_back, btn_close;
    RecyclerView recyclerView_tinhNangXe;
    TinhNangXeAdpater tinhNangXeAdpater;
    Dialog dialogDiaChi;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    ArrayList<TinhNangXe> listTinhNang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_chi_tiet);

        mapping();
        load();
        btn_back.setOnClickListener(view -> onBackPressed());
        btn_close.setOnClickListener(view -> Dialog_Thoat_DangKy.showDialog(this, false));
        btn_tieptuc.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), GiaChoThue_Activity.class)));
        btn_chonDiaChiXe.setOnClickListener(view -> showDialog_DiaDiem());
    }

    private void mapping() {
        btn_tieptuc = findViewById(R.id.btn_continue_ttct_inThemXe);
        btn_chonDiaChiXe = findViewById(R.id.btn_chonDiaChiXe_inThemXe);
        tv_diachi = findViewById(R.id.tv_diachiXe_inThemXe);
        edt_mota = findViewById(R.id.edt_motaXe_inThemXe);
        edt_TieuHao = findViewById(R.id.edt_sl_tieuThuXe);
        tv_tieuHao = findViewById(R.id.tv_sl_tieuThuXe);
        btn_back = findViewById(R.id.icon_back_in_ttct);
        btn_close = findViewById(R.id.icon_close_dangky_inThemXe);
        recyclerView_tinhNangXe = findViewById(R.id.recyclerView_tinhnangXe);
    }

    private void load() {
        listTinhNang = new ArrayList<>();
        recyclerView_tinhNangXe.setLayoutManager(new GridLayoutManager(this, 3));
        addItemTinhNang();
        tinhNangXeAdpater = new TinhNangXeAdpater(listTinhNang, this);
        recyclerView_tinhNangXe.setAdapter(tinhNangXeAdpater);
    }

    public void showDialog_DiaDiem() {
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View custom = inflater.inflate(R.layout.layout_chon_diadiem_thuexe, null);
        dialogDiaChi = new Dialog(this);
        dialogDiaChi.setContentView(custom);
        dialogDiaChi.setCanceledOnTouchOutside(false);

        Window window = dialogDiaChi.getWindow();
        if (window == null) {
            return;
        }
        // set kích thước dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialogDiaChi.show();

        ImageView btn_back = dialogDiaChi.findViewById(R.id.icon_back_in_chon_diadiem_thueXe);
        TextView getDiaChiHienTai = dialogDiaChi.findViewById(R.id.btn_getVitri_hientai);
        TextView confirm = dialogDiaChi.findViewById(R.id.btn_confirm_diadiem_inDialog);
        EditText edt_diachi = dialogDiaChi.findViewById(R.id.edt_diadiem_inDialog);

        // back
        btn_back.setOnClickListener(view1 -> dialogDiaChi.dismiss());

        // get vị trí hiện tại
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getDiaChiHienTai.setOnClickListener(view1 -> getLocation(dialogDiaChi));

        // confirm
        confirm.setOnClickListener(view1 -> {
            String diachi = edt_diachi.getText().toString();
            if (!diachi.isEmpty()) {
                callBackDialog(diachi);
                dialogDiaChi.dismiss();
            } else {
                CustomDialogNotify.showToastCustom(this, "Vui lòng nhập địa điểm hoặc chọn vị trí hiện tại");
            }
        });
    }

    private void getLocation(Dialog dialog) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(ThongTinChiTiet_Activity.this, Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                callBackDialog(addresses.get(0).getAddressLine(0));
                                dialog.dismiss();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(ThongTinChiTiet_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(dialogDiaChi);
            } else {
                CustomDialogNotify.showToastCustom(this, "Vui lòng cấp quyền để sử dụng dịch vụ");
            }
        }
    }

    private void callBackDialog(String diachi) {
        tv_diachi.setText(diachi);
    }

    private void addItemTinhNang() {
        listTinhNang.clear();
        listTinhNang.add(new TinhNangXe("Bản đồ", R.drawable.ictn_bando));
        listTinhNang.add(new TinhNangXe("Bluetooth", R.drawable.ictn_bluetooth));
        listTinhNang.add(new TinhNangXe("Camera 360", R.drawable.ictn_cam360));
        listTinhNang.add(new TinhNangXe("Camera cập lề", R.drawable.ictn_caple));
        listTinhNang.add(new TinhNangXe("Camera hành trình", R.drawable.ictn_cam_hanhtrinh));
        listTinhNang.add(new TinhNangXe("Camera lùi", R.drawable.ictn_cam_lui));
        listTinhNang.add(new TinhNangXe("Cảm biến lốp", R.drawable.ictn_cambienlop));
        listTinhNang.add(new TinhNangXe("Cảm biến va chạm", R.drawable.ictn_cambien_vacham));
        listTinhNang.add(new TinhNangXe("Cảnh báo tốc độ", R.drawable.ictn_canhbao_tocdo));
        listTinhNang.add(new TinhNangXe("Cửa sổ trời", R.drawable.ictn_cusotroi));
        listTinhNang.add(new TinhNangXe("Định vị GPS", R.drawable.ictn_gps));
        listTinhNang.add(new TinhNangXe("Ghế trẻ em", R.drawable.ictn_ghetreem));
        listTinhNang.add(new TinhNangXe("Khe cắm USB", R.drawable.ictn_usb));
        listTinhNang.add(new TinhNangXe("Lốp dự phòng", R.drawable.ictn_lop_duphong));
        listTinhNang.add(new TinhNangXe("Màn hình DVD", R.drawable.ictn_manhinhdvd));
        listTinhNang.add(new TinhNangXe("Nắp thùng xe bán tải", R.drawable.ictn_napthung_bantai));
        listTinhNang.add(new TinhNangXe("ETC", R.drawable.ictn_etc));
        listTinhNang.add(new TinhNangXe("Túi khí", R.drawable.ictn_tuikhi));
    }

}