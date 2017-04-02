package com.almaorient.ferno92.almaorienteering.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.homepage.NewMainActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by lucas on 06/03/2017.
 */

public class LoginActivity extends BaseActivity {
    private FirebaseAuth mAuth;
    EditText mEmailEdit;
    EditText mPwdEdit;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "Sign in";
    private static String MAIL_END = "@studio.unibo.it";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.login_activity);
        AppCompatButton signupButton = (AppCompatButton) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        AppCompatImageButton help = (AppCompatImageButton)findViewById(R.id.help_password);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPWDAlert();
            }
        });

        AppCompatButton loginButton = (AppCompatButton) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmailEdit.getText().toString();
                String password = mPwdEdit.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    mAuth.signInWithEmailAndPassword(email + MAIL_END, password);
                }else{
                    Toast.makeText(LoginActivity.this, "Mancano alcuni dati!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent i = new Intent(LoginActivity.this, NewMainActivity.class);
                    i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        initView();
    }

    private void showRecoverPWDAlert() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.alert_recover_pwd, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Recupera password");
        builder.setView(promptView);
        builder.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText userEmail = (EditText) promptView.findViewById(R.id.email_to_recover);
                String email = userEmail.getText().toString();
                if(!email.isEmpty()){
                    mAuth.sendPasswordResetEmail(email + MAIL_END)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(LoginActivity.this, "Email inviata! Controlla la tua casella di posta",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, "Inserisci la tua email!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initView(){
        mEmailEdit = (EditText) findViewById(R.id.email);
        mPwdEdit = (EditText) findViewById(R.id.pwd);
    }
}
