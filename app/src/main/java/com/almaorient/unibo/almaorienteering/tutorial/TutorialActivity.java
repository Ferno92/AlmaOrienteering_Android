package com.almaorient.unibo.almaorienteering.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.almaorient.unibo.almaorienteering.ChooseActivity;
import com.almaorient.unibo.almaorienteering.R;
import com.almaorient.unibo.almaorienteering.homepage.NewMainActivity;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by lucas on 02/04/2017.
 */

public class TutorialActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TutorialPagerAdapter mPagerAdapter;
    private boolean mIsFirstTime;
    private SharedPreferences mSharedPreferences;
    private  boolean mFromHomepage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFromHomepage = getIntent().getBooleanExtra("fromHomepage", false);

        mSharedPreferences = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        mIsFirstTime = mSharedPreferences.getBoolean("isFirstTime", true);

        if((!mFromHomepage && mIsFirstTime) || (mFromHomepage && !mIsFirstTime)) {
            setContentView(R.layout.tutorial_layout);

            mViewPager = (ViewPager) findViewById(R.id.tutorial_pager);
            mPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mPagerAdapter);

            final InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.tutorial_indicator);
            inkPageIndicator.setViewPager(mViewPager);

            TextView skipText = (TextView) findViewById(R.id.skip);
            final TextView doneText = (TextView) findViewById(R.id.done);
            final TextView nextText = (TextView) findViewById(R.id.next);

            skipText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToHomepage();
                }
            });

            doneText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToHomepage();
                }
            });

            nextText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = mViewPager.getCurrentItem();
                    mViewPager.setCurrentItem(currentPosition + 1);
                }
            });

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 4) {
                        nextText.setVisibility(View.GONE);
                        doneText.setVisibility(View.VISIBLE);
                    } else {
                        nextText.setVisibility(View.VISIBLE);
                        doneText.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }else{

            Intent i = new Intent(TutorialActivity.this, ChooseActivity.class);
            i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    private void goToHomepage(){
        Intent i;
        if(mIsFirstTime){
            i = new Intent(TutorialActivity.this, ChooseActivity.class);
            i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            startActivity(i);
            finish();
            editor.commit();
        }else if(!mFromHomepage){
            i = new Intent(TutorialActivity.this, NewMainActivity.class);
            i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }else{
           finish();
        }

    }
}
