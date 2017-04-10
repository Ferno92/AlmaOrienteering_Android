package com.almaorient.unibo.almaorienteering;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Marco on 23/03/2017.
 */

public class ModusActivity extends BaseActivity {
    EditText mNomeStudente;
    EditText mOggetto;
    EditText mBody;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modus_activity);

        mNomeStudente = (EditText)findViewById(R.id.nome_studente);
        mOggetto = (EditText) findViewById(R.id.oggetto);
        mBody = (EditText) findViewById(R.id.body_email);

        AppCompatButton sendEmail = (AppCompatButton)findViewById(R.id.send_email);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@modusweb.org"});
                i.putExtra(Intent.EXTRA_SUBJECT, mOggetto.getText().toString());
                i.putExtra(Intent.EXTRA_TEXT   , mBody.getText().toString() + "\n\n" + mNomeStudente.getText().toString());
                try {
                    startActivity(Intent.createChooser(i, "Invia email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ModusActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
