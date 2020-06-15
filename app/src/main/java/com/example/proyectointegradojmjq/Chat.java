package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;


public class Chat extends AppCompatActivity implements View.OnClickListener, KeyListener {

    SharedPreferences sharedPref;


    String idReceptor;
    String nombre;
    String urlImagen;
    ImageButton btnEnviar;

    TextView txtEnviar;
    TextView txtFecha;
    Handler handler = new Handler();
    Runnable refresh;

    MessageAdapter messageAdapter;
    ListView messagesView;

    ImageView imgUsuario;

    TextView lblNombre;

    BaseDatos dbHelper;

    Boolean mePertence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        idReceptor = intent.getStringExtra("idReceptor");
        nombre = intent.getStringExtra("nombre");
        urlImagen = intent.getStringExtra("urlImagen");

        //Log.println(Log.ASSERT, "IMAGEN", urlImagen);

        //ActionBar actionBar = setActionBar();

        imgUsuario = findViewById(R.id.imgUsuarioChat);

        FileUtils.deleteQuietly(getApplicationContext().getCacheDir());
        Picasso.with(this).invalidate(urlImagen);
        Picasso.with(this).load(urlImagen).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(imgUsuario);

        lblNombre = findViewById(R.id.lblNombreUsuarioChat);
        lblNombre.setText(nombre);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);


        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(this);

        txtEnviar = findViewById(R.id.txtMensajeEnviar);
        txtFecha = findViewById(R.id.fechaMensaje);


        dbHelper = new BaseDatos(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {

            try {


                Cursor c = db.rawQuery("SELECT mensaje, timeStamperino, idEmisor, mePertenece FROM conversaciones WHERE idEmisor = " + sharedPref.getString("idUsuario", "") + "" +
                        " and idReceptorFK = " + idReceptor, null);

                if (c != null) {
                    c.moveToFirst();
                    do {
                        String m = c.getString(c.getColumnIndex("mensaje"));
                        String t = c.getString(c.getColumnIndex("timeStamperino"));
                        //int idE = c.getInt(c.getColumnIndex("idEmisor"));
                        int esMio = c.getInt(c.getColumnIndex("mePertenece"));

                        if (esMio == 1) {
                            mePertence = true;
                        } else {
                            mePertence = false;
                        }

                        t = t.replace("-", "/");
                        t = t.replace("//", "  ");
                        t = t.substring(0, t.lastIndexOf(":"));

                        Message men = new Message(m, mePertence, t);
                        messageAdapter.add(men);

                    } while (c.moveToNext());
                }
                c.close();
                db.close();
            } catch (Exception e) {

            }
        }

        refresh = new Runnable() {
            public void run() {
                recibirMensaje();
                handler.postDelayed(refresh, 1000);
            }
        };
        handler.post(refresh);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnviar:

                enviarMensaje();
                break;
        }
    }

    //Metodo para enviar mensajes
    public void enviarMensaje() {
        if(!txtEnviar.getText().toString().equals("")) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    try {


                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'--'HH:mm:ss");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                        System.out.println(sdf.format(new Date()));

                        String marcaDeTiempo = sdf.format(new Date());

                        String respuesta = "";
                        HashMap<String, String> postDataParams = new HashMap<String, String>();
                        postDataParams.put("mensaje", txtEnviar.getText().toString());
                        postDataParams.put("idEmisor", sharedPref.getString("idUsuario", ""));
                        postDataParams.put("idReceptorFK", idReceptor);
                        postDataParams.put("timeStamperino", marcaDeTiempo);

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

                            final String tiempoSQLITE = marcaDeTiempo;

                            marcaDeTiempo = marcaDeTiempo.replace("-", "/");
                            marcaDeTiempo = marcaDeTiempo.replace("//", "  ");
                            marcaDeTiempo = marcaDeTiempo.substring(0, marcaDeTiempo.lastIndexOf(":"));

                            final String finalMarcaDeTiempo = marcaDeTiempo;
                            final String finalMarcaDeTiempo1 = marcaDeTiempo;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Message m = new Message(txtEnviar.getText().toString(), true, finalMarcaDeTiempo);
                                    messageAdapter.add(m);

                                    String nombreRealEmisor = sharedPref.getString("nombreRealUsuario", "");



                                    dbHelper = new BaseDatos(getApplicationContext());
                                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                                    if (db != null) {
                                        db.execSQL("INSERT INTO conversaciones (mensaje, timeStamperino, idEmisor, idReceptorFK, mePertenece, nombreEmisor) VALUES ('" + txtEnviar.getText().toString() + "'," +
                                                "'" + tiempoSQLITE + "', " + sharedPref.getString("idUsuario", "") + ", " + idReceptor + " , 1, '" + nombreRealEmisor + "');");
                                    }



                                    db.close();
                                    txtEnviar.setText("");

                                }
                            });
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

                            final JSONObject[] jsonObject = {null};


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    String mensaje = "";
                                    String fecha = "";

                                    try {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            Log.println(Log.ASSERT, "Longitud1", i + "");
                                            jsonObject[i] = jsonArray.getJSONObject(i);
                                            mensaje = jsonObject[i].getString("mensaje");
                                            fecha = jsonObject[i].getString("timeStamperino");
                                            Log.println(Log.ASSERT, "Longitud2", jsonArray.length() + "");

                                            final String tiempoSQLITE = fecha;
                                            final String finalMensaje = mensaje;

                                            fecha = fecha.replace("-", "/");
                                            fecha = fecha.replace("//", "  ");
                                            fecha = fecha.substring(0, fecha.lastIndexOf(":"));


                                            final String finalFecha = fecha;

                                            Message m = new Message(finalMensaje, false, finalFecha);
                                            messageAdapter.add(m);

                                            dbHelper = new BaseDatos(getApplicationContext());
                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                            if (db != null) {
                                                db.execSQL("INSERT INTO conversaciones (mensaje, timeStamperino, idEmisor, idReceptorFK, mePertenece) VALUES ('" + mensaje + "'," +
                                                        "'" + tiempoSQLITE + "', " + sharedPref.getString("idUsuario", "") + ", " +  idReceptor + ", 0)");
                                            }

                                            db.close();

                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            });

                            responseBody.close();
                            responseBodyReader.close();
                            myConnection.disconnect();


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

    @Override
    public int getInputType() {
        return 0;
    }

    @Override
    public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyOther(View view, Editable text, KeyEvent event) {
        return false;
    }

    @Override
    public void clearMetaKeyState(View view, Editable content, int states) {

    }
}