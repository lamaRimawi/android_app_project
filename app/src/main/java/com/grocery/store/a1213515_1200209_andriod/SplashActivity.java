package com.grocery.store.a1213515_1200209_andriod;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoImage = findViewById(R.id.splash_logo);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1500);
        scaleAnimation.setFillAfter(true);

        logoImage.startAnimation(scaleAnimation);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, IntroductionActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}