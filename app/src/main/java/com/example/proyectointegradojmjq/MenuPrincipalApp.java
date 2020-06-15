package com.example.proyectointegradojmjq;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectointegradojmjq.ui.MiPerfil.FragmentoMiPerfil;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MenuPrincipalApp extends AppCompatActivity implements KeyListener {

    private AppBarConfiguration mAppBarConfiguration;

    SharedPreferences sharedPref;

    String idUsuario;
    String nombreUsuario;
    String nombreUsuarioReal;

    ImageView imgPerfil;
    TextView lblNavUsuario;
    TextView lblNavNombreCompleto;

    Uri uri;

    NavigationView navigationView;

    String imgPerfilUrl;

    String mensaje;
    String idEmisor;
    String nombreUser;
    String notificado;
    String foto;

    ArrayList<String> mensajesArray;
    ArrayList<String> nombresArray;
    ArrayList<String> notificadoArray;
    ArrayList<String> idEmisoresArray;
    ArrayList<String> fotosArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_busqueda, R.id.nav_conversaciones,
                R.id.nav_miperfil)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);

        imgPerfil = headerView.findViewById(R.id.imgPerfilMenu);
        lblNavUsuario = headerView.findViewById(R.id.lblNavUsuario);
        lblNavNombreCompleto = headerView.findViewById(R.id.lblNavNombreCompleto);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);

        idUsuario = sharedPref.getString("idUsuario", "0");

        imgPerfilUrl = "http://www.teamchaterinos.com/images/ " + idUsuario + ".png";


        FileUtils.deleteQuietly(getApplicationContext().getCacheDir());
        Picasso.with(this).invalidate(imgPerfilUrl);
        Picasso.with(this).load(imgPerfilUrl).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(imgPerfil);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.teamchaterinos.com/prueba.php?idUsuario=" + idUsuario);
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

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonobject = result.getJSONObject(i);

                            nombreUsuario = jsonobject.getString("nombreUsuario");
                            nombreUsuarioReal = jsonobject.getString("nombreRealUsuario");
                        }

                        responseBody.close();
                        responseBodyReader.close();
                        myConnection.disconnect();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lblNavUsuario.setText(nombreUsuario);
                                lblNavNombreCompleto.setText(nombreUsuarioReal);

                            }
                        });

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("nombreRealUsuario", nombreUsuarioReal);
                        editor.commit();

                    } else {
                        Log.println(Log.ASSERT, "Error", "Error");
                    }
                } catch (Exception e) {
                    Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona. " + e.getMessage());
                }
            }
        });


        AsyncTask.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    URL url = new URL("http://www.teamchaterinos.com/pruebachat.php?idReceptorFK=" + idUsuario);

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

                        mensaje = mensaje.replace("null", "");
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
                .setStyle(new Notification.BigTextStyle().bigText(mensaje))
                .setSmallIcon((R.drawable.chaterinoslogo))
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(m, notification);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                final AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle(getString(R.string.mn_cerrarsesion_mensaje));
                //alert.setMessage("Se perderan sus datos y conversaciones");

                alert.setPositiveButton(getString(R.string.mn_cerrarsesion_aceptar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("isLogged", false);
                        editor.putString("idUsuario", "");
                        editor.putString("nombreUsuario", "");

                        editor.commit();

                        Intent intentLogin = new Intent(MenuPrincipalApp.this, Login.class);
                        startActivity(intentLogin);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        finish();
                    }
                });
                alert.setNegativeButton(getString(R.string.mn_cerrarsesion_cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();

                return true;


            case R.id.configuracion:

                Intent intentConfiguracion = new Intent(MenuPrincipalApp.this, ConfiguracionPerfil.class);
                startActivity(intentConfiguracion);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_app, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                uri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCrop(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                FragmentoMiPerfil.imgPerfilMiP.setImageURI(result.getUri());
            }
        }
    }

    private void startCrop(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowFlipping(false)
                .setAllowRotation(false)
                .setAspectRatio(200, 200)
                .setMultiTouchEnabled(true)
                .start(this);
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
        return false;    }

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
