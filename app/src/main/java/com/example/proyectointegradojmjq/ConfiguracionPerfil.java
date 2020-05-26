package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectointegradojmjq.ui.Inicio.FragmentoInicio;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.MemoryPolicy;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

public class ConfiguracionPerfil extends AppCompatActivity implements View.OnClickListener {


    EditText txtCorreo;
    EditText txtClave;
    EditText txtClave2;

    TextInputLayout txtIn;
    TextInputLayout txtIn2;
    TextInputLayout txtIn3;

    Button btnGuardarCambios;
    Button btnCancelar;
    Button btnBorrarPerfil;

    SharedPreferences sharedPref;
    String idUsuario;

    String correoEmail;
    String nuevoCorreo;

    String nuevaClave;
    String nuevaClave2;
    String nuevaClaveEncriptada;

    boolean todoOk = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_perfil);

        txtIn = findViewById(R.id.textInputLayoutEmailConf);
        txtIn2 = findViewById(R.id.textInputLayoutClaveConf);
        txtIn3 = findViewById(R.id.textInputLayoutClaveConf2);

        txtCorreo = findViewById(R.id.txtEmailConf);
        txtClave = findViewById(R.id.txtClaveConf);
        txtClave2 = findViewById(R.id.txtClaveConf2);

        btnGuardarCambios = findViewById(R.id.btnGuardarCambiosConf);
        btnCancelar = findViewById(R.id.btnCancelarConf);
        btnBorrarPerfil = findViewById(R.id.btnBorrarPerfilConf);

        btnGuardarCambios.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnBorrarPerfil.setOnClickListener(this);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);
        idUsuario = sharedPref.getString("idUsuario", "0");


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

                            correoEmail = jsonobject.getString("emailUsuario");
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                txtIn.setHint("Correo Electronico");
                                txtIn2.setHint("Nueva Clave");
                                txtIn3.setHint("Repetir Nueva Clave");
                                txtCorreo.setText(correoEmail);
                                txtClave.setText("**********");
                                txtClave2.setText("**********");
                            }
                        });


                        responseBody.close();
                        responseBodyReader.close();
                        myConnection.disconnect();

                    } else {
                        Log.println(Log.ASSERT, "Error", "Error");
                    }
                } catch (Exception e) {
                    Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona. " + e.getMessage());
                }
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnGuardarCambiosConf:


                txtIn.setError(null);
                txtIn2.setError(null);

                nuevoCorreo = txtCorreo.getText().toString();
                nuevaClave = txtClave.getText().toString();
                nuevaClave2 = txtClave2.getText().toString();
                nuevaClaveEncriptada = new String(Hex.encodeHex(DigestUtils.md5(nuevaClave)));

                if (!nuevoCorreo.contains("@") || txtCorreo.getText().toString().equals("")) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            txtIn.setError(("Correo No Válido"));
                        }
                    });
                    todoOk = false;
                }

                if (txtClave.getText().toString().equals("") || txtClave2.getText().toString().equals("") || !nuevaClave.equals(nuevaClave2)) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            txtIn2.setError(getString(R.string.errorClaveUsuarioRepetidaCU));
                        }
                    });
                    todoOk = false;
                }
                Log.println(Log.ASSERT, "Registro exitoso", "Registrado con éxito1: " + todoOk);

                if (todoOk) {

                    if (nuevaClave.contains("*")) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String response = "";


                                    Uri uri = new Uri.Builder()
                                            .scheme("http").authority("www.teamchaterinos.com")
                                            .path("prueba.php")
                                            .appendQueryParameter("emailUsuario", nuevoCorreo)
                                            .appendQueryParameter("idUsuario", idUsuario)
                                            .build();//
                                    // Create connection
                                    URL url = new URL(uri.toString());

                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setReadTimeout(15000);
                                    connection.setConnectTimeout(15000);
                                    connection.setRequestMethod("PUT");
                                    connection.setDoInput(true);
                                    connection.setDoOutput(true);

                                    int responseCode = connection.getResponseCode();

                                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                                        String line;
                                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                                        while ((line = br.readLine()) != null) {
                                            response += line;
                                        }
                                    } else {
                                        response = "";
                                    }

                                    connection.getResponseCode();

                                    if (connection.getResponseCode() == 200) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), R.string.cambiosGuardados, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Intent intencion = new Intent(getApplicationContext(), MenuPrincipalApp.class);
                                        startActivity(intencion);
                                        finish();

                                        connection.disconnect();
                                    } else {
                                        // Error handling code goes here
                                        Log.println(Log.ASSERT, "Error", "Error");
                                    }
                                } catch (Exception e) {
                                    Log.println(Log.ASSERT, "Excepción", e.getMessage());
                                }
                            }
                        });

                    } else {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String response = "";


                                    Uri uri = new Uri.Builder()
                                            .scheme("http").authority("www.teamchaterinos.com")
                                            .path("prueba.php")
                                            .appendQueryParameter("emailUsuario", nuevoCorreo)
                                            .appendQueryParameter("claveUsuario", nuevaClaveEncriptada)
                                            .appendQueryParameter("idUsuario", idUsuario)
                                            .build();//
                                    // Create connection
                                    URL url = new URL(uri.toString());

                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setReadTimeout(15000);
                                    connection.setConnectTimeout(15000);
                                    connection.setRequestMethod("PUT");
                                    connection.setDoInput(true);
                                    connection.setDoOutput(true);

                                    int responseCode = connection.getResponseCode();

                                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                                        String line;
                                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                                        while ((line = br.readLine()) != null) {
                                            response += line;
                                        }
                                    } else {
                                        response = "";
                                    }

                                    connection.getResponseCode();

                                    if (connection.getResponseCode() == 200) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), R.string.cambiosGuardados, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Intent intencion = new Intent(getApplicationContext(), MenuPrincipalApp.class);
                                        startActivity(intencion);
                                        finish();

                                        connection.disconnect();
                                    } else {
                                        // Error handling code goes here
                                        Log.println(Log.ASSERT, "Error", "Error");
                                    }
                                } catch (Exception e) {
                                    Log.println(Log.ASSERT, "Excepción", e.getMessage());
                                }
                            }
                        });
                    }
                }

                todoOk = true;

                Log.println(Log.ASSERT, "Registro exitoso", "Registrado con éxito: " + todoOk);
                break;


            case R.id.btnCancelarConf:

                Intent intencionMenuPrincipal = new Intent(ConfiguracionPerfil.this, MenuPrincipalApp.class);
                startActivity(intencionMenuPrincipal);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            case R.id.btnBorrarPerfilConf:

                final AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("¿ESTAS SEGURO DE BORRAR TU PERFIL? ESTO SERÁ IRREVERSIBLE");
                //alert.setMessage("Se perderan sus datos y conversaciones");

                alert.setPositiveButton(getString(R.string.mn_cerrarsesion_aceptar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Intent intentLogin = new Intent(ConfiguracionPerfil.this, Login.class);
                        startActivity(intentLogin);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String response = "";


                                    Uri uri = new Uri.Builder()
                                            .scheme("http").authority("www.teamchaterinos.com")
                                            .path("prueba.php")
                                            .appendQueryParameter("idUsuario", idUsuario)
                                            .build();//
                                    // Create connection
                                    URL url = new URL(uri.toString());

                                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                    connection.setReadTimeout(15000);
                                    connection.setConnectTimeout(15000);
                                    connection.setRequestMethod("DELETE");
                                    connection.setDoInput(true);
                                    connection.setDoOutput(true);

                                    int responseCode = connection.getResponseCode();

                                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                                        String line;
                                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                                        while ((line = br.readLine()) != null) {
                                            response += line;
                                        }
                                    } else {
                                        response = "";
                                    }

                                    connection.getResponseCode();

                                    if (connection.getResponseCode() == 200) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {

                                                Toast.makeText(getApplicationContext(), R.string.cambiosGuardados, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Intent intencion = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intencion);
                                        finish();

                                        connection.disconnect();
                                    } else {
                                        // Error handling code goes here
                                        Log.println(Log.ASSERT, "Error", "Error");
                                    }
                                } catch (Exception e) {
                                    Log.println(Log.ASSERT, "Excepción", e.getMessage());
                                }
                            }
                        });
                    }
                });
                alert.setNegativeButton(getString(R.string.mn_cerrarsesion_cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
        }
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
