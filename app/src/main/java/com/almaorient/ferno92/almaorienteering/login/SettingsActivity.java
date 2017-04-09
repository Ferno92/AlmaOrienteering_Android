package com.almaorient.ferno92.almaorienteering.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lucas on 29/03/2017.
 */

public class SettingsActivity extends BaseActivity {

    private String mUserName;
    private String mUserSurname;
    private String mCorso;
    private String mCorsoId;
    private String mScuola;
    private DatabaseReference mRef;
    private TextView mEmailEdit;
    private Spinner mScuolaSpinner;
    private Spinner mCorsoSpinner;
    Scuola mSelectedScuola;
    private List<Corso> mListaCorsi = new ArrayList<Corso>();
    private EditText mNomeEdit;
    private EditText mCognomeEdit;
    private Corso mSelectedCorso;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        mUserName = getIntent().getStringExtra("nome");
        mUserSurname = getIntent().getStringExtra("cognome");
        mCorso = getIntent().getStringExtra("corso");
        mCorsoId = getIntent().getStringExtra("corsoId");
        mScuola = getIntent().getStringExtra("scuola");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRef = database.getReference();

        initView();
        initScuolaArray();
    }

    private void initView() {
        mEmailEdit = (TextView) findViewById(R.id.email_logged_user);
        mNomeEdit = (EditText)findViewById(R.id.nome);
        mCognomeEdit = (EditText)findViewById(R.id.cognome);
        mScuolaSpinner = (Spinner) findViewById(R.id.scuola_spinner);
        mCorsoSpinner = (Spinner) findViewById(R.id.corso_spinner);
        AppCompatButton saveButton = (AppCompatButton)findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewData();
            }
        });

        mEmailEdit.setText(mAuth.getCurrentUser().getEmail());
        mNomeEdit.setText(mUserName);
        mCognomeEdit.setText(mUserSurname);
    }

    private void setNewData() {
        Query query = mRef.child("users").orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.child("userId").getValue().equals(mAuth.getCurrentUser().getEmail())){
                        mRef.child("users/" + data.getKey() + "/nome").setValue(mNomeEdit.getText().toString());
                        mRef.child("users/" + data.getKey() + "/cognome").setValue(mCognomeEdit.getText().toString());
                        mRef.child("users/" + data.getKey() + "/scuola").setValue(mSelectedScuola.getNome());
                        mRef.child("users/" + data.getKey() + "/corso/id").setValue(mSelectedCorso.getCorsoCodice());
                        mRef.child("users/" + data.getKey() + "/corso/nome").setValue(mSelectedCorso.getNome());
                        break;
                    }
                    Toast.makeText(SettingsActivity.this, "Modifiche salvate",
                            Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null){

                }
            }
        });
    }

    private void initScuolaArray(){
        Scuola[] scuolaArray = new Scuola[]{
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
        };
        ArrayAdapter spinnerScuolaArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, scuolaArray);
        mScuolaSpinner.setAdapter(spinnerScuolaArrayAdapter);

        for(int i = 0; i < scuolaArray.length; i++){
            if(scuolaArray[i].getNome().equals(mScuola)){
                mScuolaSpinner.setSelection(i);

//                setSpinnerCorso();
            }
        }
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
        Query query = mRef.child("corso/" + this.mSelectedScuola.getScuolaId()).orderByKey();
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
    private void fillSpinner(){

        ArrayAdapter spinnerCorsoArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, mListaCorsi);
        mCorsoSpinner.setAdapter(spinnerCorsoArrayAdapter);
        for(int i = 0; i < mListaCorsi.size(); i++){
            if(mListaCorsi.get(i).getNome().equals(mCorso)){
                mCorsoSpinner.setSelection(i);
            }
        }
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
