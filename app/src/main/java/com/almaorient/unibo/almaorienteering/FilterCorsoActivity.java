package com.almaorient.unibo.almaorienteering;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;



public class FilterCorsoActivity extends BaseActivity {

    private void intent_elenco_scuole (String tipo_laurea, String campus){
        Intent i = new Intent(this,ElencoScuoleActivity.class);
        i.putExtra("tipo_laurea",tipo_laurea);
        i.putExtra("campus",campus);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_corso);
        setTitle("Filtra Corsi");

        final String[] tipo_selezionato = {""};

        final RadioGroup tipo_laurea = (RadioGroup) findViewById(R.id.tipo_laurea);
        tipo_laurea.check(R.id.tutte_le_lauree);
        tipo_laurea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.tutte_le_lauree:
                        tipo_selezionato[0] = "";
                        break;
                    case R.id.laurea:
                        tipo_selezionato[0] = "Laurea";
                        break;
                    case R.id.laurea_magistrale:
                        tipo_selezionato[0] = "Laurea Magistrale";
                        break;
                    case R.id.laurea_magistrale_ciclo_unico:
                        tipo_selezionato[0] = "Laurea Magistrale a ciclo unico";
                        break;
            }
        }});

        final RadioGroup campus = (RadioGroup) findViewById(R.id.campus);

        campus.check(R.id.tutti_i_campus);
        final String[] campus_selezionato = {""};

        campus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.tutti_i_campus:
                        campus_selezionato[0] = "";
                        break;
                    case R.id.bologna:
                        campus_selezionato[0] = "Bologna";
                        break;
                    case R.id.cesena:
                        campus_selezionato[0] = "Cesena";
                        break;
                    case R.id.forli:
                        campus_selezionato[0] = "Forli'";
                        break;
                    case R.id.ravenna:
                        campus_selezionato[0] = "Ravenna";
                        break;
                    case R.id.rimini:
                        campus_selezionato[0] = "Rimini";
                        break;
                }
            }});


        final ImageButton next = (ImageButton) findViewById(R.id.nextbtn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent_elenco_scuole(tipo_selezionato[0],campus_selezionato[0]);

            }
        });

    }
}
