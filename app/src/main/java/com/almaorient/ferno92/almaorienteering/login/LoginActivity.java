package com.almaorient.ferno92.almaorienteering.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.homepage.NewMainActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
    private Set<String> mSuggestions;
    private SharedPreferences mSharedPreferences;

    //autocompletetextview

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.login_activity);
        initView();

        mSharedPreferences = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        Set<String> fakeSet = new TreeSet<>();
        mSuggestions = mSharedPreferences.getStringSet("login_suggestions", fakeSet);

        String[] suggestionList = new String[mSuggestions.size()];
        Iterator it=mSuggestions.iterator();
        int i = 0;
        while(it.hasNext())
        {
            String value = (String)it.next();
            if(value.indexOf("|") != -1) {
                String user = value.substring(0, value.indexOf("|"));
                suggestionList[i] = user;
            }else{
                suggestionList[i] = value;
            }
            i++;
        }

        AutoCompleteTextView autocompleteUser = (AutoCompleteTextView) findViewById(R.id.email);
        ArrayAdapter<String> adapterUser = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestionList
        );
        autocompleteUser.setAdapter(adapterUser);

        autocompleteUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos,long id) {
                String selected = (String)adapter.getItemAtPosition(pos);
                Iterator it=mSuggestions.iterator();
                while(it.hasNext())
                {
                    String value = (String)it.next();
                    String user = value.substring(0, value.indexOf("|"));
                    String pwd = value.substring(value.indexOf("|") + 1, value.length());
                    if(user.equals(selected)){
                        mPwdEdit.setText(pwd);
                        break;
                    }
                }
            }
        });

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
                    mAuth.signInWithEmailAndPassword(email + MAIL_END, password).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthException) {
                                ((FirebaseAuthException) e).getErrorCode();
                                Toast.makeText(LoginActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mSuggestions.add(email + "|" + password);
                    mSharedPreferences.edit().putStringSet("login_suggestions", mSuggestions).commit();
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
