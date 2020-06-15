package com.example.proyectointegradojmjq;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

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

    String idRecibido;
    String nombreUsuario;
    String nombreRecibido;
    String edadRecibida;
    String imgUrlRecibida;

    String mensaje;
    String idEmisor;
    String nombreUser;
    String notificado;
    String idUser;
    String foto;

    ArrayList<String> mensajesArray;
    ArrayList<String> nombresArray;
    ArrayList<String> notificadoArray;
    ArrayList<String> idEmisoresArray;
    ArrayList<String> fotosArray;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        idRecibido = intent.getStringExtra("idUsuario");
        nombreUsuario = intent.getStringExtra("nombreUsuario");
        nombreRecibido = intent.getStringExtra("nombre");
        edadRecibida = intent.getStringExtra("edad");
        imgUrlRecibida = intent.getStringExtra("image");

        Log.println(Log.ASSERT, "id", idRecibido);

        lblNombreVP.setText(nombreRecibido);
        lblEdadVP.setText(edadRecibida + " años");

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);
        idUser = sharedPref.getString("idUsuario", "0");

        FileUtils.deleteQuietly(getApplicationContext().getCacheDir());
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
                            final JSONObject jsonObject = jsonArray.getJSONObject(0);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        lblGeneroVP.setText(jsonObject.getString("generoUsuario"));
                                        lblAlturaVP.setText(jsonObject.getString("alturaUsuario") + " cm");
                                        lblEstadoCivilVP.setText(jsonObject.getString("estadoCivilUsuario"));
                                        lblDescripcionVP.setText(jsonObject.getString("descripcionUsuario"));
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
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    URL url = new URL("http://www.teamchaterinos.com/pruebachat.php?idReceptorFK=" + idUser);

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

                        while ((line = bR.readLine()) != null) {
                            responseStrBuilder.append(line);
                        }

                        JSONArray result = new JSONArray(responseStrBuilder.toString());

                        mensajesArray = new ArrayList<String>();
                        nombresArray = new ArrayList<String>();
                        notificadoArray = new ArrayList<String>();
                        idEmisoresArray = new ArrayList<String>();
                        fotosArray= new ArrayList<String>();

                        for (int i = 0; i < result.length(); i++)
                        {
                            JSONObject jsonobject = result.getJSONObject(i);

                            mensaje = jsonobject.getString("mensaje") + "\n";
                            idEmisor = jsonobject.getString("idEmisor");
                            nombreUser = jsonobject.getString("nombreRealUsuario");
                            notificado = jsonobject.getString("notificado");
                            foto = jsonobject.getString("fotoPerfilUsuario");

                            mensajesArray.add(mensaje);
                            nombresArray.add(nombreUser);
                            notificadoArray.add(notificado);
                            idEmisoresArray.add(idEmisor);
                            fotosArray.add(foto);

                        }

                        Log.println(Log.ASSERT, "Mensaje Recibido", mensajesArray.toString());
                        Log.println(Log.ASSERT, "DE: ", nombresArray.toString());
                        Log.println(Log.ASSERT, "NOTIFICADO: ", notificadoArray.toString());
                        Log.println(Log.ASSERT, "FOTOS: ", fotosArray.toString());
                        Log.println(Log.ASSERT, "ID'S EMISORES: ", idEmisoresArray.toString());

                        //mensaje = mensaje.replace("null", "");
                        //Log.println(Log.ASSERT, "DE: ", mensaje);

                        for (int i = 0; i < mensajesArray.size(); i++)
                        {

                            if (notificadoArray.get(i).equals("0"))
                            {
                                notificandoApp(nombresArray.get(i), mensajesArray.get(i), fotosArray.get(i), idEmisoresArray.get(i));

                                Log.println(Log.ASSERT, "NOTIFICATION: ", nombresArray.get(i) + " - - - - - - - - - - " + mensajesArray.get(i));
                            }
                        }


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

        //enable back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    public void notificandoApp(String nombreEmisor, String mensaje, String foto, String idEmisor)
    {

        final int m = generateRandom();

        Intent intent=new Intent(getApplicationContext(), Chat.class);

        intent.putExtra("idReceptor", idEmisor);
        intent.putExtra("nombre", nombreEmisor);
        intent.putExtra("urlImagen", foto);

        Log.println(Log.ASSERT, "LAFOTO", foto);


        String CHANNEL_ID="MYCHANNEL";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_HIGH);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle("DE: " + nombreEmisor)
                .setContentText("Mensaje: " +  mensaje)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true)
                .setStyle(new Notification.BigTextStyle().bigText(mensaje))
                .setSmallIcon((R.drawable.chaterinoslogo))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(m, notification);
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
                Intent intent = new Intent(this, Chat.class);
                intent.putExtra("idReceptor", idRecibido);
                intent.putExtra("nombre", nombreRecibido);
                intent.putExtra("urlImagen", imgUrlRecibida);

                startActivity(intent);
                break;

            case R.id.btnCancelarVP:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
