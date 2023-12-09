package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignUp_Activity extends AppCompatActivity {
    EditText edtEamil, edtPass, EdtRespass;
    AppCompatButton BtnSignin;
    FirebaseAuth mAuth;
    ImageView btn_close;
    TextView tvBack;
    ImageView btnShowPass, btnShowRePass;
    private boolean isShowingPass = false;
    private boolean isShowingRePass = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        find();
        mAuth = FirebaseAuth.getInstance();
        BtnSignin.setOnClickListener(view -> checkSignUp());
        btn_close.setOnClickListener(view -> onBackPressed());
        tvBack.setOnClickListener(view -> onBackPressed());

        btnShowPass.setOnClickListener(view -> {
            if (isShowingPass) {
                isShowingPass = false;
                edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnShowPass.setImageResource(R.drawable.icon_show_pasword);
            } else {
                isShowingPass = true;
                edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnShowPass.setImageResource(R.drawable.icon_hide_password);
            }
        });

        btnShowRePass.setOnClickListener(view -> {
            if (isShowingRePass) {
                isShowingRePass = false;
                EdtRespass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnShowRePass.setImageResource(R.drawable.icon_show_pasword);
            } else {
                isShowingRePass = true;
                EdtRespass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnShowRePass.setImageResource(R.drawable.icon_hide_password);
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    private void checkSignUp() {
        String email = edtEamil.getText().toString();
        String pass = edtPass.getText().toString();
        String repass = EdtRespass.getText().toString();

        if (validateForm(email, pass, repass)) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // send email verify
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            edtEamil.setText("");
                                            edtPass.setText("");
                                            EdtRespass.setText("");
                                            CustomDialogNotify.showToastCustom(getBaseContext(), "Đăng ký tài khoản thành công. Vui lòng kiểm tra email để xác minh!");

                                            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful()) {
                                                        if(!mAuth.getCurrentUser().isEmailVerified()) {
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Intent intent = new Intent(SignUp_Activity.this, Login_Activity.class);
                                                                    intent.putExtra("email", email);
                                                                    intent.putExtra("pass", pass);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }, 2000);
                                                        }
                                                    }
                                                }
                                            });

                                        } else {
                                            CustomDialogNotify.showToastCustom(getBaseContext(), task.getException().getMessage());
                                        }
                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user.
                                CustomDialogNotify.showToastCustom(SignUp_Activity.this, "Email đã được sử dụng");
                            }
                        }
                    });
        }
    }

    private boolean validateForm(String email, String pass, String repass) {
        if (email.length() == 0) {
            CustomDialogNotify.showToastCustom(this, "Chưa có email");
            edtEamil.requestFocus();
            return false;
        } else {
            if (!validateEmail(email)) {
                CustomDialogNotify.showToastCustom(this, "Email không đúng định dạng");
                edtEamil.requestFocus();
                return false;
            }
        }

        if (pass.length() == 0) {
            CustomDialogNotify.showToastCustom(this, "Chưa có mật khẩu");
            edtPass.requestFocus();
            return false;
        } else {
            if (pass.length() < 6) {
                CustomDialogNotify.showToastCustom(this, "Mật khẩu phải dài hơn 6 ký tự");
                edtPass.requestFocus();
                return false;
            }
        }

        if (repass.length() == 0) {
            CustomDialogNotify.showToastCustom(this, "Chưa có mật khẩu nhập lại");
            EdtRespass.requestFocus();
            return false;
        }

        if (!pass.equals(repass)) {
            CustomDialogNotify.showToastCustom(this, "Mật khẩu không trùng khớp");
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
        tvBack = findViewById(R.id.btnBack_inReg);
        btnShowPass = findViewById(R.id.show_pass_inreg);
        btnShowRePass = findViewById(R.id.show_repass_inreg);
        progressBar = findViewById(R.id.progressBar_inRegister);
    }
}