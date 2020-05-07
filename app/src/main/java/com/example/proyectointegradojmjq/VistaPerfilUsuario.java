package com.example.proyectointegradojmjq;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VistaPerfilUsuario extends AppCompatActivity implements View.OnClickListener {

    TextView lblNombreVP;
    TextView lblEdadVP;
    TextView lblGeneroVP;
    TextView lblEstadoCivilVP;
    TextView lblAlturaVP;
    TextView lblDescripcionVP;

    ImageView imgPerfilVP;

    Button btnChatearVP;
    Button btnCancelarVP;

    String nombreUsuario;
    String nombreRecibido;
    String edadRecibida;
    String imgUrlRecibida;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_perfil_usuario);

        lblNombreVP = findViewById(R.id.lblNombreVP);
        lblEdadVP = findViewById(R.id.lblEdadVP);
        lblEstadoCivilVP = findViewById(R.id.lblEstadoCivilVP);
        lblGeneroVP = findViewById(R.id.lblGeneroVP);
        lblAlturaVP = findViewById(R.id.lblAlturaVP);
        lblDescripcionVP = findViewById(R.id.lblDescripcionVP);

        imgPerfilVP = findViewById(R.id.imgPerfilVP);
        btnChatearVP = findViewById(R.id.btnChatearVP);
        btnCancelarVP = findViewById(R.id.btnCancelarVP);

        btnChatearVP.setOnClickListener(this);
        btnCancelarVP.setOnClickListener(this);

        Intent intent = getIntent();
        nombreUsuario = intent.getStringExtra("nombreUsuario");
        nombreRecibido = intent.getStringExtra("nombre");
        edadRecibida = intent.getStringExtra("edad");
        imgUrlRecibida = intent.getStringExtra("image");

        lblNombreVP.setText(nombreRecibido);
        lblEdadVP.setText(edadRecibida + " años");
        Picasso.with(this).invalidate(imgUrlRecibida);
        Picasso.with(this).load(imgUrlRecibida).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(imgPerfilVP);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = null;
                    try {
                        url = new URL("http://www.teamchaterinos.com/prueba.php?nombreUsuario=" + nombreUsuario + "&nombreRealUsuario=" + nombreRecibido + "");
                        HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
                        myConnection.setRequestMethod("GET");
                        if (myConnection.getResponseCode() == 200) {
                            InputStream responseBody = myConnection.getInputStream();
                            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                            BufferedReader bR = new BufferedReader(responseBodyReader);
                            String line = "";
                            String respuesta = "";
                            while ((line = bR.readLine()) != null) {
                                respuesta += line;
                            }

                            JSONArray jsonArray = new JSONArray(respuesta);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            lblGeneroVP.setText(jsonObject.getString("generoUsuario"));
                            lblAlturaVP.setText(jsonObject.getString("alturaUsuario") + " cm");
                            lblEstadoCivilVP.setText(jsonObject.getString("estadoCivilUsuario"));
                            lblDescripcionVP.setText(jsonObject.getString("descripcionUsuario"));

                            responseBody.close();
                            responseBodyReader.close();
                            myConnection.disconnect();

                           // Log.println(Log.ASSERT, "sas", "sos");
                            //Log.println(Log.ASSERT, "Resultado", lblNombreVP.getText().toString());

                        } else {
                            Log.println(Log.ASSERT, "Error", "Error");
                        }

                    } catch (Exception e) {
                        Log.println(Log.ASSERT, "Error", e.getMessage());
                    }
                } catch (Exception e) {
                    Log.println(Log.ASSERT, "Error", "Error2");
                }
            }
        });


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //enable back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChatearVP:


            case R.id.btnCancelarVP:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
