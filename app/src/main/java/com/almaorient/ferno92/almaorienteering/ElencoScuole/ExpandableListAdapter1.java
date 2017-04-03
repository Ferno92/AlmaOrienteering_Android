package com.almaorient.ferno92.almaorienteering.ElencoScuole;

import android.widget.BaseExpandableListAdapter;

import com.almaorient.ferno92.almaorienteering.R;

/**
 * Created by ale96 on 30/03/2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

    public class ExpandableListAdapter1 extends BaseExpandableListAdapter {
        private Context context;
        private List<String> listDataHeader;
        private HashMap<String,List<String>> listHashMap;

        public ExpandableListAdapter1(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listHashMap = listHashMap;
        }

        @Override
        public int getGroupCount() {
            //return 0;
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return listHashMap.get(listDataHeader.get(i)).size();
            //return 0;
        }

        @Override
        public Object getGroup(int i) {
            return listDataHeader.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return listHashMap.get(listDataHeader.get(i)).get(i1); // i = Group Item , i1 = ChildItem
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean isexpanded, View view, ViewGroup viewGroup) {
            String headerTitle = (String)getGroup(i);
            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_group_elencoscuole,null);
            }
            TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);



            ImageView indicatore = (ImageView) view
                    .findViewById(R.id.elencoscuolegroupimg);




            if (isexpanded){
                indicatore.setImageResource(R.drawable.ic_expand_less);
            }
            else{
                indicatore.setImageResource(R.drawable.ic_expand_more);
            }

            ImageView ic_scuola = (ImageView) view
                    .findViewById(R.id.ic_scuola);

            switch (headerTitle){
                case "Agraria e Medicina veterinaria":
                    ic_scuola.setImageResource(R.drawable.ic_agraria);
                    break;
                case "Economia, Management e Statistica":
                    ic_scuola.setImageResource(R.drawable.ic_ems);
                    break;
                case "Medicina e Chirurgia":
                    ic_scuola.setImageResource(R.drawable.ic_medicina_e_chirurgia);
                    break;
                case "Farmacia, Biotecnologie e Scienze motorie":
                    ic_scuola.setImageResource(R.drawable.ic_farmacia);
                    break;
                case "Ingegneria e Architettura":
                    ic_scuola.setImageResource(R.drawable.ic_icona_ingegneria_e_architettura);
                    break;
                case "Scienze":
                    ic_scuola.setImageResource(R.drawable.ic_icona_scienze);
                    break;
                case "Scienze Politiche":
                    ic_scuola.setImageResource(R.drawable.ic_icona_scienze_politiche);
                    break;
                case "Giurisprudenza":
                    ic_scuola.setImageResource(R.drawable.ic_icona_giurisprudenza);
                    break;
                case "Lingue e Letterature, Traduzione e Interpretazione":
                    ic_scuola.setImageResource(R.drawable.ic_icona_lingue_e_letterature_traduzione_e_interpretazione);
                    break;
                case "Psicologia":
                    ic_scuola.setImageResource(R.drawable.ic_psicologia);
                    break;
                case "Lettere e Beni culturali":
                    ic_scuola.setImageResource(R.drawable.ic_lettere_e_beni_culturali);
                    break;
            }

            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            final String childText = (String)getChild(i,i1);
            if(view == null)
            {
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_elencoscuole,null);
            }

            TextView txtListChild = (TextView)view.findViewById(R.id.lblListItem);
            txtListChild.setText(childText);

            ImageView indicatore = (ImageView) view
                    .findViewById(R.id.indicatorcorsi);
            indicatore.setImageResource(R.drawable.ic_arrow_right_pianostudi);

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

