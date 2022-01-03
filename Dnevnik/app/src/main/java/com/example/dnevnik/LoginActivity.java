package com.example.dnevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button nastavnik = (Button) findViewById(R.id.btnNastavnink);
        Button posetilac = (Button) findViewById(R.id.btnPosetilac);
        ImageButton ib = (ImageButton) findViewById(R.id.btnAdmin);
        EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
        EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        DatabaseHelper db = new DatabaseHelper(this);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                if(username.equals("admin") && password.equals("admin")){
                    Toast.makeText(LoginActivity.this,"Dobro došao administratore!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            }
        });

        nastavnik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                try{
                    if(username.length() > 0 && password.length() >0)
                    {
                        DatabaseHelper dbUser = new DatabaseHelper(LoginActivity.this);
                        db.getNastavnikData();

                        if(db.LoginNastavnik(username, password))
                        {
                            Toast.makeText(LoginActivity.this,"Uspešno ste se ulogovali", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, NastavnikActivity.class);
                            intent.putExtra("usr", username);
                            intent.putExtra("pass", password);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,"Pogrešan Username ili Password", Toast.LENGTH_LONG).show();
                        }
                        dbUser.close();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Polja ne smeju biti prazna", Toast.LENGTH_LONG).show();
                    }

                }catch(Exception e)
                {
                    Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }


        });

        posetilac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                try{
                    if(username.length() > 0 && password.length() >0)
                    {
                        DatabaseHelper dbUser = new DatabaseHelper(LoginActivity.this);
                        db.getUcenikData();

                        if(db.LoginUcenik(username, password))
                        {
                            Toast.makeText(LoginActivity.this,"Uspešno ste se ulogovali", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, PosetilacActivity.class);
                            intent.putExtra("usr", username);
                            intent.putExtra("pass", password);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this,"Pogrešan Username ili Password", Toast.LENGTH_LONG).show();
                        }
                        dbUser.close();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Polja ne smeju biti prazna", Toast.LENGTH_LONG).show();
                    }

                }catch(Exception e)
                {
                    Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}