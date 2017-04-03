package com.almaorient.ferno92.almaorienteering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
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

import static android.R.attr.maxWidth;
import static com.almaorient.ferno92.almaorienteering.R.id.expandableelencoscuole;
import static com.almaorient.ferno92.almaorienteering.R.id.listaagraria;
import static com.almaorient.ferno92.almaorienteering.R.id.wrap;
import static com.almaorient.ferno92.almaorienteering.R.id.wrap_content;

public class ElencoScuoleActivity extends BaseActivity {

    //private int mPosition = 0;
    private ArrayList<Scuola> mListaScuole = new ArrayList<>();
    private ListView mElenco;
    ArrayList<Corso> defCorsoList;
    ArrayList<Corso> tempCorsoList;
    ArrayList<Corso> completecorsolist;


    private void pressbutton(String key, final ListView listView) {
        String buttonIDname = key + "layout";
        int buttonID = getResources().getIdentifier(buttonIDname, "id", getPackageName());
        final RelativeLayout scuolaLayout = (RelativeLayout) findViewById(buttonID);

        int arrowButtonID = getResources().getIdentifier(key + "plus", "id", getPackageName());
        final ImageButton arrowButton = (ImageButton) scuolaLayout.findViewById(arrowButtonID);
        scuolaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listView.getVisibility() == View.GONE) {
                    listView.setVisibility(view.VISIBLE);

                } else {
                    listView.setVisibility(view.GONE);
                }
                float deg = arrowButton.getRotation() + 180F;
                arrowButton.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
            }

        });
    }

    private void onItemClick(ListView listView, final String key){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adattatore, View view, int pos, long l) {
                                    // recupero il titolo memorizzato nella riga tramite l'ArrayAdapter
                                    final String titoloriga = ((Corso) adattatore.getItemAtPosition(pos)).getNome();
                                    for(int j = 0; j < mListaScuole.size(); j++){
                                        if(mListaScuole.get(j).getScuolaId().equals(key)){
                                            for (int i = 0; i < mListaScuole.get(j).getListaCorsi().size(); i++) {
                                                ArrayList<Corso> listaCorsi = (ArrayList<Corso>) mListaScuole.get(j).getListaCorsi();
                                                if (listaCorsi.get(i).getNome().equals(titoloriga)) {
                                                    String codicecorsoagraria = listaCorsi.get(i).getCorsoCodice();
                                                    String url = listaCorsi.get(i).getUrl();
                                                    String tipo = listaCorsi.get(i).getTipo();
                                                    String campus = listaCorsi.get(i).getCampus();
                                                    String accesso = listaCorsi.get(i).getAccesso();
                                                    Long scuolaid = listaCorsi.get(i).getIdScuola();
                                                    Long durata = listaCorsi.get(i).getDurata();
                                                    String sededidattica = listaCorsi.get(i).getSedeDidattica();

                                                    richiamoPaginaInterna(titoloriga, codicecorsoagraria, url, key, tipo, campus, accesso,scuolaid,durata,sededidattica);
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                }
                            });
    }


    public static final Scuola[] mElencoScuola1 = new Scuola[]{
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

    public static void setListViewHeightBasedOnChildren(ListView listView, ListAdapter listAdapter) {
        //ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i,null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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

        final RelativeLayout agrarialayout = (RelativeLayout) findViewById(R.id.agrarialayout);
        final RelativeLayout economialayout = (RelativeLayout) findViewById(R.id.economialayout);
        final RelativeLayout farmacialayout = (RelativeLayout) findViewById(R.id.farmacialayout);
        final RelativeLayout giurisprudenzalayout = (RelativeLayout) findViewById(R.id.giurisprudenzalayout);
        final RelativeLayout ingegnerialayout = (RelativeLayout) findViewById(R.id.ingegnerialayout);
        final RelativeLayout letterelayout = (RelativeLayout) findViewById(R.id.letterelayout);
        final RelativeLayout linguelayout = (RelativeLayout) findViewById(R.id.linguelayout);
        final RelativeLayout medicinalayout = (RelativeLayout) findViewById(R.id.medicinalayout);
        final RelativeLayout psicologialayout = (RelativeLayout) findViewById(R.id.psicologialayout);
        final RelativeLayout scienzelayout = (RelativeLayout) findViewById(R.id.scienzelayout);
        final RelativeLayout scienze_politichelayout = (RelativeLayout) findViewById(R.id.scienze_politichelayout);




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
                        String abbreviazionescuola = "";

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
                            mElenco = (ListView) findViewById(listaagraria);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaAgraria.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter);

                            if (adapter.isEmpty()){
                                agrarialayout.setVisibility(View.GONE);
                            }

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Agraria e Medicina veterinaria");
                                mMapelencocorsiscuola.put("Agraria e Medicina veterinaria", scuolaAgraria.getElencoCorsi());
                            }


                            setListViewHeightBasedOnChildren(mElenco,adapter);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "economia":
                            Scuola scuolaEconomia = new Scuola(key, "Scuola di Economia, Management e Statistica", defCorsoList);
                            mListaScuole.add(scuolaEconomia);
                            mElenco = (ListView) findViewById(R.id.listaeconomia);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaEconomia.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter2);

                            setListViewHeightBasedOnChildren(mElenco,adapter2);

                            if (adapter2.isEmpty()){
                                economialayout.setVisibility(View.GONE);
                            }
                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Economia, Management e Statistica");
                                mMapelencocorsiscuola.put("Economia, Management e Statistica", scuolaEconomia.getElencoCorsi());
                            }

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "farmacia":
                            Scuola scuolaFarmacia = new Scuola(key, "Scuola di Farmacia, Biotecnologie e Scienze motorie", defCorsoList);
                            mListaScuole.add(scuolaFarmacia);
                            mElenco = (ListView) findViewById(R.id.listafarmacia);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaFarmacia.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter3);

                            setListViewHeightBasedOnChildren(mElenco,adapter3);

                            if (adapter3.isEmpty()){
                                farmacialayout.setVisibility(View.GONE);
                            }
                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Farmacia, Biotecnologie e Scienze motorie");
                                mMapelencocorsiscuola.put("Farmacia, Biotecnologie e Scienze motorie", scuolaFarmacia.getElencoCorsi());
                            }

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "giurisprudenza":
                            Scuola scuolaGiurisprudenza = new Scuola(key, "Scuola di Giurisprudenza", defCorsoList);
                            mListaScuole.add(scuolaGiurisprudenza);
                            mElenco = (ListView) findViewById(R.id.listagiurisprudenza);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaGiurisprudenza.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter4);

                            setListViewHeightBasedOnChildren(mElenco,adapter4);

                            if (adapter4.isEmpty()){
                                giurisprudenzalayout.setVisibility(View.GONE);
                            }
                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Giurisprudenza");
                                mMapelencocorsiscuola.put("Giurisprudenza", scuolaGiurisprudenza.getElencoCorsi());
                            }


                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "ingegneria":
                            Scuola scuolaIngegneria = new Scuola(key, "Scuola di Ingegneria e Architettura", defCorsoList);
                            mListaScuole.add(scuolaIngegneria);
                            mElenco = (ListView) findViewById(R.id.listaingegneria);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaIngegneria.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter5);

                            setListViewHeightBasedOnChildren(mElenco,adapter5);

                            if (adapter5.isEmpty()){
                                ingegnerialayout.setVisibility(View.GONE);
                            }
                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Ingegneria e Architettura");
                                mMapelencocorsiscuola.put("Ingegneria e Architettura", scuolaIngegneria.getElencoCorsi());
                            }

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "lettere":
                            Scuola scuolaLettere = new Scuola(key, "Scuola di Lettere e Beni culturali", defCorsoList);
                            mListaScuole.add(scuolaLettere);
                            mElenco = (ListView) findViewById(R.id.listalettere);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaLettere.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter6);

                            setListViewHeightBasedOnChildren(mElenco,adapter6);

                            if (adapter6.isEmpty()){
                                letterelayout.setVisibility(View.GONE);
                            }
                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Lettere e Beni culturali");
                                mMapelencocorsiscuola.put("Lettere e Beni culturali", scuolaLettere.getElencoCorsi());
                            }

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "lingue":
                            Scuola scuolalingue = new Scuola(key, "Scuola di Lingue e Letterature, Traduzione e Interpretazione", defCorsoList);
                            mListaScuole.add(scuolalingue);
                            mElenco = (ListView) findViewById(R.id.listalingue);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolalingue.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter7);

                            setListViewHeightBasedOnChildren(mElenco,adapter7);

                            if (adapter7.isEmpty()){
                                linguelayout.setVisibility(View.GONE);
                            }
                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Lingue e Letterature, Traduzione e Interpretazione");
                                mMapelencocorsiscuola.put("Lingue e Letterature, Traduzione e Interpretazione", scuolalingue.getElencoCorsi());
                            }

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "medicina":
                            Scuola scuolaMedicina = new Scuola(key, "Scuola di Medicina e Chirurgia", defCorsoList);
                            mListaScuole.add(scuolaMedicina);
                            mElenco = (ListView) findViewById(R.id.listamedicina);
                            // riempio la lista arrayadapter sua
                            Integer a=0;

                            for (int i=0; i<defCorsoList.size(); i++){
                                if (defCorsoList.get(i).getNome().contains("Tecniche")){
                                    a=0;
                                    break;
                                }
                                else {
                                    a=1;
                                }
                            }

                            switch (a){
                                case 0:
                                    ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_long_corsi, scuolaMedicina.getListaCorsi());
                                    mElenco.setAdapter(adapter8);
                                    setListViewHeightBasedOnChildren(mElenco,adapter8);
                                    break;
                                case 1:
                                    ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_long_corsi, scuolaMedicina.getListaCorsi());
                                    mElenco.setAdapter(adapter12);
                                    setListViewHeightBasedOnChildren(mElenco,adapter12);
                                    break;
                            }

                            //inietto i dati

                            if (defCorsoList.isEmpty()){
                                medicinalayout.setVisibility(View.GONE);
                            }

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Medicina e Chirurgia");
                                mMapelencocorsiscuola.put("Medicina e Chirurgia", scuolaMedicina.getElencoCorsi());
                            }

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "psicologia":
                            Scuola scuolaPsicologia = new Scuola(key, "Scuola di Psicologia", defCorsoList);
                            mListaScuole.add(scuolaPsicologia);
                            mElenco = (ListView) findViewById(R.id.listapsicologia);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter9 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaPsicologia.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter9);

                            if (adapter9.isEmpty()){
                                psicologialayout.setVisibility(View.GONE);
                            }

                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Psicologia");
                                mMapelencocorsiscuola.put("Psicologia", scuolaPsicologia.getElencoCorsi());
                            }

                            setListViewHeightBasedOnChildren(mElenco,adapter9);

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "scienze":
                            Scuola scuolaScienze = new Scuola(key, "Scuola di Scienze", defCorsoList);
                            mListaScuole.add(scuolaScienze);
                            mElenco = (ListView) findViewById(R.id.listascienze);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaScienze.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter10);

                            setListViewHeightBasedOnChildren(mElenco,adapter10);

                            if (adapter10.isEmpty()){
                                scienzelayout.setVisibility(View.GONE);
                            }
                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Scienze");
                                mMapelencocorsiscuola.put("Scienze", scuolaScienze.getElencoCorsi());
                            }

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
                            break;

                        case "scienze_politiche":
                            Scuola scuolaScienze_politiche = new Scuola(key, "Scuola di Scienze Politiche", defCorsoList);
                            mListaScuole.add(scuolaScienze_politiche);
                            mElenco = (ListView) findViewById(R.id.listascienze_politiche);
                            // riempio la lista arrayadapter sua
                            ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item, scuolaScienze_politiche.getListaCorsi());

                            //inietto i dati
                            mElenco.setAdapter(adapter11);

                            setListViewHeightBasedOnChildren(mElenco,adapter11);

                            if (adapter11.isEmpty()){
                                scienze_politichelayout.setVisibility(View.GONE);
                            }
                            if (!defCorsoList.isEmpty()) {
                                mDataHeader.add("Scienze Politiche");
                                mMapelencocorsiscuola.put("Scienze Politiche", scuolaScienze_politiche.getElencoCorsi());
                            }

                            pressbutton(key,mElenco);

                            onItemClick(mElenco, key);
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
                                //Toast.makeText(getApplicationContext(),(String)expadapter.getChild(i,i1),Toast.LENGTH_SHORT).show();

                                if (completecorsolist.get(b).getNome().contains((CharSequence) expadapter.getChild(i,i1))){
                                    Toast.makeText(getApplicationContext(),(String)expadapter.getChild(i,i1),Toast.LENGTH_LONG).show();

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
                ScrollView scuoleScrollView = (ScrollView) findViewById(R.id.scuoleScrollView);
                scuoleScrollView.setVisibility(View.VISIBLE);


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}