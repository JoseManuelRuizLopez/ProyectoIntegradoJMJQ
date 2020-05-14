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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectointegradojmjq.VistaPerfilUsuario;
import com.example.proyectointegradojmjq.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

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

    ArrayList<String> idUsuarios;
    ArrayList<String> nombresUsuarios;
    ArrayList<String> nombres;
    ArrayList<String> edades;
    ArrayList<String> fotosPerfil;

    SharedPreferences sharedPref;
    SharedPreferences sharedPrefB;

    AsyncTask myTaskerain;

    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        root = inflater.inflate(R.layout.fragment_inicio, container, false);

        sharedPref = getActivity().getSharedPreferences("logeado", Context.MODE_PRIVATE);
        sharedPrefB = getActivity().getSharedPreferences("prefBusqueda", Context.MODE_PRIVATE);
/*
        inicioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        */


        if (sharedPrefB.getString("estadoCivil", "").equals("") && sharedPrefB.getString("genero", "").equals("")) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = null;
                        try {
                            url = new URL("http://www.teamchaterinos.com/prueba.php");

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

                                idUsuarios = new ArrayList<String>();
                                nombresUsuarios = new ArrayList<String>();
                                nombres = new ArrayList<String>();
                                edades = new ArrayList<String>();
                                fotosPerfil = new ArrayList<String>();

                                Log.println(Log.ASSERT, "Nullp", respuesta + "");

                                JSONArray jsonArray1 = new JSONArray(respuesta);

                                Log.println(Log.ASSERT, "Nullp", jsonArray1.length() + "");

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json_data = jsonArray1.getJSONObject(i);

                                    String fechajsn = json_data.getString("fechaNacimientoUsuario") + "";

                                    if (!json_data.getString("nombreUsuario").equals(sharedPref.getString("nombreUsuario", ""))
                                            && (json_data.getString("fechaNacimientoUsuario").contains("-"))) {

                                        idUsuarios.add(json_data.getString("idUsuario"));
                                        nombresUsuarios.add(json_data.getString("nombreUsuario"));
                                        nombres.add(json_data.getString("nombreRealUsuario"));

                                        String fecha = json_data.getString("fechaNacimientoUsuario");
                                        String fechaSplit[] = fecha.split("-");
                                        int año = Integer.parseInt(fechaSplit[0]);
                                        int mes = Integer.parseInt(fechaSplit[1]);
                                        int dia = Integer.parseInt(fechaSplit[2]);

                                        String edadUsuario = getAge(año, mes, dia) + "";

                                        edades.add(edadUsuario);

                                        fotosPerfil.add(json_data.getString("fotoPerfilUsuario"));
                                    }
                                }

                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                                gridView = root.findViewById(R.id.gridview);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        CustomAdapter customAdapter = new CustomAdapter();

                                        gridView.setAdapter(customAdapter);
                                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent = new Intent(getActivity(), VistaPerfilUsuario.class);
                                                intent.putExtra("idUsuario", idUsuarios.get(i));
                                                intent.putExtra("nombreUsuario", nombresUsuarios.get(i));
                                                intent.putExtra("nombre", nombres.get(i));
                                                intent.putExtra("image", fotosPerfil.get(i));
                                                intent.putExtra("edad", edades.get(i));
                                                startActivity(intent);
                                                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                            }
                                        });
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

        } else if (sharedPrefB.getString("genero", "").equals("Otro")) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = null;
                        try {
                            url = new URL("http://www.teamchaterinos.com/prueba.php?estadoCivilUsuario=" + sharedPrefB.getString("estadoCivil", "")
                                    + "&fechaMin=" + fechaAñoAprox((sharedPrefB.getInt("edadMax", 0)))
                                    + "&fechaMax=" + fechaAñoAprox((sharedPrefB.getInt("edadMin", 0))) + "");

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

                                idUsuarios = new ArrayList<String>();
                                nombresUsuarios = new ArrayList<String>();
                                nombres = new ArrayList<String>();
                                edades = new ArrayList<String>();
                                fotosPerfil = new ArrayList<String>();

                                JSONArray jsonArray1 = new JSONArray(respuesta);

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json_data = jsonArray1.getJSONObject(i);
                                    if (!json_data.getString("nombreUsuario").equals(sharedPref.getString("nombreUsuario", ""))
                                            && (json_data.getString("fechaNacimientoUsuario").contains("-"))) {
                                        idUsuarios.add(json_data.getString("idUsuario"));
                                        nombresUsuarios.add(json_data.getString("nombreUsuario"));
                                        nombres.add(json_data.getString("nombreRealUsuario"));

                                        String fecha = json_data.getString("fechaNacimientoUsuario");
                                        String fechaSplit[] = fecha.split("-");
                                        int año = Integer.parseInt(fechaSplit[0]);
                                        int mes = Integer.parseInt(fechaSplit[1]);
                                        int dia = Integer.parseInt(fechaSplit[2]);

                                        String edadUsuario = getAge(año, mes, dia) + "";

                                        edades.add(edadUsuario);

                                        fotosPerfil.add(json_data.getString("fotoPerfilUsuario"));
                                    }
                                }

                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                                gridView = root.findViewById(R.id.gridview);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CustomAdapter customAdapter = new CustomAdapter();

                                        gridView.setAdapter(customAdapter);
                                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent = new Intent(getActivity(), VistaPerfilUsuario.class);
                                                intent.putExtra("idUsuario", idUsuarios.get(i));
                                                intent.putExtra("nombreUsuario", nombresUsuarios.get(i));
                                                intent.putExtra("nombre", nombres.get(i));
                                                intent.putExtra("image", fotosPerfil.get(i));
                                                intent.putExtra("edad", edades.get(i));
                                                startActivity(intent);
                                                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                            }
                                        });

                                    }
                                });
                            } else {
                                Log.println(Log.ASSERT, "Error2", "Error");
                            }

                        } catch (MalformedURLException e) {
                            Log.println(Log.ASSERT, "Error2", "Error1");
                        }
                    } catch (Exception e) {
                        Log.println(Log.ASSERT, "Error2", "Error2");
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
                            url = new URL("http://www.teamchaterinos.com/prueba.php?generoUsuario=" + sharedPrefB.getString("genero", "")
                                    + "&estadoCivilUsuario=" + sharedPrefB.getString("estadoCivil", "")
                                    + "&fechaMin=" + fechaAñoAprox((sharedPrefB.getInt("edadMax", 0)))
                                    + "&fechaMax=" + fechaAñoAprox((sharedPrefB.getInt("edadMin", 0))) + "");

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

                                idUsuarios = new ArrayList<String>();
                                nombresUsuarios = new ArrayList<String>();
                                nombres = new ArrayList<String>();
                                edades = new ArrayList<String>();
                                fotosPerfil = new ArrayList<String>();

                                JSONArray jsonArray1 = new JSONArray(respuesta);

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject json_data = jsonArray1.getJSONObject(i);
                                    if (!json_data.getString("nombreUsuario").equals(sharedPref.getString("nombreUsuario", ""))
                                            && (json_data.getString("fechaNacimientoUsuario").contains("-"))) {
                                        idUsuarios.add(json_data.getString("idUsuario"));
                                        nombresUsuarios.add(json_data.getString("nombreUsuario"));
                                        nombres.add(json_data.getString("nombreRealUsuario"));

                                        String fecha = json_data.getString("fechaNacimientoUsuario");
                                        String fechaSplit[] = fecha.split("-");
                                        int año = Integer.parseInt(fechaSplit[0]);
                                        int mes = Integer.parseInt(fechaSplit[1]);
                                        int dia = Integer.parseInt(fechaSplit[2]);

                                        String edadUsuario = getAge(año, mes, dia) + "";

                                        edades.add(edadUsuario);

                                        fotosPerfil.add(json_data.getString("fotoPerfilUsuario"));
                                    }
                                }

                                responseBody.close();
                                responseBodyReader.close();
                                myConnection.disconnect();

                                gridView = root.findViewById(R.id.gridview);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CustomAdapter customAdapter = new CustomAdapter();

                                        gridView.setAdapter(customAdapter);
                                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent = new Intent(getActivity(), VistaPerfilUsuario.class);
                                                intent.putExtra("idUsuario", idUsuarios.get(i));
                                                intent.putExtra("nombreUsuario", nombresUsuarios.get(i));
                                                intent.putExtra("nombre", nombres.get(i));
                                                intent.putExtra("image", fotosPerfil.get(i));
                                                intent.putExtra("edad", edades.get(i));
                                                startActivity(intent);
                                                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                            }
                                        });

                                    }
                                });
                            } else {
                                Log.println(Log.ASSERT, "Error2", "Error");
                            }

                        } catch (MalformedURLException e) {
                            Log.println(Log.ASSERT, "Error2", "Error1");
                        }
                    } catch (Exception e) {
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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            final View view1 = getLayoutInflater().inflate(R.layout.row_data, null);

            final TextView nombre = view1.findViewById(R.id.lblNombre);
            final TextView edad = view1.findViewById(R.id.lblEdad);
            final ImageView image = view1.findViewById(R.id.imgTablaInicio);

            nombre.setText(nombres.get(i));
            edad.setText(edades.get(i) + " años");

            Picasso.with(getActivity().getApplicationContext()).invalidate(fotosPerfil.get(i));
            Picasso.with(getActivity().getApplicationContext()).load(fotosPerfil.get(i)).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(image);

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