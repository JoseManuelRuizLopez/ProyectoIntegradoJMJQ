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
    String fechaNacimientoUsuario;
    StringBuilder responseStrBuilder;

    boolean isNull = false;

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

                if (logeado == true) {

                    Intent intentMenuPrincipal = new Intent(MainActivity.this, MenuPrincipalApp.class);
                    startActivity(intentMenuPrincipal);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                } else if (logeado == false){
                    Intent intentLogin = new Intent(MainActivity.this, Login.class);
                    startActivity(intentLogin);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();

                }
                /*else if (estaNull)
                {
                    Intent intentBienvenida = new Intent(MainActivity.this, BienvenidaUsuario.class);
                    startActivity(intentBienvenida);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

                 */
            }
        });
    }

    /*public boolean comprobarNull()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String nombreUsuario = sharedPref.getString("nombreUsuario", "");

                    URL url = new URL("http://www.teamchaterinos.com/prueba.php?idUsuario=" + idUser + "&nombreUsuario= " + nombreUsuario);

                    //Create connection
                    HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

                    //Establecer método por defecto GET
                    myConnection.setRequestMethod("GET");

                    if (myConnection.getResponseCode() == 200)
                    {
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                        BufferedReader bR = new BufferedReader(responseBodyReader);
                        String line = "";

                        responseStrBuilder = new StringBuilder();

                        while ((line = bR.readLine()) != null) {
                            responseStrBuilder.append(line);
                        }

                        JSONObject jsonobject = new JSONObject(responseStrBuilder.toString());

                        fechaNacimientoUsuario = jsonobject.getString("mensajeNull");


                        Log.println(Log.ASSERT, "Esta es 0 o 1", fechaNacimientoUsuario + "");
                        responseBody.close();
                        responseBodyReader.close();
                        myConnection.disconnect();

                        if (fechaNacimientoUsuario.equals("0"))
                        {
                            isNull = false;
                        }
                        else
                        {
                            isNull = true;
                        }

                    } else {
                        Log.println(Log.ASSERT, "Error", "Error");
                    }
                } catch (Exception e)
                {
                    Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona. " + e.getMessage());
                }
            }
        });

        return isNull;
    }*/
}
