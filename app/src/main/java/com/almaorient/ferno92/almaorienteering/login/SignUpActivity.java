package com.almaorient.ferno92.almaorienteering.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.calendar.CalendarModel;
import com.almaorient.ferno92.almaorienteering.homepage.NewMainActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by luca.fernandez on 10/03/2017.
 */

public class SignUpActivity extends BaseActivity {
//    https://firebase.google.com/docs/analytics/android/properties
//    https://firebase.google.com/docs/auth/android/password-auth
//    https://firebase.google.com/docs/auth/android/manage-users

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String TAG = "Sign UP";
    private String BASE_EMAIL_TYPE = "@studio.unibo.it";
    private DatabaseReference mRef;
    EditText mEmailEdit;
    EditText mPwdEdit;
    Spinner mScuolaSpinner;
    Spinner mCorsoSpinner;
    Scuola mSelectedScuola;
    Corso mSelectedCorso;
    private List<Corso> mListaCorsi = new ArrayList<Corso>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_activity);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRef = database.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        AppCompatButton signupButton = (AppCompatButton) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });

        initView();
        initScuolaArray();
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
        mScuolaSpinner = (Spinner) findViewById(R.id.scuola_spinner);
        mCorsoSpinner = (Spinner) findViewById(R.id.corso_spinner);
    }

    private void initScuolaArray(){
        ArrayAdapter spinnerScuolaArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, new Scuola[]{
            new Scuola("agraria", "Agraria e Medicina veterinaria"),
                new Scuola("economia", "Economia, Mangement e Statistica"),
                new Scuola("farmacia", "Farmacia, Biotecnologie e Scienze motorie"),
                new Scuola("giurisprudenza", "Giurisprudenza"),
                new Scuola("ingegneria", "Ingegneria e architettura"),
                new Scuola("lettere", "Lettere e Beni culturali"),
                new Scuola("lingue", "Lingue e letterature, Traduzione e Interpretazione"),
                new Scuola("medicina", "Medicina e Chirurgia"),
                new Scuola("psicologia", "Psicologia e Scienze della formazione"),
                new Scuola("scienze", "Scienze"),
                new Scuola("politiche", "Scienze politiche")
        });
        mScuolaSpinner.setAdapter(spinnerScuolaArrayAdapter);
        mScuolaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedScuola = (Scuola) mScuolaSpinner.getSelectedItem();
                mListaCorsi.clear();
                setSpinnerCorso();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSpinnerCorso() {
        Query query = mRef.child("corso").child(this.mSelectedScuola.getScuolaId()).orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String nome = (String) data.child("corso_descrizione").getValue();
                    String id = String.valueOf(data.child("corso_codice").getValue());

                    Corso corso = new Corso(id, nome,"","","","",null,null,"","");
                    mListaCorsi.add(corso);
                }
                Collections.sort(mListaCorsi, new CorsoComparator());
                fillSpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null){

                }
            }
        });
    }

    private void createNewUser(){

        final String email = mEmailEdit.getText().toString();
        String password = mPwdEdit.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){

                mAuth.createUserWithEmailAndPassword(email + BASE_EMAIL_TYPE, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    // Add user properties
                                    Map<String, String> corso = new HashMap<String, String>();
                                    corso.put("id", mSelectedCorso.getCorsoCodice());
                                    corso.put("nome", mSelectedCorso.getNome());

                                    StudenteUnibo user = new StudenteUnibo(email, "", "",
                                            corso,
                                            mSelectedScuola.getNome());
                                    mRef.child("users").push().setValue(user);

                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Registrazione avvenuta! Conferma la tua effettiva identità con la mail che ti abbiamo appena inviato",
                                                        Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(SignUpActivity.this, NewMainActivity.class);
                                                i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    });


                                }
                            }
                        });
        }else{
            Toast.makeText(SignUpActivity.this, "Mancano dei dati obbligatori",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void fillSpinner(){
        ArrayAdapter spinnerCorsoArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, mListaCorsi);
        mCorsoSpinner.setAdapter(spinnerCorsoArrayAdapter);
        mCorsoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCorso = (Corso) mCorsoSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class CorsoComparator implements Comparator<Corso> {
        @Override
        public int compare(Corso a, Corso b) {
            return a.getNome().compareTo(b.getNome());
        }
    }
}

