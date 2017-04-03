package com.almaorient.ferno92.almaorienteering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.almaorient.ferno92.almaorienteering.ElencoScuole.ExpandableListAdapter1;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ElencoScuoleActivity extends BaseActivity {

    private ArrayList<Scuola> mListaScuole = new ArrayList<>();
    ArrayList<Corso> defCorsoList;
    ArrayList<Corso> tempCorsoList;
    ArrayList<Corso> completecorsolist;

    private void richiamoPaginaInterna(String nomecorso, String codicecorso, String url, String nomescuola,
                                       String tipo, String campus, String accesso,Long idscuola, Long durata, String sededidattica) {
        Intent nuovapagina = new Intent(this, DettagliCorsoActivity.class);
        nuovapagina.putExtra("Vocecliccata", nomecorso);
        nuovapagina.putExtra("Codicecorso", codicecorso);
        nuovapagina.putExtra("Sitocorso", url);
        nuovapagina.putExtra("Nomescuola", nomescuola);
        nuovapagina.putExtra("Tipocorso", tipo);
        nuovapagina.putExtra("Campus", campus);
        nuovapagina.putExtra("Accesso", accesso);
        nuovapagina.putExtra("IdScuola",idscuola);
        nuovapagina.putExtra("Durata",durata);
        nuovapagina.putExtra("Sededidattica", sededidattica);
        startActivity(nuovapagina);
    }

    List <String> mDataHeader;
    HashMap <String, List<String>> mMapelencocorsiscuola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elenco_scuole);

        setTitle("Scuole");

        final Integer a = 0;

        final String tipo_laurea = getIntent().getExtras().getString("tipo_laurea");
        final String campus_selezionato = getIntent().getExtras().getString("campus");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        Query query = ref.child("corso");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap meMap = (HashMap) dataSnapshot.getValue();
                Iterator scuolaIterator = meMap.keySet().iterator();

                mDataHeader = new ArrayList<String>();
                mMapelencocorsiscuola = new HashMap<String, List<String>>();
                completecorsolist = new ArrayList<Corso>();

                while (scuolaIterator.hasNext()) {
                    String key = (String) scuolaIterator.next();
                    ArrayList value = (ArrayList) meMap.get(key);
                    tempCorsoList = new ArrayList<Corso>();

                    for (int i = 0; i < value.size(); i++) {
                        HashMap corsoMap = (HashMap) value.get(i);
                        Iterator corsoIterator = corsoMap.keySet().iterator();
                        String codicedelcorso = "";
                        String nomecorso = "";
                        String sito = "";
                        String tipo = "";
                        String campus = "";
                        String accesso = "";
                        Long idscuola= null;
                        Long durata =null;
                        String sededidattica="";

                        while (corsoIterator.hasNext()) {
                            String corsoKey = (String) corsoIterator.next();

                            switch (corsoKey) {
                                case "corso_codice":
                                    codicedelcorso = String.valueOf(corsoMap.get(corsoKey));
                                    break;
                                case "corso_descrizione":
                                    nomecorso = (String) corsoMap.get(corsoKey);
                                    break;
                                case "url":
                                    sito = (String) corsoMap.get(corsoKey);
                                    break;
                                case "tipologia":
                                    tipo = (String) corsoMap.get(corsoKey);
                                    break;
                                case "campus":
                                    campus = (String) corsoMap.get(corsoKey);
                                    break;
                                case "accesso":
                                    accesso = (String) corsoMap.get(corsoKey);
                                    break;
                                case "cod_scuola":
                                    idscuola= (Long) corsoMap.get(corsoKey);
                                    break;
                                case "durata":
                                    durata= (Long) corsoMap.get(corsoKey);
                                    break;
                                case "sededidattica":
                                    sededidattica=(String) corsoMap.get(corsoKey);
                                    break;
                            }
                        }
                        Corso corso = new Corso(codicedelcorso, nomecorso, sito, tipo, campus, accesso, idscuola,durata,sededidattica,key);
                        tempCorsoList.add(corso);
                        completecorsolist.add(corso);
                    }

                    //Switch loop
                    ArrayList<Corso> tempCorsoList2 = new ArrayList<Corso>();
                    defCorsoList = new ArrayList<Corso>();

                    //Switch per dati passati dal filtro

                    switch (tipo_laurea) {
                        case "Laurea":
                            for (int i=0; i<tempCorsoList.size(); i++) {
                                if (tempCorsoList.get(i).getTipo().equals("Laurea")) {
                                    tempCorsoList2.add(tempCorsoList.get(i));
                                }
                            }
                            break;
                        case "Laurea Magistrale":
                            for (int i=0; i<tempCorsoList.size(); i++) {
                                if (tempCorsoList.get(i).getTipo().equals("Laurea Magistrale")) {
                                    tempCorsoList2.add(tempCorsoList.get(i));
                                }
                            }
                            break;
                        case "Laurea Magistrale a ciclo unico":
                            for (int i=0; i<tempCorsoList.size(); i++) {
                                if (tempCorsoList.get(i).getTipo().equals("Laurea Magistrale a ciclo unico")) {
                                    tempCorsoList2.add(tempCorsoList.get(i));
                                }
                            }
                            break;
                        case "":
                            for (int i=0; i<tempCorsoList.size(); i++) {
                                    tempCorsoList2.add(tempCorsoList.get(i));
                            }
                            break;
                    }

                    switch (campus_selezionato){
                        case "Bologna":
                            for (int i=0; i<tempCorsoList2.size(); i++) {
                                if (tempCorsoList2.get(i).getCampus().equals("Bologna")) {
                                    defCorsoList.add(tempCorsoList2.get(i));
                                }
                            }
                            break;
                        case "Ravenna":
                            for (int i=0; i<tempCorsoList2.size(); i++) {
                                if (tempCorsoList2.get(i).getCampus().equals("Ravenna")) {
                                    defCorsoList.add(tempCorsoList2.get(i));
                                }
                            }
                            break;
                        case "Cesena":
                            for (int i=0; i<tempCorsoList2.size(); i++) {
                                if (tempCorsoList2.get(i).getCampus().equals("Cesena")) {
                                    defCorsoList.add(tempCorsoList2.get(i));
                                }
                            }
                            break;
                        case "Forli'":
                            for (int i=0; i<tempCorsoList2.size(); i++) {
                                if (tempCorsoList2.get(i).getCampus().equals("Forli'") || tempCorsoList2.get(i).getCampus().equals("Forli")) {
                                    defCorsoList.add(tempCorsoList2.get(i));
                                }
                            }
                            break;
                        case "Rimini":
                            for (int i=0; i<tempCorsoList2.size(); i++) {
                                if (tempCorsoList2.get(i).getCampus().equals("Rimini")) {
                                    defCorsoList.add(tempCorsoList2.get(i));
                                }
                            }
                            break;
                        case "":
                            for (int i=0; i<tempCorsoList2.size(); i++) {
                                    defCorsoList.add(tempCorsoList2.get(i));
                            }
                            break;
                    }


                    switch (key) {
                        case "agraria":
                            Scuola scuolaAgraria = new Scuola(key, "Agraria e Medicina veterinaria", defCorsoList);
                            mListaScuole.add(scuolaAgraria);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Agraria e Medicina veterinaria");
                                mMapelencocorsiscuola.put("Agraria e Medicina veterinaria", scuolaAgraria.getElencoCorsi());
                            }

                            break;

                        case "economia":
                            Scuola scuolaEconomia = new Scuola(key, "Scuola di Economia, Management e Statistica", defCorsoList);
                            mListaScuole.add(scuolaEconomia);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Economia, Management e Statistica");
                                mMapelencocorsiscuola.put("Economia, Management e Statistica", scuolaEconomia.getElencoCorsi());
                            }
                            break;

                        case "farmacia":
                            Scuola scuolaFarmacia = new Scuola(key, "Scuola di Farmacia, Biotecnologie e Scienze motorie", defCorsoList);
                            mListaScuole.add(scuolaFarmacia);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Farmacia, Biotecnologie e Scienze motorie");
                                mMapelencocorsiscuola.put("Farmacia, Biotecnologie e Scienze motorie", scuolaFarmacia.getElencoCorsi());
                            }

                            break;

                        case "giurisprudenza":
                            Scuola scuolaGiurisprudenza = new Scuola(key, "Scuola di Giurisprudenza", defCorsoList);
                            mListaScuole.add(scuolaGiurisprudenza);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Giurisprudenza");
                                mMapelencocorsiscuola.put("Giurisprudenza", scuolaGiurisprudenza.getElencoCorsi());
                            }

                            break;

                        case "ingegneria":
                            Scuola scuolaIngegneria = new Scuola(key, "Scuola di Ingegneria e Architettura", defCorsoList);
                            mListaScuole.add(scuolaIngegneria);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Ingegneria e Architettura");
                                mMapelencocorsiscuola.put("Ingegneria e Architettura", scuolaIngegneria.getElencoCorsi());
                            }

                            break;

                        case "lettere":
                            Scuola scuolaLettere = new Scuola(key, "Scuola di Lettere e Beni culturali", defCorsoList);
                            mListaScuole.add(scuolaLettere);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Lettere e Beni culturali");
                                mMapelencocorsiscuola.put("Lettere e Beni culturali", scuolaLettere.getElencoCorsi());
                            }

                            break;

                        case "lingue":
                            Scuola scuolalingue = new Scuola(key, "Scuola di Lingue e Letterature, Traduzione e Interpretazione", defCorsoList);
                            mListaScuole.add(scuolalingue);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Lingue e Letterature, Traduzione e Interpretazione");
                                mMapelencocorsiscuola.put("Lingue e Letterature, Traduzione e Interpretazione", scuolalingue.getElencoCorsi());
                            }

                            break;

                        case "medicina":
                            Scuola scuolaMedicina = new Scuola(key, "Scuola di Medicina e Chirurgia", defCorsoList);
                            mListaScuole.add(scuolaMedicina);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Medicina e Chirurgia");
                                mMapelencocorsiscuola.put("Medicina e Chirurgia", scuolaMedicina.getElencoCorsi());
                            }

                            break;

                        case "psicologia":
                            Scuola scuolaPsicologia = new Scuola(key, "Scuola di Psicologia", defCorsoList);
                            mListaScuole.add(scuolaPsicologia);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Psicologia");
                                mMapelencocorsiscuola.put("Psicologia", scuolaPsicologia.getElencoCorsi());
                            }

                            break;

                        case "scienze":
                            Scuola scuolaScienze = new Scuola(key, "Scuola di Scienze", defCorsoList);
                            mListaScuole.add(scuolaScienze);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Scienze");
                                mMapelencocorsiscuola.put("Scienze", scuolaScienze.getElencoCorsi());
                            }

                            break;

                        case "scienze_politiche":
                            Scuola scuolaScienze_politiche = new Scuola(key, "Scuola di Scienze Politiche", defCorsoList);
                            mListaScuole.add(scuolaScienze_politiche);

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Scienze Politiche");
                                mMapelencocorsiscuola.put("Scienze Politiche", scuolaScienze_politiche.getElencoCorsi());
                            }

                            break;

                        default:
                            break;

                    }

                }

                final ExpandableListView elencoscuole = (ExpandableListView) findViewById(R.id.expandableelencoscuole);

                if (elencoscuole!=null) {
                    final ExpandableListAdapter1 expadapter = new ExpandableListAdapter1(getApplicationContext(), mDataHeader, mMapelencocorsiscuola);
                    elencoscuole.setAdapter(expadapter);

                    elencoscuole.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                            expadapter.notifyDataSetChanged();
                            for (int b=0; b<completecorsolist.size();b++){

                                if (completecorsolist.get(b).getNome().contains((CharSequence) expadapter.getChild(i,i1))){

                                    String codicecorso = completecorsolist.get(b).getCorsoCodice();
                                    String url = completecorsolist.get(b).getUrl();
                                    String tipo = completecorsolist.get(b).getTipo();
                                    String campus = completecorsolist.get(b).getCampus();
                                    String accesso = completecorsolist.get(b).getAccesso();
                                    Long scuolaid = completecorsolist.get(b).getIdScuola();
                                    Long durata = completecorsolist.get(b).getDurata();
                                    String sededidattica = completecorsolist.get(b).getSedeDidattica();
                                    String abbreviazionescuola = completecorsolist.get(b).getAbbreviazioneSCuola();

                                    richiamoPaginaInterna((String)expadapter.getChild(i,i1), codicecorso, url, abbreviazionescuola,
                                           tipo, campus, accesso, scuolaid, durata, sededidattica);
                                    break;

                                }
                            }

                            return false;
                        }
                    });
                }

                Log.d("scuole: ", String.valueOf(mListaScuole.size()));
                LottieAnimationView spiderLoader = (LottieAnimationView)findViewById(R.id.spider_loader) ;
                spiderLoader.setVisibility(View.GONE);
                elencoscuole.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}