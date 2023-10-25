package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
        BtnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSignUp();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
    private void find(){
        edtEamil = findViewById(R.id.SignUp_sdt_email);
        edtPass = findViewById(R.id.SignUp_matkhau);
        EdtRespass = findViewById(R.id.SignUp_ResMatkhau);
        BtnSignin = findViewById(R.id.SignUp_btn_SigUp);
        btn_close = findViewById(R.id.SignUP_btn_close);

    }
}