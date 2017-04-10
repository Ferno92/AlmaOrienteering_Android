package com.almaorient.unibo.almaorienteering.versus;

/**
 * Created by lucas on 15/03/2017.
 */

public class StatCorsoModel {

    public static final String CORSO = "corso";
    public static final String ESAMI = "voto_esami";
    public static final String LAUREA = "voto_laurea";
    public static final String INCORSO = "in_corso_perc";
    public static final String DURATA = "durata";
    public static final String RITARDO = "ritardo";
    public static final String ERASMUS = "erasmus";
    public static final String STAGE = "stage_perc";
    public static final String SODDISFAZIONE = "soddisfazione_perc";
    //ecc ecc

    String mCorso;
    String mVotoEsami;
    String mVotoLaurea;
    String mInCorso;
    String mDurata;
    String mRitardo;
    String mErasmus;
    String mStage;
    String mSoddisfazione;

    public StatCorsoModel(String corso, String votoEsami, String votoLaurea, String inCorso,
                          String durata, String ritardo, String erasmus, String stage, String soddisfazione){
        this.mCorso = corso;
        this.mVotoEsami = votoEsami;
        this.mVotoLaurea = votoLaurea;
        this.mInCorso = inCorso;
        this.mDurata = durata;
        this.mRitardo = ritardo;
        this.mErasmus = erasmus;
        this.mStage = stage;
        this.mSoddisfazione = soddisfazione;
    }

    public String getCorso() {
        return mCorso;
    }

    public String getVotoEsami() {
        return mVotoEsami;
    }

    public String getVotoLaurea() {
        return mVotoLaurea;
    }

    public String getInCorso() {
        return mInCorso;
    }

    public String getDurata() {
        return mDurata;
    }

    public String getRitardo() {
        return mRitardo;
    }

    public String getErasmus() {
        return mErasmus;
    }

    public String getStage() {
        return mStage;
    }

    public String getSoddisfazione() {
        return mSoddisfazione;
    }
}
