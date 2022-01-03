package com.example.dnevnik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PosetilacActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ArrayList<String> datum, nastavnik, predmet, ocena;
    String ime, prezime, usr, pass;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posetilac);

        Intent intent = getIntent();
        TextView tv = (TextView) findViewById(R.id.textView4);
        usr = intent.getStringExtra("usr");
        pass = intent.getStringExtra("pass");
        recyclerView = findViewById(R.id.recView);
        databaseHelper = new DatabaseHelper(this);
        datum = new ArrayList<>();
        nastavnik = new ArrayList<>();
        predmet = new ArrayList<>();
        ocena = new ArrayList<>();

        tv.setText(usr);

        String[] split = usr.split("\\s+");
        ime = split[0];
        prezime = split[1];

        upisiVrednosti();

        customAdapter = new CustomAdapter(PosetilacActivity.this, datum, nastavnik, predmet, ocena);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PosetilacActivity.this));
    }
    void upisiVrednosti(){
        Cursor cursor = databaseHelper.ispisiOcene(ime, prezime, pass);
        if(cursor.getCount()==0){
            Toast.makeText(PosetilacActivity.this, "Nema ocena za prikaz", Toast.LENGTH_LONG).show();
        } else{
            while (cursor.moveToNext()){
                datum.add(cursor.getString(0));
                nastavnik.add(cursor.getString(12)+" "+cursor.getString(13));
                predmet.add(cursor.getString(6));
                ocena.add(cursor.getString(3));
            }
        }
    }
}