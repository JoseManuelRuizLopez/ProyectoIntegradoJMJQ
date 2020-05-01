package com.example.proyectointegradojmjq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

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

public class CrearUsuario extends AppCompatActivity implements View.OnClickListener
{

    TextInputLayout til1;
    TextInputLayout til2;
    TextInputLayout til3;
    TextInputLayout til4;

    EditText txtNombreUsuario;
    EditText txtClaveUsuario;
    EditText txtRepetirClaveUsuario;
    EditText txtEmailUsuario;

    Button btnCrearUsuario;
    Button btnLimpiar;
    Button btnCancelar;

    ProgressBar cargaCU;

    String nombreUsuario;
    String claveUsuario;
    String repetirClaveUsuario;
    String emailUsuario;

    String claveEncriptada;

    String respuestaCorreo;
    String respuestaUsuario;

    boolean todoOk = true;

    SharedPreferences sharedPref;
    SharedPreferences sharedPrefUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);


        til1 = findViewById(R.id.textInputLayout1F2);
        til2 = findViewById(R.id.textInputLayout2F2);
        til3 = findViewById(R.id.textInputLayoutMiP);
        til4 = findViewById(R.id.textInputLayout4);

        txtNombreUsuario = findViewById(R.id.txtNombreUsuarioCU);
        txtClaveUsuario = findViewById(R.id.txtClaveUsuarioCU);
        txtRepetirClaveUsuario = findViewById(R.id.txtRepetirClaveUsuarioCU);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuarioCU);

        btnCrearUsuario = findViewById(R.id.btnCrearUsuarioCU);
        btnLimpiar = findViewById(R.id.btnLimpiarCU);
        btnCancelar = findViewById(R.id.btnCancelarCU);

        cargaCU = findViewById(R.id.cargaCU);

        btnCrearUsuario.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        sharedPref = getSharedPreferences("logeado", Context.MODE_PRIVATE);

        cargaCU.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnCrearUsuarioCU:

                nombreUsuario = txtNombreUsuario.getText().toString();
                claveUsuario = txtClaveUsuario.getText().toString();
                repetirClaveUsuario = txtRepetirClaveUsuario.getText().toString();
                emailUsuario = txtEmailUsuario.getText().toString();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            URL url = new URL("http://www.teamchaterinos.com/prueba.php?emailUsuario=" + emailUsuario);

                            //Create connection
                            HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

                            //Establecer método por defecto GET
                            myConnection.setRequestMethod("GET");

                            if (myConnection.getResponseCode() == 200)
                            {
                                InputStream responseBody = myConnection.getInputStream();
                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                                BufferedReader bR = new BufferedReader(responseBodyReader);
                                String line = "";

                                StringBuilder responseStrBuilder = new StringBuilder();

                                while ((line = bR.readLine()) != null) {
                                    responseStrBuilder.append(line);
                                }

                                JSONObject jsonobject = new JSONObject(responseStrBuilder.toString());
                                respuestaCorreo = jsonobject.getString("mensajeCorreo");

                                Log.println(Log.ASSERT, "Error1", respuestaCorreo);

                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                            } else {
                                Log.println(Log.ASSERT, "Error", "Error");
                            }


                            URL url2 = new URL("http://www.teamchaterinos.com/prueba.php?nombreUsuario=" + nombreUsuario);

                            //Create connection
                            HttpURLConnection myConnection2 = (HttpURLConnection) url2.openConnection();

                            //Establecer método por defecto GET
                            myConnection2.setRequestMethod("GET");

                            if (myConnection2.getResponseCode() == 200) {
                                InputStream responseBody2 = myConnection2.getInputStream();
                                InputStreamReader responseBodyReader2 = new InputStreamReader(responseBody2, "UTF-8");

                                BufferedReader bR2 = new BufferedReader(responseBodyReader2);
                                String line = "";

                                StringBuilder responseStrBuilder2 = new StringBuilder();

                                while ((line = bR2.readLine()) != null) {
                                    responseStrBuilder2.append(line);
                                }

                                JSONObject jsonobject = new JSONObject(responseStrBuilder2.toString());
                                respuestaUsuario = jsonobject.getString("mensajeUsuario");

                                Log.println(Log.ASSERT, "Error2", respuestaUsuario);

                                responseBody2.close();
                                responseBodyReader2.close();
                                myConnection.disconnect();

                            } else {
                                Log.println(Log.ASSERT, "Error", "Error");
                            }


                            if (nombreUsuario.length() <= 3 || txtNombreUsuario.getText().toString().equals("")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        til1.setError(getString(R.string.errorNombreUsuarioCU));
                                    }
                                });
                                todoOk = false;
                            }

                            if (!claveUsuario.equals(repetirClaveUsuario) || txtClaveUsuario.getText().toString().equals("") || txtRepetirClaveUsuario.getText().toString().equals("")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        til3.setError(getString(R.string.errorClaveUsuarioRepetidaCU));
                                    }
                                });
                                todoOk = false;
                            }


                            if (!emailUsuario.contains("@") || txtEmailUsuario.getText().toString().equals("")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        til4.setError(getString(R.string.errorEmailUsuarioCU));
                                    }
                                });
                                todoOk = false;
                            }

                            if (respuestaCorreo.equals("1")) {
                                runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        til4.setError(getResources().getString(R.string.errorEmailUsuarioExisteCU));
                                    }
                                });
                                todoOk = false;
                            }

                            if (respuestaUsuario.equals("1")) {
                                runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        til1.setError(getResources().getString(R.string.errorNombreUsuarioExisteCU));
                                    }
                                });
                                todoOk = false;
                            }

                            Log.println(Log.ASSERT, "Registro exitoso", "Registrado con éxito: " + todoOk);
                                //cargaCU.setVisibility(View.VISIBLE);
                            if (todoOk)
                            {
                                try
                                {

                                    claveEncriptada = new String(Hex.encodeHex(DigestUtils.md5(claveUsuario)));

                                    String respuesta = "";
                                    HashMap<String, String> postDataParams = new HashMap<String, String>();
                                    postDataParams.put("nombreUsuario", nombreUsuario);
                                    postDataParams.put("claveUsuario", claveEncriptada);
                                    postDataParams.put("emailUsuario", emailUsuario);

                                    URL url3 = new URL("http://www.teamchaterinos.com/prueba.php");
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

                                        while ((line = br.readLine()) != null)
                                        {
                                            respuesta += line;
                                        }
                                    }
                                    else
                                        {
                                        respuesta = "";
                                    }
                                    connection.getResponseCode();

                                    if (connection.getResponseCode() == 200)
                                    {
                                        Log.println(Log.ASSERT, "Registro exitoso", "Registrado con éxito: " + respuesta);
                                        //cargaCU.setVisibility(View.GONE);
                                        connection.disconnect();

                                        String respuestaId = "";

                                        JSONArray result = new JSONArray(respuesta.toString());

                                        for(int i=0; i < result.length(); i++)
                                        {
                                            JSONObject jsonobject = result.getJSONObject(i);

                                            respuestaId = jsonobject.getString("idUsuario");
                                        }

                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("idUsuario", respuestaId);
                                        editor.commit();

                                        Intent intentBienvenida = new Intent(CrearUsuario.this, BienvenidaUsuario.class);
                                        startActivity(intentBienvenida);
                                        finish();

                                    } else {
                                        Log.println(Log.ASSERT, "Error", "Error");
                                    }
                                } catch (Exception e) {
                                    Log.println(Log.ASSERT, "Excepción", e.getMessage());
                                }
                            }

                        } catch (Exception e)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(CrearUsuario.this, R.string.errorConexion, Toast.LENGTH_SHORT).show();
                                }
                            });

                            Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona");
                        }
                    }
                });

                til1.setError(null);
                til2.setError(null);
                til3.setError(null);
                til4.setError(null);

                todoOk = true;

                break;

            case R.id.btnLimpiarCU:

                txtNombreUsuario.setText("");
                txtClaveUsuario.setText("");
                txtEmailUsuario.setText("");
                txtRepetirClaveUsuario.setText("");
                break;

            case R.id.btnCancelarCU:

                finish();
                break;

        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet())
        {
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