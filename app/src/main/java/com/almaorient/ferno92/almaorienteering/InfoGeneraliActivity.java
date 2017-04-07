package com.almaorient.ferno92.almaorienteering;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.almaorient.ferno92.almaorienteering.ElencoScuole.ExpandableListAdapter1;
import com.almaorient.ferno92.almaorienteering.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.btn_minus;
import static android.R.drawable.btn_plus;
import static com.almaorient.ferno92.almaorienteering.R.id.pin;
import static com.almaorient.ferno92.almaorienteering.R.id.pluscinque;
import static com.almaorient.ferno92.almaorienteering.R.id.plusdue;
import static com.almaorient.ferno92.almaorienteering.R.id.plusquattro;
import static com.almaorient.ferno92.almaorienteering.R.id.plussei;
import static com.almaorient.ferno92.almaorienteering.R.id.plustre;
import static com.almaorient.ferno92.almaorienteering.R.id.plusuno;
import static com.almaorient.ferno92.almaorienteering.R.id.textView;
import static com.almaorient.ferno92.almaorienteering.R.id.ulterioriinfo4;
import static com.almaorient.ferno92.almaorienteering.R.id.webview2;

public class InfoGeneraliActivity extends BaseActivity {

    //////BROWSER
    private void richiamoBrowser(String url) {
        Intent browser = new Intent(this, EmbedBrowser.class);
        browser.putExtra("url", url);
        startActivity(browser);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView, ListAdapter listAdapter) {
        //ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
//            TextView textView = (TextView) listView.findViewById(R.id.lblListItem);
//            listAdapter.getView(i,null,listView);
            View listItem = listAdapter.getView(i,null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_generali);
        setTitle("Informazioni");

///Contatti
        final WebView unoWebView = (WebView) findViewById(R.id.webview1);
        unoWebView.loadData(getString(R.string.contatti), "text/html; charset=utf-8", "utf-8");
        final ImageView primoplusImageButton = (ImageView) findViewById(plusuno);
        RelativeLayout relativeLayout1=(RelativeLayout) findViewById(R.id.relativelayout1);
        final Button facebook = (Button) findViewById(R.id.facebookbtn);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richiamoBrowser("https://www.facebook.com/unibo.it/?fref=ts");
            }
        });

        relativeLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (unoWebView.getVisibility() == View.GONE)
                    {
                        unoWebView.setVisibility(view.VISIBLE);
                        primoplusImageButton.setImageResource(R.drawable.ic_expand_less);
                        facebook.setVisibility(View.VISIBLE);

                    } else {
                        unoWebView.setVisibility(view.GONE);
                        primoplusImageButton.setImageResource(R.drawable.ic_expand_more);
                        facebook.setVisibility(View.GONE);
                    }
                }
        });



//Internazionale

        final Button ulterioriinfo2Button = (Button) findViewById(R.id.ulterioriinfo2);
        ulterioriinfo2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               richiamoBrowser("http://www.unibo.it/it/internazionale/studiare-all-estero");
            }
        });
        final WebView dueWebView = (WebView) findViewById(R.id.webview2);
        dueWebView.loadData(getString(R.string.internazionale), "text/html; charset=utf-8", "utf-8");
        final ImageView secondoplusImageButton = (ImageView) findViewById(plusdue);
        RelativeLayout relativeLayout2=(RelativeLayout) findViewById(R.id.relativelayout2);
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dueWebView.getVisibility() == View.GONE)
                {
                    dueWebView.setVisibility(view.VISIBLE);
                    ulterioriinfo2Button.setVisibility(view.VISIBLE);
                    secondoplusImageButton.setImageResource(R.drawable.ic_expand_less);

                } else {
                    dueWebView.setVisibility(view.GONE);
                    ulterioriinfo2Button.setVisibility(view.GONE);
                    secondoplusImageButton.setImageResource(R.drawable.ic_expand_more);
                }

            }

        });

// Multicampus

        final WebView treWebView = (WebView) findViewById(R.id.webview3);
        treWebView.loadData(getString(R.string.Multicampus), "text/html; charset=utf-8", "utf-8");
        final ImageView terzoplusImageButton = (ImageView) findViewById(plustre);
        final ListView listview3 = (ListView) findViewById(R.id.listview3);
        ArrayList list3 = new ArrayList<String>();
        list3.add("Campus di Bologna");
        list3.add("Campus di Forlì");
        list3.add("Campus di Cesena");
        list3.add("Campus di Rimini");
        list3.add("Campus di Ravenna");

        ArrayAdapter adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list3);

        listview3.setAdapter(adapter3);

        setListViewHeightBasedOnChildren(listview3,adapter3);

        listview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        richiamoBrowser("http://www.unibo.it/it");
                        break;
                    case 1:
                        richiamoBrowser("http://www.unibo.it/it/campus-forli");
                        break;
                    case 2:
                        richiamoBrowser("http://www.unibo.it/it/campus-cesena");
                        break;
                    case 3:
                        richiamoBrowser("http://www.unibo.it/it/campus-rimini");
                        break;
                    case 4:
                        richiamoBrowser("http://www.unibo.it/it/campus-ravenna");
                        break;
                }
            }
        });

        RelativeLayout relativeLayout3=(RelativeLayout) findViewById(R.id.relativelayout3);
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (treWebView.getVisibility() == View.GONE)
                {
                    treWebView.setVisibility(view.VISIBLE);
                    listview3.setVisibility(view.VISIBLE);
                    terzoplusImageButton.setImageResource(R.drawable.ic_expand_less);

                } else {
                    treWebView.setVisibility(view.GONE);
                    listview3.setVisibility(view.GONE);
                    terzoplusImageButton.setImageResource(R.drawable.ic_expand_more);
                }

            }

        });

//Sistema Universitario

        final WebView quattroWebView = (WebView) findViewById(R.id.webview4);
        quattroWebView.loadData(getString(R.string.sistemauniv), "text/html; charset=utf-8", "utf-8");
        final ImageView schemaImageView = (ImageView) findViewById(R.id.schema);
        final WebView quattro4WebView = (WebView) findViewById(R.id.webview44);
        final Button ulterioriinfo4Button = (Button) findViewById(R.id.ulterioriinfo4);
        RelativeLayout relativeLayout4=(RelativeLayout) findViewById(R.id.relativelayout4);


        ulterioriinfo4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                richiamoBrowser("http://www.unibo.it/it/didattica/iscrizioni-trasferimenti-e-laurea/il-sistema-universitario/il-sistema-universitario");

            }
        });


        quattro4WebView.loadData(getString(R.string.sistemauniv2), "text/html; charset=utf-8", "utf-8");
        final ImageView quartoplusImageButton = (ImageView) findViewById(plusquattro);
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quattroWebView.getVisibility() == View.GONE)
                {
                    quattroWebView.setVisibility(view.VISIBLE);
                    quattro4WebView.setVisibility(view.VISIBLE);
                    schemaImageView.setVisibility(View.VISIBLE);
                    ulterioriinfo4Button.setVisibility(view.VISIBLE);
                    quartoplusImageButton.setImageResource(R.drawable.ic_expand_less);


                } else {
                    quattroWebView.setVisibility(view.GONE);
                    quattro4WebView.setVisibility(view.GONE);
                    schemaImageView.setVisibility(View.GONE);
                    ulterioriinfo4Button.setVisibility(view.GONE);
                    quartoplusImageButton.setImageResource(R.drawable.ic_expand_more);
                }

            }

        });

//Scuole e dipartimenti

        final WebView cinque = (WebView) findViewById(R.id.webview5);
        cinque.loadData(getString(R.string.Scuole), "text/html; charset=utf-8", "utf-8");
        final ImageView quintoplusImageButton = (ImageView) findViewById(pluscinque);
        final ListView listview5 = (ListView) findViewById(R.id.listview5);
        ArrayList list5 = new ArrayList<String>();
        list5.add("Scuole");
        list5.add("Dipartimenti");
        list5.add("Offerta Formativa");

        ArrayAdapter adapter5 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list5);

        listview5.setAdapter(adapter5);

        setListViewHeightBasedOnChildren(listview5,adapter5);

        listview5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        richiamoBrowser("http://www.unibo.it/it/ateneo/sedi-e-strutture/scuole");
                        break;
                    case 1:
                        richiamoBrowser("http://www.unibo.it/it/ateneo/sedi-e-strutture/dipartimenti");
                        break;
                    case 2:
                        Intent intent = new Intent(InfoGeneraliActivity.this, FilterCorsoActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });

        RelativeLayout relativeLayout5=(RelativeLayout) findViewById(R.id.relativelayout5);
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cinque.getVisibility() == View.GONE)
                {
                    cinque.setVisibility(view.VISIBLE);
                    listview5.setVisibility(view.VISIBLE);
                    quintoplusImageButton.setImageResource(R.drawable.ic_expand_less);

                } else {
                    cinque.setVisibility(view.GONE);
                    listview5.setVisibility(view.GONE);
                    quintoplusImageButton.setImageResource(R.drawable.ic_expand_more);
                }

            }

        });

//Servizi

        final ImageView sestoplusImageButton = (ImageView) findViewById(plussei);
        final ListView listview6 = (ListView) findViewById(R.id.listview6);
        ArrayList list6 = new ArrayList<String>();
        list6.add("CLA: Centro Linguistico di Ateneo");
        list6.add("Sistema Bibliotecario di Ateneo");
        list6.add("Borse di studio e agevolazioni");

        ArrayAdapter adapter6 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list6);

        listview6.setAdapter(adapter6);

        setListViewHeightBasedOnChildren(listview6,adapter6);

        listview6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        richiamoBrowser("http://www.cla.unibo.it/");
                        break;
                    case 1:
                        richiamoBrowser("http://www.sba.unibo.it/it");
                        break;
                    case 2:
                        richiamoBrowser("http://www.unibo.it/it/servizi-e-opportunita/borse-di-studio-e-agevolazioni/borse-di-studio-e-agevolazioni\n");
                        break;

                }
            }
        });

        RelativeLayout relativeLayout6=(RelativeLayout) findViewById(R.id.relativelayout6);
        relativeLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listview6.getVisibility() == View.GONE)
                {
                    listview6.setVisibility(view.VISIBLE);
                    sestoplusImageButton.setImageResource(R.drawable.ic_expand_less);

                } else {
                    listview6.setVisibility(view.GONE);
                    sestoplusImageButton.setImageResource(R.drawable.ic_expand_more);
                }

            }

        });
    }

}
