package com.example.dnevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NastavnikActivity extends AppCompatActivity {

    String ime, prezime, usr, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nastavnik);

        Intent intent = getIntent();
        usr = intent.getStringExtra("usr");
        pass = intent.getStringExtra("pass");

        Button ucenici = (Button) findViewById(R.id.btnUcenici);
        Button ocene = (Button) findViewById(R.id.btnOcene);

        String[] split = usr.split("\\s+");
        ime = split[0];
        prezime = split[1];

        ucenici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NastavnikActivity.this, UceniciActivity.class);
                startActivity(intent);
            }
        });

        ocene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NastavnikActivity.this, OceneActivity.class);
                intent.putExtra("ime", ime);
                intent.putExtra("prezime", prezime);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });
    }
}