package com.almaorient.ferno92.almaorienteering.strutturaUnibo;

import java.util.ArrayList;

/**
 * Created by luca.fernandez on 10/03/2017.
 */

public class Scuola {
    public String mId;
    public String mNome;
    public ArrayList<Corso> mListaCorsi = new ArrayList<>();

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

    public String toString()
    {
        return this.mNome;
    }
}
