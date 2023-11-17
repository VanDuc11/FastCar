package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
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
import java.util.regex.Pattern;

public class SignUp_Activity extends AppCompatActivity {
    TextInputLayout edtEamil, edtPass, EdtRespass;
    AppCompatButton BtnSignin;
    FirebaseAuth mAuth;
    ImageView btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        find();
        mAuth = FirebaseAuth.getInstance();
        BtnSignin.setOnClickListener(view -> checkSignUp());
        btn_close.setOnClickListener(view -> onBackPressed());
    }

    private void checkSignUp() {
        String email = edtEamil.getEditText().getText().toString();
        String pass = edtPass.getEditText().getText().toString();
        String repass = EdtRespass.getEditText().getText().toString();

        if(validateForm(email, pass, repass)) {
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this);
                                CustomDialogNotify.showToastCustom(SignUp_Activity.this, "Đăng nhập thành công");
                                Intent intent = new Intent(SignUp_Activity.this, KhamPha_Activity.class);
                                intent.putExtra("pass", pass);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                CustomDialogNotify.showToastCustom(SignUp_Activity.this, "Email đã được sử dụng");
                            }
                        }
                    });
        }
    }

    private boolean validateForm(String email, String pass, String repass) {

        if(email.length() == 0) {
            edtEamil.setError("Không để trống email");
            edtEamil.requestFocus();
            return false;
        } else {
            if(!validateEmail(email)) {
                edtEamil.setError("Email không đúng định dạng");
                edtEamil.requestFocus();
                return false;
            }
        }

        if(pass.length() == 0) {
            edtPass.setError("Không để trống mật khẩu");
            edtPass.requestFocus();
            return false;
        } else {
            if(pass.length() < 6) {
                edtPass.setError("Mật khẩu phải dài hơn 6 ký tự");
                edtPass.requestFocus();
                return false;
            }
        }

        if(repass.length() == 0) {
            EdtRespass.setError("Không để trống mật khẩu");
            EdtRespass.requestFocus();
            return false;
        }

        if(!pass.equals(repass)) {
            EdtRespass.setError("Mật khẩu không trùng khớp");
            edtPass.setError("Mật khẩu không trùng khớp");
            return false;
        }
        return true;
    }
    private boolean validateEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    private void find() {
        edtEamil = findViewById(R.id.SignUp_sdt_email);
        edtPass = findViewById(R.id.SignUp_matkhau);
        EdtRespass = findViewById(R.id.SignUp_ResMatkhau);
        BtnSignin = findViewById(R.id.SignUp_btn_SigUp);
        btn_close = findViewById(R.id.SignUP_btn_close);

    }
}