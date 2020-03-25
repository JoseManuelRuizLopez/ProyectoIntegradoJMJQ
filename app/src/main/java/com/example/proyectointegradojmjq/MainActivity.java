package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView logoApp;
    TextView txt;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoApp = findViewById(R.id.logoApp);
        logoApp.setOnClickListener(this);

        txt = findViewById(R.id.lblLogoApp);
        txt.setOnClickListener(this);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.logoApp:

                Boolean logeado = sharedPref.getBoolean("isLogged", false);

                if(logeado == true)
                {
                    Intent intentLogin = new Intent(MainActivity.this, MenuPrincipal.class);
                    startActivity(intentLogin);
                    finish();
                    break;
                }
                else
                {
                    Intent intentLogin = new Intent(MainActivity.this, Login.class);
                    startActivity(intentLogin);
                    finish();
                    break;
                }

            case R.id.lblLogoApp:
                Intent intentNuevo = new Intent(MainActivity.this, CrearUsuario.class);
                startActivity(intentNuevo);
                finish();
                break;
        }
    }
}
