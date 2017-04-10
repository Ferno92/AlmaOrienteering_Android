package com.almaorient.ferno92.almaorienteering.versus;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.EmbedBrowser;
import com.almaorient.ferno92.almaorienteering.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lucas on 12/03/2017.
 */

public class VersusActivity extends BaseActivity {

    private boolean haveNetworkConnection() {
        boolean haveConnection = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnection = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnection = true;
        }
        return haveConnection;
    }

    String mScuola1;
    String mScuola2;
    int mPosCorso1;
    int mPosCorso2;
    StatCorsoModel mCorso1;
    StatCorsoModel mCorso2;
    DatabaseReference mRef;
    boolean mIsStat1Visible = false;
    boolean mIsStat2Visible = false;
    LinearLayout mStatScroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.versus_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!haveNetworkConnection()){
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Impossibile contattare il server, verifica la tua connessione ad internet e riprova",Toast.LENGTH_LONG).show();
                }
            }
        },1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!mIsStat1Visible || !mIsStat2Visible){
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Impossibile contattare il server, verifica la tua connessione ad internet e riprova",Toast.LENGTH_LONG).show();
                }
            }
        },6000);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRef = database.getReference();

        this.mScuola1 = getIntent().getExtras().getString("scuola1");
        this.mScuola2 = getIntent().getExtras().getString("scuola2");
        this.mPosCorso1 = getIntent().getExtras().getInt("pos1");
        this.mPosCorso2 = getIntent().getExtras().getInt("pos2");

        for(int i = 0; i < VersusSelectorActivity.mElencoScuola1.length; i++){
            if(VersusSelectorActivity.mElencoScuola1[i].getScuolaId().equals(this.mScuola1)){
                TextView nomeScuola1 = (TextView) findViewById(R.id.nome_scuola_a);
                nomeScuola1.setText(VersusSelectorActivity.mElencoScuola1[i].getNome());
            }
            if(VersusSelectorActivity.mElencoScuola1[i].getScuolaId().equals(this.mScuola2)){
                TextView nomeScuola2 = (TextView) findViewById(R.id.nome_scuola_b);
                nomeScuola2.setText(VersusSelectorActivity.mElencoScuola1[i].getNome());
            }
        }
        AppCompatButton linkButton = (AppCompatButton) findViewById(R.id.link_almalaurea);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser = new Intent(VersusActivity.this, EmbedBrowser.class);
                browser.putExtra("url", "http://www2.almalaurea.it/cgi-php/lau/sondaggi/intro.php?config=profilo");
                startActivity(browser);
            }
        });

        this.mStatScroll = (LinearLayout) findViewById(R.id.stat_scroll);
        if(mScuola1 == "seleziona"){
            //predisposizione per recuperare statistiche generali
        }else{
            Query query = mRef.child("statistiche").child(mScuola1).child(String.valueOf(this.mPosCorso1)).orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap meMap = (HashMap) dataSnapshot.getValue();
                    Iterator corsoIterator = meMap.keySet().iterator();
                    while(corsoIterator.hasNext()) {
                        String key=(String)corsoIterator.next();
                        String value=String.valueOf(meMap.get(key));
                        value = value.replace("*", "N/D");
                        if (!key.equals(StatCorsoModel.CORSO)) {
                            value = value.replace("-", "N/D");
                        }
                        //Switch loop
                        switch(key){
                            case StatCorsoModel.CORSO:
                                TextView corsoText1 = (TextView) findViewById(R.id.nome_corso_a);
                                corsoText1.setText(value);
                                break;
                            case StatCorsoModel.DURATA:
                                LinearLayout stat3 = (LinearLayout) findViewById(R.id.stat_3);
                                TextView esamiText3 = (TextView) stat3.getChildAt(0);
                                esamiText3.setText(value);

                                break;
                            case StatCorsoModel.ERASMUS:
                                LinearLayout stat5 = (LinearLayout) findViewById(R.id.stat_5);
                                TextView esamiText5 = (TextView) stat5.getChildAt(0);
                                esamiText5.setText(value);
                                break;
                            case StatCorsoModel.ESAMI:
                                LinearLayout stat1 = (LinearLayout) findViewById(R.id.stat_1);
                                TextView esamiText1 = (TextView) stat1.getChildAt(0);
                                esamiText1.setText(value);
                                break;
                            case StatCorsoModel.INCORSO:
                                LinearLayout stat4 = (LinearLayout) findViewById(R.id.stat_4);
                                TextView esamiText4 = (TextView) stat4.getChildAt(0);
                                esamiText4.setText(value + " %");
                                break;
                            case StatCorsoModel.LAUREA:
                                LinearLayout stat2 = (LinearLayout) findViewById(R.id.stat_2);
                                TextView esamiText2 = (TextView) stat2.getChildAt(0);
                                esamiText2.setText(value);
                                break;
                            case StatCorsoModel.RITARDO:
                                break;
                            case StatCorsoModel.SODDISFAZIONE:
                                LinearLayout stat7 = (LinearLayout) findViewById(R.id.stat_7);
                                TextView esamiText7 = (TextView) stat7.getChildAt(0);
                                esamiText7.setText(value + " %");
                                break;
                            case StatCorsoModel.STAGE:
                                LinearLayout stat6 = (LinearLayout) findViewById(R.id.stat_6);
                                TextView esamiText6 = (TextView) stat6.getChildAt(0);
                                esamiText6.setText(value + " %");
                                break;
                            default:
                                break;
                        }
                    }

                    mIsStat1Visible = true;
                    showStats();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(databaseError != null){

                    }
                }
            });
        }
        if(mScuola2 == "seleziona"){
            //predisposizione per recuperare statistiche generali

        }else{
            Query query = mRef.child("statistiche").child(mScuola2).child(String.valueOf(this.mPosCorso2)).orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap meMap = (HashMap) dataSnapshot.getValue();
                    Iterator corsoIterator = meMap.keySet().iterator();
                    while(corsoIterator.hasNext()) {
                        String key=(String)corsoIterator.next();
                        String value=String.valueOf(meMap.get(key));
                        value = value.replace("*", "N/D");
                        value = value.replace("-", "N/D");
                        //Switch loop
                        switch(key){
                            case StatCorsoModel.CORSO:
                                TextView corsoText2 = (TextView) findViewById(R.id.nome_corso_b);
                                corsoText2.setText(value);
                                break;
                            case StatCorsoModel.DURATA:
                                LinearLayout stat3 = (LinearLayout) findViewById(R.id.stat_3);
                                TextView esamiText3 = (TextView) stat3.getChildAt(2);
                                esamiText3.setText(value);
                                break;
                            case StatCorsoModel.ERASMUS:
                                LinearLayout stat5 = (LinearLayout) findViewById(R.id.stat_5);
                                TextView esamiText5 = (TextView) stat5.getChildAt(2);
                                esamiText5.setText(value);
                                break;
                            case StatCorsoModel.ESAMI:
                                LinearLayout stat1 = (LinearLayout) findViewById(R.id.stat_1);
                                TextView esamiText1 = (TextView) stat1.getChildAt(2);
                                esamiText1.setText(value);
                                break;
                            case StatCorsoModel.INCORSO:
                                LinearLayout stat4 = (LinearLayout) findViewById(R.id.stat_4);
                                TextView esamiText4 = (TextView) stat4.getChildAt(2);
                                esamiText4.setText(value + " %");
                                break;
                            case StatCorsoModel.LAUREA:
                                LinearLayout stat2 = (LinearLayout) findViewById(R.id.stat_2);
                                TextView esamiText2 = (TextView) stat2.getChildAt(2);
                                esamiText2.setText(value);
                                break;
                            case StatCorsoModel.RITARDO:
                                break;
                            case StatCorsoModel.SODDISFAZIONE:
                                LinearLayout stat7 = (LinearLayout) findViewById(R.id.stat_7);
                                TextView esamiText7 = (TextView) stat7.getChildAt(2);
                                esamiText7.setText(value + " %");
                                break;
                            case StatCorsoModel.STAGE:
                                LinearLayout stat6 = (LinearLayout) findViewById(R.id.stat_6);
                                TextView esamiText6 = (TextView) stat6.getChildAt(2);
                                esamiText6.setText(value + " %");
                                break;
                            default:
                                break;
                        }
                    }
                    mIsStat2Visible = true;
                    showStats();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(databaseError != null){

                    }
                }
            });
        }
    }

    private void showStats() {
        if(this.mIsStat1Visible && this.mIsStat2Visible){
            this.mStatScroll.setVisibility(View.VISIBLE);
        }
    }
}
