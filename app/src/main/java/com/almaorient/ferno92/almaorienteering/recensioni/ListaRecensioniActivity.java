package com.almaorient.ferno92.almaorienteering.recensioni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by lucas on 26/03/2017.
 */

public class ListaRecensioniActivity extends BaseActivity {
    private DatabaseReference mRef;
    private ArrayList<RecensioniModel> mRecensioniList = new ArrayList<RecensioniModel>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_recensioni);

        String nomeCorso = getIntent().getExtras().getString("nome_corso");
        String scuola = getIntent().getExtras().getString("scuola");
        final String codiceCorso = getIntent().getExtras().getString("codice_corso");
        TextView corsoTextView = (TextView) findViewById(R.id.nome_corso);
        corsoTextView.setText(nomeCorso);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.mRef = database.getReference();
        RecensioniModel[] recensioniArray;
        Query query = mRef.child("recensioni/" + scuola).orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(String.valueOf(data.child("corso_codice").getValue()).equals(codiceCorso)) {// if id è lo stesso
                        DataSnapshot recensioniList = data.child("recensioni");

                        for(DataSnapshot recData : recensioniList.getChildren()){
                                HashMap recMap = (HashMap) recData.getValue();;
                                Iterator recIterator = recMap.keySet().iterator();
                                String email = "";
                                String recensione = "";
                                int quota = 0;
                                String voto = "";
                                while (recIterator.hasNext()) {
                                    String recKey = (String) recIterator.next();
                                    switch (recKey) {
                                        case "email":
                                            email = String.valueOf(recMap.get(recKey));
                                            break;
                                        case "recensione":
                                            recensione = String.valueOf(recMap.get(recKey));
                                            break;
                                        case "quota":
                                            quota = ((Long) recMap.get(recKey)).intValue();
                                            break;
                                        case "voto":
                                            voto = String.valueOf(recMap.get(recKey));
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                RecensioniModel rec = new RecensioniModel(email, voto, recensione, quota);
                                mRecensioniList.add(rec);

                        }
                    }
                }
                initRecensioniList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null){

                }
            }
        });

    }

    private void initRecensioniList() {

        RecensioniModel[] recensioni = {
                new RecensioniModel("luca.fernandez@studio.unibo.it", "4.5", "Proprio bello questo corso, ho veramente scelto bene!", 15),
                new RecensioniModel("tizio.caio@studio.unibo.it", "1", "Ma chi me l'ha fatto fare", 14),
                new RecensioniModel("super.pippo@studio.unibo.it", "3", "Poteva andarmi meglio ma non mi lamento.. Yuk!!", 13),
                new RecensioniModel("paolo.rossi@studio.unibo.it", "4.5", "Bella", 12),
                new RecensioniModel("ale.lotti@studio.unibo.it", "5", "Sono super entusiasta, mi sto divertendo un sacco! I professori sono fantastici e ci seguono molto! Abbiamo anche dei tutor meravigliosi e starei ore e ore e ore a parlare di questo fantastico corso! Si, lo so sono un po' logorrroico ma che ci vuoi fare ;)", 15),
                new RecensioniModel("marco.mariotti@studio.unibo.it", "2", "Mah, niente di speciale..", 11),
                new RecensioniModel("donald.duck@studio.unibo.it", "2", "Io speravo ci fossero più donne..", 10),
                new RecensioniModel("son.goku@studio.unibo.it", "0.5", "Pessimo, se continua così mi ritiro!", 9),
        };
        //riempimento casuale della lista delle persone
        Random r=new Random();
        for(int i=0;i<5;i++){
            mRecensioniList.add(recensioni[r.nextInt(recensioni.length)]);
        }
        float sum = 0;
        //TODO: magari ordiniamo la lista di recensioni che arrivano dalla query per quota
        for(int i = 0; i < mRecensioniList.size(); i++){
            sum += Float.parseFloat(mRecensioniList.get(i).getVoto());
        }
        float ratingMedio = round((sum / recensioni.length), 2);
        TextView media = (TextView)findViewById(R.id.media_rating);
        media.setText(String.valueOf(ratingMedio) + " / 5");

        RecComparer comparer = new RecComparer();
        Collections.sort(mRecensioniList, comparer );

        //Questa è la lista che rappresenta la sorgente dei dati della listview
        //ogni elemento è una mappa(chiave->valore)
        ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
        int resourceId = getResources().getIdentifier("ic_recensione_success" , "drawable", getPackageName());



        for(int i=0;i<mRecensioniList.size();i++){
            RecensioniModel p = mRecensioniList.get(i);

            HashMap<String,Object> recMap=new HashMap<String, Object>();//creiamo una mappa di valori

            recMap.put("voto", p.getVoto());
            recMap.put("recensione", p.getRecensione());
            recMap.put("quota", p.getQuota());
            recMap.put("up", resourceId);
            recMap.put("down", resourceId);
            data.add(recMap);  //aggiungiamo la mappa di valori alla sorgente dati
        }


        SimpleAdapter adapter = new SimpleAdapter(this, data , R.layout.recensioni_list_item,
                new String[]{"voto","recensione", "quota", "up", "down"}, //dai valori contenuti in queste chiavi
                new int[]{R.id.rating_recensione,R.id.testo_recensione, R.id.quota, R.id.rec_up, R.id.rec_down}//agli id delle view
        );
        adapter.setViewBinder(new RecensioniBinder());
        ((ListView)findViewById(R.id.lista_recensioni)).setAdapter(adapter);
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    public class RecComparer implements Comparator<RecensioniModel> {
        @Override
        public int compare(RecensioniModel x, RecensioniModel y) {

            return  compare(x.getQuota(), y.getQuota());
        }

        private int compare(int a, int b) {
            return a > b ? -1
                    : a < b ? 1
                    : 0;
        }
    }
}
