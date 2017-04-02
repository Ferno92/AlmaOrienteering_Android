package com.almaorient.ferno92.almaorienteering;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.almaorient.ferno92.almaorienteering.homepage.NewMainActivity;
import com.almaorient.ferno92.almaorienteering.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by lucas on 06/03/2017.
 */

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            Intent i = new Intent(ChooseActivity.this, NewMainActivity.class);
            i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        }else{
            setContentView(R.layout.choose_activity);

            AppCompatButton studenteButton = (AppCompatButton) findViewById(R.id.studente);
            AppCompatButton orientatiButton = (AppCompatButton) findViewById(R.id.orientati);

            studenteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ChooseActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });

            orientatiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(ChooseActivity.this, NewMainActivity.class);
                    startActivity(i);
                }
            });
        }

    }
}
