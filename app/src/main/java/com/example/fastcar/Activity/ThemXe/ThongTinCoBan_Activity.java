    package com.example.fastcar.Activity.ThemXe;

    import androidx.appcompat.app.AppCompatActivity;

    import android.animation.Animator;
    import android.animation.AnimatorInflater;
    import android.animation.AnimatorListenerAdapter;
    import android.animation.ArgbEvaluator;
    import android.animation.ValueAnimator;
    import android.content.Intent;
    import android.os.Bundle;

    import android.view.View;
    import android.view.animation.TranslateAnimation;
    import android.widget.TextView;

    import com.example.fastcar.R;

    public class ThongTinCoBan_Activity extends AppCompatActivity {

        TextView button1  ,button2,btn_tieptuc ;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_thong_tin_co_ban);
            button1 = findViewById(R.id.btn_themxe);
            button2 = findViewById(R.id.btn_themxe1);
            btn_tieptuc = findViewById(R.id.btn_tieptuc);


            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button1.setBackgroundColor(getResources().getColor(R.color.black));
                    button1.setTextColor(getResources().getColor(R.color.white));

                    button2.setBackgroundColor(getResources().getColor(R.color.button_default_bg));
                    button2.setTextColor(getResources().getColor(R.color.black));
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button2.setBackgroundColor(getResources().getColor(R.color.black));
                    button2.setTextColor(getResources().getColor(R.color.white));

                    button1.setBackgroundColor(getResources().getColor(R.color.button_default_bg));
                    button1.setTextColor(getResources().getColor(R.color.black));
                }
            });
            btn_tieptuc.setOnClickListener(view -> startActivity(new Intent(getBaseContext(), ThongTinChiTiet_Activity.class)));
        }

    }