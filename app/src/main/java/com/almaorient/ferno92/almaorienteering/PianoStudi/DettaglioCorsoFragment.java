package com.almaorient.ferno92.almaorienteering.PianoStudi;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.DettagliCorsoActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.recensioni.ListaRecensioniActivity;
import com.almaorient.ferno92.almaorienteering.tutorial.TutorialFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by lucas on 09/04/2017.
 */

@SuppressLint("ValidFragment")
public class DettaglioCorsoFragment extends Fragment {

    private int mPosition;
    private View mRootView;
    private ThreeLevelExpandableListView.Listener mListener;

    @SuppressLint("ValidFragment")
    public DettaglioCorsoFragment(ThreeLevelExpandableListView.Listener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        this.mPosition = getArguments().getInt("pos");
        switch (this.mPosition) {
            case 0:
                this.mRootView = inflater.inflate(R.layout.dettaglio_corso_tab1, container, false);

                TextView tipocorsoText = (TextView) this.mRootView.findViewById(R.id.tipotxtview);
                TextView campuscorsoText = (TextView) this.mRootView.findViewById(R.id.campustxtview);
                final TextView accessoText = (TextView) this.mRootView.findViewById(R.id.tipoaccessoview);
                TextView durataText = (TextView) this.mRootView.findViewById(R.id.duratatxtview);
                TextView sededidatticaText = (TextView) this.mRootView.findViewById(R.id.sedidatticatxtview);
                TextView codiceText = (TextView) this.mRootView.findViewById(R.id.codicetxtview);
                TextView scuolaText = (TextView) this.mRootView.findViewById(R.id.scuolatxtview);
                Button sitocorsobtn = (Button) this.mRootView.findViewById(R.id.sitocorsobtn);
                Button recensioniCorsoButton = (Button) this.mRootView.findViewById(R.id.button_recensioni);
                Button maps = (Button) this.mRootView.findViewById(R.id.mapsbutton);


                accessoText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), accessoText.getText(), Toast.LENGTH_SHORT).show();
                    }
                });

                final String corso = getArguments().getBundle("intent_bundle").getString("Vocecliccata");
                final String scuola = getArguments().getBundle("intent_bundle").getString("Nomescuola");
                final String corsocodice = getArguments().getBundle("intent_bundle").getString("Codicecorso");
                final String urlcorso = getArguments().getBundle("intent_bundle").getString("Sitocorso");
                final String tipo = getArguments().getBundle("intent_bundle").getString("Tipocorso");
                String campus = getArguments().getBundle("intent_bundle").getString("Campus");
                String accesso = getArguments().getBundle("intent_bundle").getString("Accesso");
                final Long scuolaid = getArguments().getBundle("intent_bundle").getLong("IdScuola");
                final Long durata1 = getArguments().getBundle("intent_bundle").getLong("Durata");
                final String sededidattica = getArguments().getBundle("intent_bundle").getString("Sededidattica");
                final Integer durata = (int) (long) durata1;

                maps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.openMaps(scuolaid, "dettagliCorso");
                    }

                });

                tipocorsoText.setText(tipo);
                campuscorsoText.setText(campus);
                accessoText.setText(accesso);
                codiceText.setText(String.valueOf(corsocodice));
                scuolaText.setText(String.valueOf(DettagliCorsoActivity.mScuolaadatt[(int) (long) scuolaid]));

                durataText.setText(String.valueOf(durata) + " anni");
                sededidatticaText.setText(sededidattica);

                sitocorsobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.openBrowser(urlcorso);
                    }
                });

                recensioniCorsoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent recensioniIntent = new Intent(getActivity(), ListaRecensioniActivity.class);
                        recensioniIntent.putExtra("nome_corso", corso);
                        recensioniIntent.putExtra("scuola", scuola);
                        recensioniIntent.putExtra("codice_corso", corsocodice);
                        startActivity(recensioniIntent);
                    }
                });

                final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                final DatabaseReference ref1 = database1.getReference();
                final Query posstats = ref1.child("statistiche/" + scuola).orderByChild("codice_corso").equalTo(Integer.parseInt(corsocodice));

                posstats.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data1 : dataSnapshot.getChildren()) {
                            final String pos = (String) String.valueOf(data1.getKey());

                            final Button buttonstats = (Button) mRootView.findViewById(R.id.buttonstats);

                            buttonstats.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int a = Integer.parseInt(pos);
                                    mListener.openStats(scuola, scuola, a, 0);

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });



                break;
            case 1:
                this.mRootView = inflater.inflate(R.layout.dettaglio_corso_tab2, container, false);
                break;
            case 2:
                this.mRootView = inflater.inflate(R.layout.dettaglio_corso_tab3, container, false);
                final String scuolaTab3 = getArguments().getBundle("intent_bundle").getString("Nomescuola");
                final String corsocodiceTab3 = getArguments().getBundle("intent_bundle").getString("Codicecorso");

                final Button obiettivibtn = (Button) mRootView.findViewById(R.id.obiettivibtn);
                final TextView obiettivitextview = (TextView) mRootView.findViewById(R.id.obiettivitxtview);
                final Button sbocchibtn = (Button) mRootView.findViewById(R.id.sbocchibtn);
                final TextView sbocchitextview = (TextView) mRootView.findViewById(R.id.sbocchitxtview);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference();


                final Query query7 = ref.child("info_corsi/").child(scuolaTab3).child(corsocodiceTab3).orderByKey();
                query7.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String obiettivi = "";
                        String sbocchi = "";
                        //for (DataSnapshot data : dataSnapshot.getChildren()) {
                        obiettivi = (String) dataSnapshot.child("obiettivi_formativi").getValue();
                        sbocchi = (String) dataSnapshot.child("sbocchi").getValue();
                        //}
//
                        obiettivitextview.setText(obiettivi);
                        sbocchitextview.setText(sbocchi);

                        final ScrollView scrollview = (ScrollView) mRootView.findViewById(R.id.scrollviewtab4);


                        obiettivibtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if ((obiettivitextview.getLineCount()) == 8) {
                                    obiettivitextview.setMaxLines(500);
                                    obiettivitextview.setEllipsize(null);
                                    obiettivibtn.setText("Visualizza meno");
                                } else if (obiettivitextview.getLineCount() < 8) {
                                    Toast.makeText(getActivity(), "Non c'è altro da visualizzare", Toast.LENGTH_SHORT).show();
                                } else {
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
                                if ((sbocchitextview.getLineCount()) == 8) {
                                    sbocchitextview.setMaxLines(500);
                                    sbocchitextview.setEllipsize(null);
                                    sbocchibtn.setText("Visualizza meno");

                                } else if (sbocchitextview.getLineCount() < 8) {
                                    Toast.makeText(getActivity(), "Non c'è altro da visualizzare", Toast.LENGTH_SHORT).show();
                                } else {
                                    sbocchitextview.setMaxLines(8);
                                    sbocchitextview.setEllipsize(TextUtils.TruncateAt.END);
                                    sbocchibtn.setText("Visualizza tutto");
                                    scrollview.fullScroll(ScrollView.FOCUS_UP);

                                }
                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null) {


                        }

                    }


                });
                break;
        }

        return this.mRootView;
    }

    public static DettaglioCorsoFragment newInstance(int position, Bundle extras, ThreeLevelExpandableListView.Listener listener) {
        DettaglioCorsoFragment tutorialFragment = new DettaglioCorsoFragment(listener);
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putBundle("intent_bundle", extras);
        tutorialFragment.setArguments(args);

        return tutorialFragment;
    }


}
