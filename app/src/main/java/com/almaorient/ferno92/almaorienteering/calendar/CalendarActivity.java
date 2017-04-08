package com.almaorient.ferno92.almaorienteering.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by luca.fernandez on 13/03/2017.
 */

public class CalendarActivity extends BaseActivity implements  ListEventiAdapter.Listener {
    private LinearLayout mRootView;
    private ArrayList<CalendarModel> mMonthList = new ArrayList<>();
    private ProgressDialog mProgress;
    private int mId;

    private boolean haveNetworkConnection() {
        boolean haveConnection = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnection = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnection = true;
        }
        return haveConnection;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!haveNetworkConnection()){
                    mProgress.dismiss();
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Impossibile contattare il server, verifica la tua connessione ad internet e riprova",Toast.LENGTH_LONG).show();
                }
            }
        },1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mMonthList.isEmpty() && haveNetworkConnection()){
                    mProgress.dismiss();
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Impossibile contattare il server, verifica la tua connessione ad internet e riprova",Toast.LENGTH_LONG).show();
                }
            }
        },6000);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Stiamo cercando gli eventi più interessanti");
        mProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        mProgress.show();
        mProgress.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i==keyEvent.KEYCODE_BACK){
                    mProgress.dismiss();
                    finish();
                }
                return false;
            }
        });

        this.mRootView = (LinearLayout) findViewById(R.id.main_calendar_layout);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        Query query = ref.child("calendar/2017");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showEventNotification();

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

    private void showEventNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Nuovo evento AlmaOrienta!")
                        .setContentText("E' ora disponibile un nuovo evento AlmaOrienta, controlla sull'app.");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, CalendarActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(CalendarActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }

    private void insertMonthInView(ArrayList<HashMap> month, String nomeMese) {
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newLayout = inflater.inflate(R.layout.mese_layout, mRootView, false);
        TextView meseText = (TextView) newLayout.findViewById(R.id.nome_mese);
        meseText.setText(nomeMese + " 2017");
        CalendarListView eventiList = (CalendarListView) newLayout.findViewById(R.id.lista_eventi);


        ListEventiAdapter listEventiAdapter = new ListEventiAdapter(this, month, this);

        eventiList.setAdapter(listEventiAdapter);

        mRootView.addView(newLayout, 0);
    }

    @Override
    public void addEventToCalendar(HashMap evento, ViewGroup parent) {

        // get startime and endTime
        long startTime = getTime((String)evento.get("day"), (String)evento.get("start"), parent);
        long endTime = getTime((String)evento.get("day"), (String)evento.get("end"), parent);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        intent.putExtra(CalendarContract.Events.TITLE, (String)evento.get("title"));
        intent.putExtra(CalendarContract.Events.DESCRIPTION, (String)evento.get("description"));
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, (String)((String) evento.get("title")).replace("OPEN-DAY", ""));
        startActivity(intent);
    }

    @Override
    public void showEventDetail(HashMap evento, ViewGroup parent) {
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(this);
        final View main_view = LayoutInflater.from(this).inflate(R.layout.calendar_event_dialog, null);
        dialogBuilder.setView(main_view);
        final Dialog dialog = dialogBuilder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView nomeEvento = (TextView) main_view.findViewById(R.id.nome_evento);
        nomeEvento.setText((String)evento.get("title"));
        TextView dataEvento = (TextView) main_view.findViewById(R.id.data);
        String data = (String)evento.get("day") + " ";
        data +=  ((TextView)((LinearLayout)parent.getParent()).findViewById(R.id.nome_mese)).getText().toString();
        dataEvento.setText(data);
        TextView oraText = (TextView)main_view.findViewById(R.id.ora);
        oraText.setText((String)evento.get("start") + " - " + (String)evento.get("end"));
        TextView descrizione = (TextView)main_view.findViewById(R.id.descrizione);
        descrizione.setText((String)evento.get("description"));
        AppCompatButton okButton = (AppCompatButton)main_view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private long getTime(String day, String hour, ViewGroup parent){
        String nomeMese = ((TextView)((LinearLayout)parent.getParent()).findViewById(R.id.nome_mese)).getText().toString();
        nomeMese = nomeMese.replace(" 2017", "");
        int meseIndex = -1;

        for(int i = 0; i < mMonthList.size(); i++){
            if(mMonthList.get(i).getNomeMese().equals(nomeMese)){
                meseIndex = mMonthList.get(i).getPosition();
                break;
            }
        }
        String input = String.format("%02d", Integer.parseInt(day)) + "-" + String.format("%02d", meseIndex) + "-2017 " + hour;
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = null;
        try {
            date = inFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date.getTime();
    }

    public ArrayList getMonthList(){
        return this.mMonthList;
    }

    class MonthComparator implements Comparator<CalendarModel> {
        @Override
        public int compare(CalendarModel a, CalendarModel b) {
            return a.getPosition() > b.getPosition() ? -1 : a.getPosition() == b.getPosition() ? 0 : 1;
        }
    }
}
