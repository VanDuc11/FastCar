package com.example.fastcar.Activity.act_bottom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fastcar.Activity.DanhSachXe_Activity;
import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.Adapter.DanhSachXeAdapter;
import com.example.fastcar.Adapter.KhuyenMaiApdater;
import com.example.fastcar.Adapter.XeKhamPhaAdapter;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KhamPha_Activity extends AppCompatActivity {
    TextView goDS, tvName;
    RecyclerView recy1, recy2;

    private FirebaseAuth auth;
    private FirebaseUser fBaseuser;
    FirebaseDatabase database;


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
        goDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), DanhSachXe_Activity.class);
                startActivity(i);
            }
        });


    }

    void mapping() {
        goDS = findViewById(R.id.txt_ds);
        tvName = findViewById(R.id.tvName);
        recy1 = findViewById(R.id.recyclerView);
        recy2 = findViewById(R.id.recyclerView2);
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
        XeKhamPhaAdapter adapter1 = new XeKhamPhaAdapter(this,itemList);
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
        tvName.setText(userName);

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
        startActivity(new Intent(getBaseContext(), ThongBao_Activity.class));
    }

    public void tab1_to_tab3(View view) {
        startActivity(new Intent(getBaseContext(), ChuyenXe_Activity.class));
    }

    public void tab1_to_tab4(View view) {
        startActivity(new Intent(getBaseContext(), HoTro_Activity.class));
    }

    public void tab1_to_tab5(View view) {
        startActivity(new Intent(getBaseContext(), CaNhan_Activity.class));
    }

    @Override
    public void onBackPressed() {

    }

}