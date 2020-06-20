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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;


public class Chat extends AppCompatActivity implements View.OnClickListener
{

    SharedPreferences sharedPref;

    String idReceptor;
    String nombre;
    String urlImagen;
    String edad;
    String genero;
    String estadoCivil;
    String altura;
    String descripcion;

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

    Boolean meFui = false;

    String nombre2;
    String fechaNac2;
    String altura2;
    String genero2;
    String descripcion2;
    String estadoCivil2;

    int año;
    int mes;
    int dia;
    int edad2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);


        AsyncTask asyntico;

        Intent intent = getIntent();
        idReceptor = intent.getStringExtra("idReceptor");
        nombre = intent.getStringExtra("nombre");
        urlImagen = intent.getStringExtra("urlImagen");

        /*Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            idReceptor = extras.getString("idReceptor");
            nombre = extras.getString("nombre");
            urlImagen = extras.getString("urlImagen");
            edad = extras.getString("edad");
            genero = extras.getString("genero");
            altura = extras.getString("altura");
            estadoCivil = extras.getString("estadoCivil");
            descripcion = extras.getString("descripcion");
        }*/


        edad = intent.getStringExtra("edad");
        genero = intent.getStringExtra("genero");
        altura = intent.getStringExtra("altura");
        estadoCivil = intent.getStringExtra("estadoCivil");
        descripcion = intent.getStringExtra("descripcion");

        imgUsuario = findViewById(R.id.imgUsuarioChat);
        imgUsuario.setOnClickListener(this);

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


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////d
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

            case R.id.imgUsuarioChat:


                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run()
                    {
                        try {
                            URL url = new URL("http://www.teamchaterinos.com/prueba.php?idUsuario=" + idReceptor);

                            //Create connection
                            HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

                            //Establecer método por defecto GET
                            myConnection.setRequestMethod("GET");

                            if (myConnection.getResponseCode() == 200) {
                                InputStream responseBody = myConnection.getInputStream();
                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                                BufferedReader bR = new BufferedReader(responseBodyReader);
                                String line = "";

                                StringBuilder responseStrBuilder = new StringBuilder();

                                while ((line = bR.readLine()) != null)
                                {
                                    responseStrBuilder.append(line);
                                }

                                JSONArray result = new JSONArray(responseStrBuilder.toString());

                                for (int i = 0; i < result.length(); i++)
                                {

                                    JSONObject jsonobject = result.getJSONObject(i);



                                    nombre2 = jsonobject.getString("nombreRealUsuario");
                                    fechaNac2 = jsonobject.getString("fechaNacimientoUsuario");
                                    altura2 = jsonobject.getString("alturaUsuario");
                                    descripcion2 = jsonobject.getString("descripcionUsuario");
                                    estadoCivil2 = jsonobject.getString("estadoCivilUsuario");
                                    genero2 = jsonobject.getString("generoUsuario");



                                }

                                String[] diaNac = fechaNac2.split("-");

                                año = Integer.parseInt(diaNac[0]);
                                mes = Integer.parseInt(diaNac[1]);
                                dia = Integer.parseInt(diaNac[2]);

                                edad2 = getAge(año, mes, dia );

                                Log.println(Log.ASSERT, "FECHANACIMIENTO", fechaNac2);

                                Intent intent = new Intent(getApplicationContext(), VerPerfilChat.class);
                                intent.putExtra("urlImagen", urlImagen);
                                intent.putExtra("nombre", nombre2);
                                intent.putExtra("edad", edad2);
                                intent.putExtra("genero", genero2);
                                intent.putExtra("altura", altura2);
                                intent.putExtra("descripcion", descripcion2);
                                intent.putExtra("estadoCivil", estadoCivil2);

                                startActivity(intent);

                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                            } else {
                                Log.println(Log.ASSERT, "Error", "Error");
                            }
                        } catch (Exception e) {
                            Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona2. " + e.getMessage());
                        }
                    }
                });



                Log.println(Log.ASSERT, "Excepción", "COMPRO" + año + "-" + mes + "-" + dia);


                break;
        }
    }

    //Metodo para enviar mensajes
    public void enviarMensaje() {
        if(!txtEnviar.getText().toString().equals(""))
        {
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
                        postDataParams.put("recibido", "0");
                        postDataParams.put("notificado", "0");

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
    public void recibirMensaje()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //while (true) {
                try {
                    URL url = null;
                    try {


                        if (meFui)
                        {
                            handler.removeCallbacks(refresh);
                             handler.removeCallbacksAndMessages(null);
                            handler = null;
                            refresh = null;
                        }

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
                                                db.execSQL("INSERT INTO conversaciones (mensaje, timeStamperino, idEmisor, idReceptorFK, mePertenece, nombreEmisor) VALUES ('" + mensaje + "'," +
                                                        "'" + tiempoSQLITE + "', " + sharedPref.getString("idUsuario", "") + ", " +  idReceptor + ", 0, '" + lblNombre.getText().toString() + "')");
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
                            Log.println(Log.ASSERT, "Error1", "Error");
                        }

                    } catch (Exception e) {
                        Log.println(Log.ASSERT, "Error2", e.getMessage());
                    }
                } catch (Exception e) {
                    Log.println(Log.ASSERT, "Error3", "Error2");
                }
            }
            //}
        });

    }

    public int getAge(int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0) {
            a = 0;
        }
        return a;
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
    public void onBackPressed()
    {
        meFui = true;
        finish();
        super.onBackPressed();
    }
}