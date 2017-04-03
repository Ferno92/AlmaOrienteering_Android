package com.almaorient.ferno92.almaorienteering.tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.homepage.HomePagerAdapter;

/**
 * Created by lucas on 02/04/2017.
 */

public class TutorialActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TutorialPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);

        mViewPager = (ViewPager)findViewById(R.id.tutorial_pager);
        mPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
    }
}
