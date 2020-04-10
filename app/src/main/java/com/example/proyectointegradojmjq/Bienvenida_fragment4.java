package com.example.proyectointegradojmjq;


import android.content.Intent;
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

        lbl = view.findViewById(R.id.lblAddFotosF4);


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

                String generoUsuario = getActivity().getIntent().getExtras().getString("generoUsuario");
                String estadoCivilUsuario = getActivity().getIntent().getExtras().getString("estadoCivilUsuario");
                int alturaUsuario = getActivity().getIntent().getExtras().getInt("alturaUsuario");

                AsyncTask.execute(new Runnable()
                {
                                      @Override
                                      public void run()
                                      {
                                          try
                                          {
                                              String response = "";



                                               /*
                                              HashMap<String, String> postDataParams = new HashMap<String, String>();
                                              postDataParams.put("nombreUsuario", nombreReal);
                                              postDataParams.put("idUsuario", "1");
                                              postDataParams.put("emailUsuario", descripcionUsuario);
                                              URL url = new URL("http://192.168.1.66/prueba.php");*/



                                              Uri uri = new Uri.Builder()
                                                      .scheme("http").authority("192.168.1.66")
                                                      .path("prueba.php")
                                                      .appendQueryParameter("nombreUsuario", nombreReal)
                                                      .appendQueryParameter("emailUsuario", descripcionUsuario)
                                                      .appendQueryParameter("idUsuario", "1")
                                                      //.appendQueryParameter("telefonoContacto", "95487485")
                                                      // .appendQueryParameter("correoContacto", "95487485")
                                                      // .appendQueryParameter("idUsuario", "95487485")
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
                                                          Toast.makeText(getContext(), "¡Perfil Creado, Bienvenido¡", Toast.LENGTH_SHORT).show();
                                                          Intent intencion = new Intent(getActivity(), MenuPrincipalApp.class);
                                                          startActivity(intencion);
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
