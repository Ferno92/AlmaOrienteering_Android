package com.almaorient.ferno92.almaorienteering.versus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.firebaseDB.AulaModel;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 14/03/2017.
 */

public class VersusCorsoActivity extends BaseActivity {

    private String mScuola1;
    private String mScuola2;
    private Spinner mCorso1Spinner;
    private Spinner mCorso2Spinner;
    private DatabaseReference mRef;
    private List<Corso> mListaCorsi1 = new ArrayList<Corso>();
    private List<Corso> mListaCorsi2 = new ArrayList<Corso>();
    private Corso mSelectedCorso1;
    Corso mSelectedCorso2;
    ProgressDialog mProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.versus_corso_activity);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Stiamo cercando i corsi...");
        mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        mProgress.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRef = database.getReference();

        this.mScuola1 = getIntent().getExtras().getString("scuola1");
        this.mScuola2 = getIntent().getExtras().getString("scuola2");

        initView();

    }

    private void initView(){
        mCorso1Spinner = (Spinner) findViewById(R.id.spinner_scuola1);
        mCorso2Spinner = (Spinner) findViewById(R.id.spinner_scuola2);

        for(int i = 0; i < VersusSelectorActivity.mElencoScuola1.length; i++){
            if(VersusSelectorActivity.mElencoScuola1[i].getScuolaId().equals(this.mScuola1)){
                TextView scuola1 = (TextView) findViewById(R.id.text_scuola1);
                scuola1.setText(VersusSelectorActivity.mElencoScuola1[i].getNome());
            }
            if(VersusSelectorActivity.mElencoScuola1[i].getScuolaId().equals(this.mScuola2)){
                TextView scuola2 = (TextView) findViewById(R.id.text_scuola2);
                scuola2.setText(VersusSelectorActivity.mElencoScuola1[i].getNome());
            }
        }
// Qui sarà possibile implementare dati "tutte le scuole" al posto di seleziona scuola
        if (!this.mScuola1.equals("seleziona")) {
            LinearLayout corso1Layout = (LinearLayout) findViewById(R.id.corso1);
            corso1Layout.setVisibility(View.VISIBLE);
            fillSpinner1();
        }
        if (!this.mScuola2.equals("seleziona")) {
            LinearLayout corso2Layout = (LinearLayout) findViewById(R.id.corso2);
            corso2Layout.setVisibility(View.VISIBLE);
            fillSpinner2();
        }

        AppCompatButton nextButton = (AppCompatButton)findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VersusCorsoActivity.this, VersusActivity.class);
                i.putExtra("scuola1", mScuola1);
                i.putExtra("scuola2", mScuola2);
                i.putExtra("pos1", mListaCorsi1.indexOf(mSelectedCorso1));
                i.putExtra("pos2", mListaCorsi2.indexOf(mSelectedCorso2));
                startActivity(i);
            }
        });
        AppCompatButton prevButton = (AppCompatButton)findViewById(R.id.prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Back
                finish();
            }
        });
    }

    private void fillSpinner1(){

        Query query = mRef.child("statistiche").child(mScuola1).orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProgress.dismiss();
                int i = 0;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String nome = (String) data.child("corso").getValue();
                    String id = String.valueOf(i);

                    Corso corso = new Corso(id, nome,"","","","",null,null);
                    mListaCorsi1.add(corso);
                    i++;
                }
                Log.d("size lista aule", String.valueOf(mListaCorsi1.size()));
                initSpinner1();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null){

                }
            }
        });
    }
    private void initSpinner1(){
        ArrayAdapter spinnerScuola1ArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.mListaCorsi1);
        mCorso1Spinner.setAdapter(spinnerScuola1ArrayAdapter);
        mCorso1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCorso1 = (Corso) mCorso1Spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fillSpinner2(){

        Query query = mRef.child("statistiche").child(mScuola2).orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    String nome = (String) data.child("corso").getValue();
                    String id = String.valueOf(i);

                    Corso corso = new Corso(id, nome,"","","","",null,null);
                    mListaCorsi2.add(corso);
                    i++;
                }
                Log.d("size lista aule", String.valueOf(mListaCorsi2.size()));
                initSpinner2();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null){

                }
            }
        });
    }
    private void initSpinner2(){
        ArrayAdapter spinnerScuola2ArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.mListaCorsi2);
        mCorso2Spinner.setAdapter(spinnerScuola2ArrayAdapter);
        mCorso2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCorso2 = (Corso) mCorso2Spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
