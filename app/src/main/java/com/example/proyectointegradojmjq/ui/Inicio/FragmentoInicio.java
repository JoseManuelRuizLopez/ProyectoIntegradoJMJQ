package com.example.proyectointegradojmjq.ui.Inicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectointegradojmjq.VistaPerfilUsuario;
import com.example.proyectointegradojmjq.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentoInicio extends Fragment {

    private InicioViewModel inicioViewModel;

    View root;

    GridView gridView;

    ArrayList<String> nombresUsuarios;
    ArrayList<String> nombres;
    ArrayList<String> edades;
    ArrayList<Integer> fotosPerfil;

    SharedPreferences sharedPref;

    //String[] nombres;
    //String[] edades;
    //int[] fotosPerfil = {R.drawable.senora, R.drawable.senior, R.drawable.senora, R.drawable.senora, R.drawable.senora, R.drawable.senora, R.drawable.senora};

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        root = inflater.inflate(R.layout.fragment_inicio, container, false);

        sharedPref = getActivity().getSharedPreferences("prefBusqueda", Context.MODE_PRIVATE);
/*
        inicioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        */

        if (sharedPref.getString("estadoCivil", "").equals("") && sharedPref.getString("genero", "").equals("")) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = null;
                        try {
                            url = new URL("http://192.168.1.42/prueba.php");

                            //Create connection
                            HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

                            //Establecer método por defecto GET
                            myConnection.setRequestMethod("GET");

                            if (myConnection.getResponseCode() == 200) {
                                InputStream responseBody = myConnection.getInputStream();
                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                                BufferedReader bR = new BufferedReader(responseBodyReader);
                                String line = "";

                                String respuesta = "";

                                while ((line = bR.readLine()) != null) {
                                    respuesta += line;
                                }

                                nombresUsuarios = new ArrayList<String>();
                                nombres = new ArrayList<String>();
                                edades = new ArrayList<String>();

                                fotosPerfil = new ArrayList<Integer>();

                                Log.println(Log.ASSERT, "Nullp", respuesta +  "");

                                JSONArray jsonArray1 = new JSONArray(respuesta);

                                Log.println(Log.ASSERT, "Nullp", jsonArray1.length() +  "");

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json_data = jsonArray1.getJSONObject(i);
                                    nombresUsuarios.add(json_data.getString("nombreUsuario"));
                                    nombres.add(json_data.getString("nombreRealUsuario"));

                                    String fecha = json_data.getString("fechaNacimientoUsuario");
                                    String fechaSplit[] = fecha.split("-");
                                    int año = Integer.parseInt(fechaSplit[0]);
                                    int mes = Integer.parseInt(fechaSplit[1]);
                                    int dia = Integer.parseInt(fechaSplit[2]);

                                    String edadUsuario = getAge(año, mes, dia) + "";

                                    edades.add(edadUsuario);

                                    fotosPerfil.add(R.drawable.usericonpred);
                                }

                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                                gridView = root.findViewById(R.id.gridview);

                                CustomAdapter customAdapter = new CustomAdapter();

                                gridView.setAdapter(customAdapter);
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(getActivity(), VistaPerfilUsuario.class);
                                        intent.putExtra("nombreUsuario", nombresUsuarios.get(i));
                                        intent.putExtra("nombre", nombres.get(i));
                                        intent.putExtra("image", fotosPerfil.get(i));
                                        intent.putExtra("edad", edades.get(i));
                                        startActivity(intent);

                                    }
                                });

                            } else {
                                Log.println(Log.ASSERT, "Error", "Error");
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = null;
                        try {
                            url = new URL("http://192.168.1.42/prueba.php?generoUsuario=" + sharedPref.getString("genero", "")
                                    + "&estadoCivilUsuario=" + sharedPref.getString("estadoCivil", "")
                                    + "&fechaMin=" + fechaAñoAprox((sharedPref.getInt("edadMax", 0)))
                                    + "&fechaMax=" + fechaAñoAprox((sharedPref.getInt("edadMin", 0))) + "");


                            Log.println(Log.ASSERT, "Max", fechaAñoAprox((sharedPref.getInt("edadMax", 0))));
                            Log.println(Log.ASSERT, "Min", fechaAñoAprox((sharedPref.getInt("edadMin", 0))));

                            //Create connection
                            HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

                            //Establecer método por defecto GET
                            myConnection.setRequestMethod("GET");

                            if (myConnection.getResponseCode() == 200) {
                                InputStream responseBody = myConnection.getInputStream();
                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                                BufferedReader bR = new BufferedReader(responseBodyReader);
                                String line = "";

                                String respuesta = "";

                                while ((line = bR.readLine()) != null) {
                                    respuesta += line;
                                }

                                nombresUsuarios = new ArrayList<String>();
                                nombres = new ArrayList<String>();
                                edades = new ArrayList<String>();

                                fotosPerfil = new ArrayList<Integer>();


                                JSONArray jsonArray1 = new JSONArray(respuesta);

                                Log.println(Log.ASSERT, "Nullp", jsonArray1.length() +  "");

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json_data = jsonArray1.getJSONObject(i);
                                    nombresUsuarios.add(json_data.getString("nombreUsuario"));
                                    nombres.add(json_data.getString("nombreRealUsuario"));

                                    String fecha = json_data.getString("fechaNacimientoUsuario");
                                    String fechaSplit[] = fecha.split("-");
                                    int año = Integer.parseInt(fechaSplit[0]);
                                    int mes = Integer.parseInt(fechaSplit[1]);
                                    int dia = Integer.parseInt(fechaSplit[2]);

                                    String edadUsuario = getAge(año, mes, dia) + "";

                                    edades.add(edadUsuario);

                                    fotosPerfil.add(R.drawable.usericonpred);
                                }

                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                                gridView = root.findViewById(R.id.gridview);

                                CustomAdapter customAdapter = new CustomAdapter();

                                gridView.setAdapter(customAdapter);
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(getActivity(), VistaPerfilUsuario.class);
                                        intent.putExtra("nombreUsuario", nombresUsuarios.get(i));
                                        intent.putExtra("nombre", nombres.get(i));
                                        intent.putExtra("image", fotosPerfil.get(i));
                                        intent.putExtra("edad", edades.get(i));
                                        startActivity(intent);

                                    }
                                });

                            } else {
                                Log.println(Log.ASSERT, "Error2", "Error");
                            }

                        } catch (MalformedURLException e) {
                            Log.println(Log.ASSERT, "Error2", "Error1");
                        }
                    } catch (
                            Exception e) {
                        Log.println(Log.ASSERT, "Error2", "Error2");
                    }
                }
            });
        }
        return root;
    }


    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return fotosPerfil.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data, null);
            //getting view in row_data
            TextView nombre = view1.findViewById(R.id.lblNombre);
            TextView edad = view1.findViewById(R.id.lblEdad);
            ImageView image = view1.findViewById(R.id.images);

            nombre.setText(nombres.get(i));
            edad.setText(edades.get(i) + " años");
            image.setImageResource(fotosPerfil.get(i));

            return view1;
        }

    }

    public String fechaAñoAprox(int años) {
        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR) - años;
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);

        return y + "-" + m + "-" + d;
    }

    public int getAge(int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0) {
            a = 0;
        }
        return a;
    }
}