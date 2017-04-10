package com.almaorient.unibo.almaorienteering.firebaseDB;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by luca.fernandez on 09/03/2017.
 */

public class AulaMarker implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public AulaMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public AulaMarker(double lat, double lng, String title, String snippet) {
        this.mPosition = new LatLng(lat, lng);
        this.mTitle = title;
        this.mSnippet = snippet;
    }
    @Override
    public LatLng getPosition() {
        return this.mPosition;
    }

    @Override
    public String getTitle() {
        return this.mTitle;
    }

    @Override
    public String getSnippet() {
        return this.mSnippet;
    }
}
