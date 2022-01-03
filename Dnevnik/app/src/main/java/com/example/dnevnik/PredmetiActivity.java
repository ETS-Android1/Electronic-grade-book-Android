package com.example.dnevnik;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PredmetiActivity extends AppCompatActivity {

    private Button dodaj, obrisi, prikaziNastavnike, prikaziPredmete;
    DatabaseHelper databaseHelper;
    private EditText txtID, txtNastavnikID, txtNaziv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predmeti);

        databaseHelper = new DatabaseHelper(this);
        dodaj = findViewById(R.id.btnPredmetiDodaj);
        obrisi = findViewById(R.id.btnPredmetiUkloni);
        prikaziNastavnike = findViewById(R.id.btnPredmetiNastLista);
        prikaziPredmete = findViewById(R.id.btnPredmetiLista);
        txtID = findViewById(R.id.txtPredmetiID);
        txtNastavnikID = findViewById(R.id.txtPredmetiNastID);
        txtNaziv = findViewById(R.id.txtPredmetiNaziv);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(provera()){
                    Cursor cursor = databaseHelper.nadjiNastavnika(Integer.parseInt(txtNastavnikID.getText().toString()));
                    if(cursor.getCount() > 0){
                        boolean oo = databaseHelper.noviPredmet(txtNastavnikID.getText().toString().trim(), txtNaziv.getText().toString().trim());
                        if(oo) {
                            Toast.makeText(PredmetiActivity.this, "Uspešno registrovan predmet", Toast.LENGTH_LONG).show();
                            clear();
                        } else {
                            Toast.makeText(PredmetiActivity.this,"Greška! Već postoji predmet sa istim nazivom!", Toast.LENGTH_LONG).show();
                        }
                    } else{
                        Toast.makeText(PredmetiActivity.this,"Ne postoji nastavnik sa tim ID!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        obrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtID.getText().toString().equals("")){
                    Cursor data = databaseHelper.nadjiPredmet(Integer.parseInt(txtID.getText().toString().trim()));
                    if(data.getCount() > 0) {
                        boolean oo = databaseHelper.obrisiPredmet(txtID.getText().toString().trim());
                        if (oo) {
                            Toast.makeText(PredmetiActivity.this,"Uspesno obrisan predmet!", Toast.LENGTH_LONG).show();
                            clear2();
                        } else {
                            Toast.makeText(PredmetiActivity.this,"Doslo je do greske!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(PredmetiActivity.this,"Ne postoji predmet sa tim ID", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        txtID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    Cursor data = databaseHelper.nadjiPredmet(Integer.parseInt(txtID.getText().toString().trim()));
                    if(data.getCount() > 0) {
                        Toast.makeText(PredmetiActivity.this,"Pronadjen je predmet", Toast.LENGTH_LONG).show();
                        while(data.moveToNext()){
                            txtNastavnikID.setText(data.getString(1));
                            txtNaziv.setText(data.getString(2));
                        }
                    } else {
                        Toast.makeText(PredmetiActivity.this,"Ne postoji predmet sa tim ID", Toast.LENGTH_LONG).show();
                        clear();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtNastavnikID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    Cursor data = databaseHelper.nadjiNastavnika(Integer.parseInt(txtNastavnikID.getText().toString().trim()));
                    if(data.getCount() > 0) {
                        while(data.moveToNext()) {
                            String ime = data.getString(1);
                            String prezime = data.getString(2);
                            Toast.makeText(PredmetiActivity.this, "Izabran je nastavnik "+ime+" "+prezime, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(PredmetiActivity.this,"Ne postoji nastavnik sa tim ID", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        prikaziNastavnike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.getNastavnikData();
                if (cursor.getCount() == 0){
                    Toast.makeText(PredmetiActivity.this, "Nema podataka o nastavnicima", Toast.LENGTH_SHORT).show();
                }
                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()){
                    builder.append("ID: ").append(cursor.getString(0)).append("\n");
                    builder.append("Nastavnik: ").append(cursor.getString(1)).append(" ");
                    builder.append(cursor.getString(2)).append("\n\n");
                }
                showMessage("Spisak nastavnika", builder.toString());
            }
        });

        prikaziPredmete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.getPredmetiData();
                if (cursor.getCount() == 0){
                    Toast.makeText(PredmetiActivity.this, "Nema podataka o predmetima", Toast.LENGTH_SHORT).show();
                }
                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()){
                    builder.append("ID: ").append(cursor.getString(0)).append("\n");
                    builder.append("Predmet: ").append(cursor.getString(2)).append("\n");
                    builder.append("Nastavnik: ").append(cursor.getString(4)).append(" ");
                    builder.append(cursor.getString(5)).append("\n\n");
                }
                showMessage2("Spisak predmeta", builder.toString());
            }
        });
    }

    private void showMessage(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(PredmetiActivity.this);
        dialog.setTitle("Lista nastavnika");
        dialog.setMessage(message);
        dialog.show();
    }
    private void showMessage2(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(PredmetiActivity.this);
        dialog.setTitle("Lista predmeta");
        dialog.setMessage(message);
        dialog.show();
    }
    private boolean provera() {
        if(txtNastavnikID.getText().toString().equals("")) {
            txtNastavnikID.setError("Ovo polje je obavezno");
            return false;
        }
        if(txtNaziv.getText().toString().equals("")) {
            txtNaziv.setError("Ovo polje je obavezno");
            return false;
        }
        return true;
    }
    private void clear() {
        txtNastavnikID.setText("");
        txtNaziv.setText("");
    }
    private void clear2() {
        txtID.setText("");
        txtNastavnikID.setText("");
        txtNaziv.setText("");
    }

}