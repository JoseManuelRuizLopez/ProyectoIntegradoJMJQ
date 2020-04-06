package com.example.proyectointegradojmjq;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

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

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

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
    public void onClick(View v)
    {
        switch(v.getId())
        {

            case R.id.btnCrearUsuarioCU:

                nombreUsuario = txtNombreUsuario.getText().toString();
                claveUsuario =  txtClaveUsuario.getText().toString();
                repetirClaveUsuario = txtRepetirClaveUsuario.getText().toString();
                emailUsuario = txtEmailUsuario.getText().toString();


                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            URL url = new URL("http://192.168.1.66/prueba.php?emailUsuario=" + emailUsuario);

                            //Create connection
                            HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

                            //Establecer método por defecto GET
                            myConnection.setRequestMethod("GET");

                            if (myConnection.getResponseCode() == 200)
                            {
                                InputStream responseBody = myConnection.getInputStream();
                                InputStreamReader responseBodyReader =new InputStreamReader(responseBody, "UTF-8");

                                BufferedReader bR = new BufferedReader(responseBodyReader);
                                String line = "";

                                StringBuilder responseStrBuilder = new StringBuilder();

                                while((line =  bR.readLine()) != null)
                                {
                                    responseStrBuilder.append(line);
                                }

                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();



                            }
                            else
                                {
                                    Log.println(Log.ASSERT,"Error", "Error");
                                }
                        }
                        catch (Exception e)
                        {
                            Log.println(Log.ASSERT,"Excepción", e.getMessage());
                        }
                    }
                });


                /*if (nombreUsuario.length() <= 3)
                {
                    txtNombreUsuario.setError(getString(R.string.errorNombreUsuarioCU));
                }
                else if (!claveUsuario.equals(repetirClaveUsuario))
                {
                    txtRepetirClaveUsuario.setError(getString(R.string.errorClaveUsuarioRepetidaCU));
                }
                else if (!emailUsuario.contains("@"))
                {
                    txtEmailUsuario.setError(getString(R.string.errorEmailUsuarioCU));
                }
                else
                {
                    //cargaCU.setVisibility(View.VISIBLE);

                    AsyncTask.execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                claveEncriptada = new String(Hex.encodeHex(DigestUtils.md5(claveUsuario)));

                                String respuesta = "";
                                HashMap<String, String> postDataParams = new HashMap<String, String>();
                                postDataParams.put("nombreUsuario", nombreUsuario);
                                postDataParams.put("claveUsuario", claveEncriptada);
                                postDataParams.put("emailUsuario", emailUsuario);

                                URL url = new URL("http://192.168.1.66/prueba.php");
                                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
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

                                if (responseCode == HttpsURLConnection.HTTP_OK)
                                {
                                    String line;
                                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                                    while ((line=br.readLine()) != null)
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
                                    Log.println(Log.ASSERT,"Registro exitoso", "Registrado con éxito: " + respuesta);
                                    //cargaCU.setVisibility(View.GONE);
                                    connection.disconnect();

                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("isLogged", true);

                                    editor.commit();
                                }
                                else
                                {
                                    Log.println(Log.ASSERT,"Error", "Error");
                                }
                            }
                            catch(Exception e)
                            {
                                Log.println(Log.ASSERT,"Excepción", e.getMessage());
                            }
                            //finish();
                        }
                    });
                }*/

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

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
            {
                first = false;
            }
            else
            {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}