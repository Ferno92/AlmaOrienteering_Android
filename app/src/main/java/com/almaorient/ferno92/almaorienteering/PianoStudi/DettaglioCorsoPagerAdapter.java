package com.almaorient.ferno92.almaorienteering.PianoStudi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.TitlePageIndicator;

/**
 * Created by lucas on 09/04/2017.
 */

public class DettaglioCorsoPagerAdapter extends FragmentPagerAdapter {
    public Bundle mExtras;
    ThreeLevelExpandableListView.Listener mListener;

    public DettaglioCorsoPagerAdapter(FragmentManager fm, Bundle extras, ThreeLevelExpandableListView.Listener listener) {
        super(fm);
        this.mExtras = extras;
        this.mListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        return DettaglioCorsoFragment.newInstance(position, mExtras,mListener);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch(position){
            case 0:
                title = "Info";
                break;
            case 1:
                title = "Piano didattico";
                break;
            case 2:
                title = "Obiettivi";
                break;
        }

        return title;
    }
}
