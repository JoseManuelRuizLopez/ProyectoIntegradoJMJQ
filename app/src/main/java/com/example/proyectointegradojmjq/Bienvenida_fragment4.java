package com.example.proyectointegradojmjq;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Bienvenida_fragment4 extends Fragment implements View.OnClickListener
{

    ImageView imgPerfil;

    TextView lbl;

    Button btnSiguienteF4;
    Button btnAtrasF4;

    String nombreUsuario;

    SharedPreferences sharedPref;

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

        imgPerfil.setOnClickListener(this);
        btnSiguienteF4.setOnClickListener(this);
        btnAtrasF4.setOnClickListener(this);

        sharedPref = getActivity().getSharedPreferences("logeado", Context.MODE_PRIVATE);

        lbl = view.findViewById(R.id.lblAddFotosF4);

        lbl.setText(nombreUsuario);


        return view;
    }

    @Override
    public void onClick(View v)
    {

        switch(v.getId())
        {

            case R.id.imgDefectoF4:


            case R.id.btnSiguienteWelcomeF4:

                final String nombreReal = getActivity().getIntent().getExtras().getString("nombreReal");
                final String fechaAmericana = getActivity().getIntent().getExtras().getString("fechaAmericana");
                final String descripcionUsuario = getActivity().getIntent().getExtras().getString("descripcion");

                final String generoUsuario = getActivity().getIntent().getExtras().getString("generoUsuario");
                final String estadoCivilUsuario = getActivity().getIntent().getExtras().getString("estadoCivilUsuario");
                final String alturaUsuario = getActivity().getIntent().getExtras().getString("alturaUsuario");

                AsyncTask.execute(new Runnable()
                {
                                      @Override
                                      public void run()
                                      {
                                          try
                                          {
                                              String response = "";

                                              String idUser = sharedPref.getString("idUsuario", "");

                                              Uri uri = new Uri.Builder()
                                                      .scheme("http").authority("192.168.1.66")
                                                      .path("prueba.php")
                                                      .appendQueryParameter("nombreRealUsuario", nombreReal)
                                                      .appendQueryParameter("generoUsuario", generoUsuario)
                                                      .appendQueryParameter("estadoCivilUsuario", estadoCivilUsuario)
                                                      .appendQueryParameter("alturaUsuario", alturaUsuario)
                                                      .appendQueryParameter("fechaNacimientoUsuario", fechaAmericana)
                                                      .appendQueryParameter("descripcionUsuario", descripcionUsuario)
                                                      .appendQueryParameter("idUsuario", idUser)
                                                      //.appendQueryParameter("fotoPerfilusuario", "    ")
                                                      .build();//
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
                                                          editor.commit();

                                                          Intent intencion = new Intent(getActivity(), MenuPrincipalApp.class);
                                                          startActivity(intencion);
                                                          getActivity().finish();
                                                      }
                                                  });

                                                  connection.disconnect();
                                              }
                                              else
                                                  {
                                                      // Error handling code goes here
                                                      Log.println(Log.ASSERT,"Error", "Error");
                                                  }
                                          }
                                          catch (Exception e)
                                          {
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
}
