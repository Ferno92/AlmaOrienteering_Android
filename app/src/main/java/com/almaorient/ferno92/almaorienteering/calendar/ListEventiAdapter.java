package com.almaorient.ferno92.almaorienteering.calendar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.homepage.NewMainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lucas on 04/04/2017.
 */

public class ListEventiAdapter extends ArrayAdapter<HashMap> {
    private Context mContext;
    private Listener mListener;
    public ListEventiAdapter(@NonNull Context context, @NonNull ArrayList<HashMap> objects, Listener listener) {
        super(context, R.layout.agenda_list_item, objects);
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.agenda_list_item, parent, false);
        }

        final HashMap evento = getItem(position);
        TextView dayText = (TextView)view.findViewById(R.id.day);
        dayText.setText((String)evento.get("day"));
        TextView titoloText = (TextView)view.findViewById(R.id.titolo);
        titoloText.setText((String)evento.get("title"));
        TextView oraText = (TextView)view.findViewById(R.id.ora);
        oraText.setText((String)evento.get("start") + " - " + (String)evento.get("end"));
        TextView descrizioneText = (TextView)view.findViewById(R.id.descrizione);
        descrizioneText.setText((String)evento.get("description"));

        String dayName = getDayName("2017", parent, (String)evento.get("day"));
        TextView dayNameText = (TextView)view.findViewById(R.id.day_name);
        dayNameText.setText(dayName);

        ImageButton addToCalendarImageButton = (ImageButton)view.findViewById(R.id.add_to_calendar);
        addToCalendarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.addEventToCalendar(evento, parent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showEventDetail(evento, parent);
            }
        });

        return view;
    }

    private String getDayName(String year, ViewGroup parent, String day) {
        String nomeMese = ((TextView)((LinearLayout)parent.getParent()).findViewById(R.id.nome_mese)).getText().toString();
        nomeMese = nomeMese.replace(" 2017", "");
        ArrayList<CalendarModel> monthList = ((CalendarActivity)mContext).getMonthList();
        int meseIndex = -1;

        for(int i = 0; i < monthList.size(); i++){
            if(monthList.get(i).getNomeMese().equals(nomeMese)){
                meseIndex = monthList.get(i).getPosition();
                break;
            }
        }
        String input = String.format("%02d", Integer.parseInt(day)) + "-" + String.format("%02d", meseIndex) + "-" + year;

        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
        String goal = outFormat.format(date);

        return goal;
    }


    public interface  Listener{
        void addEventToCalendar(HashMap evento, ViewGroup parent);
        void showEventDetail(HashMap evento, ViewGroup parent);
    }


}
