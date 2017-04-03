package com.almaorient.ferno92.almaorienteering;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.almaorient.ferno92.almaorienteering.tutorial.TutorialActivity;

/**
 * Created by luca.fernandez on 06/03/2017.
 */

public class SplashActivity extends AppCompatActivity {
    int timeout = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent i = new Intent(SplashActivity.this, TutorialActivity.class);
                startActivity(i);
                finish();
            }
        }, timeout);

    }
}
