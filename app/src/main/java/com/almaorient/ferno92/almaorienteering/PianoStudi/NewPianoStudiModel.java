package com.almaorient.ferno92.almaorienteering.PianoStudi;

import com.almaorient.ferno92.almaorienteering.strutturaUnibo.Corso;

import java.util.ArrayList;

/**
 * Created by ale96 on 29/03/2017.
 */

public class NewPianoStudiModel {

    public static final String NOME_INSEGNAMENTO = "materia_descrizione";
    public static final String PADRE = "componente_padre";
    public static final String RADICE = "componente_radice";
    public static final String URL = "url";

    //ecc ecc
    Integer mCorsoCodice;
    String mCorsoNome;
    Integer mPadre;
    Integer mRadice;
    String mUrl;
    ArrayList<Corso> mListaInsegnamenti = new ArrayList<>();


    public NewPianoStudiModel(String corsoNome, Integer padre, Integer radice,
                          String url){
        this.mCorsoNome = corsoNome;
        this.mPadre = padre;
        this.mRadice = radice;
        this.mUrl = url;

    }

    public NewPianoStudiModel(String corsoNome, Integer padre, Integer radice,
                              String url, ArrayList listaInsegnamenti){
        this.mCorsoNome = corsoNome;
        this.mPadre = padre;
        this.mRadice = radice;
        this.mUrl = url;
        this.mListaInsegnamenti=listaInsegnamenti;

    }

    public String getCorsoNome () {
        return mCorsoNome;
    }

    public Integer getPadre() {
        return mPadre;
    }

    public Integer getRadice() {
        return mRadice;
    }

    public String getUrl() {
        return mUrl;
    }

    public ArrayList getListaInsegnamenti () { return mListaInsegnamenti; }

    public String toString()
    {
        return this.mCorsoNome;
    }

}
