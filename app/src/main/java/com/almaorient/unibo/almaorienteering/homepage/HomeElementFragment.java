package com.almaorient.unibo.almaorienteering.homepage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.almaorient.unibo.almaorienteering.calendar.CalendarActivity;
import com.almaorient.unibo.almaorienteering.FilterCorsoActivity;
import com.almaorient.unibo.almaorienteering.InfoGeneraliActivity;
import com.almaorient.unibo.almaorienteering.MapsActivity;
import com.almaorient.unibo.almaorienteering.ModusActivity;
import com.almaorient.unibo.almaorienteering.R;
import com.almaorient.unibo.almaorienteering.recensioni.RecensioniActivity;
import com.almaorient.unibo.almaorienteering.tutorial.TutorialActivity;
import com.almaorient.unibo.almaorienteering.versus.VersusSelectorActivity;

/**
 * Created by lucas on 28/03/2017.
 */

public class HomeElementFragment extends Fragment {
    private View mRootView;
    private int mPosition;
    public static HomeElementModel[] mElementList = new HomeElementModel[]{
            new HomeElementModel(0, "Come funziona UNIBO", "Informati sul funzionamento dell'ateneo e sui servizi che offre.", "ic_unibo_big"),
            new HomeElementModel(1, "Offerta formativa", "Visualizza tutte le informazioni sui corsi delle 11 Scuole dell'Università di Bologna.", "ic_elenco_scuole_big"),
            new HomeElementModel(2, "Mappa aule", "Prendi visione della posizione delle aule dell'Ateneo e scopri dove si svolgono le lezioni dei vari corsi.", "ic_mappa_big"),
            new HomeElementModel(3, "Fai una domanda", "Grazie alla collaborazione\n con l'associazione culturale Modus,\n potrai avere un contatto supplementare per eventuali domande irrisolte.", "ic_icona_chat_big"),
            new HomeElementModel(4, "Statistiche", "Metti a confronto Scuole o Corsi per avere un riscontro immediato sulle statistiche Almalaurea.", "ic_statistica_big"),
            new HomeElementModel(5, "Calendario eventi", "Rimani sempre aggiornato su eventuali incontri/orientamenti organizzati dalle Scuole.", "ic_calendario_icona_big"),
            new HomeElementModel(6, "Tutorial", "Non sai da dove partire? Consulta nuovamente il tutorial di Almaorienteering", "ic_guida_big"),
            new HomeElementModel(7, "Recensioni", "Rilascia qui la tua opinione sul corso di laurea che stai frequentando per aiutare i futuri studenti", "ic_recensioni_big")
    };


    public HomeElementFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.homepage_element, container, false);
        this.mPosition = getArguments().getInt("pos");
        TextView titoloView = (TextView)mRootView.findViewById(R.id.titolo_elemento);
        titoloView.setText(mElementList[mPosition].getTitle());
        TextView descrizioneView = (TextView)mRootView.findViewById(R.id.descrizione);
        descrizioneView.setText(mElementList[mPosition].getDescription());
        ImageView imageView = (ImageView)mRootView.findViewById(R.id.image_elemento);
        int resourceId = getResources().getIdentifier(mElementList[mPosition].getImgSource() , "drawable", getContext().getPackageName());
        LinearLayout linearView = (LinearLayout) mRootView.findViewById(R.id.container_elemento);
        imageView.setImageResource(resourceId);
        linearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                SharedPreferences sp = getContext().getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("pager_position", mPosition);
                editor.commit();
                switch(mPosition){
                    case 0:
                        i = new Intent(getContext(), InfoGeneraliActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(getContext(), FilterCorsoActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(getContext(), MapsActivity.class);
                        i.putExtra("CallingActivity","main");
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(getContext(), ModusActivity.class);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(getContext(), VersusSelectorActivity.class);
                        startActivity(i);
                        break;
                    case 5:
                        i = new Intent(getContext(), CalendarActivity.class);
                        startActivity(i);
                        break;
                    case 6:
                        i = new Intent(getContext(), TutorialActivity.class);
                        i.putExtra("fromHomepage", true);
                        startActivity(i);
                        break;
                    case 7:
                        i = new Intent(getContext(), RecensioniActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
            }
        });

        return this.mRootView;

    }

    public  static HomeElementFragment newInstance(int position){
        HomeElementFragment homeElementFragment = new HomeElementFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        homeElementFragment.setArguments(args);

        return homeElementFragment;
    }
}
