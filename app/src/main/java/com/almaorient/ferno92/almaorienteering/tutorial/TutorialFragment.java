package com.almaorient.ferno92.almaorienteering.tutorial;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaorient.ferno92.almaorienteering.R;

/**
 * Created by luca.fernandez on 03/04/2017.
 */

public class TutorialFragment extends Fragment {
    private int mPosition;
    private View mRootView;

    public TutorialFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        this.mPosition = getArguments().getInt("pos");
        switch(this.mPosition){
            case 0:
                this.mRootView = inflater.inflate(R.layout.homepage_element, container, false);
                // ecc ecc
        }

        return this.mRootView;
    }

    public static TutorialFragment newInstance(int position){
        TutorialFragment tutorialFragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        tutorialFragment.setArguments(args);

        return tutorialFragment;
    }

}
