package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Button btnRegistrarLogin, btnEntrarLogin;
    TextView lblRecordarClave;
    ProgressBar cargaLogin;

    EditText txtUsuarioLogin, txtClaveLogin;

    Switch swMantenerSesion;

    ImageView imgOjo;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegistrarLogin = findViewById(R.id.btnRegistrarLogin);
        lblRecordarClave = findViewById(R.id.lblRecordarClave);
        btnEntrarLogin = findViewById(R.id.btnEntrarLogin);
        cargaLogin = findViewById(R.id.cargaLogin);
        txtUsuarioLogin = findViewById(R.id.txtUsuarioLogin);
        txtClaveLogin = findViewById(R.id.txtClaveLogin);
        swMantenerSesion = findViewById(R.id.swMantenerSesion);
        imgOjo = findViewById(R.id.imgOjoLogin);

        btnRegistrarLogin.setOnClickListener(this);
        lblRecordarClave.setOnClickListener(this);
        btnEntrarLogin.setOnClickListener(this);
        imgOjo.setOnTouchListener(this);

        cargaLogin.setVisibility(View.GONE);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnRegistrarLogin:
                Intent intentRegistrar = new Intent(Login.this, CrearUsuario.class);
                startActivity(intentRegistrar);
                break;

            case R.id.lblRecordarClave:
                Intent intentRecordarClave = new Intent(Login.this, RecordarClave.class);
                startActivity(intentRecordarClave);
                break;

            case R.id.btnEntrarLogin:
                cargaLogin.setVisibility(View.VISIBLE);

                final String usuario = txtUsuarioLogin.getText().toString();
                final String clave = txtClaveLogin.getText().toString();
                final String claveEncriptada = new String(Hex.encodeHex(DigestUtils.md5(clave)));

                String respuesta = "";

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {


                            URL url = new URL("http://192.168.1.42/prueba.php?nombreUsuario=" + usuario + "");
                            HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
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

                                JSONObject jsonobject = new JSONObject(responseStrBuilder.toString());

                                String respuesta = jsonobject.getString("claveUsuario");
                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                                Log.println(Log.ASSERT, "Resultado", respuesta);

                                if (respuesta.equals(claveEncriptada)) {
                                    if (swMantenerSesion.isChecked()) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putBoolean("isLogged", true);

                                        editor.commit();

                                        Intent intentMenuPrincipal = new Intent(Login.this, MenuPrincipalApp.class);
                                        startActivity(intentMenuPrincipal);
                                        finish();
                                    }
                                    else
                                    {
                                        Intent intentMenuPrincipal = new Intent(Login.this, MenuPrincipalApp.class);
                                        startActivity(intentMenuPrincipal);
                                        finish();
                                    }
                                } else {
                                   cargaLogin.setVisibility(View.GONE);


                                    Snackbar.make(v, "El usuario o la contraseña son incorrectas", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }

/*
                                if (respuesta.equals("0")) {
                                    if (switch1.isChecked()) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("user", user);
                                        editor.putBoolean("isLogin", true);
                                        editor.commit();
                                    }

                                    //Toast.makeText(LoginActivity.this, "Primera Vez", Toast.LENGTH_SHORT).show();
                                    // Arrancamos Activity
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                } else {
                                    respuesta = "Credenciales incorrectas";
                                    Log.println(Log.ASSERT, "Error", respuesta);
                                    Snackbar.make(view, respuesta,
                                            Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    loadingProgressBar.setVisibility(View.INVISIBLE);

                                }
*/
                            } else {
                                // Error handling code goes here
                                //cargaLogin.setVisibility(View.GONE);
                                Snackbar.make(v, "Ha ocurrido un error", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            }
                        } catch (Exception e) {
                            //cargaLogin.setVisibility(View.GONE);
                            Snackbar.make(v, "Ha ocurrido un error", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }



                });

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                txtClaveLogin.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case MotionEvent.ACTION_UP:
                txtClaveLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }
        return true;
    }
}
