package com.almaorient.ferno92.almaorienteering.homepage;

/**
 * Created by lucas on 28/03/2017.
 */

public class HomeElementModel {
    private int mPos;
    private String mTitle;
    private String mDescription;
    private String mImgSource;

    public HomeElementModel(int pos, String title, String description, String imgSource){
        this.mPos = pos;
        this.mTitle = title;
        this.mDescription = description;
        this.mImgSource = imgSource;
    }

    public String getTitle(){
        return this.mTitle;
    }
    public String getDescription(){
        return this.mDescription;
    }
    public String getImgSource(){
        return this.mImgSource;
    }
    public int getPos(){
        return this.mPos;
    }
}
