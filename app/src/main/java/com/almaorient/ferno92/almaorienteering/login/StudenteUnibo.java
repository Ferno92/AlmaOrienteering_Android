package com.almaorient.ferno92.almaorienteering.login;

import java.util.Map;

/**
 * Created by lucas on 19/03/2017.
 */

public class StudenteUnibo {

    private String mId;
    private String mNome;
    private String mCognome;
    private Map<String, String> mCorso;
    private String mScuola;

    public StudenteUnibo(String id, String nome, String cognome, Map<String, String> corso, String scuola){
        this.mId = id;
        this.mNome = nome;
        this.mCognome = cognome;
        this.mCorso = corso;
        this.mScuola = scuola;
    }

    public String getCognome() {
        return mCognome;
    }

    public Map<String, String> getCorso() {
        return mCorso;
    }

    public String getUserId() {
        return mId;
    }

    public String getNome() {
        return mNome;
    }

    public String getScuola() {
        return mScuola;
    }

}
