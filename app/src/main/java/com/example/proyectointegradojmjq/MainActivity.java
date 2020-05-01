package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView logoApp;
    TextView txt;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoApp = findViewById(R.id.logoApp);

        txt = findViewById(R.id.lblLogoApp);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                Boolean logeado = sharedPref.getBoolean("isLogged", false);


                if (logeado == true) {

                    Intent intentMenuPrincipal = new Intent(MainActivity.this, Login.class);
                    startActivity(intentMenuPrincipal);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                } else {
                    Intent intentLogin = new Intent(MainActivity.this, Login.class);
                    startActivity(intentLogin);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                }
            }
        });
    }

}
