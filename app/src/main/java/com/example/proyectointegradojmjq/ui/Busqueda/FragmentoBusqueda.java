package com.example.proyectointegradojmjq.ui.Busqueda;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.proyectointegradojmjq.Chat;
import com.example.proyectointegradojmjq.MainActivity;
import com.example.proyectointegradojmjq.MenuPrincipalApp;
import com.example.proyectointegradojmjq.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class FragmentoBusqueda extends Fragment implements View.OnClickListener {

    private BusquedaViewModel busquedaViewModel;

    Button btnEstablecer;
    Button btnDeshacer;
    TextView lblEdadMin;
    TextView lblEdadMax;
    CrystalRangeSeekbar rangeSeekbar;
    Spinner spnGeneroBusqueda;
    RadioButton rb1, rb2, rb3;
    RadioGroup rgGrupo;

    SharedPreferences sharedPref;
    SharedPreferences sharedPref2;

    String mensaje;
    String idEmisor;
    String nombreUsuario;
    String notificado;
    String idUser;
    String foto;

    ArrayList<String> mensajesArray;
    ArrayList<String> nombresArray;
    ArrayList<String> notificadoArray;
    ArrayList<String> idEmisoresArray;
    ArrayList<String> fotosArray;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        busquedaViewModel = ViewModelProviders.of(this).get(BusquedaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_busqueda, container, false);

        rangeSeekbar = (CrystalRangeSeekbar) root.findViewById(R.id.rangeSeekbar1);
        lblEdadMax = root.findViewById(R.id.lblEdadMax);
        lblEdadMin = root.findViewById(R.id.lblEdadMin);
        btnEstablecer = root.findViewById(R.id.btnBuscarBusqueda);
        btnDeshacer = root.findViewById(R.id.btnDeshacerBusqueda);
        spnGeneroBusqueda = root.findViewById(R.id.spnGeneroBusqueda);
        rb1 = root.findViewById(R.id.rBoton1Busqueda);
        rb2 = root.findViewById(R.id.rBoton2Busqueda);
        rb3 = root.findViewById(R.id.rBoton3Busqueda);
        rgGrupo = root.findViewById(R.id.rGroupBusqueda);

        btnDeshacer.setOnClickListener(this);
        btnEstablecer.setOnClickListener(this);

        sharedPref = getActivity().getSharedPreferences("prefBusqueda", Context.MODE_PRIVATE);
        sharedPref2 = getActivity().getSharedPreferences("logeado", Context.MODE_PRIVATE);

        ArrayList<String> arraySpinner = new ArrayList<String>();

        arraySpinner.add(getResources().getString(R.string.genero1Busqueda));
        arraySpinner.add(getResources().getString(R.string.genero2Busqueda));
        arraySpinner.add(getResources().getString(R.string.genero3Busqueda));
        arraySpinner.add(getResources().getString(R.string.genero4Busqueda));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGeneroBusqueda.setAdapter(spinnerArrayAdapter);

        rangeSeekbar.setMinValue(18).setMaxValue(95).apply();
        //rangeSeekbar.setMaxValue(95).apply();

        if (!sharedPref.getString("genero", "").equals("") && !sharedPref.getString("estadoCivil", "").equals("")) {
            String generospn = sharedPref.getString("genero", "");
            int spinnerPosition = spinnerArrayAdapter.getPosition(generospn);
            spnGeneroBusqueda.setSelection(spinnerPosition);

            if (sharedPref.getString("estadoCivil", "").equals(getString(R.string.rBtnSolteroBusqueda))) {
                rb1.setChecked(true);
            }

            if (sharedPref.getString("estadoCivil", "").equals(getString(R.string.rBtnCasadoaBusqueda))) {
                rb2.setChecked(true);

            }

            if (sharedPref.getString("estadoCivil", "").equals(getString(R.string.rBtnViudoaBusqueda))) {
                rb3.setChecked(true);

            }

            Log.println(Log.ASSERT, "Min", sharedPref.getInt("edadMin", 18) + "");
            Log.println(Log.ASSERT, "Max", sharedPref.getInt("edadMax", 95) + "");


            rangeSeekbar.setMinStartValue(sharedPref.getInt("edadMin", 18)).setMaxStartValue(sharedPref.getInt("edadMax", 95)).apply();
        }

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                lblEdadMin.setText(String.valueOf(minValue));
                lblEdadMax.setText(String.valueOf(maxValue));

                Log.println(Log.ASSERT, "Minimoso", minValue + "");
                Log.println(Log.ASSERT, "Maximoso", maxValue + "");
            }
        });


        idUser = sharedPref2.getString("idUsuario", "0");
        Log.println(Log.ASSERT, "Maximoso", idUser);

        Log.println(Log.ASSERT, "IDUSUARIOSO", idUser);

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

                            mensaje = jsonobject.getString("mensaje") + "\n";
                            idEmisor = jsonobject.getString("idEmisor");
                            nombreUsuario = jsonobject.getString("nombreRealUsuario");
                            notificado = jsonobject.getString("notificado");
                            foto = jsonobject.getString("fotoPerfilUsuario");

                            mensajesArray.add(mensaje);
                            nombresArray.add(nombreUsuario);
                            notificadoArray.add(notificado);
                            idEmisoresArray.add(idEmisor);
                            fotosArray.add(foto);

                        }

                        Log.println(Log.ASSERT, "Mensaje Recibido", mensajesArray.toString());
                        Log.println(Log.ASSERT, "DE: ", nombresArray.toString());
                        Log.println(Log.ASSERT, "NOTIFICADO: ", notificadoArray.toString());
                        Log.println(Log.ASSERT, "FOTOS: ", fotosArray.toString());
                        Log.println(Log.ASSERT, "ID'S EMISORES: ", idEmisoresArray.toString());

                        //mensaje = mensaje.replace("null", "");
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

        return root;
    }

    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    public void notificandoApp(String nombreEmisor, String mensaje, String foto, String idEmisor)
    {

        final int m = generateRandom();

        Intent intent=new Intent(getActivity().getApplicationContext(), Chat.class);

        intent.putExtra("idReceptor", idEmisor);
        intent.putExtra("nombre", nombreEmisor);
        intent.putExtra("urlImagen", foto);

        Log.println(Log.ASSERT, "LAFOTO", foto);


        String CHANNEL_ID="MYCHANNEL";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_HIGH);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getActivity().getApplicationContext(),CHANNEL_ID)
                .setContentTitle("DE: " + nombreEmisor)
                .setContentText("Mensaje: " +  mensaje)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true)
                .setStyle(new Notification.BigTextStyle().bigText(mensaje))
                .setSmallIcon((R.drawable.chaterinoslogo))
                .build();

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(m, notification);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscarBusqueda:
                String genero;
                String estadoCivil = "";
                int edadMax;
                int edadMin;

                boolean fallo = false;

                if (spnGeneroBusqueda.getSelectedItem().toString().equals(getString(R.string.genero1Busqueda))) {
                    Toast.makeText(getContext(), getString(R.string.toastGenroBusqueda), Toast.LENGTH_SHORT).show();
                    fallo = true;
                }

                if (!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked()) {
                    Toast.makeText(getContext(), getString(R.string.toastNoEstadoCivilBusqueda), Toast.LENGTH_SHORT).show();
                    fallo = true;
                }

                if (fallo == false) {
                    if (rb1.isChecked()) {
                        estadoCivil = getString(R.string.rBtnSolteroBusqueda);
                    }
                    if (rb2.isChecked()) {
                        estadoCivil = getString(R.string.rBtnCasadoaBusqueda);

                    }
                    if (rb3.isChecked()) {
                        estadoCivil = getString(R.string.rBtnViudoaBusqueda);
                    }

                    genero = spnGeneroBusqueda.getSelectedItem().toString();
                    edadMin = Integer.parseInt(rangeSeekbar.getSelectedMinValue().toString());
                    edadMax = Integer.parseInt(rangeSeekbar.getSelectedMaxValue().toString());

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("genero", genero);
                    editor.putString("estadoCivil", estadoCivil);
                    editor.putInt("edadMin", edadMin);
                    editor.putInt("edadMax", edadMax);

                    editor.commit();

                    Toast.makeText(getContext(), getString(R.string.toastPrefCambiadas), Toast.LENGTH_SHORT).show();

                    Intent intencion = new Intent(getActivity(), MenuPrincipalApp.class);
                    startActivity(intencion);
                    getActivity().finish();
                }


                fallo = false;

                break;

            case R.id.btnDeshacerBusqueda:

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("edadMin", 0);
                editor.putInt("edadMax", 0);
                editor.putString("genero", "");
                editor.putString("estadoCivil", "");
                editor.commit();

                rgGrupo.clearCheck();
                spnGeneroBusqueda.setSelection(0);

                rangeSeekbar.setMinStartValue(18).apply();
                rangeSeekbar.setMaxStartValue(95).apply();

                Toast.makeText(getContext(), getString(R.string.toastPrefDeshechas), Toast.LENGTH_SHORT).show();

                Intent intencion = new Intent(getActivity(), MenuPrincipalApp.class);
                startActivity(intencion);
                getActivity().finish();

                break;
        }
    }
}