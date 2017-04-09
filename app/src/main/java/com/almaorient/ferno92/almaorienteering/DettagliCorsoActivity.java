package com.almaorient.ferno92.almaorienteering;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.PianoStudi.NewPianoStudiModel;
import com.almaorient.ferno92.almaorienteering.PianoStudi.ThreeLevelExpandableListView;
import com.almaorient.ferno92.almaorienteering.recensioni.ListaRecensioniActivity;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.almaorient.ferno92.almaorienteering.versus.VersusActivity;
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
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.round;

public class DettagliCorsoActivity extends BaseActivity implements ThreeLevelExpandableListView.Listener {

    public static final Scuola[] mScuolaadatt = new Scuola[]{
            new Scuola("", "Seleziona scuola"),
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

    private void richiamoBrowser(String url) {
        Intent browser = new Intent(this, EmbedBrowser.class);
        browser.putExtra("url", url);
        startActivity(browser);
    }

    private void statistiche(String scuola1, String scuola2, int corso1, int corso2) {
        Intent statistiche = new Intent(this, VersusActivity.class);
        statistiche.putExtra("scuola1", scuola1);
        statistiche.putExtra("scuola2", scuola2);
        statistiche.putExtra("pos1", corso1);
        statistiche.putExtra("pos2", corso2);
        startActivity(statistiche);
    }

    private void mappe(Long idscuola, Integer codcorso, String Activity) {
        Intent maps = new Intent(this, MapsActivity.class);
        maps.putExtra("idscuola", idscuola);
        maps.putExtra("codcorso", codcorso);
        maps.putExtra("CallingActivity",Activity);
        startActivity(maps);
    }

    public void threelevellistview(Integer durata, ArrayList<NewPianoStudiModel> elencoinsegnamenti) {
        Integer b=-1;
        Integer k=0;
        Integer c=-1;
        Integer f=0;
        for (int i = 1; i <= durata; i++) {
            for (c = c + 1; c < elencoinsegnamenti.size(); c++) {
                b = b + 1;
                k = f;
                for (int a = 0; a < elencoinsegnamenti.size(); a++) {
                    if (elencoinsegnamenti.get(a).getPadre().equals(elencoinsegnamenti.get(c).getRadice())) {
                        mSecondoTerzoLivello.add(elencoinsegnamenti.get(a).getCorsoNome());
                        f += 1;
                    }
                    if (!k.equals(f)) {
                        mTerzoLivello2sublist = new ArrayList<String>(mSecondoTerzoLivello.subList(k, f));

                        Collections.sort(mTerzoLivello2sublist, new Comparator<String>(){
                            @Override
                            public int compare(String esame1, String esame2)
                            {

                                return  esame1.compareTo(esame2);
                            }
                        });
                        mThirdlevelmap.put(elencoinsegnamenti.get(c).getCorsoNome(), mTerzoLivello2sublist);
                    } else {
                        List<String> vuoto = new ArrayList<String>();
                        mThirdlevelmap.put(elencoinsegnamenti.get(c).getCorsoNome(), vuoto);
                    }

                }

            }
        }
        richiamoexpandablelistview();
    }

    private void richiamoexpandablelistview() {
        mExpandableListView = (ExpandableListView) findViewById(R.id.pianistudioExpListView);


        if (mExpandableListView != null) {
            final ThreeLevelExpandableListView parentLevelAdapter = new ThreeLevelExpandableListView(DettagliCorsoActivity.this, listDataHeader,
                    mSeconlevelmap, mThirdlevelmap, mMapUrlSecondLevel, mMapUrlThirdLevel, this);
            mExpandableListView.setAdapter(parentLevelAdapter);
            mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                    parentLevelAdapter.notifyDataSetChanged();

                    return false;
                }
            });

            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int i) {
                    parentLevelAdapter.getGroup(i);

                }
            });
            // display only one expand item
//            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//                int previousGroup = -1;
//                @Override
//                public void onGroupExpand(int groupPosition) {
//                    if (groupPosition != previousGroup)
//                        mExpandableListView.collapseGroup(previousGroup);
//                    previousGroup = groupPosition;
//                }
//            });
        }
    }



    float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen to swap
            case MotionEvent.ACTION_DOWN: {
                lastX = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float currentX = touchevent.getX();

                // da dex a sx
                if (lastX-currentX >150) {

                    switchTabs(false); //+1
                }

                // if right to left swipe on screen
                if (currentX - lastX>150) {//-1
                    switchTabs(true);
                }

                break;
            }
        }
        return false;
    }

    public void switchTabs(boolean direction) {
        if (direction) // true = move right
        {
            if (mTabHost.getCurrentTab() == 0)
                mTabHost.setCurrentTab(mTabHost.getTabWidget().getTabCount());
            else
                mTabHost.setCurrentTab(mTabHost.getCurrentTab() - 1);
        } else
        // move left
        {
            //mTabHost.setCurrentTab(mTabHost.getCurrentTab() -1 );

            if (mTabHost.getCurrentTab() != (mTabHost.getTabWidget()
                    .getTabCount()))
                mTabHost.setCurrentTab(mTabHost.getCurrentTab() + 1);
            else
                mTabHost.setCurrentTab(0);
        }
    }
    //verso sx aumento di 1

    List<String> listDataHeader;

    List<String> mSecondoLivelloElencoAnno;

    List<String> mSecondoTerzoLivello;
    HashMap<String, List<String>> mSeconlevelmap;
    HashMap<String, List<String>> mThirdlevelmap;

    List<String> mAnno1;
    List<String> mAnno2;
    List<String> mAnno3;
    List<String> mAnno4;
    List<String> mAnno5;
    List<String> mAnno6;

    List <String> mTerzoLivello2sublist;

    HashMap<String, String> mMapUrlSecondLevel;
    HashMap<String, String> mMapUrlThirdLevel;

    private ViewPager mViewPager;

    private TabHost mTabHost;

    ExpandableListView mExpandableListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_corso);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!haveNetworkConnection()){
                    Toast.makeText(getApplicationContext(),
                            "Impossibile contattare i server, verifica la tua connessione ad internet e riprova",Toast.LENGTH_LONG).show();
                }
            }
        },1500);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mSecondoTerzoLivello.isEmpty()&&haveNetworkConnection()){
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Impossibile contattare il server, verifica la tua connessione ad internet e riprova",Toast.LENGTH_LONG).show();
                }
            }
        },10000);



        //TextView nomecorsoText = (TextView) findViewById(R.id.nomecorso);
        TextView tipocorsoText = (TextView) findViewById(R.id.tipotxtview);
        TextView campuscorsoText = (TextView) findViewById(R.id.campustxtview);
        final TextView accessoText = (TextView) findViewById(R.id.tipoaccessoview);
        TextView durataText = (TextView) findViewById(R.id.duratatxtview);
        TextView sededidatticaText = (TextView) findViewById(R.id.sedidatticatxtview);
        TextView codiceText = (TextView) findViewById(R.id.codicetxtview);
        TextView scuolaText = (TextView) findViewById(R.id.scuolatxtview);
        Button sitocorsobtn = (Button) findViewById(R.id.sitocorsobtn);
        Button recensioniCorsoButton = (Button) findViewById(R.id.button_recensioni);

        accessoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),accessoText.getText(),Toast.LENGTH_SHORT).show();
            }
        });


        final String corso = getIntent().getExtras().getString("Vocecliccata");
        final String scuola = getIntent().getExtras().getString("Nomescuola");
        final String corsocodice = getIntent().getExtras().getString("Codicecorso");
        final String urlcorso = getIntent().getExtras().getString("Sitocorso");
        final String tipo = getIntent().getExtras().getString("Tipocorso");
        String campus = getIntent().getExtras().getString("Campus");
        String accesso = getIntent().getExtras().getString("Accesso");
        final Long scuolaid = getIntent().getExtras().getLong("IdScuola");
        final Long durata1 = getIntent().getExtras().getLong("Durata");
        final String sededidattica = getIntent().getExtras().getString("Sededidattica");

        final Integer durata = (int) (long) durata1;


        mTabHost = (TabHost) findViewById(R.id.tab_host);
        mTabHost.setup();
        TabHost.TabSpec spec;

        //Tab 1
        spec = mTabHost.newTabSpec("Tab 1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Info");
        mTabHost.addTab(spec);

        //Tab 2
        spec = mTabHost.newTabSpec("Tab 3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Piano didattico");
        mTabHost.addTab(spec);

        //Tab 3
        spec = mTabHost.newTabSpec("Tab 4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Obiettivi");
        mTabHost.addTab(spec);

//        AnimatedTabHostListener nuovo = new AnimatedTabHostListener(this,mTabHost);
//        mTabHost.setOnTabChangedListener(nuovo);
        mViewPager = (ViewPager) findViewById(R.id.vpPager1);

        mTabHost.setCurrentTab(0);

        setTitle(corso);


        tipocorsoText.setText(tipo);
        campuscorsoText.setText(campus);
        accessoText.setText(accesso);
        codiceText.setText(String.valueOf(corsocodice));
        scuolaText.setText(String.valueOf(mScuolaadatt[(int) (long) scuolaid]));

        durataText.setText(String.valueOf(durata) + " anni");
        sededidatticaText.setText(sededidattica);

        sitocorsobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richiamoBrowser(urlcorso);
            }
        });

        recensioniCorsoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recensioniIntent = new Intent(getApplicationContext(), ListaRecensioniActivity.class);
                recensioniIntent.putExtra("nome_corso", corso);
                recensioniIntent.putExtra("scuola", scuola);
                recensioniIntent.putExtra("codice_corso", corsocodice);
                startActivity(recensioniIntent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference();

        final Query posstats = ref.child("statistiche/" + scuola).orderByChild("codice_corso").equalTo(Integer.parseInt(corsocodice));

        posstats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                    final String pos = (String) String.valueOf(data1.getKey());

                    final Button buttonstats = (Button) findViewById(R.id.buttonstats);

                    buttonstats.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int a = Integer.parseInt(pos);
                            statistiche(scuola, scuola, a, 0);

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final Button maps = (Button) findViewById(R.id.mapsbutton);

        Query query4 = ref.child("corso/" + mScuolaadatt[(int) (long) scuolaid].getScuolaId()).orderByChild("corso_codice")
                .equalTo(Integer.parseInt(corsocodice));
        query4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final Integer spinneridcorso = (Integer) Integer.parseInt(data.getKey());
                    final Integer spinneridcorso2 = spinneridcorso + 1;
                    maps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mappe(scuolaid, spinneridcorso2, "dettagliCorso");
                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                }
            }
        });

        //Piani studio
        mSecondoTerzoLivello = new ArrayList<String>();

        final ArrayList<NewPianoStudiModel> elencoinsegnamenti = new ArrayList<>();

        final Query query6 = ref.child("piani_studio/").child(scuola).child(corsocodice).orderByKey();
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList chiavi = (ArrayList) dataSnapshot.getValue();

                for (int i = 0; i < chiavi.size(); i++) {
                    HashMap meMap = (HashMap) chiavi.get(i);
                    Iterator elenco = meMap.keySet().iterator();

                    String nomeinsegnamento = "";
                    Integer padre = 0;
                    Integer radice = 0;
                    String url = "";

                    while (elenco.hasNext()) {
                        String esamiKey = (String) elenco.next();
                        switch (esamiKey) {
                            case NewPianoStudiModel.NOME_INSEGNAMENTO:
                                nomeinsegnamento = (String) meMap.get(esamiKey);
                                break;
                            case NewPianoStudiModel.PADRE:
                                padre = (Integer) (int) (long) meMap.get(esamiKey);
                                break;
                            case NewPianoStudiModel.RADICE:
                                radice = (Integer) (int) (long) meMap.get(esamiKey);
                                break;
                            case NewPianoStudiModel.URL:
                                url = (String) meMap.get(esamiKey);
                                break;
                            default:
                                break;
                        }
                    }
                    NewPianoStudiModel insegnamento = new NewPianoStudiModel(nomeinsegnamento, padre, radice, url);
                    elencoinsegnamenti.add(insegnamento);
                }


                mSeconlevelmap = new HashMap<String, List<String>>();
                mThirdlevelmap = new HashMap<String, List<String>>();
                mSecondoLivelloElencoAnno = new ArrayList<String>();

                Integer d = 0; //mi dice quanti insegnamenti mettere ogni anno
                Integer numeroinsegnamenti = 0;
                mMapUrlSecondLevel = new HashMap<String, String>();
                mMapUrlThirdLevel = new HashMap<String, String>();
                for (int i = 0; i < elencoinsegnamenti.size(); i++) {
                    if (elencoinsegnamenti.get(i).getPadre() == 0 &&
                            !mSecondoLivelloElencoAnno.contains(elencoinsegnamenti.get(i).getCorsoNome())) {
                        //per evitare duplicati in caso di corsi con più curriculum
                        numeroinsegnamenti = numeroinsegnamenti + 1;
                        mSecondoLivelloElencoAnno.add(elencoinsegnamenti.get(i).getCorsoNome());
                        mMapUrlSecondLevel.put(elencoinsegnamenti.get(i).getCorsoNome(), elencoinsegnamenti.get(i).getUrl());
                    } else {
                        mMapUrlThirdLevel.put(elencoinsegnamenti.get(i).getCorsoNome(), elencoinsegnamenti.get(i).getUrl());
                    }
                }
                d = numeroinsegnamenti / durata;
                Integer e = 0;

                if (numeroinsegnamenti > (d * durata)) {
                    e = numeroinsegnamenti - d * durata;
                }

                listDataHeader = new ArrayList<String>();

                for (int i = 1; i <= durata; i++) {
                    listDataHeader.add(String.valueOf(i) + "° anno di corso");
                }

                switch (durata) {
                    case 2:
                        mAnno1 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(0, d - 1));
                        mAnno2 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(d, 2 * d - 1 + e));

                        mSeconlevelmap.put(listDataHeader.get(0), mAnno1);
                        mSeconlevelmap.put(listDataHeader.get(1), mAnno2);

                        threelevellistview(durata, elencoinsegnamenti);
                        break;

                    case 3:
                        mAnno1 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(0, d - 1));
                        mAnno2 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(d, 2 * d - 1));
                        mAnno3 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(2 * d, 3 * d + e));

                        mSeconlevelmap.put(listDataHeader.get(0), mAnno1);
                        mSeconlevelmap.put(listDataHeader.get(1), mAnno2);
                        mSeconlevelmap.put(listDataHeader.get(2), mAnno3);

                        threelevellistview(durata, elencoinsegnamenti);
                        break;

                    case 5:
                        mAnno1 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(0, d - 1));
                        mAnno2 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(d, 2 * d - 1));
                        mAnno3 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(2 * d, 3 * d - 1));
                        mAnno4 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(3 * d, 4 * d - 1));
                        mAnno5 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(4 * d, 5 * d - 1 + e));

                        mSeconlevelmap.put(listDataHeader.get(0), mAnno1);
                        mSeconlevelmap.put(listDataHeader.get(1), mAnno2);
                        mSeconlevelmap.put(listDataHeader.get(2), mAnno3);
                        mSeconlevelmap.put(listDataHeader.get(3), mAnno4);
                        mSeconlevelmap.put(listDataHeader.get(4), mAnno5);

                        threelevellistview(durata, elencoinsegnamenti);
                        break;

                    case 6:
                        mAnno1 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(0, d - 1));
                        mAnno2 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(d, 2 * d - 1));
                        mAnno3 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(2 * d, 3 * d - 1));
                        mAnno4 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(3 * d, 4 * d - 1));
                        mAnno5 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(4 * d, 5 * d - 1));
                        mAnno6 = new ArrayList<String>(mSecondoLivelloElencoAnno.subList(5 * d, 6 * d - 1 + e));

                        mSeconlevelmap.put(listDataHeader.get(0), mAnno1);
                        mSeconlevelmap.put(listDataHeader.get(1), mAnno2);
                        mSeconlevelmap.put(listDataHeader.get(2), mAnno3);
                        mSeconlevelmap.put(listDataHeader.get(3), mAnno4);
                        mSeconlevelmap.put(listDataHeader.get(4), mAnno5);
                        mSeconlevelmap.put(listDataHeader.get(5), mAnno6);

                        threelevellistview(durata, elencoinsegnamenti);
                        break;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                }
            }
        });

        final Button obiettivibtn = (Button) findViewById(R.id.obiettivibtn);
        final TextView obiettivitextview = (TextView) findViewById(R.id.obiettivitxtview);
        final Button sbocchibtn = (Button) findViewById(R.id.sbocchibtn);
        final TextView sbocchitextview = (TextView) findViewById(R.id.sbocchitxtview);


        final Query query7 = ref.child("info_corsi/").child(scuola).child(corsocodice).orderByKey();
        query7.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String obiettivi="";
                String sbocchi ="";
                //for (DataSnapshot data : dataSnapshot.getChildren()) {
                    obiettivi = (String) dataSnapshot.child("obiettivi_formativi").getValue();
                    sbocchi = (String) dataSnapshot.child("sbocchi").getValue();
                //}

                obiettivitextview.setText(obiettivi);
                sbocchitextview.setText(sbocchi);

                final ScrollView scrollview = (ScrollView) findViewById(R.id.scrollviewtab4);


                obiettivibtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if((obiettivitextview.getLineCount())==8){
                            obiettivitextview.setMaxLines(500);
                            obiettivitextview.setEllipsize(null);
                            obiettivibtn.setText("Visualizza meno");
                        }
                        else if (obiettivitextview.getLineCount()<8){
                            Toast.makeText(getApplicationContext(),"Non c'è altro da visualizzare",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            obiettivitextview.setMaxLines(8);
                            obiettivitextview.setEllipsize(TextUtils.TruncateAt.END);
                            obiettivibtn.setText("Visualizza tutto");
                            scrollview.fullScroll(ScrollView.FOCUS_UP);

                        }
                    }
                });

                sbocchibtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if((sbocchitextview.getLineCount())==8){
                            sbocchitextview.setMaxLines(500);
                            sbocchitextview.setEllipsize(null);
                            sbocchibtn.setText("Visualizza meno");

                        }

                        else if (sbocchitextview.getLineCount()<8){
                            Toast.makeText(getApplicationContext(),"Non c'è altro da visualizzare",Toast.LENGTH_SHORT).show();
                        }

                        else{
                            sbocchitextview.setMaxLines(8);
                            sbocchitextview.setEllipsize(TextUtils.TruncateAt.END);
                            sbocchibtn.setText("Visualizza tutto");
                            scrollview.fullScroll(ScrollView.FOCUS_UP);

                        }
                    }
                });


            }

            @Override
            public void onCancelled (DatabaseError databaseError){
                if (databaseError != null) {


                }

            }


        });

    }

    @Override
    public void openBrowser(String url) {
        Intent browser = new Intent(this, EmbedBrowser.class);
        browser.putExtra("url", url);
        this.startActivity(browser);
    }
}