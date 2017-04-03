package com.almaorient.ferno92.almaorienteering.PianoStudi;


import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.EmbedBrowser;
import com.almaorient.ferno92.almaorienteering.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ThreeLevelExpandableListView extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<String> mListDataHeader;
    private final Map<String,String> mUrlSecondoLivello;
    private final Map<String,String> mUrlTerzoLivello;
    private final Map<String, List<String>> mListData_SecondLevel_Map;
    private final Map<String, List<String>> mListData_ThirdLevel_Map;

    private void richiamoBrowser(String url) {
        Intent browser = new Intent(mContext, EmbedBrowser.class);
        browser.putExtra("url", url);
        mContext.startActivity(browser);
    }

    public ThreeLevelExpandableListView(Context mContext, List<String> mListDataHeader, HashMap<String, List<String>> second_level_map, HashMap<String,
            List<String>> third_level_map, HashMap<String,String> urlsecondolivello, HashMap<String,String>urlterzolivello) {
        this.mContext = mContext;
        this.mListDataHeader = mListDataHeader;


        // SECOND LEVEL
        mListData_SecondLevel_Map=second_level_map;
        mUrlSecondoLivello= urlsecondolivello;
        mUrlTerzoLivello=urlterzolivello;
        // THIRD LEVEL
        mListData_ThirdLevel_Map=third_level_map;

//        for (Object o : mListData_SecondLevel_Map.entrySet()) {
//            Map.Entry entry = (Map.Entry) o;
//            Object object = entry.getValue();
//            if (object instanceof List) {
//                List<String> stringList = new ArrayList<>();
//                Collections.addAll(stringList, (String[]) ((List) object).toArray());
//                for (int i = 0; i < stringList.size(); i++) {
//                    mItemChildOfChild = mContext.getResources().getStringArray(R.array.items_array_expandable_level_three);
//                    listChild = Arrays.asList(mItemChildOfChild);
//                    mListData_ThirdLevel_Map.put(stringList.get(i), listChild);
//                }
//            }
//        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, final View convertView, final ViewGroup parent) {
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.mContext);
        final String parentNode = (String) getGroup(groupPosition);
        final SecondLevelAdapter adapter = new SecondLevelAdapter(this.mContext,mListData_SecondLevel_Map.get(parentNode),
                mListData_ThirdLevel_Map,mUrlTerzoLivello,mUrlSecondoLivello);
        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.mContext,mListData_SecondLevel_Map.get(parentNode),
                mListData_ThirdLevel_Map,mUrlTerzoLivello,mUrlSecondoLivello));
        secondLevelExpListView.setGroupIndicator(null);
//        secondLevelExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            int previousGroup = -1;
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                if (groupPosition != previousGroup)
//                    secondLevelExpListView.collapseGroup(previousGroup);
//                previousGroup = groupPosition;
//            }
//        });


        secondLevelExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                    TextView text = (TextView) view.findViewById(R.id.lblListItem);
                    if (mUrlTerzoLivello.get(((TextView) text).getText()).contains("http://")) {
                        richiamoBrowser(mUrlTerzoLivello.get(((TextView) text).getText()));
                    }
                    else if (mUrlSecondoLivello.get((String)expandableListView.getExpandableListAdapter().getGroup(i)).contains("http://")){
                        richiamoBrowser(mUrlSecondoLivello.get((String)expandableListView.getExpandableListAdapter().getGroup(i)));
                    }
                return false;
            }
        });

        secondLevelExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if (expandableListView.getExpandableListAdapter().getChildrenCount(i)==0) {
                    if (mUrlSecondoLivello.get((String)expandableListView.getExpandableListAdapter().getGroup(i)).contains("http://")) {
                        richiamoBrowser(mUrlSecondoLivello.get((String)expandableListView.getExpandableListAdapter().getGroup(i)));
                        adapter.notifyDataSetChanged();

                    }
                }

                return false;
            }

        });

        return secondLevelExpListView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

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
    public View getGroupView(int groupPosition, final boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.dettaglicorso_list_group_first_level, parent, false);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        ImageView image = (ImageView) convertView
                .findViewById(R.id.frecciafirstlevel);

        if (isExpanded){
            image.setImageResource(R.drawable.ic_expand_less);
        }
        else {
            image.setImageResource(R.drawable.ic_expand_more);

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

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {
    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }



}