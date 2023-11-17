package com.example.fastcar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastcar.Activity.act_bottom.KhamPha_Activity;
import com.example.fastcar.Dialog.CustomDialogNotify;
import com.example.fastcar.R;
import com.example.fastcar.Retrofit.RetrofitClient;
import com.facebook.CallbackManager;
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

public class Login_Activity extends AppCompatActivity {
    FrameLayout btnGoogle;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    GoogleSignInAccount acct;
    AppCompatButton btn_login;
    TextView tvSignUp;
    TextInputLayout edtEmail,edtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        find();
        googleBuile();
        btnGoogle.setOnClickListener(view -> signIn());
        btn_login.setOnClickListener(v -> logInEmail());
        tvSignUp.setOnClickListener(view -> {
            Intent intent  = new Intent(Login_Activity.this,SignUp_Activity.class);
            startActivity(intent);
        });
    }

    private void logInEmail() {
        String email = edtEmail.getEditText().getText().toString();
        String pass = edtPass.getEditText().getText().toString();
        if (email.length() ==0){
            edtEmail.setError("Không được để trống.");
        }else if (pass.length() == 0){
            edtPass.setError("Không được để trống");
        }else if (email.length()!=0 && pass.length() != 0 ){
            // firebase
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                CustomDialogNotify.showToastCustom(Login_Activity.this, "Đăng nhập thành công");
                                navigateToSecondActivity();
                            } else {
                                // If sign in fails, display a message to the user.
                                CustomDialogNotify.showToastCustom(Login_Activity.this, "Tài khoản hoặc mật khẩu không chính xác");
                            }
                        }
                    });


            // call api login
//            RequestQueue queue = Volley.newRequestQueue(this);
//            String url = HostApi.API_URL + "/api/user/login";
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    CustomDialogNotify.showToastCustom(Login_Activity.this, "Đăng nhập thành công");
//                    System.out.println("Đăng nhập thành công" + response.toString());
//                    navigateToSecondActivity();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    CustomDialogNotify.showToastCustom(Login_Activity.this, "Tài khoản hoặc mật khẩu không chính xác");
//                }
//            }) {
//                @androidx.annotation.Nullable
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> data = new HashMap<>();
//                    data.put("email", email);
//                    data.put("pass", pass);
//                    return data;
//                }
//            };
//
//            queue.add(stringRequest);

        }
    }

    private void find() {
        btnGoogle = findViewById(R.id.loginAct_btnGoogle);
        btn_login =findViewById(R.id.Logint_btn_login);
        tvSignUp = findViewById(R.id.Login_Signup);
        edtEmail = findViewById(R.id.Login_sdt_email);
        edtPass = findViewById(R.id.LogIn_matkhau);
    }
    private void googleBuile(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                . requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
    }

    int RC_SIGN_IN = 40;
    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
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
        if (mAuth.getCurrentUser() !=null){
            navigateToSecondActivity();
        }
    }
    private void  handleGoogleAccessToken(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            navigateToSecondActivity();
                        }
                        else {
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


}