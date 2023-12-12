package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.Model.ResMessage;
import com.example.fastcar.Model.User;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.CallbackManager;
import com.facebook.login.Login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {
    LinearLayout btnGoogle;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    GoogleSignInAccount acct;
    AppCompatButton btn_login;
    TextView tvSignUp, btnForgotPassword;
    EditText edtEmail, edtPass;
    ImageView btnShowPass;
    private boolean passwordShowing = false;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        find();
        load();
        googleBuile();
        btnGoogle.setOnClickListener(view -> signIn());
        btn_login.setOnClickListener(v -> logInEmail());
        tvSignUp.setOnClickListener(view -> startActivity(new Intent(Login_Activity.this, SignUp_Activity.class)));
        btnForgotPassword.setOnClickListener(view -> startActivity(new Intent(this, ForgotPassword_Activity.class)));

        btnShowPass.setOnClickListener(view -> {
            if (passwordShowing) {
                passwordShowing = false;
                edtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnShowPass.setImageResource(R.drawable.icon_show_pasword);
            } else {
                passwordShowing = true;
                edtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnShowPass.setImageResource(R.drawable.icon_hide_password);
            }
            edtPass.setSelection(edtPass.length());
        });
        progressBar.setVisibility(View.GONE);
    }

    private void load() {
        Intent intent = getIntent();
        edtEmail.setText(intent.getStringExtra("email"));
        edtPass.setText(intent.getStringExtra("pass"));
    }

    private void logInEmail() {
        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();
        if (validateForm(email, pass)) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser() != null) {
                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        CustomDialogNotify.showToastCustom(Login_Activity.this, "Đăng nhập thành công");
                                        Intent intent = new Intent(Login_Activity.this, KhamPha_Activity.class);
                                        intent.putExtra("pass", pass);
                                        startActivity(intent);
                                    } else {
                                        CustomDialogNotify.showToastCustom(Login_Activity.this, "Vui lòng xác minh email của bạn");
                                    }
                                }
                            } else {
                                CustomDialogNotify.showToastCustom(Login_Activity.this, "Tài khoản hoặc mật khẩu không chính xác");
                            }
                        }
                    });
        }
    }

    private void find() {
        btnGoogle = findViewById(R.id.loginAct_btnGoogle);
        btn_login = findViewById(R.id.Logint_btn_login);
        tvSignUp = findViewById(R.id.Login_Signup);
        edtEmail = findViewById(R.id.Login_sdt_email);
        edtPass = findViewById(R.id.LogIn_matkhau);
        btnForgotPassword = findViewById(R.id.btnForgot_Password);
        btnShowPass = findViewById(R.id.show_pass_inlogin);
        progressBar = findViewById(R.id.progressBar_inLogin);
    }

    private void googleBuile() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
    }

    int RC_SIGN_IN = 40;

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                acct = GoogleSignIn.getLastSignedInAccount(this);
                handleGoogleAccessToken(acct);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            navigateToSecondActivity();
        }
    }

    private void handleGoogleAccessToken(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            navigateToSecondActivity();
                        } else {
                            Toast.makeText(Login_Activity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(Login_Activity.this, KhamPha_Activity.class);
        startActivity(intent);
    }

    private boolean validateForm(String email, String password) {
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

        if (password.length() == 0) {
            CustomDialogNotify.showToastCustom(this, "Chưa có mật khẩu");
            edtPass.requestFocus();
            return false;
        } else {
            if (password.length() < 6) {
                CustomDialogNotify.showToastCustom(this, "Mật khẩu phải dài hơn 6 ký tự");
                edtPass.requestFocus();
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

    @Override
    public void onBackPressed() {
    }
}