package com.iti.intake40.tripguide.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.iti.intake40.tripguide.R;

public class Launch extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Animation fadeIn= AnimationUtils.loadAnimation(this,R.anim.fade_in);
        findViewById(R.id.splash_text).startAnimation(fadeIn);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Launch.this,Login.class);
                Launch.this.startActivity(mainIntent);
                Launch.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
