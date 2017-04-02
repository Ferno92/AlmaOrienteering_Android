package com.almaorient.ferno92.almaorienteering.homepage;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.almaorient.ferno92.almaorienteering.ChooseActivity;
import com.almaorient.ferno92.almaorienteering.R;
import com.almaorient.ferno92.almaorienteering.login.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.viewpagerindicator.IconPageIndicator;

import java.util.HashMap;
import java.util.Iterator;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class NewMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager mViewPager;
    private HomePagerAdapter mPagerAdapter;
    private FirebaseAuth mAuth;
    private String mUserName;
    private String mUserSurname;
    private String mCorso;
    private String mCorsoId;
    private String mScuola;
    private Toolbar mToolbar;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.vpPager);
        mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), getApplicationContext());

        mPagerAdapter.setNumItems(7);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        if (this.mAuth.getCurrentUser() != null) {
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setVisibility(View.VISIBLE);

            LinearLayout navigationHeader = (LinearLayout) navigationView.getHeaderView(0);
            TextView mailText = (TextView) navigationHeader.findViewById(R.id.logged_user_email);
            mailText.setText(String.valueOf(this.mAuth.getCurrentUser().getEmail()));
            Menu navigationMenu = (Menu) navigationView.getMenu();
            final MenuItem scuolaItem = (MenuItem) navigationMenu.findItem(R.id.nav_share);
            final MenuItem corsoItem = (MenuItem) navigationMenu.findItem(R.id.nav_send);


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            Query query = ref.child("users");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String id = (String) data.child("userId").getValue();
                        if (mAuth.getCurrentUser() != null) {
                            if (id.equals(String.valueOf(mAuth.getCurrentUser().getEmail()))) {
                                mUserName = (String) data.child("nome").getValue();
                                mUserSurname = (String) data.child("cognome").getValue();
                                mScuola = (String) data.child("scuola").getValue();

                                scuolaItem.setTitle((String) data.child("scuola").getValue());
                                HashMap corsoMap = (HashMap) data.child("corso").getValue();
                                Iterator corsoIterator = corsoMap.keySet().iterator();
                                String nomeCorso = "";
                                while (corsoIterator.hasNext()) {
                                    String corsoKey = (String) corsoIterator.next();
                                    switch (corsoKey) {
                                        case "id":
                                            mCorsoId = String.valueOf(corsoMap.get(corsoKey));
                                            break;
                                        case "nome":
                                            nomeCorso = String.valueOf(corsoMap.get(corsoKey));
                                            mCorso = nomeCorso;
                                            break;
                                        default:
                                            break;
                                    }
                                }

                                corsoItem.setTitle(nomeCorso);
                            }

                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if (!mAuth.getCurrentUser().isEmailVerified()) {

                mPagerAdapter.setNumItems(7);
                showMissingVerifyAlert();
            } else {

                mPagerAdapter.setNumItems(8);

            }
        } else {

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        int pager_position = sp.getInt("pager_position", 0);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(pager_position);
//
//        final InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
//        inkPageIndicator.setViewPager(mViewPager);

//Bind the title indicator to the adapter
        IconPageIndicator iconIndicator = (IconPageIndicator)findViewById(R.id.indicator);
        iconIndicator.setViewPager(mViewPager);

    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

                SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("pager_position", mViewPager.getCurrentItem());
                editor.commit();
                super.onBackPressed();
            }
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else
        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (this.mAuth.getCurrentUser() != null) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(NewMainActivity.this, SettingsActivity.class);
            i.putExtra("nome", mUserName);
            i.putExtra("cognome", mUserSurname);
            i.putExtra("corsoId", mCorsoId);
            i.putExtra("corso", mCorso);
            i.putExtra("scuola", mScuola);
            startActivity(i);

            return true;
        } else if (id == R.id.logout) {
            if (this.mAuth.getCurrentUser() != null) {
                this.mAuth.signOut();
                Intent i = new Intent(NewMainActivity.this, ChooseActivity.class);
                i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        } else if (id == R.id.delete_user) {
            this.mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("Delete:", "User account deleted.");
                        Intent i = new Intent(NewMainActivity.this, ChooseActivity.class);
                        i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }


    private void showMissingVerifyAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(NewMainActivity.this);
        String message = "Non hai ancora verificato la tua email, alcune funzionalità dell'app non saranno accessibili fino ad avvenuta conferma! \n ";
        message += "Se invece hai già confermato allora premi Logout e loggati nuovamente per poter accedere ai contenuti esclusivi ;)";
        builder.setTitle("Recupera password");
        builder.setMessage(message);
        builder.setPositiveButton("Invia di nuovo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                mAuth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(NewMainActivity.this, "Email inviata! Controlla la tua casella di posta",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mAuth.signOut();
                Intent i = new Intent(NewMainActivity.this, ChooseActivity.class);
                i.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            }
        });
        builder.show();
    }


}