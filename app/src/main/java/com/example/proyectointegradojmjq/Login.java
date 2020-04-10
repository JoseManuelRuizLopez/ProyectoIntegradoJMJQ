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
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
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

    Switch swMantenerSesion;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegistrarLogin = findViewById(R.id.btnRegistrarLogin);
        lblRecordarClave = findViewById(R.id.lblRecordarClave);
        btnEntrarLogin = findViewById(R.id.btnEntrarLogin);
        cargaLogin = findViewById(R.id.cargaLogin);
        txtUsuarioLogin = findViewById(R.id.txtUsuarioLogin);
        txtClaveLogin = findViewById(R.id.txtClaveLogin);
        swMantenerSesion = findViewById(R.id.swMantenerSesion);

        btnRegistrarLogin.setOnClickListener(this);
        lblRecordarClave.setOnClickListener(this);
        btnEntrarLogin.setOnClickListener(this);

        cargaLogin.setVisibility(View.GONE);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(final View v)
    {
        switch (v.getId())
        {
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

                            URL url = new URL("http://192.168.1.66/prueba.php?nombreUsuario=" + usuario + "&claveUsuario=" + claveEncriptada + "");
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

                                String respuesta = jsonobject.getString("mensaje");
                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                                Log.println(Log.ASSERT, "Resultado", respuesta);

                                if (respuesta.equals("1")) {
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
                                   //cargaLogin.setVisibility(View.GONE);

                                    Snackbar.make(v, getString(R.string.snkBarClaveIncorrecta), Snackbar.LENGTH_LONG).setAction("Action", null).show();

                                }

                            } else {
                                // Error handling code goes here
                                //cargaLogin.setVisibility(View.GONE);
                                Snackbar.make(v, getString(R.string.snkBarError), Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            }
                        } catch (Exception e) {
                            //cargaLogin.setVisibility(View.GONE);
                            Snackbar.make(v, getString(R.string.snkBarError), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }



                });

                break;
        }
    }
}
