package com.almaorient.unibo.almaorienteering.strutturaUnibo;

import java.util.ArrayList;

/**
 * Created by luca.fernandez on 10/03/2017.
 */

public class Scuola {
    public String mId;
    public String mNome;
    public ArrayList<Corso> mListaCorsi = new ArrayList<>();
    public ArrayList<String>mElencoNomiCorsi = new ArrayList<>();

    public Scuola(String id, String nome){
        this.mId = id;
        this.mNome = nome;
    }
    public Scuola(String id, String nome, ArrayList listaCorsi){
        this.mId = id;
        this.mNome = nome;
        this.mListaCorsi = listaCorsi;
    }

    public String getNome(){
        return this.mNome;
    }

    public String getScuolaId(){
        return this.mId;
    }

    public ArrayList getListaCorsi(){
        return this.mListaCorsi;
    }

    public ArrayList <String> getElencoCorsi (){
        for (int i=0; i<mListaCorsi.size();i++){
            mElencoNomiCorsi.add(mListaCorsi.get(i).getNome());
        }
        return mElencoNomiCorsi;
    }

    public String toString()
    {
        return this.mNome;
    }
}
