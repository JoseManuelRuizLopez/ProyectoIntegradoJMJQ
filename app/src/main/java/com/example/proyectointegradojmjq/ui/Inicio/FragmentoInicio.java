package com.example.proyectointegradojmjq.ui.Inicio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import com.example.proyectointegradojmjq.Chat;
import com.example.proyectointegradojmjq.MainActivity;
import com.example.proyectointegradojmjq.VistaPerfilUsuario;
import com.example.proyectointegradojmjq.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
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
import java.util.Random;

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

    String mensaje;
    String idEmisor;
    String nombreUser;
    String notificado;
    String idUser;

    String mensaje2;
    String idEmisor2;
    String nombreUsuario2;
    String notificado2;
    String foto2;

    ArrayList<String> mensajesArray;
    ArrayList<String> nombresArray;
    ArrayList<String> notificadoArray;
    ArrayList<String> idEmisoresArray;
    ArrayList<String> fotosArray;

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
                                    if (!json_data.getString("nombreUsuario").equals(sharedPref.getString("nombreUsuario", "")) && (json_data.getString("fechaNacimientoUsuario").contains("-"))) {
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
                                    if (!json_data.getString("nombreUsuario").equals(sharedPref.getString("nombreUsuario", "")) && (json_data.getString("fechaNacimientoUsuario").contains("-")))
                                    {
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
                                                //getActivity().finish();

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

            idUser = sharedPref.getString("idUsuario", "0");


            AsyncTask.execute(new Runnable() {
                @Override
                public void run()
                {
                    try {
                        URL url = new URL("http://www.teamchaterinos.com/pruebachat.php?idReceptorFK=" + idUser);

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

                            mensajesArray = new ArrayList<String>();
                            nombresArray = new ArrayList<String>();
                            notificadoArray = new ArrayList<String>();
                            idEmisoresArray = new ArrayList<String>();
                            fotosArray= new ArrayList<String>();

                            for (int i = 0; i < result.length(); i++)
                            {
                                JSONObject jsonobject = result.getJSONObject(i);

                                mensaje2 = jsonobject.getString("mensaje") + "\n";
                                idEmisor2 = jsonobject.getString("idEmisor");
                                nombreUsuario2 = jsonobject.getString("nombreRealUsuario");
                                notificado2 = jsonobject.getString("notificado");
                                foto2 = jsonobject.getString("fotoPerfilUsuario");

                                mensajesArray.add(mensaje2);
                                nombresArray.add(nombreUsuario2);
                                notificadoArray.add(notificado2);
                                idEmisoresArray.add(idEmisor2);
                                fotosArray.add(foto2);

                            }

                            Log.println(Log.ASSERT, "Mensaje Recibido", mensajesArray.toString());
                            Log.println(Log.ASSERT, "DE: ", nombresArray.toString());
                            Log.println(Log.ASSERT, "NOTIFICADO: ", notificadoArray.toString());
                            Log.println(Log.ASSERT, "FOTOS: ", fotosArray.toString());
                            Log.println(Log.ASSERT, "ID'S EMISORES: ", idEmisoresArray.toString());

                            //mensaje2 = mensaje2.replace("null", "");
                            //Log.println(Log.ASSERT, "DE: ", mensaje);

                            for (int i = 0; i < mensajesArray.size(); i++)
                            {

                                if (notificadoArray.get(i).equals("0"))
                                {
                                    notificandoApp(nombresArray.get(i), mensajesArray.get(i), fotosArray.get(i), idEmisoresArray.get(i));

                                    Log.println(Log.ASSERT, "NOTIFICATION: ", nombresArray.get(i) + " - - - - - - - - - - " + mensajesArray.get(i));
                                }
                            }


                            responseBody.close();
                            responseBodyReader.close();
                            myConnection.disconnect();

                        } else {
                            Log.println(Log.ASSERT, "Error", "Error");
                        }
                    } catch (Exception e) {
                        Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona2. " + e.getMessage());
                    }
                }
            });
        }

        return root;
    }


    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    public void notificandoApp(String nombreEmisor, String mensaje, String foto, String idEmisor)
    {

        int m = generateRandom();
        int h = generateRandom();

        Intent intent = new Intent(getActivity().getApplicationContext(), Chat.class);

        /*intent.putExtra("idReceptor", idEmisor);
        intent.putExtra("nombre", nombreEmisor);
        intent.putExtra("urlImagen", foto);*/

        Bundle bundle = new Bundle();
        bundle.putString("idReceptor", idEmisor);
        bundle.putString("nombre", nombreEmisor);
        bundle.putString("urlImagen", foto);

        intent.putExtras(bundle);

        Log.println(Log.ASSERT, "COMPROBEMOS", foto + "------NOMBRE-------" + nombreEmisor + "-----------IDRECEPTOR-----------" +  idEmisor);

        String CHANNEL_ID="MYCHANNEL";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_HIGH);
        //int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), h, intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getActivity().getApplicationContext(),CHANNEL_ID)
                .setContentTitle("DE: " + nombreEmisor)
                .setContentText("Mensaje: " +  mensaje)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true)
                //.addExtras(bundle)
                .setStyle(new Notification.BigTextStyle().bigText(mensaje))
                .setSmallIcon((R.drawable.chaterinoslogo))
                .build();

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(m, notification);
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

            FileUtils.deleteQuietly(getActivity().getCacheDir());
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