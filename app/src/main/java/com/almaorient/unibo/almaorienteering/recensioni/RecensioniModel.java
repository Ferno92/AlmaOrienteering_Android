package com.almaorient.unibo.almaorienteering.recensioni;

/**
 * Created by lucas on 26/03/2017.
 */

public class RecensioniModel {

    private String mEmail;
    private String mVoto;
    private String mRecensione;
    private int mQuota;

    public RecensioniModel(String email, String voto, String recensione, int quota){
        this.mEmail = email;
        this.mVoto = voto;
        this.mRecensione = recensione;
        this.mQuota = quota;
    }

    public String getEmail(){
        return this.mEmail;
    }

    public String getVoto(){
        return this.mVoto;
    }

    public String getRecensione(){
        return this.mRecensione;
    }

    public int getQuota(){
        return  this.mQuota;
    }

}
