package com.almaorient.ferno92.almaorienteering;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.almaorient.ferno92.almaorienteering.ElencoScuole.ExpandableListAdapter1;
import com.almaorient.ferno92.almaorienteering.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.btn_minus;
import static android.R.drawable.btn_plus;
import static com.almaorient.ferno92.almaorienteering.R.id.pin;
import static com.almaorient.ferno92.almaorienteering.R.id.plusdue;
import static com.almaorient.ferno92.almaorienteering.R.id.plusquattro;
import static com.almaorient.ferno92.almaorienteering.R.id.plustre;
import static com.almaorient.ferno92.almaorienteering.R.id.plusuno;
import static com.almaorient.ferno92.almaorienteering.R.id.ulterioriinfo4;
import static com.almaorient.ferno92.almaorienteering.R.id.webview2;

public class InfoGeneraliActivity extends BaseActivity {

    //////BROWSER
    private void richiamoBrowser(String url) {
        Intent browser = new Intent(this, EmbedBrowser.class);
        browser.putExtra("url", url);
        startActivity(browser);
    }

    private void onPressButton(RelativeLayout relativeLayout, final ImageView imageView, final WebView webView){
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.getVisibility() == View.GONE)
                {
                    webView.setVisibility(view.VISIBLE);
                    imageView.setImageResource(R.drawable.ic_expand_less);

                } else {
                    webView.setVisibility(view.GONE);
                    imageView.setImageResource(R.drawable.ic_expand_more);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_generali);
        setTitle("Come funziona?");

///Contatti
        final WebView unoWebView = (WebView) findViewById(R.id.webview1);
        unoWebView.loadData(getString(R.string.contatti), "text/html; charset=utf-8", "utf-8");
        final ImageView primoplusImageButton = (ImageView) findViewById(plusuno);
        RelativeLayout relativeLayout1=(RelativeLayout) findViewById(R.id.relativelayout1);
        onPressButton(relativeLayout1,primoplusImageButton,unoWebView);

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

        final WebView treWebView = (WebView) findViewById(R.id.webview3);
        treWebView.loadData(getString(R.string.multicampus), "text/html; charset=utf-8", "utf-8");
        final ImageView terzoplusImageButton = (ImageView) findViewById(plustre);
        RelativeLayout relativeLayout3=(RelativeLayout) findViewById(R.id.relativelayout3);
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (treWebView.getVisibility() == View.GONE)
                {
                    treWebView.setVisibility(view.VISIBLE);
                    terzoplusImageButton.setImageResource(R.drawable.ic_expand_less);

                } else {
                    treWebView.setVisibility(view.GONE);
                    terzoplusImageButton.setImageResource(R.drawable.ic_expand_more);
                }

            }

        });

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
    }

}
