package com.example.proyectointegradojmjq;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuPrincipalApp extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    SharedPreferences sharedPref;

    String idUsuario;
    String nombreUsuario;
    String nombreUsuarioReal;

    ImageView imgPerfil;
    TextView lblNavUsuario;
    TextView lblNavNombreCompleto;

    String imgPerfilUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        Picasso.with(this).load(imgPerfilUrl).into(imgPerfil);

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

                        lblNavUsuario.setText(nombreUsuario);
                        lblNavNombreCompleto.setText(nombreUsuarioReal);

                    } else {
                        Log.println(Log.ASSERT, "Error", "Error");
                    }
                } catch (Exception e) {
                    Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona. " + e.getMessage());
                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
}
