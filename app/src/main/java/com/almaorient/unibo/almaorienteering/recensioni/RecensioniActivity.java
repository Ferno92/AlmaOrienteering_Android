package com.almaorient.unibo.almaorienteering.recensioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.unibo.almaorienteering.BaseActivity;
import com.almaorient.unibo.almaorienteering.MapsActivity;
import com.almaorient.unibo.almaorienteering.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by lucas on 18/03/2017.
 */

public class RecensioniActivity extends BaseActivity {
    DatabaseReference mRef;
    private String mCorsoId;
    private String mScuolaId;
    private String mNomeCorso;
    private boolean mIsValid = false;
    private int mPosition;
    private RatingBar mRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recensioni_activity);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        Query query = mRef.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String id = (String) data.child("userId").getValue();
                    id = id.indexOf("@studio.unibo.it") == -1 ? id + "@studio.unibo.it" : id;
                    if (id.equals(String.valueOf(mAuth.getCurrentUser().getEmail()))) {
                        HashMap corsoMap = (HashMap) data.child("corso").getValue();
                        Iterator corsoIterator = corsoMap.keySet().iterator();
                        while (corsoIterator.hasNext()) {
                            String corsoKey = (String) corsoIterator.next();
                            switch (corsoKey) {
                                case "id":
                                    mCorsoId = String.valueOf(corsoMap.get(corsoKey));
                                    break;
                                case "nome":
                                    mNomeCorso = String.valueOf(corsoMap.get(corsoKey));
                                    break;
                                default:
                                    break;
                            }
                        }

                        TextView nomeCorsoTextView = (TextView) findViewById(R.id.nome_corso);
                        nomeCorsoTextView.setText(mNomeCorso);

                        //Set scuola
                        String scuola = (String) data.child("scuola").getValue();
                        for (int i = 0; i < MapsActivity.mScuolaadatt.length; i++) {
                            if (MapsActivity.mScuolaadatt[i].getNome().equals(scuola)) {
                                mScuolaId = MapsActivity.mScuolaadatt[i].getScuolaId();
                            }
                        }
                    }
                }
                initSendButton();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initSendButton() {
        AppCompatButton sendButton = (AppCompatButton) findViewById(R.id.send_recensione);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.form_recensione);
                final RelativeLayout successLayout = (RelativeLayout) findViewById(R.id.success_layout);

                mIsValid = validateRecensione();

                if (mIsValid) {

                    Query query = mRef.child("recensioni/" + mScuolaId).orderByKey();
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int pos = 0;
                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                if(String.valueOf(data.child("corso_codice").getValue()).equals(mCorsoId)) {// if id è lo stesso
                                    mPosition = pos;
                                    break;
                                }
                                pos++;
                            }

                            EditText textRec = (EditText)findViewById(R.id.text_rec);
                            String testoRecensione = textRec.getText().toString();

                            RecensioniModel rec = new RecensioniModel(String.valueOf(mAuth.getCurrentUser().getEmail()),
                                    String.valueOf(mRating.getRating()), testoRecensione, 0);
                            mRef.child("recensioni/" + mScuolaId + "/" + mPosition + "/recensioni").push().setValue(rec);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if(databaseError != null){

                            }
                        }
                    });


                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(500);

                    fadeOut.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            mainLayout.setVisibility(View.GONE);

                            startRecensioniListActivity();
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {

                            successLayout.setVisibility(View.VISIBLE);
                        }
                    });
                    mainLayout.startAnimation(fadeOut);
                }


            }
        });
    }

    private boolean validateRecensione() {
        mRating = (RatingBar) findViewById(R.id.rating_recensione);
        if (mRating.getRating() != 0) {
            return true;
        } else {
            Toast.makeText(this, "Non hai votato!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void startRecensioniListActivity() {

        Intent i = new Intent(RecensioniActivity.this, ListaRecensioniActivity.class);
        i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("nome_corso", mNomeCorso);
        i.putExtra("scuola", mScuolaId);
        i.putExtra("codice_corso", mCorsoId);
        startActivity(i);
        finish();

    }


}
