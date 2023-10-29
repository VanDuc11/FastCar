package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.R;
import com.example.fastcar.Server.HostApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUp_Activity extends AppCompatActivity {
    TextInputLayout edtEamil,edtPass,EdtRespass;
    AppCompatButton BtnSignin;
    FirebaseAuth mAuth;
    ImageButton btn_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        find();
        mAuth = FirebaseAuth.getInstance();
        BtnSignin.setOnClickListener(view -> checkSignUp());
        btn_close.setOnClickListener(view -> onBackPressed());
    }
    private void checkSignUp(){
        String email = edtEamil.getEditText().getText().toString();
        String pass = edtPass.getEditText().getText().toString();
        if (email.length() == 0 ){
            edtEamil.setError("Không để trống email");

        }else  if (pass.length() == 0){
            edtPass.setError("Không để trống mật khẩu");
        }else if (EdtRespass.getEditText().getText().toString().length() == 0){
            EdtRespass.setError("Không để trống mật khẩu");
        }else if (pass.length() <= 6){
            EdtRespass.setError("Mật khẩu phải dài hơn 6 ký tự");
        }else if (pass.equalsIgnoreCase(EdtRespass.getEditText().getText().toString())){

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                createUser_inMongoDB(email, pass);
                                Toast.makeText(SignUp_Activity.this, "Đăng nhập thành công.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp_Activity.this, KhamPha_Activity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUp_Activity.this, "Email đã được sử dụng.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }else {
            edtPass.setError("Mật khẩu không khớp");
            EdtRespass.setError("Mật khẩu không khớp");
        }
    }

    private void createUser_inMongoDB(String email, String pass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = HostApi.API_URL + "/api/user/create";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Đăng ký thành công in MongoDB" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Đăng ký thất bại in MongoDB" + error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Date getTimeNow = new Date();
                Map<String, String> data = new HashMap<>();
                data.put("userName", "UserName");
                data.put("email", email);
                data.put("MatKhau", pass);
                data.put("NgayThamGia", String.valueOf(getTimeNow));
                return data;
            }
        };

        queue.add(stringRequest);
    }
    private void find(){
        edtEamil = findViewById(R.id.SignUp_sdt_email);
        edtPass = findViewById(R.id.SignUp_matkhau);
        EdtRespass = findViewById(R.id.SignUp_ResMatkhau);
        BtnSignin = findViewById(R.id.SignUp_btn_SigUp);
        btn_close = findViewById(R.id.SignUP_btn_close);

    }
}