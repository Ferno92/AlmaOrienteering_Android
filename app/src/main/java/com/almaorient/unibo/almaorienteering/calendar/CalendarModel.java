package com.almaorient.unibo.almaorienteering.calendar;

import java.util.ArrayList;

/**
 * Created by lucas on 05/04/2017.
 */

public class CalendarModel {
    private int mPosition;
    private String mNomeMese;
    private ArrayList mListaEventi;

    public CalendarModel(int pos, String nome, ArrayList lista){
        this.mPosition = pos;
        this.mNomeMese = nome;
        this.mListaEventi = lista;
    }

    public int getPosition(){
        return this.mPosition;
    }

    public String getNomeMese(){
        return this.mNomeMese;
    }

    public ArrayList getListaEventi(){
        return  this.mListaEventi;
    }

}
