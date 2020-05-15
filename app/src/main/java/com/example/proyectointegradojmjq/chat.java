package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class chat extends AppCompatActivity implements View.OnClickListener {

    EditText txtEnviar;
    EditText txtRecibir;
    Button btnEnviar;
    Button btnRecibir;
    SharedPreferences sharedPref;

    String idReceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        txtEnviar = findViewById(R.id.txtEnviar);
        txtRecibir = findViewById(R.id.txtRecibir);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnRecibir = findViewById(R.id.btnRecibir);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        idReceptor = intent.getStringExtra("idReceptor");

        btnEnviar.setOnClickListener(this);
        btnRecibir.setOnClickListener(this);

        //recibirMensaje();

        Log.println(Log.ASSERT, "hsha", "sisisisi");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnviar:

                enviarMensaje();
                break;

            case R.id.btnRecibir:
                recibirMensaje();
                break;
        }
    }

    //Metodo para enviar mensajes
    public void enviarMensaje() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    String respuesta = "";
                    HashMap<String, String> postDataParams = new HashMap<String, String>();
                    postDataParams.put("mensaje", txtEnviar.getText().toString());
                    postDataParams.put("idEmisor", sharedPref.getString("idUsuario", ""));
                    postDataParams.put("idReceptorFK", idReceptor);

                    URL url3 = new URL("http://www.teamchaterinos.com/pruebachat.php");
                    HttpURLConnection connection = (HttpURLConnection) url3.openConnection();
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        while ((line = br.readLine()) != null) {
                            respuesta += line;
                        }
                    } else {
                        respuesta = "";
                    }
                    connection.getResponseCode();

                    if (connection.getResponseCode() == 200) {

                        Log.println(Log.ASSERT, "Mensaje Enviado", respuesta);
                        connection.disconnect();


                    } else {
                        Log.println(Log.ASSERT, "Error", "Error");
                    }
                } catch (Exception e) {
                    Log.println(Log.ASSERT, "Excepción", e.getMessage());
                }
            }
        });
    }

    //Metodo para recibir mensajes
    public void recibirMensaje() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //while (true) {
                    try {
                        URL url = null;
                        try {
                            url = new URL("http://www.teamchaterinos.com/pruebachat.php?idEmisor=" + idReceptor +
                                    "&idReceptorFK=" + sharedPref.getString("idUsuario", "") + "");
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


                                final JSONArray jsonArray = new JSONArray(respuesta);


                                final JSONObject jsonObject = jsonArray.getJSONObject(0);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String s = "";
                                        try {
                                            for(int i=0; i<jsonArray.length(); i++) {
                                               s += jsonObject.getString("mensaje");
                                               txtRecibir.setText(s);
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                });


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
            //}
        });

    }


    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
