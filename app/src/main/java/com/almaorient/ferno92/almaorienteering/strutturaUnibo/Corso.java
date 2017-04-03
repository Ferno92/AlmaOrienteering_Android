package com.almaorient.ferno92.almaorienteering.strutturaUnibo;

import java.util.ArrayList;

/**
 * Created by luca.fernandez on 10/03/2017.
 */

public class Corso {
    public String mCodiceCorso;
    public String mNome;
    public String mUrl;
    public String mTipo;
    public String mCampus;
    public String mAccesso;
    public Long mIdScuola;
    public Long mDurata;
    public String mSedeDidattica;
    public String mAbbreviazioneScuola;


    public Corso(String codice, String nome, String url, String tipo, String campus, String accesso,
                 Long idscuola,Long durata,String sededidattica,String abbreviazionescuola){
        this.mCodiceCorso = codice;
        this.mNome = nome;
        this.mUrl = url;
        this.mTipo = tipo;
        this.mCampus = campus;
        this.mAccesso = accesso;
        this.mIdScuola=idscuola;
        this.mDurata=durata;
        this.mSedeDidattica = sededidattica;
        this.mAbbreviazioneScuola=abbreviazionescuola;
    }

    public String getNome(){
        return this.mNome;
    }

    public String getCorsoCodice(){
        return this.mCodiceCorso;
    }

    public String getUrl() { return this.mUrl; }

    public String getTipo(){
        return this.mTipo;
    }

    public String getCampus(){
        return this.mCampus;
    }

    public String getAccesso() { return this.mAccesso; }

    public Long getIdScuola() {return this.mIdScuola;}

    public Long getDurata() {return this.mDurata; }

    public String getSedeDidattica () {return this.mSedeDidattica; }

    public String getAbbreviazioneSCuola () {return this.mAbbreviazioneScuola; }

    public String toString()
    {
        return this.mNome;
    }
}
