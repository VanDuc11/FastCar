package com.example.fastcar.Activity.act_bottom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.fastcar.Activity.DanhSachXe_Activity;
import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Adapter.KhuyenMaiApdater;
import com.example.fastcar.Adapter.XeKhamPhaAdapter;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.facebook.login.LoginManager;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class KhamPha_Activity extends AppCompatActivity {
    TextView goDS, tvName, btnTim, tvTime_inKhamPha;
    RecyclerView recy1, recy2;
    CircleImageView img_user;

    private FirebaseAuth auth;
    private FirebaseUser fBaseuser;
    FirebaseDatabase database;
    MaterialDatePicker<Pair<Long, Long>> datePicker;
    Long todayInMillis, tomorrowInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kham_pha);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fBaseuser = auth.getCurrentUser();

        mapping();
        Save();
        load();
        build_DatePicker();
        setDefault_SelectionDate();

        goDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), DanhSachXe_Activity.class);
                startActivity(i);
            }
        });
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), DanhSachXe_Activity.class);
                startActivity(i);
            }
        });


    }

    void mapping() {
        goDS = findViewById(R.id.txt_ds);
        tvName = findViewById(R.id.tvName);
        btnTim = findViewById(R.id.act_khamha_tvTimXe);
        tvTime_inKhamPha = findViewById(R.id.tvTime_inKhamPha);
        recy1 = findViewById(R.id.recyclerView);
        recy2 = findViewById(R.id.recyclerView2);
        img_user = findViewById(R.id.img_user_inKhamPha);
    }

    void load() {
        recy1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recy2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        List<String> itemList = new ArrayList<>();
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");
        itemList.add("aaaaaa");// Replace with your data
        KhuyenMaiApdater adapter = new KhuyenMaiApdater(this, itemList);
        recy1.setAdapter(adapter);
        XeKhamPhaAdapter adapter1 = new XeKhamPhaAdapter(this, itemList);
        recy2.setAdapter(adapter1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fBaseuser == null) {
            signOut();
        }
    }

    void Save() {
        String personName = fBaseuser.getDisplayName();
        String userName = personName == "" ? "UserName" : personName;
        String Email = fBaseuser.getEmail();
        Uri uri = fBaseuser.getPhotoUrl();
        tvName.setText(userName);

        if (uri != null) {
            Glide.with(getBaseContext())
                    .load(uri)
                    .into(img_user);
        } else {
            Glide.with(getBaseContext())
                    .load(R.drawable.img_avatar_user)
                    .into(img_user);
        }


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = HostApi.API_URL + "/user/creater_user";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Đăng nhập thành công" + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("erro" + error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("userName", userName);
                data.put("email", Email);
                data.put("UID", fBaseuser.getUid());
                return data;
            }
        };

        queue.add(stringRequest);
    }

    void signOut() {
        auth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, Login_Activity.class));
        finish();
    }

    void navigate_() {
        startActivity(new Intent(getBaseContext(), DanhSachXe_Activity.class));
    }


    public void tab1_to_tab2(View view) {
        startActivity(new Intent(getBaseContext(), ChuyenXe_Activity.class));
    }

    public void tab1_to_tab3(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    public void tab1_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
    }

    private void build_DatePicker() {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

        todayInMillis = today.getTimeInMillis();
        tomorrowInMillis = tomorrow.getTimeInMillis();

        datePicker = MaterialDatePicker.Builder.dateRangePicker()
//                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
//                        MaterialDatePicker.todayInUtcMilliseconds()))
                .setSelection(Pair.create(todayInMillis, tomorrowInMillis))
                .setCalendarConstraints(buildCalendarConstraints())
                .build();

        System.out.println("date: " + todayInMillis);
    }

    public void showDatePicker(View view) {
        datePicker.show(getSupportFragmentManager(), "Material_Range");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                // Lấy ngày bắt đầu và ngày kết thúc từ Pair
                Long startDate = selection.first;
                Long endDate = selection.second;

                // Định dạng ngày tháng theo định dạng tiếng Việt
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedStartDate = sdf.format(new Date(startDate));
                String formattedEndDate = sdf.format(new Date(endDate));

                String resultText = formattedStartDate + " - " + formattedEndDate;
                tvTime_inKhamPha.setText(resultText);
            }
        });
    }

    private void setDefault_SelectionDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedToday = sdf.format(todayInMillis);
        String formattedTomorrow = sdf.format(tomorrowInMillis);

        String defaultText = formattedToday + " - " + formattedTomorrow;
        tvTime_inKhamPha.setText(defaultText);
    }

    private CalendarConstraints buildCalendarConstraints() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        // Chỉ cho phép chọn từ ngày hiện tại trở đi
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        constraintsBuilder.setStart(today);

        // Vô hiệu hóa ngày trong quá khứ bằng cách sử dụng setValidator
        // Các ngày không hợp lệ (trong quá khứ) sẽ không thể chọn được
        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                return date >= today;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
            }
        });

        return constraintsBuilder.build();
    }

    @Override
    public void onBackPressed() {

    }

}