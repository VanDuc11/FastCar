package com.example.fastcar.Activity.ThemXe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastcar.Adapter.DanhSachTenHangXeAdapter;
import com.example.fastcar.Adapter.DanhSachTenModelXeAdapter;
import com.example.fastcar.Dialog.Dialog_Thoat_DangKy;
import com.example.fastcar.Model.AddCar;
import com.example.fastcar.Model.HangXe.CarApiResponse;
import com.example.fastcar.Model.HangXe.HangXe;
import com.example.fastcar.Model.MauXe.CarModelApiResponse;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClent1;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongTinCoBan_Activity extends AppCompatActivity {
    RelativeLayout ic_back, ic_close;
    TextView sl_soSan, sl_soTD, btn_nextScreen, sl_xang, sl_dau, sl_dien,ten_hang,ten_mau;
    DanhSachTenHangXeAdapter adapter;
    TextView make_id;
    EditText bks;
    String truyenDong="Số sàn",loaiNhienLieu="Xăng",NSX;
    SharedPreferences preferences;
    int soGhe,currentYear;

    Integer[] item={4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20} ;
    AutoCompleteTextView autoCompleteTextView,autoCompleteYear;

    List<String> years;
    ArrayAdapter<Integer> adapterItems;
    List<HangXe> listfind,list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_co_ban);

        mapping();
        load();
        loadItemSelected();
        String userStr = preferences.getString("user", "");
        Gson gson =new Gson();
        User user = gson.fromJson(userStr,User.class);



        adapterItems = new ArrayAdapter<Integer>(this,R.layout.list_item_spinner,item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soGhe =Integer.parseInt(parent.getItemAtPosition(position).toString());

            }
        });


        for (int i = currentYear; i >=1970 ; i--) {
            years.add(String.valueOf(i));
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, R.layout.list_item_spinner, years);
        autoCompleteYear.setAdapter(yearAdapter);
        autoCompleteYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NSX =parent.getItemAtPosition(position).toString();

            }
        });


        ic_back.setOnClickListener(view -> onBackPressed());

        ic_close.setOnClickListener(view -> Dialog_Thoat_DangKy.showDialog(this, false));
        ten_mau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMauXeDialog();
            }
        });

        btn_nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bks.getText().toString().length()==0){
                    bks.setError("Không được để trống");

                } else if (soGhe==0) {
                    Toast.makeText(ThongTinCoBan_Activity.this, "Vui lòng chọn số ghế", Toast.LENGTH_SHORT).show();
                } else if (NSX.length()==0) {
                    Toast.makeText(ThongTinCoBan_Activity.this, "Vui lòng chọn năm snar xuất", Toast.LENGTH_SHORT).show();
                } else if(ten_hang.getText().toString().equals("Chưa chọn")) {
                    Toast.makeText(ThongTinCoBan_Activity.this, "Vui lòng chọn tên hãng", Toast.LENGTH_SHORT).show();

                } else if (ten_mau.getText().toString().equals("Chưa chọn")) {
                    Toast.makeText(ThongTinCoBan_Activity.this, "Vui lòng chọn tên mẫu xe", Toast.LENGTH_SHORT).show();
                }else {
                    AddCar list =new AddCar(bks.getText().toString(),ten_hang.getText().toString(),ten_mau.getText().toString(),NSX,soGhe,truyenDong,loaiNhienLieu,0,"","",200,user.get_id());
                    Intent i = new Intent(getBaseContext(),ThongTinChiTiet_Activity.class);
                    i.putExtra("addCar",list );
                    startActivity(i);
                }


            }
        });
    }

    private void mapping() {
        sl_soSan = findViewById(R.id.sl_soSan);
        sl_soTD = findViewById(R.id.sl_soTuDong);
        btn_nextScreen = findViewById(R.id.btn_continue_ttcb_inThemXe);
        ic_back = findViewById(R.id.icon_back_in_thongtincoban);
        ic_close = findViewById(R.id.icon_close_in_thongtincoban);
        sl_xang = findViewById(R.id.sl_xang);
        sl_dau = findViewById(R.id.sl_dau);
        ten_mau=findViewById(R.id.txt_ten_mau_xe);
        sl_dien = findViewById(R.id.sl_dien);
        ten_hang = findViewById(R.id.txt_ten_hang);
        make_id = findViewById(R.id.id_make);
        bks = findViewById(R.id.edt_bks_inThemXe);
        preferences = getSharedPreferences("model_user_login", Context.MODE_PRIVATE);
        autoCompleteTextView= findViewById(R.id.auto_comple);
        autoCompleteYear = findViewById(R.id.auto_comple_year);
        years = new ArrayList<>();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);


    }

    private void load() {
        sl_soSan.setBackgroundResource(R.drawable.custom_item_selected);
        sl_soSan.setTextColor(getResources().getColor(R.color.white));

        sl_xang.setBackgroundResource(R.drawable.custom_item_selected);
        sl_xang.setTextColor(getResources().getColor(R.color.white));

    }

    private void loadItemSelected() {
        sl_soSan.setOnClickListener(view -> {
            truyenDong="Số sàn";
            sl_soSan.setBackgroundResource(R.drawable.custom_item_selected);
            sl_soSan.setTextColor(getResources().getColor(R.color.white));


            sl_soTD.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_soTD.setTextColor(getResources().getColor(R.color.black));
        });

        sl_soTD.setOnClickListener(view -> {
            truyenDong="Số tự động";
            sl_soSan.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_soSan.setTextColor(getResources().getColor(R.color.black));

            sl_soTD.setBackgroundResource(R.drawable.custom_item_selected);
            sl_soTD.setTextColor(getResources().getColor(R.color.white));
        });

        sl_xang.setOnClickListener(view -> {
            loaiNhienLieu = "Xăng";
            sl_xang.setBackgroundResource(R.drawable.custom_item_selected);
            sl_xang.setTextColor(getResources().getColor(R.color.white));

            sl_dau.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_dau.setTextColor(getResources().getColor(R.color.black));

            sl_dien.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_dien.setTextColor(getResources().getColor(R.color.black));
        });

        sl_dau.setOnClickListener(view -> {
            loaiNhienLieu="Dầu Diesel";
            sl_xang.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_xang.setTextColor(getResources().getColor(R.color.black));

            sl_dien.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_dien.setTextColor(getResources().getColor(R.color.black));

            sl_dau.setBackgroundResource(R.drawable.custom_item_selected);
            sl_dau.setTextColor(getResources().getColor(R.color.white));
        });

        sl_dien.setOnClickListener(view -> {
            loaiNhienLieu="Điện";
            sl_xang.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_xang.setTextColor(getResources().getColor(R.color.black));
            sl_dau.setBackgroundResource(R.drawable.custom_item_non_selected);
            sl_dau.setTextColor(getResources().getColor(R.color.black));
            sl_dien.setBackgroundResource(R.drawable.custom_item_selected);
            sl_dien.setTextColor(getResources().getColor(R.color.white));
        });
    }


    public void Select_Hang_Xe(View view) {
       showHangXeDialog();
    }

    public void showHangXeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_name_hang_xe);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        RecyclerView lv_ten_hang_xe = dialog.findViewById(R.id.lv_ten_hang_xe);
        EditText textFind = dialog.findViewById(R.id.ed_search_list);
        TextView tv_list_null = dialog.findViewById(R.id.id_list_null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lv_ten_hang_xe.setLayoutManager(layoutManager);
        RetrofitClent1.FC_services().getListHang().enqueue(new Callback<CarApiResponse>() {
            @Override
            public void onResponse(Call<CarApiResponse> call, Response<CarApiResponse> response) {
                if(response.code()==200){
                    ten_mau.setText("Chưa chọn");
                    tv_list_null.setVisibility(View.GONE);
                    list = new ArrayList<>(response.body().getData());
                    listfind= new ArrayList<>(list);
                    adapter=new DanhSachTenHangXeAdapter(listfind,getApplicationContext(),ten_hang,make_id,dialog);
                    lv_ten_hang_xe.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<CarApiResponse> call, Throwable t) {
                System.out.println("Có lỗi khi thực hiện: " + t);
            }
        });

        textFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_list_null.setText("Không tìm thấy hãng xe bạn tìm");
                listfind.clear();
                for (HangXe item : list) {
                    if (item.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        listfind.add(item);
                    }
                }
                if(listfind.size()>0){
                    tv_list_null.setVisibility(View.GONE);
                    lv_ten_hang_xe.setVisibility(View.VISIBLE);
                }else {
                    tv_list_null.setVisibility(View.VISIBLE);
                    lv_ten_hang_xe.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void showMauXeDialog() {

        if(Integer.parseInt(make_id.getText().toString())==0){
            Toast.makeText(this,"Vui lòng chọn tên hãng trước", Toast.LENGTH_SHORT).show();
        }else {

            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_name_hang_xe);
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            RecyclerView lv_ten_hang_xe = dialog.findViewById(R.id.lv_ten_hang_xe);
            TextView tv_list_null = dialog.findViewById(R.id.id_list_null);
            EditText textFind = dialog.findViewById(R.id.ed_search_list);
            textFind.setVisibility(View.GONE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            lv_ten_hang_xe.setLayoutManager(layoutManager);
            RetrofitClent1.FC_services().getListModel(Integer.parseInt(make_id.getText().toString()),2020).enqueue(new Callback<CarModelApiResponse>() {
                @Override
                public void onResponse(Call<CarModelApiResponse> call, Response<CarModelApiResponse> response) {
                    if(response.code()==200){
                        System.out.println("aaaaaaaaaaa"+response.body().getData());
                        DanhSachTenModelXeAdapter  adapter=new DanhSachTenModelXeAdapter(response.body().getData(),getApplicationContext(),ten_mau,dialog);
                        lv_ten_hang_xe.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if(response.body().getData().size()>0){
                            lv_ten_hang_xe.setVisibility(View.VISIBLE);
                            tv_list_null.setVisibility(View.GONE);
                        }else {
                            lv_ten_hang_xe.setVisibility(View.GONE);
                            tv_list_null.setVisibility(View.VISIBLE);
                        }

                    }else {
                        System.out.println("đểu "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<CarModelApiResponse> call, Throwable t) {

                }
            });
        }
    }

}