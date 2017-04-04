package com.almaorient.ferno92.almaorienteering.calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.BaseActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by luca.fernandez on 13/03/2017.
 */

public class CalendarActivity extends BaseActivity {
    private LinearLayout mRootView;
    private ArrayList<CalendarModel> mMonthList = new ArrayList<>();
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Stiamo cercando gli eventi più interessanti");
        mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        mProgress.show();

        this.mRootView = (LinearLayout) findViewById(R.id.main_calendar_layout);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        Query query = ref.child("calendar/2017");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap yearMap = (HashMap) dataSnapshot.getValue();
                Iterator monthIterator = yearMap.keySet().iterator();
                while (monthIterator.hasNext()) {
                    String monthKey = (String) monthIterator.next();
                    switch (monthKey) {
                        case "Aprile":
//                            insertMonthInView(aprileArray, "Aprile");
                            mMonthList.add(new CalendarModel(4, monthKey, (ArrayList) yearMap.get(monthKey)));
                            break;
                        case "Maggio":
                            mMonthList.add(new CalendarModel(5, monthKey, (ArrayList) yearMap.get(monthKey)));
                            break;
                        case "Giugno":
                            mMonthList.add(new CalendarModel(6, monthKey, (ArrayList) yearMap.get(monthKey)));
                            break;
                        case "Luglio":
                            mMonthList.add(new CalendarModel(7, monthKey, (ArrayList) yearMap.get(monthKey)));
                            break;
                        case "Settembre":
                            mMonthList.add(new CalendarModel(9, monthKey, (ArrayList) yearMap.get(monthKey)));
                            break;
                        case "Ottobre":
                            mMonthList.add(new CalendarModel(10, monthKey, (ArrayList) yearMap.get(monthKey)));
                            break;
                        case "Novembre":
                            mMonthList.add(new CalendarModel(11, monthKey, (ArrayList) yearMap.get(monthKey)));
                            break;
                        case "Dicembre":
                            mMonthList.add(new CalendarModel(12, monthKey, (ArrayList) yearMap.get(monthKey)));
                            break;
                        default:
                            break;
                    }
                }
                //Order arraylist month
                Collections.sort(mMonthList, new MonthComparator());

                Log.d("ciao: ", "ciao");

                for (int i = 0; i < mMonthList.size(); i++) {
                    insertMonthInView(mMonthList.get(i).getListaEventi(), mMonthList.get(i).getNomeMese());
                }

                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void insertMonthInView(ArrayList<HashMap> month, String nomeMese) {
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newLayout = inflater.inflate(R.layout.mese_layout, mRootView, false);
        TextView meseText = (TextView) newLayout.findViewById(R.id.nome_mese);
        meseText.setText(nomeMese);
        CalendarListView eventiList = (CalendarListView) newLayout.findViewById(R.id.lista_eventi);


        ListEventiAdapter listEventiAdapter = new ListEventiAdapter(this, month);

        eventiList.setAdapter(listEventiAdapter);

        mRootView.addView(newLayout, 0);
    }

    class MonthComparator implements Comparator<CalendarModel> {
        @Override
        public int compare(CalendarModel a, CalendarModel b) {
            return a.getPosition() > b.getPosition() ? -1 : a.getPosition() == b.getPosition() ? 0 : 1;
        }
    }
}
