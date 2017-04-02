package com.almaorient.ferno92.almaorienteering;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.firebaseDB.AulaMarker;
import com.almaorient.ferno92.almaorienteering.firebaseDB.AulaModel;
import com.almaorient.ferno92.almaorienteering.firebaseDB.IndirizziModel;
import com.almaorient.ferno92.almaorienteering.firebaseDB.ScuoleMarker;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;
import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Scuola;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fillType;
import static android.R.attr.padding;
import static com.almaorient.ferno92.almaorienteering.R.id.all;
import static com.almaorient.ferno92.almaorienteering.R.id.center;
import static com.almaorient.ferno92.almaorienteering.R.id.map;

/**
 * Created by luca.fernandez on 07/03/2017.
 */

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {
    List<AulaModel> mListaAule = new ArrayList<AulaModel>();

    private ClusterManager<AulaMarker> mClusterManager;
    private ClusterManager<ScuoleMarker>mClusterManager2;
    ProgressDialog mProgress;
    GoogleMap mMap;

    Spinner mScuolaSpinner;
    Spinner mCorsoSpinner;
    Scuola mSelectedScuola;
    Corso mSelectedCorso;
    Integer mCount;
    Integer mCountResetScuola;

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

    private List<Corso> mListaCorsi = new ArrayList<Corso>();
    private ArrayList<IndirizziModel> mListaIndirizzi = new ArrayList<IndirizziModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        mCount=0;
        mCountResetScuola=0;

        setTitle("Tutte le aule Unibo");

        initScuolaArray();

        mCorsoSpinner = (Spinner) findViewById(R.id.spinnercorso);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
//
        Query query2 = ref.child("aule").orderByKey();
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String codice = (String) data.child("aula_codice").getValue();
                    String nome = (String) data.child("aula_nome").getValue();
                    String indirizzo = (String) data.child("aula_indirizzo").getValue();
                    String piano = (String) data.child("aula_piano").getValue();
                    String latitudine = (String) data.child("latitude").getValue();
                    String longitudine = (String) data.child("longitude").getValue();

                    AulaModel aula = new AulaModel(codice, nome, indirizzo, piano, latitudine, longitudine);
                    mListaAule.add(aula);
                }
                Log.d("size lista aule", String.valueOf(mListaAule.size()));
                initMap();
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {

                }
            }
        });

    }

    private void initScuolaArray(){
        ArrayAdapter spinnerScuolaArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.mScuolaadatt);
        mScuolaSpinner = (Spinner) findViewById(R.id.spinnerscuola);
        mScuolaSpinner.setAdapter(spinnerScuolaArrayAdapter);
        final String callingactivity = getIntent().getExtras().getString("CallingActivity");
        if (callingactivity.equals("dettagliCorso")) {
            final Long idscuola = getIntent().getExtras().getLong("idscuola");
            mScuolaSpinner.setSelection((int) (long) idscuola);
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Loading");
            mProgress.setMessage("Stiamo caricando i dati");
            mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            mProgress.show();
        }

        if (callingactivity.equals("main")) {
            mProgress = new ProgressDialog(this);
            mProgress.setTitle("Loading");
            mProgress.setMessage("Stiamo caricando i dati");
            mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            mProgress.show();
        }

        initMap();

        mScuolaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedScuola = (Scuola) mScuolaSpinner.getSelectedItem();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();

                mCount=mCount+1;
                mCountResetScuola=mCountResetScuola+1;
                setTitle("Tutte le aule Unibo");

                if (mScuolaSpinner.getSelectedItemPosition()!=0) {

                    mCorsoSpinner.setClickable(true);

                    Query query = ref.child("corso/" + mScuolaadatt[mScuolaSpinner.getSelectedItemPosition()].getScuolaId());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mListaCorsi.clear();
                            Corso vuoto = new Corso("","Seleziona un corso","","","","",null,null,"");
                                mListaCorsi.add(vuoto);
                                initMap();

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                String nome = (String) data.child("corso_descrizione").getValue();
                                String codicecorso = String.valueOf(data.child("corso_codice").getValue());

                                Corso corso = new Corso(codicecorso, nome, "", "", "", "",null,null,"");
                                mListaCorsi.add(corso);

                            }
                            initCorsoArray();
                            mCorsoSpinner.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (databaseError != null) {

                            }
                        }
                    });
                }
                else if (mCount==1 ) {
                    mCorsoSpinner.setVisibility(View.GONE);
                    initMap();
                    if (mClusterManager2!=null){
                        mClusterManager2.clearItems();
                    }
                }
                else {
                    mCorsoSpinner.setSelection(0);
                    mCorsoSpinner.setClickable(false);
                    if (mClusterManager2 != null) {
                        mMap.clear();
                    }
                    initMap();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initCorsoArray(){
        ArrayAdapter spinnerCorsoArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, this.mListaCorsi);

        mCorsoSpinner.setAdapter(spinnerCorsoArrayAdapter);

        final String callingactivity = getIntent().getExtras().getString("CallingActivity");
        if (callingactivity.equals("dettagliCorso") && mCountResetScuola==1) {
            Integer codcorso = getIntent().getExtras().getInt("codcorso");
            mCorsoSpinner.setSelection(codcorso);
            mProgress.dismiss();
        }

        mCorsoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedCorso = (Corso) mCorsoSpinner.getSelectedItem();
                if (mCorsoSpinner.getSelectedItemPosition()!=0) {
                    String codicecorso = mSelectedCorso.getScuolaId();

                    setTitle("Sedi del corso selezionato");

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();

                    Query query2 = ref.child("mappe/" + mScuolaadatt[mScuolaSpinner.getSelectedItemPosition()].getScuolaId())
                            .orderByChild("corso_codice").equalTo(Integer.parseInt(codicecorso));
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mListaIndirizzi.clear();

                            if (callingactivity.equals("main") || mCountResetScuola!=1) {
                                mClusterManager.clearItems();
                                mClusterManager2.clearItems();
                            }
                            if (mMap != null) {
                                mMap.clear();
                            }
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                final String codicecorso = String.valueOf(data.child("corso_codice").getValue());

                                Double latitude = (Double) data.child("latitude").getValue();
                                Double longitude = (Double) data.child("longitude").getValue();
                                String corsolaurea = (String) data.child("Corso di laurea").getValue();
                                String indirizzo = (String) data.child("indirizzo").getValue();

                                IndirizziModel address = new IndirizziModel(codicecorso, latitude, longitude,corsolaurea,indirizzo);
                                mListaIndirizzi.add(address);
                            }
                            initMap();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (databaseError != null) {

                            }
                        }
                    });

                }
                else {

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();

                    setTitle("Sedi della scuola");

                    Query query2 = ref.child("mappe/" + mScuolaadatt[mScuolaSpinner.getSelectedItemPosition()].getScuolaId()).orderByKey();
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mListaIndirizzi.clear();

                            if (callingactivity.equals("main") || mCountResetScuola != 1) {
                                mClusterManager.clearItems();
                            }
                            if (mMap != null) {
                                mMap.clear();
                            }
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                final String codicecorso = String.valueOf(data.child("corso_codice").getValue());

                                Double latitude = (Double) data.child("latitude").getValue();
                                Double longitude = (Double) data.child("longitude").getValue();
                                String corsolaurea = (String) data.child("Corso di laurea").getValue();
                                String indirizzo = (String) data.child("indirizzo").getValue();

                                IndirizziModel address = new IndirizziModel(codicecorso, latitude, longitude, corsolaurea, indirizzo);
                                mListaIndirizzi.add(address);
                            }
                            Log.d("size lista indirizzi",String.valueOf(mListaIndirizzi.size()));
                            initMap();

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                if (databaseError != null) {

                                }
                            }
                        });
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;

        if (mCorsoSpinner.getSelectedItemPosition()!=0 && !mListaIndirizzi.isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (IndirizziModel indirizzi : this.mListaIndirizzi) {
                if (indirizzi.getLatitudine() != null) {
                    map.addMarker(new MarkerOptions().position(new LatLng(indirizzi.getLatitudine(), indirizzi.getLongitudine()))
                            .title(indirizzi.getCorso()).snippet(indirizzi.getIndirizzo()));

                }

                LatLng pos = new LatLng(indirizzi.getLatitudine(), indirizzi.getLongitudine());
                builder.include(pos);
            }
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds((bounds),300);
            this.mMap.animateCamera(cu);
            mMap.setMaxZoomPreference((float) 16.5);
        }

        if (mCorsoSpinner.getSelectedItemPosition()==0 && mScuolaSpinner.getSelectedItemPosition()!=0){
            setUpClusterer2();
        }

        if (mScuolaSpinner.getSelectedItemPosition()==0) {
            setUpClusterer();
            setTitle("Tutte le aule Unibo");
        }

    }

    public void initMap(){

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);

        mapFragment.getMapAsync(this);

    }


////
    private void setUpClusterer() {
//
//
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.32, 11.78), 8.0f));
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)


        mClusterManager = new ClusterManager<AulaMarker>(this, this.mMap);



        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        this.mMap.setOnCameraIdleListener(mClusterManager);
        this.mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<AulaMarker>() {
                    @Override
                    public boolean onClusterClick(Cluster<AulaMarker> cluster) {

                        LatLng latLng = null;
                        int i = 0;
                        Boolean isDifferent = false;
                        String nomiAule = "";
                        for(AulaMarker marker : cluster.getItems()){
                            if(i == 0){
                                latLng = marker.getPosition();
                            }
                            if(i != 0 && !marker.getPosition().equals(latLng)){
                                isDifferent = true;
                                break;
                            }else{
                                nomiAule += marker.getTitle() + "\r\n";
                            }
                            i++;
                        }
                        if(isDifferent){
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    cluster.getPosition(), (float) Math.floor(mMap
                                            .getCameraPosition().zoom + 1)), 300,
                                    null);
                        }else{
                            new AlertDialog.Builder(MapsActivity.this)
                                    .setTitle("Gruppo di aule")
                                    .setMessage(nomiAule)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        return true;
                    }

                });

        // Add cluster items (markers) to the cluster manager.
        addMarkers();
    }

    private void addMarkers() {


        for(AulaModel aula : this.mListaAule){
            if(!aula.getLatitudine().isEmpty() && !aula.getLongitudine().isEmpty()){
                AulaMarker marker = new AulaMarker(Double.valueOf(aula.getLatitudine()), Double.valueOf(aula.getLongitudine()), aula.getNome(), aula.getPiano());
                mClusterManager.addItem(marker);
            }

        }

    }

    private void setUpClusterer2() {
//
//        // To dismiss the dialog
//        mProgress.dismiss();
//
//
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.32, 11.78), 8.0f));
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)


        mClusterManager2 = new ClusterManager<ScuoleMarker>(this, this.mMap);
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        this.mMap.setOnCameraIdleListener(mClusterManager2);
        this.mMap.setOnMarkerClickListener(mClusterManager2);

        mClusterManager2.setRenderer(new CustomRenderer(getApplicationContext(),mMap,mClusterManager2));

        mClusterManager2
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ScuoleMarker>() {
                    @Override
                    public boolean onClusterClick(Cluster<ScuoleMarker> cluster) {
                        LatLng latLng = null;
                        int i = 0;
                        Boolean isDifferent = false;
                        String nomiCorsi = "";
                        String indirizzo = "";

                        for(ScuoleMarker marker : cluster.getItems()){
                            setUpMapIfNeeded();
                            if(i == 0){
                                latLng = marker.getPosition();
                            }
                            if(i != 0 && !marker.getPosition().equals(latLng)){
                                isDifferent = true;
                                break;
                            }else{
                                nomiCorsi += marker.getTitle() + "\r\n";
                                indirizzo=marker.getSnippet();
                            }
                            i++;
                        }
                        if(isDifferent){
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    cluster.getPosition(), (float) Math.floor(mMap
                                            .getCameraPosition().zoom + 1)), 300,
                                    null);
                        }else{
                            new AlertDialog.Builder(MapsActivity.this)
                                    .setTitle(indirizzo)
                                    .setMessage(nomiCorsi)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        return true;
                    }

                });

        addMarkers2();

        // Add cluster items (markers) to the cluster manager.

    }

    private void addMarkers2() {


        for(IndirizziModel addresses : this.mListaIndirizzi){
            if(addresses.getLatitudine()!=null && addresses.getLongitudine()!=null){
                ScuoleMarker marker = new ScuoleMarker(addresses.getLatitudine(), addresses.getLongitudine(), addresses.getCorso(), addresses.getIndirizzo());
                mClusterManager2.addItem(marker);
            }

        }

    }

    class CustomRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T>
    {
        public CustomRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
            //start clustering if at least 2 items overlap
            return cluster.getSize() > 1;
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        if (mMap != null) {
            mClusterManager2 = new ClusterManager<ScuoleMarker>(this, mMap);
            mClusterManager2.setRenderer(new CustomRenderer<ScuoleMarker>(this, mMap, mClusterManager2));

        }
    }

    //    https://developers.google.com/maps/documentation/android-api/config
//    https://developers.google.com/maps/documentation/android-api/map
//    https://developers.google.com/maps/documentation/android-api/marker
}
