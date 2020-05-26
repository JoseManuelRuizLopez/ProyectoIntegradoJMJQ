package com.example.proyectointegradojmjq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class RecordarClave extends AppCompatActivity implements View.OnClickListener
{

    Button btnCancelarRC;
    Button btnEnviarRC;

    TextInputLayout textInputLayoutCorreoRC;
    TextView txtCorreo;

    String correoUsuario;

    String respuestaCorreo;

    boolean todoOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordar_clave);

        btnEnviarRC = findViewById(R.id.btnEnviarRC);
        btnCancelarRC = findViewById(R.id.btnCancelarRC);

        textInputLayoutCorreoRC = findViewById(R.id.textInputLayoutCorreoRC);
        txtCorreo = findViewById(R.id.txtCorreoRC);

        btnEnviarRC.setOnClickListener(this);
        btnCancelarRC.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnEnviarRC:

                correoUsuario = txtCorreo.getText().toString();

                textInputLayoutCorreoRC.setError(null);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://www.teamchaterinos.com/prueba.php?emailUsuario=" + correoUsuario);

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

                            }
                            else
                                {
                                Log.println(Log.ASSERT, "Error", "Error");
                            }


                            if (respuestaCorreo.equals("0")) {
                                runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        textInputLayoutCorreoRC.setError(getResources().getString(R.string.errorEmailUsuarioNoExisteRC));
                                    }
                                });
                                todoOk = false;
                            }

                            if (todoOk)
                            {
                                try
                                {

                                    String respuesta = "";
                                    HashMap<String, String> postDataParams = new HashMap<String, String>();
                                    postDataParams.put("emailUsuario", correoUsuario);

                                    URL url3 = new URL("http://www.teamchaterinos.com/correo.php");
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

                                    if (responseCode == HttpsURLConnection.HTTP_OK)
                                    {
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
                                        finish();
                                    }
                                    else
                                        {
                                        Log.println(Log.ASSERT, "Error", "Error");
                                    }
                                }
                                catch (Exception e)
                                {
                                    Log.println(Log.ASSERT, "Excepción", e.getMessage());
                                }
                            }

                        }
                        catch (Exception e)
                        {
                            Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona");
                        }
                    }
                });






                todoOk = true;

                Toast.makeText(this, R.string.tsCorreoEnviadoRC, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnCancelarRC:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
