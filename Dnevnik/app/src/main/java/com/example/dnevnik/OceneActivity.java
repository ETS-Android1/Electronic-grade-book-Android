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
import android.widget.TextView;
import android.widget.Toast;

public class OceneActivity extends AppCompatActivity {

    private Button dodaj, azuriraj, prikaziPredmete, prikaziUcenike, prikaziOcene;
    DatabaseHelper databaseHelper;
    private EditText txtPredmetID, txtUcenikID, txtDatum, txtOcena;
    String ime, prezime, pass, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocene);

        Intent intent = getIntent();
        ime = intent.getStringExtra("ime");
        prezime = intent.getStringExtra("prezime");
        pass = intent.getStringExtra("pass");
        databaseHelper = new DatabaseHelper(this);
        dodaj = findViewById(R.id.btnOceneDodaj);
        azuriraj = findViewById(R.id.btnOceneAzuriraj);
        prikaziPredmete = findViewById(R.id.btnOceneListaPredmeta);
        prikaziUcenike = findViewById(R.id.btnOceneListaUcenika);
        prikaziOcene = findViewById(R.id.btnOceneListaOcena);
        txtPredmetID = findViewById(R.id.txtOcenePredmetID);
        txtUcenikID = findViewById(R.id.txtOceneUcenikID);
        txtDatum = findViewById(R.id.txtOceneDatum);
        txtOcena = findViewById(R.id.txtOceneOcena);

        nadjiID();

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(provera()){
                    Cursor cursor = databaseHelper.nadjiMojPredmet(Integer.parseInt(txtPredmetID.getText().toString().trim()), id);
                    Cursor cursor2 = databaseHelper.nadjiUcenika(Integer.parseInt(txtUcenikID.getText().toString().trim()));
                    if(cursor.getCount() > 0 && cursor2.getCount() > 0) {
                        boolean oo = databaseHelper.novaOcena(txtPredmetID.getText().toString().trim(), txtUcenikID.getText().toString().trim(), txtDatum.getText().toString().trim(), txtOcena.getText().toString().trim());
                        if (oo) {
                            Toast.makeText(OceneActivity.this, "Uspešno registrovana ocena", Toast.LENGTH_LONG).show();
                            clear();
                        } else {
                            Toast.makeText(OceneActivity.this, "Učenik već ima ocenu iz izabranog predmeta! Možete je ažurirati", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(OceneActivity.this, "Uneti su pogrešni ID!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        azuriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(provera()){
                    Cursor dataPredmet = databaseHelper.nadjiMojPredmet(Integer.parseInt(txtPredmetID.getText().toString().trim()), id);
                    Cursor dataUcenik = databaseHelper.nadjiUcenika(Integer.parseInt(txtUcenikID.getText().toString().trim()));
                    Cursor dataOcena = databaseHelper.nadjiOcenu(Integer.parseInt(txtPredmetID.getText().toString().trim()), Integer.parseInt(txtUcenikID.getText().toString().trim()));
                    if(dataPredmet.getCount() > 0 && dataUcenik.getCount() > 0 && dataOcena.getCount() > 0) {
                        databaseHelper.updateOcena(Integer.parseInt(txtPredmetID.getText().toString().trim()), Integer.parseInt(txtUcenikID.getText().toString().trim()),
                                txtDatum.getText().toString().trim(), txtOcena.getText().toString().trim());
                        Toast.makeText(OceneActivity.this,"Uspešno ažurirana ocena", Toast.LENGTH_LONG).show();
                        clear();
                    } else {
                        Toast.makeText(OceneActivity.this,"Uneti su pogrešni ID!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        prikaziPredmete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.getMojiPredmetiData(id);
                if (cursor.getCount() == 0){
                    Toast.makeText(OceneActivity.this, "Nema podataka o predmetima", Toast.LENGTH_SHORT).show();
                }
                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()){
                    builder.append("ID: ").append(cursor.getString(0)).append("\n");
                    builder.append("Predmet: ").append(cursor.getString(2)).append("\n\n");
                }
                showMessage("Spisak predmeta", builder.toString());
            }
        });

        prikaziUcenike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.getUcenikData();
                if (cursor.getCount() == 0){
                    Toast.makeText(OceneActivity.this, "Nema podataka o učenicima", Toast.LENGTH_SHORT).show();
                }
                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()){
                    builder.append("ID: ").append(cursor.getString(0)).append("\n");
                    builder.append("Učenik: ").append(cursor.getString(1)).append(" ");
                    builder.append(cursor.getString(2)).append("\n\n");

                }
                showMessage2("Spisak učenika", builder.toString());
            }
        });

        prikaziOcene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.getOceneData(id);
                if (cursor.getCount() == 0){
                    Toast.makeText(OceneActivity.this, "Nema podataka o ocenama", Toast.LENGTH_SHORT).show();
                }
                StringBuilder builder = new StringBuilder();
                while (cursor.moveToNext()){
                    builder.append("Učenik: ").append(cursor.getString(8)).append(" ");
                    builder.append(cursor.getString(9)).append("\n");
                    builder.append("Predmet: ").append(cursor.getString(6)).append("\n");
                    builder.append("Ocena: ").append(cursor.getString(3)).append("\n");
                    builder.append("Datum: ").append(cursor.getString(0)).append("\n\n");

                }
                showMessage3("Spisak ocena", builder.toString());
            }
        });

        txtPredmetID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    Cursor data = databaseHelper.nadjiMojPredmet(Integer.parseInt(txtPredmetID.getText().toString().trim()), id);
                    if(data.getCount() > 0) {
                        while(data.moveToNext()) {
                            String naziv = data.getString(2);
                            Toast.makeText(OceneActivity.this, "Izabran je predmet "+naziv, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(OceneActivity.this,"Unesite ID vašeg predmeta!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtUcenikID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    Cursor data = databaseHelper.nadjiUcenika(Integer.parseInt(txtUcenikID.getText().toString().trim()));
                    if(data.getCount() > 0) {
                        while(data.moveToNext()) {
                            String ime = data.getString(1);
                            String prezime = data.getString(2);
                            Toast.makeText(OceneActivity.this, "Izabran je učenik "+ime+" "+prezime, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(OceneActivity.this,"Ne postoji učenik sa tim ID", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void showMessage(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(OceneActivity.this);
        dialog.setTitle("Lista predmeta");
        dialog.setMessage(message);
        dialog.show();
    }
    private void showMessage2(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(OceneActivity.this);
        dialog.setTitle("Lista učenika");
        dialog.setMessage(message);
        dialog.show();
    }
    private void showMessage3(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(OceneActivity.this);
        dialog.setTitle("Prikaz ocena");
        dialog.setMessage(message);
        dialog.show();
    }
    private boolean provera() {
        if(txtPredmetID.getText().toString().equals("")) {
            txtPredmetID.setError("Ovo polje je obavezno");
            return false;
        }
        if(txtUcenikID.getText().toString().equals("")) {
            txtUcenikID.setError("Ovo polje je obavezno");
            return false;
        }
        if(txtDatum.getText().toString().equals("")) {
            txtDatum.setError("Ovo polje je obavezno");
            return false;
        }
        if(txtOcena.getText().toString().equals("")) {
            txtOcena.setError("Ovo polje je obavezno");
            return false;
        }
        return true;
    }

    private void clear() {
        txtPredmetID.setText("");
        txtUcenikID.setText("");
        txtDatum.setText("");
        txtOcena.setText("");
    }

    private void nadjiID() {
        Cursor nadjiID = databaseHelper.nadjiIDNastavnika(ime, prezime, pass);
        while (nadjiID.moveToNext()){
            id = nadjiID.getString(0);
        }
    }
}