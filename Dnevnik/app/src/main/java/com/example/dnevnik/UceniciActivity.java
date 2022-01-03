package com.example.dnevnik;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UceniciActivity extends AppCompatActivity {

    private Button dodaj, obrisi, prikazi;
    DatabaseHelper databaseHelper;
    private EditText txtID, txtIme, txtPrezime, txtSifra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucenici);

        databaseHelper = new DatabaseHelper(this);
        dodaj = findViewById(R.id.btnUceniciDodaj);
        obrisi = findViewById(R.id.btnUceniciUkloni);
        prikazi = findViewById(R.id.btnUceniciPrikazi);
        txtID = findViewById(R.id.txtUceniciID);
        txtIme = findViewById(R.id.txtUceniciIme);
        txtPrezime = findViewById(R.id.txtUceniciPrezime);
        txtSifra = findViewById(R.id.txtUceniciSifra);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(provera()){
                    boolean oo = databaseHelper.noviUcenik(txtIme.getText().toString().trim(), txtPrezime.getText().toString().trim(), txtSifra.getText().toString().trim());
                    if(oo) {
                        Toast.makeText(UceniciActivity.this,"Uspešno registrovan učenik", Toast.LENGTH_LONG).show();
                        clear();
                    } else {
                        Toast.makeText(UceniciActivity.this,"Doslo je do greske!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        obrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtID.getText().toString().equals("")){
                    Cursor data = databaseHelper.nadjiUcenika(Integer.parseInt(txtID.getText().toString().trim()));
                    if(data.getCount() > 0) {
                        boolean oo = databaseHelper.obrisiUcenika(txtID.getText().toString().trim());
                        if (oo) {
                            Toast.makeText(UceniciActivity.this,"Uspesno obrisan učenik!", Toast.LENGTH_LONG).show();
                            clear2();
                        } else {
                            Toast.makeText(UceniciActivity.this,"Doslo je do greske!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(UceniciActivity.this,"Ne postoji učenik sa tim ID", Toast.LENGTH_LONG).show();
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
                    Cursor data = databaseHelper.nadjiUcenika(Integer.parseInt(txtID.getText().toString().trim()));
                    if(data.getCount() > 0) {
                        Toast.makeText(UceniciActivity.this,"Pronadjen je učenik", Toast.LENGTH_LONG).show();
                        while(data.moveToNext()){
                            txtIme.setText(data.getString(1));
                            txtPrezime.setText(data.getString(2));
                            txtSifra.setText(data.getString(3));
                        }
                    } else {
                        Toast.makeText(UceniciActivity.this,"Ne postoji učenik sa tim ID", Toast.LENGTH_LONG).show();
                        clear();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        prikazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.getUcenikData();
                if (cursor.getCount() == 0){
                    Toast.makeText(UceniciActivity.this, "Nema podataka o učenicima", Toast.LENGTH_SHORT).show();
                }
                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()){
                    builder.append("ID: ").append(cursor.getString(0)).append("\n");
                    builder.append("Učenik: ").append(cursor.getString(1)).append(" ");
                    builder.append(cursor.getString(2)).append("\n");
                    builder.append("Šifra: ").append(cursor.getString(3)).append("\n\n");

                }
                showMessage("Spisak učenika", builder.toString());
            }
        });

    }
    private void showMessage(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(UceniciActivity.this);
        dialog.setTitle("Lista učenika");
        dialog.setMessage(message);
        dialog.show();
    }
    private boolean provera() {
        if(txtIme.getText().toString().equals("")) {
            txtIme.setError("Ovo polje je obavezno");
            return false;
        }
        if(txtPrezime.getText().toString().equals("")) {
            txtPrezime.setError("Ovo polje je obavezno");
            return false;
        }
        if(txtSifra.getText().toString().equals("")) {
            txtSifra.setError("Ovo polje je obavezno");
            return false;
        }
        return true;
    }

    private void clear() {
        txtIme.setText("");
        txtPrezime.setText("");
        txtSifra.setText("");
    }
    private void clear2() {
        txtID.setText("");
        txtIme.setText("");
        txtPrezime.setText("");
        txtSifra.setText("");
    }

}

