package com.almaorient.ferno92.almaorienteering.PianoStudi;

/**
 * Created by ale96 on 30/03/2017.
 */
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<String> mListDataHeader;
    private final Map<String, List<String>> mListDataChild;
    private final Map<String,String>mUrlThirdlevel;
    private final Map<String,String>mUrlSecondlevel;


    public SecondLevelAdapter(Context mContext, List<String> mListDataHeader, Map<String, List<String>> mListDataChild,
                              Map<String,String>Urlthirdlevel, Map<String,String>Urlsecondlevel) {
        this.mContext = mContext;
        this.mListDataHeader = mListDataHeader;
        this.mListDataChild = mListDataChild;
        this.mUrlThirdlevel = Urlthirdlevel;
        this.mUrlSecondlevel = Urlsecondlevel;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.dettaglicorso_list_item_third_level, parent, false);
        }
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        txtListChild.setText(childText);

        ImageView image = (ImageView) convertView
                .findViewById(R.id.frecciathirdlevel);
        image.setImageBitmap(null); //altrimenti in alcuni casi mette le icone sbagliate


        if (mUrlThirdlevel.get(getChild(groupPosition,childPosition)).contains("http://") ||
                mUrlSecondlevel.get(getGroup(groupPosition)).contains("http://")) {

            image.setImageResource(R.drawable.ic_arrow_right_pianostudi);
           //richiamoBrowser(mUrlTerzoLivello.get(((TextView) view).getText()));
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.dettaglicorso_list_group_second_level, parent, false);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        ImageView image = (ImageView) convertView
                .findViewById(R.id.frecciasecondlevel);

        image.setImageBitmap(null); //altrimenti in alcuni casi mette le icone sbagliate

        if (!(getChildrenCount(groupPosition)==0)){
            if (isExpanded){
                image.setImageResource(R.drawable.ic_arrow_up_2ndlevelpianostudi);
            }
            else {
                image.setImageResource(R.drawable.ic_arrow_down_2ndlevelpianistudio);

            }
        }
        else if (mUrlSecondlevel.get(getGroup(groupPosition)).contains("http://")){
            image.setImageResource(R.drawable.ic_arrow_right_pianostudi);

        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;

    }
}
