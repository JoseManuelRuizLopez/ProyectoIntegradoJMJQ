package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectointegradojmjq.ui.MiPerfil.FragmentoMiPerfil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView logoApp;
    TextView txt;

    SharedPreferences sharedPref;

    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoApp = findViewById(R.id.logoApp);

        txt = findViewById(R.id.lblLogoApp);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);

        idUser = sharedPref.getString("idUsuario", "0");


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                //boolean estaNull = comprobarNull();

                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Boolean logeado = sharedPref.getBoolean("isLogged", false);
                String registroCompletado = sharedPref.getString("registroCompletado", "");

                if (logeado == true)
                {

                    Intent intentMenuPrincipal = new Intent(MainActivity.this, MenuPrincipalApp.class);
                    startActivity(intentMenuPrincipal);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();


                }
                else if (logeado == false)
                {

                    if (registroCompletado.equals("0"))
                    {
                        Intent intentBienvenida = new Intent(MainActivity.this, BienvenidaUsuario.class);
                        startActivity(intentBienvenida);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                    else
                        {
                            Intent intentLogin = new Intent(MainActivity.this, Login.class);
                            startActivity(intentLogin);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                }
            }
        });
    }
}
