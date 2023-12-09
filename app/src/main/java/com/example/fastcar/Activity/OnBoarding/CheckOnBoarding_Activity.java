package com.example.fastcar.Activity.OnBoarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.fastcar.Activity.Login_Activity;
import com.example.fastcar.R;

public class CheckOnBoarding_Activity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_on_boarding);

        preferences = getSharedPreferences("onboaring", Context.MODE_PRIVATE);
        new Handler().postDelayed(() -> {
            if (preferences.getBoolean("started", false)) {
                // key = true -> login.activity
                startActivity(Login_Activity.class);
            } else {
                // key = false -> onboard.activity
                startActivity(OnBoaring_Activity.class);
            }
        }, 0);
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }
}