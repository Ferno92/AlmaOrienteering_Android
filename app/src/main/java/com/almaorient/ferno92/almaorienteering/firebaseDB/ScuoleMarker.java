package com.almaorient.ferno92.almaorienteering.firebaseDB;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ale96 on 27/03/2017.
 */

public class ScuoleMarker implements ClusterItem {
    private LatLng mPosition;
    private String mCorso;
    private String mIndirizzo;

    public ScuoleMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public ScuoleMarker(double lat, double lng, String nomecorso, String indirizzo) {
        this.mPosition = new LatLng(lat, lng);
        this.mCorso = nomecorso;
        this.mIndirizzo = indirizzo;
    }
    @Override
    public LatLng getPosition() {
        return this.mPosition;
    }

    @Override
    public String getTitle() {
        return this.mCorso;
    }

    @Override
    public String getSnippet() {
        return this.mIndirizzo;
    }
}
