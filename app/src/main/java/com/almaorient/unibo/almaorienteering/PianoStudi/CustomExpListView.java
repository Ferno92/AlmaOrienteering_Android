package com.almaorient.unibo.almaorienteering.PianoStudi;

/**
 * Created by ale96 on 30/03/2017.
 */

import android.content.Context;
import android.widget.ExpandableListView;


public class CustomExpListView extends ExpandableListView
{
    public CustomExpListView(Context context)
    {
        super(context);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(20000, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
