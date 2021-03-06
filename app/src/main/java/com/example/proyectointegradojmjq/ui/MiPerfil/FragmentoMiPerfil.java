package com.example.proyectointegradojmjq.ui.MiPerfil;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectointegradojmjq.BienvenidaUsuario;
import com.example.proyectointegradojmjq.Chat;
import com.example.proyectointegradojmjq.CrearUsuario;
import com.example.proyectointegradojmjq.MainActivity;
import com.example.proyectointegradojmjq.MenuPrincipalApp;
import com.example.proyectointegradojmjq.R;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class FragmentoMiPerfil extends Fragment implements View.OnClickListener
{

    private MiPerfilViewModel miPerfilViewModel;

    public static ImageView imgPerfilMiP;

    Spinner spnMiPerfil;
    SeekBar seekBarMiPerfil;

    TextView txtAlturaMiPerfil;
    TextView txtNombrePerfilMiP;
    TextView txtDescripcionMiP;

    Button btnGuardarCambios;

    String nombreUsuarioReal;
    String generoUsuario;
    String estadoCivilUsuario;
    String alturaUsuario;
    String fechaNacimientoUsuario;
    String descripcionUsuario;

    String idUser;
    String fotoUsuario;

    SharedPreferences sharedPref;

    Bitmap bitmapFoto;
    String encoded;
    Picasso picasin;

    String mensaje;
    String idEmisor;
    String nombreUsuario;
    String notificado;
    String foto;

    ArrayList<String> mensajesArray;
    ArrayList<String> nombresArray;
    ArrayList<String> notificadoArray;
    ArrayList<String> idEmisoresArray;
    ArrayList<String> fotosArray;

    public static final String UPLOAD_KEY = "image";

    final int VALOR_MAXIMO = 220;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        miPerfilViewModel = ViewModelProviders.of(this).get(MiPerfilViewModel.class);
        View view = inflater.inflate(R.layout.fragment_miperfil, container, false);

        sharedPref = getActivity().getSharedPreferences("logeado", Context.MODE_PRIVATE);

        imgPerfilMiP = view.findViewById(R.id.imgPerfilMiP);
        imgPerfilMiP.setOnClickListener(this);

        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambiosMiP);

        btnGuardarCambios.setOnClickListener(this);

        txtDescripcionMiP = view.findViewById(R.id.txtDescripcionMiP);

        txtNombrePerfilMiP = view.findViewById(R.id.txtNombreMiP);
        txtNombrePerfilMiP.setText("Joseter aND GETTERS");

        txtAlturaMiPerfil = view.findViewById(R.id.txtSeekBarAlturaMiP);
        txtAlturaMiPerfil.setText("140cm");

        spnMiPerfil = view.findViewById(R.id.spnGeneroMiP);

        ArrayList<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(getResources().getString(R.string.rBtnSolteroB));
        arraySpinner.add(getResources().getString(R.string.rBtnCasadoaB));
        arraySpinner.add(getResources().getString(R.string.rBtnViudoaB));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnMiPerfil.setAdapter(spinnerArrayAdapter);

        seekBarMiPerfil = view.findViewById(R.id.seekBarMiPerfil);
        seekBarMiPerfil.setMax(VALOR_MAXIMO);

        seekBarMiPerfil.setMax(220-140);
        seekBarMiPerfil.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progress += 140;
                txtAlturaMiPerfil.setText(progress + "");
                centimetrosSeekbar(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        idUser = sharedPref.getString("idUsuario", "0");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.teamchaterinos.com/prueba.php?idUsuario=" + idUser);

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

                        JSONArray result = new JSONArray(responseStrBuilder.toString());

                        for(int i=0; i < result.length(); i++)
                        {
                            JSONObject jsonobject = result.getJSONObject(i);

                            nombreUsuarioReal = jsonobject.getString("nombreRealUsuario");
                            estadoCivilUsuario = jsonobject.getString("estadoCivilUsuario");
                            generoUsuario = jsonobject.getString("generoUsuario");
                            alturaUsuario = jsonobject.getString("alturaUsuario");
                            fechaNacimientoUsuario = jsonobject.getString("fechaNacimientoUsuario");
                            descripcionUsuario = jsonobject.getString("descripcionUsuario");

                            fotoUsuario = jsonobject.getString("fotoPerfilUsuario");
                        }


                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                String[] fechaParaCumple = fechaNacimientoUsuario.split("-");

                                int edadReal = getAge(Integer.parseInt(fechaParaCumple[0]), Integer.parseInt(fechaParaCumple[1]), Integer.parseInt(fechaParaCumple[2]));

                                String estadoCivilSpinner = estadoCivilUsuario;
                                int spinnerPosition = spinnerArrayAdapter.getPosition(estadoCivilSpinner);
                                spnMiPerfil.setSelection(spinnerPosition);

                                txtNombrePerfilMiP.setText(nombreUsuarioReal + ", " + edadReal + " años");
                                txtDescripcionMiP.setText(descripcionUsuario);
                                txtAlturaMiPerfil.setText(alturaUsuario + "cm");
                                seekBarMiPerfil.setProgress(Integer.parseInt(alturaUsuario) - 140);

                                FileUtils.deleteQuietly(getActivity().getCacheDir());
                                picasin.with(getActivity().getApplicationContext()).invalidate(fotoUsuario);
                                picasin.with(getActivity().getApplicationContext()).load(fotoUsuario).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(imgPerfilMiP);

                                Log.println(Log.ASSERT, "Error", fotoUsuario);
                            }
                        });


                        responseBody.close();
                        responseBodyReader.close();
                        myConnection.disconnect();

                    } else {
                        Log.println(Log.ASSERT, "Error", "Error");
                    }
                } catch (Exception e)
                {
                    Log.println(Log.ASSERT, "Excepción", "Error de conexión, perdona. " + e.getMessage());
                }
            }
        });


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

                        mensaje = mensaje.replace("null", "");
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

        super.onCreateView(inflater, container, savedInstanceState);

        return view;
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
    public void onClick(View v)
    {

        switch (v.getId())
        {

            case R.id.imgPerfilMiP:

                CropImage.startPickImageActivity(getActivity());

                break;


            case R.id.btnGuardarCambiosMiP:



                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {

                            bitmapFoto = ((BitmapDrawable)imgPerfilMiP.getDrawable()).getBitmap();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmapFoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream .toByteArray();

                            encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

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
                                connection.disconnect();

                                String respuestaId = "";

                                JSONArray result = new JSONArray(respuesta.toString());

                                for(int i=0; i < result.length(); i++)
                                {
                                    JSONObject jsonobject = result.getJSONObject(i);

                                    respuestaId = jsonobject.getString("idUsuario");
                                }

                            } else {
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
                            String nuevaDesc = txtDescripcionMiP.getText().toString();
                            String nuevoEstadoCivil = spnMiPerfil.getSelectedItem().toString();
                            String nuevaAltura = String.valueOf(seekBarMiPerfil.getProgress() + 140);

                            Uri uri = new Uri.Builder()
                                    .scheme("http").authority("www.teamchaterinos.com")
                                    .path("prueba.php")
                                    .appendQueryParameter("nombreRealUsuario", nombreUsuarioReal)
                                    .appendQueryParameter("generoUsuario", generoUsuario)
                                    .appendQueryParameter("estadoCivilUsuario", nuevoEstadoCivil)
                                    .appendQueryParameter("alturaUsuario", nuevaAltura)
                                    .appendQueryParameter("fechaNacimientoUsuario", fechaNacimientoUsuario)
                                    .appendQueryParameter("descripcionUsuario", nuevaDesc)
                                    .appendQueryParameter("idUsuario", idUser)
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
                                        Toast.makeText(getContext(), R.string.cambiosGuardados, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Intent intencion = new Intent(getActivity(), MenuPrincipalApp.class);
                                startActivity(intencion);
                                getActivity().finish();

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


        }
    }

    private void centimetrosSeekbar(int cuanto)
    {
        String progreso = String.valueOf(cuanto);
        txtAlturaMiPerfil.setText(progreso + "cm");
        int posicionSeekbarLabel = (((seekBarMiPerfil.getRight() - seekBarMiPerfil.getLeft()) * seekBarMiPerfil.getProgress()) / seekBarMiPerfil.getMax()) + seekBarMiPerfil.getLeft();

        if (cuanto <=9)
        {
            txtAlturaMiPerfil.setX(posicionSeekbarLabel - 6);
        }
        else
        {
            txtAlturaMiPerfil.setX(posicionSeekbarLabel - 11);
        }
    }

    public int getAge (int _year, int _month, int _day)
    {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH))))
        {
            --a;
        }
        if(a < 0)
        {
            a = 0;
        }
        return a;
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