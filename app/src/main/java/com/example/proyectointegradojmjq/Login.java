package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btnRegistrarLogin, btnEntrarLogin;
    TextView lblRecordarClave;
    ProgressBar cargaLogin;

    EditText txtUsuarioLogin, txtClaveLogin;

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

        btnRegistrarLogin.setOnClickListener(this);
        lblRecordarClave.setOnClickListener(this);
        btnEntrarLogin.setOnClickListener(this);

        cargaLogin.setVisibility(View.GONE);
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

                String respuesta = "";

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // All your networking logic
                        // should be here
                        try {
                            // Create URL
                            URL url = new URL("http://192.168.1.42/prueba.php?nombreUsuario=" + usuario + "");

                            //Log.println(Log.ASSERT, "Resultado", "Registromodificado:" + clave);
                            // Create connection
                            HttpURLConnection myConnection = (HttpURLConnection)
                                    url.openConnection();
                            // Establecer método. Por defecto GET.
                            myConnection.setRequestMethod("GET");
                            if (myConnection.getResponseCode() == 200) {
                                // Success
                                InputStream responseBody =
                                        myConnection.getInputStream();
                                InputStreamReader responseBodyReader =
                                        new InputStreamReader(responseBody, "UTF-8");
                                BufferedReader bR = new
                                        BufferedReader(responseBodyReader);
                                String line = "";
                                StringBuilder responseStrBuilder = new StringBuilder();
                                while ((line = bR.readLine()) != null) {
                                    responseStrBuilder.append(line);
                                }

                                JSONObject jsonobject = new JSONObject(responseStrBuilder.toString());

                                String respuesta = jsonobject.getString("mensaje");
                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                                Log.println(Log.ASSERT, "Resultado", respuesta);

                                if(respuesta.equals(clave))
                                {
                                    Intent intentMenuPrincipal = new Intent(Login.this, MenuPrincipal.class);
                                    startActivity(intentMenuPrincipal);
                                    finish();
                                }
                                else
                                {
                                    Snackbar.make(v, "El usuario o la contraseña son incorrectas", Snackbar.LENGTH_LONG).setAction("Action", null).show();;
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
                                Snackbar.make(v, "Error", Snackbar.LENGTH_LONG).setAction("Action", null).show();;
                                Log.println(Log.ASSERT, "Error", "Error");
                            }
                        } catch (Exception e) {
                            Snackbar.make(v, "Excepción", Snackbar.LENGTH_LONG).setAction("Action", null).show();;
                            Log.println(Log.ASSERT, "Excepción", e.getMessage());
                        }
                    }
                });
        }
    }
}
