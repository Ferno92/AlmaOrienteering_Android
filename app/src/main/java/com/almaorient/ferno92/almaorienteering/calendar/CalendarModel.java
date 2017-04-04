package com.almaorient.ferno92.almaorienteering.calendar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;

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
