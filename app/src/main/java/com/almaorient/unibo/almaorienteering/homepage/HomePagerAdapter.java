package com.almaorient.unibo.almaorienteering.homepage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

/**
 * Created by lucas on 28/03/2017.
 */

public class HomePagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    private int mNumItems;
    private final FragmentManager mFragmentManager;
    private NewMainActivity mMainActivity;
    private Context mContext;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mFragmentManager = fm;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return HomeElementFragment.newInstance(position);
    }

    @Override
    public int getIconResId(int index) {
        int resourceId = mContext.getResources().getIdentifier(HomeElementFragment.mElementList[index].getImgSource().replace("_big", "") , "drawable", mContext.getPackageName());

        return resourceId;
    }

    @Override
    public int getCount() {
        return mNumItems;
    }

    public void setNumItems(int num){
        mNumItems = num;
    }

}
