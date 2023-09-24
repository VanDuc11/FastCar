package com.example.fastcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.Model.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextView name,email;
    Button signOutBtn;
    private FirebaseAuth auth;
    private FirebaseUser fBaseuser ;
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

        if (fBaseuser !=null){
            User u = new User();
            u.setUserId(fBaseuser.getUid());
            u.setEmail(fBaseuser.getEmail());
            u.setName(fBaseuser.getDisplayName());
            u.setProfile(fBaseuser.getProviderId().toString());
            database.getReference().child("Users").child(fBaseuser.getUid()).setValue(u);
        }
        String personName = fBaseuser.getDisplayName();
        String personEmail = fBaseuser.getEmail();
        name.setText(personName);
        email.setText(personEmail);

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

    void signOut(){
        auth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, Login_Activity.class));
        finish();
    }
}