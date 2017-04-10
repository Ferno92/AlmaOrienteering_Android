package com.almaorient.ferno92.almaorienteering;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class EmbedBrowser extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //final Uri uri = Uri.parse(url);
                view.loadUrl(url);
                return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embed_browser);

        String url = getIntent().getExtras().getString("url");

        final WebView embeddedwebview = (WebView) findViewById(R.id.embedwebview);
        WebSettings webviewsettings = embeddedwebview.getSettings();
        webviewsettings.setJavaScriptEnabled(true);
        embeddedwebview.loadUrl(url);
        embeddedwebview.setWebViewClient(new MyWebViewClient());

        final ProgressBar Pbar;
        Pbar = (ProgressBar) findViewById(R.id.progressBar2);
        //Pbar.getProgressDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        final LinearLayout progressbarlayout = (LinearLayout) findViewById(R.id.layoutprogressbar);
//        Pbar.setProgressTintList(ColorStateList.valueOf(Color.WHITE));

//        embeddedwebview.getSettings().setSupportZoom(true);
//
//        embeddedwebview.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                String javascript="javascript:document.getElementsByName('viewport')[0].setAttribute('content', 'initial-scale=1.0,maximum-scale=10.0');";
//                view.loadUrl(javascript);
//            }
//        });



        embeddedwebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
                    progressbarlayout.setVisibility(View.VISIBLE);
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                }

                Pbar.setProgress(progress);
                if(progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                    progressbarlayout.setVisibility(View.GONE);
                }
            }
        });


        ImageButton closbtn = (ImageButton) findViewById(R.id.closebutton);

        closbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final TextView title = (TextView) findViewById(R.id.pagetitle);
        final TextView urlcorrente = (TextView) findViewById(R.id.urlcorrente);

        embeddedwebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                title.setText(view.getTitle());
                urlcorrente.setText(embeddedwebview.getUrl());
            }
        });

        ImageButton share = (ImageButton) findViewById(R.id.sharebtn);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Condiviso da AlmaOrienteering");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, embeddedwebview.getUrl());
                startActivity(Intent.createChooser(sharingIntent, "Condividi con"));
            }
        });

        final ImageButton menubutton = (ImageButton) findViewById(R.id.menubtn);
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(EmbedBrowser.this, menubutton);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.browser_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.external:
                                Intent externalbrowser = new Intent(Intent.ACTION_VIEW);
                                externalbrowser.setData(Uri.parse(embeddedwebview.getUrl()));
                                startActivity(externalbrowser);
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        });

    }

    @Override
    public void onBackPressed() {
        WebView embeddedwebview = (WebView) findViewById(R.id.embedwebview);
        if (embeddedwebview.canGoBack()) {
            embeddedwebview.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
