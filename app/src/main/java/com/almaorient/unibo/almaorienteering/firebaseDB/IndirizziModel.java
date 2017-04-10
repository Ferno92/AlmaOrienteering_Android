package com.almaorient.unibo.almaorienteering.firebaseDB;

/**
 * Created by ale96 on 25/03/2017.
 */

public class IndirizziModel {
    public String mCodice;
    public Double mLatitudine;
    public Double mLongitudine;
    public String mCorsoLaurea;
    public String mIndirizzo;


    public IndirizziModel(String codice, Double latitudine, Double longitudine, String corsodilaurea, String indirizzo){
        this.mCodice = codice;
        this.mLatitudine = latitudine;
        this.mLongitudine = longitudine;
        this.mCorsoLaurea = corsodilaurea;
        this.mIndirizzo = indirizzo;
    }

    public String getCodice(){
        return this.mCodice;
    }


    public Double getLatitudine(){
        return this.mLatitudine;
    }
    public Double getLongitudine(){
        return this.mLongitudine;
    }

    public String getCorso(){
        return this.mCorsoLaurea;
    }

    public String getIndirizzo(){
        return this.mIndirizzo;
    }


}

