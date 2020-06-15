package com.example.proyectointegradojmjq;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;

public class Bienvenida_fragment4 extends Fragment implements View.OnClickListener, Serializable
{

    //CardView cardView;
    Uri uri;

    public static ImageView imgPerfil;

    TextView lbl;

    Button btnSiguienteF4;
    Button btnAtrasF4;

    String nombreUsuario;

    SharedPreferences sharedPref;
    SharedPreferences sharedPrefB;

    Bitmap bitmapFoto;
    String encoded;

    String idUser;

    ProgressBar cargaBienvenida4;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = null;

    public static final String UPLOAD_KEY = "image";

    public Bienvenida_fragment4()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bienvenida_fragment4, container, false);

        imgPerfil = view.findViewById(R.id.imgDefectoF4);
        btnSiguienteF4 = view.findViewById(R.id.btnSiguienteWelcomeF4);
        btnAtrasF4 = view.findViewById(R.id.btnAtrasWelcomeF4);

        //cardView = view.findViewById(R.id.cardviewF4);
        //cardView.setOnClickListener(this);

        cargaBienvenida4 = view.findViewById(R.id.cargaBienvenida4);
        cargaBienvenida4.setVisibility(View.GONE);

        imgPerfil.setOnClickListener(this);
        btnSiguienteF4.setOnClickListener(this);
        btnAtrasF4.setOnClickListener(this);

        sharedPref = getActivity().getSharedPreferences("logeado", Context.MODE_PRIVATE);
        sharedPrefB = getActivity().getSharedPreferences("prefBusqueda", Context.MODE_PRIVATE);

        lbl = view.findViewById(R.id.lblAddFotosF4);

        lbl.setText(getActivity().getString(R.string.lblAddFotosGaleriaB));


        return view;
    }

    @Override
    public void onClick(View v)
    {

        switch(v.getId())
        {

            case R.id.imgDefectoF4:

                CropImage.startPickImageActivity(getActivity());
                break;

            case R.id.btnSiguienteWelcomeF4:

                cargaBienvenida4.setVisibility(View.VISIBLE);

                final String nombreReal = getActivity().getIntent().getExtras().getString("nombreReal");
                final String fechaAmericana = getActivity().getIntent().getExtras().getString("fechaAmericana");
                final String descripcionUsuario = getActivity().getIntent().getExtras().getString("descripcion");

                final String generoUsuario = getActivity().getIntent().getExtras().getString("generoUsuario");
                final String estadoCivilUsuario = getActivity().getIntent().getExtras().getString("estadoCivilUsuario");
                final String alturaUsuario = getActivity().getIntent().getExtras().getString("alturaUsuario");


                bitmapFoto = ((BitmapDrawable)imgPerfil.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapFoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                idUser = sharedPref.getString("idUsuario", "");

                Log.println(Log.ASSERT,"Bitmap", encoded.toString());


                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {

                            String respuesta = "";
                            HashMap<String, String> postDataParams = new HashMap<String, String>();
                            postDataParams.put(UPLOAD_KEY, encoded);
                            postDataParams.put("id", idUser);

                            URL url3 = new URL("http://www.teamchaterinos.com/pruebafoto.php");
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


                            }
                            else
                                {
                                Log.println(Log.ASSERT, "Error", "Error");
                            }
                        } catch (Exception e) {
                            Log.println(Log.ASSERT, "Excepción", e.getMessage());
                        }
                    }
                });


                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            String response = "";

                            idUser = sharedPref.getString("idUsuario", "");

                            Uri uri = new Uri.Builder()
                                    .scheme("http")
                                    .authority("www.teamchaterinos.com")
                                    .appendPath("prueba.php")
                                    .appendQueryParameter("nombreRealUsuario", nombreReal)
                                    .appendQueryParameter("generoUsuario", generoUsuario)
                                    .appendQueryParameter("estadoCivilUsuario", estadoCivilUsuario)
                                    .appendQueryParameter("alturaUsuario", alturaUsuario)
                                    .appendQueryParameter("fechaNacimientoUsuario", fechaAmericana)
                                    .appendQueryParameter("descripcionUsuario", descripcionUsuario)
                                    .appendQueryParameter("idUsuario", idUser)
                                    .build();
                            // Create connection
                            URL url = new URL(uri.toString());

                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setReadTimeout(15000);
                            connection.setConnectTimeout(15000);
                            connection.setRequestMethod("PUT");
                            connection.setDoInput(true);
                            connection.setDoOutput(true);

                            int responseCode=connection.getResponseCode();

                            if (responseCode == HttpsURLConnection.HTTP_OK)
                            {
                                String line;
                                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                                while ((line=br.readLine()) != null)
                                {
                                    response += line;
                                }
                            }
                            else
                            {
                                response = "";
                            }

                            connection.getResponseCode();

                            if (connection.getResponseCode() == 200)
                            {
                                getActivity().runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        Toast.makeText(getContext(), R.string.perfilCreadoExito, Toast.LENGTH_SHORT).show();

                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putBoolean("isLogged", true);

                                        SharedPreferences.Editor editor2 = sharedPref.edit();
                                        editor2.putInt("edadMin", 0);
                                        editor2.putInt("edadMax", 0);
                                        editor2.putString("genero", "");
                                        editor2.putString("estadoCivil", "");
                                        editor2.putString("registroCompletado", "1");


                                        editor.commit();
                                        editor2.commit();

                                    }
                                });


                                Intent intencion = new Intent(getActivity(), MenuPrincipalApp.class);
                                startActivity(intencion);
                                getActivity().finish();

                                connection.disconnect();
                            }
                            else
                            {
                                Log.println(Log.ASSERT,"Error", "Error");
                            }
                        }
                        catch (Exception e)
                        {
                            cargaBienvenida4.setVisibility(View.INVISIBLE);
                            Log.println(Log.ASSERT,"Excepción", e.getMessage());
                        }
                    }
                });
                break;

            case R.id.btnAtrasWelcomeF4:

                ((BienvenidaUsuario)getActivity()).selectTab(2);
                break;
        }

    }

    public String encondeandoFoto (Bitmap bitmapImagen)
    {




        return "";
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
