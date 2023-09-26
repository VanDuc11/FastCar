package com.example.fastcar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.Model.User;
import com.example.fastcar.Server.HostApi;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView name, email;
    Button signOutBtn;
    private FirebaseAuth auth;
    private FirebaseUser fBaseuser;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signOutBtn = findViewById(R.id.signout);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        fBaseuser = auth.getCurrentUser();

        Save();


        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
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
        String userName = personName == null ? "UserName" : personName;
        String Email = fBaseuser.getEmail();

        name.setText("name: " + personName + "UID: " + fBaseuser.getUid());
        email.setText( Email);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = HostApi.API_URL +"/user/creater_user";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Đăng nhập thành công"+response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("erro" + error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams()  {
                Map<String,String> data = new HashMap<>();
                data.put("userName",userName);
                data.put("email",Email);
                data.put("UID",fBaseuser.getUid());
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
}