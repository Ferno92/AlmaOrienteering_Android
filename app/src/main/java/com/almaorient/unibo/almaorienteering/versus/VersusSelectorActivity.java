package com.almaorient.unibo.almaorienteering.versus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.almaorient.unibo.almaorienteering.BaseActivity;
import com.almaorient.unibo.almaorienteering.R;
import com.almaorient.unibo.almaorienteering.strutturaUnibo.Scuola;

/**
 * Created by lucas on 13/03/2017.
 */

public class VersusSelectorActivity extends BaseActivity {

    Spinner mScuola1Spinner;
    Spinner mScuola2Spinner;
    Scuola mSelectedScuola1;
    Scuola mSelectedScuola2;
    public static final Scuola[] mElencoScuola1 = new Scuola[]{
            new Scuola("seleziona", "Seleziona una scuola"),
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
            new Scuola("scienze_politiche", "Scienze politiche")
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.versus_selector_activity);

        initView();
        initScuolaArray();
    }

    private void initView(){
        mScuola1Spinner = (Spinner) findViewById(R.id.spinner_scuola1);
        mScuola2Spinner = (Spinner) findViewById(R.id.spinner_scuola2);

        AppCompatButton nextButton = (AppCompatButton) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mScuola1Spinner.getSelectedItemPosition()==0){
                    Toast.makeText(VersusSelectorActivity.this, "Seleziona scuola n°1", Toast.LENGTH_SHORT).show();
                }
                else if (mScuola2Spinner.getSelectedItemPosition()==0){
                    Toast.makeText(VersusSelectorActivity.this, "Seleziona scuola n°2", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(VersusSelectorActivity.this, VersusCorsoActivity.class);
                    i.putExtra("scuola1", mSelectedScuola1.getScuolaId());
                    i.putExtra("scuola2", mSelectedScuola2.getScuolaId());
                    startActivity(i);
                }
            }
        });
    }

    private void initScuolaArray(){
        ArrayAdapter spinnerScuola1ArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.mElencoScuola1);
        mScuola1Spinner.setAdapter(spinnerScuola1ArrayAdapter);
        mScuola1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedScuola1 = (Scuola) mScuola1Spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter spinnerScuola2ArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.mElencoScuola1);
        mScuola2Spinner.setAdapter(spinnerScuola2ArrayAdapter);
        mScuola2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedScuola2 = (Scuola) mScuola2Spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
