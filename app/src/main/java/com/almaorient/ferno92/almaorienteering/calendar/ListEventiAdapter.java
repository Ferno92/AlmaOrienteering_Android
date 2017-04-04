package com.almaorient.ferno92.almaorienteering.calendar;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.homepage.NewMainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lucas on 04/04/2017.
 */

public class ListEventiAdapter extends ArrayAdapter<HashMap> {
    private Context mContext;
    public ListEventiAdapter(@NonNull Context context, @NonNull ArrayList<HashMap> objects) {
        super(context, R.layout.agenda_list_item, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.agenda_list_item, parent, false);
        }

        HashMap evento = getItem(position);
        TextView dayText = (TextView)view.findViewById(R.id.day);
        dayText.setText((String)evento.get("day"));
        TextView titoloText = (TextView)view.findViewById(R.id.titolo);
        titoloText.setText((String)evento.get("title"));
        TextView oraText = (TextView)view.findViewById(R.id.ora);
        oraText.setText((String)evento.get("start") + " - " + (String)evento.get("end"));
        TextView descrizioneText = (TextView)view.findViewById(R.id.descrizione);
        descrizioneText.setText((String)evento.get("description"));

        ImageButton addToCalendarImageButton = (ImageButton)view.findViewById(R.id.add_to_calendar);
        addToCalendarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Aggiungi evento a calendario",
                        Toast.LENGTH_SHORT).show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Mostra dettagli evento",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
