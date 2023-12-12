package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ForgotPassword_Activity extends AppCompatActivity {
    EditText edtEmail;
    ImageView btnBack;
    AppCompatButton btnSend;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mapping();
        mAuth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(view -> onBackPressed());

        btnSend.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            if (validateForm(email)) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressBar.setVisibility(View.GONE);
                                CustomDialogNotify.showToastCustom(ForgotPassword_Activity.this, "Đã gửi liên kết xác thực đến email: " + email);
                                btnSend.setVisibility(View.GONE);
                                Handler handler = new Handler(Looper.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        onBackPressed();
                                        finish();
                                    }
                                };
                                handler.postDelayed(() -> handler.post(myRunnable), 2000);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                CustomDialogNotify.showToastCustom(ForgotPassword_Activity.this, "Email không tồn tại");
                            }
                        });
            }
        });

        progressBar.setVisibility(View.GONE);
    }

    private void mapping() {
        btnSend = findViewById(R.id.btnSendEmail_ResetPassword);
        btnBack = findViewById(R.id.btnBack_inForgotPassword);
        edtEmail = findViewById(R.id.edt_email_in_forgot_password);
        progressBar = findViewById(R.id.progressBar_inForgotPassword);
    }

    private boolean validateForm(String email) {
        if (email.length() == 0) {
            CustomDialogNotify.showToastCustom(this, "Chưa có email");
            edtEmail.requestFocus();
            return false;
        } else {
            if (!validateEmail(email)) {
                CustomDialogNotify.showToastCustom(this, "Email không đúng định dạng");
                edtEmail.requestFocus();
                return false;
            }
        }
        return true;
    }

    private boolean validateEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}