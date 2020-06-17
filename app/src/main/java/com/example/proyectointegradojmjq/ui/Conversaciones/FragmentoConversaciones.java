package com.example.proyectointegradojmjq.ui.Conversaciones;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectointegradojmjq.BaseDatos;
import com.example.proyectointegradojmjq.Chat;
import com.example.proyectointegradojmjq.Message;
import com.example.proyectointegradojmjq.MessageAdapter;
import com.example.proyectointegradojmjq.R;
import com.example.proyectointegradojmjq.VistaPerfilUsuario;
import com.example.proyectointegradojmjq.ui.Inicio.FragmentoInicio;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class FragmentoConversaciones extends Fragment
{

    private ConversacionesViewModel conversacionesViewModel;


    View root;

    GridView gridView;

    ArrayList<String> idUsuarios;
    ArrayList<String> nombres;
    ArrayList<String> fotosPerfil;
    ArrayList<String> messages;
    ArrayList<String> horas;
    ArrayList<String> marcasDeTiempo;
    ArrayList<String> marcasDeFecha;
    ArrayList<String> recibidoArray;

    SharedPreferences sharedPref;
    String idUsuario;

    String id;
    String name;
    String photo;
    String sms;
    String hora;
    String recibido;

    String marcaDeTiempo;

    ImageView imgNewMensaje;

    MessageAdapter messageAdapter;
    ListView messagesView;

    BaseDatos dbHelper;
    Boolean mePertence;

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

    ArrayList<String> edadesArray;
    ArrayList<String> generoArray;
    ArrayList<String> alturaArray;
    ArrayList<String> estadoCivilArray;
    ArrayList<String> descripcionArray;
    String edad;
    String genero;
    String altura;
    String estadoCivil;
    String descripcion;

    String m;
    String t;
    String nom;
    String idUserino;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        conversacionesViewModel = ViewModelProviders.of(this).get(ConversacionesViewModel.class);
        root = inflater.inflate(R.layout.fragment_conversaciones, container, false);

        sharedPref = getActivity().getSharedPreferences("logeado", Context.MODE_PRIVATE);
        idUsuario = sharedPref.getString("idUsuario", "0");

        /*Intent intent = getActivity().getIntent();
        idReceptor = intent.getStringExtra("idReceptor");
        nombre = intent.getStringExtra("nombre");
        urlImagen = intent.getStringExtra("urlImagen");*/

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = null;
                    try {
                        url = new URL("http://www.teamchaterinos.com/pruebachat.php?idEmisor=2&idReceptorFK=" + idUsuario + "&conver=2");

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
                            nombres = new ArrayList<String>();
                            fotosPerfil = new ArrayList<String>();
                            messages = new ArrayList<String>();
                            horas = new ArrayList<String>();
                            marcasDeTiempo = new ArrayList<String>();
                            marcasDeFecha = new ArrayList<String>();
                            recibidoArray = new ArrayList<String>();

                            edadesArray = new ArrayList<String>();
                            generoArray = new ArrayList<String>();
                            alturaArray = new ArrayList<String>();
                            estadoCivilArray = new ArrayList<String>();
                            descripcionArray = new ArrayList<String>();

                            Log.println(Log.ASSERT, "RESPUESTA-->", respuesta + "");

                            JSONArray jsonArray1 = new JSONArray(respuesta);

                            Log.println(Log.ASSERT, "LONGITUD-->", jsonArray1.length() + "");

                            for (int i = 0; i < jsonArray1.length(); i++) {

                                JSONObject json_data = jsonArray1.getJSONObject(i);

                                id = json_data.getString("idUsuario");
                                name = json_data.getString("nombreRealUsuario");
                                photo = json_data.getString("fotoPerfilUsuario");
                                sms = json_data.getString("mensaje");
                                hora = json_data.getString("timeStamperino");
                                recibido = json_data.getString("recibido");

                                idUsuarios.add(id);
                                nombres.add(name);
                                fotosPerfil.add(photo);
                                messages.add(sms);
                                horas.add(hora);
                                recibidoArray.add(recibido);



                                edad = json_data.getString("fechaNacimientoUsuario");
                                genero = json_data.getString("generoUsuario");
                                altura = json_data.getString("alturaUsuario");
                                estadoCivil = json_data.getString("estadoCivilUsuario");
                                descripcion = json_data.getString("descripcionusuario");

                                edadesArray.add(edad);
                                generoArray.add(genero);
                                estadoCivilArray.add(estadoCivil);
                                alturaArray.add(altura);
                                descripcionArray.add(descripcion);

                            }

                            Log.println(Log.ASSERT, "LONGITUD-->", recibidoArray.size() + "");

                            for (int j = 0; j < horas.size(); j++) {

                                marcaDeTiempo = horas.get(j).replace("-", "/");
                                marcaDeTiempo = marcaDeTiempo.replace("//", "  ");
                                marcaDeTiempo = marcaDeTiempo.substring(0, marcaDeTiempo.lastIndexOf(":"));

                                StringTokenizer tokens = new StringTokenizer(marcaDeTiempo, "  ");
                                String first = tokens.nextToken();
                                String second = tokens.nextToken();

                                StringTokenizer tokens2 = new StringTokenizer(first, "/");
                                String first2 = tokens2.nextToken();// this will contain "Fruit"
                                String second2 = tokens2.nextToken();// this will contain " they taste good"
                                String fechaBuena = first2 + "/" + second2;

                                marcasDeTiempo.add(second);
                                marcasDeFecha.add(fechaBuena);

                                //Log.println(Log.ASSERT, "marcas-->", marc[0] + "------" + marc[1]);
                            }


                            responseBody.close();
                            responseBodyReader.close();
                            myConnection.disconnect();

                            dbHelper = new BaseDatos(getActivity().getApplicationContext());
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            Log.println(Log.ASSERT, "SQLITE1-->", " - - - - " + " - - - - - ");
                            if (db != null) {

                                try {

                                    Log.println(Log.ASSERT, "SQLITE2-->", " - - - - " + " - - - - - ");
                                    Cursor c = db.rawQuery("SELECT mensaje, timeStamperino, mePertenece, nombreEmisor, idReceptorFK FROM conversaciones group by idReceptorFK order by timeStamperino desc", null);

                                    c.moveToFirst();

                                    do {

                                        m = c.getString(c.getColumnIndex("mensaje"));
                                        t = c.getString(c.getColumnIndex("timeStamperino"));
                                        nom = c.getString(c.getColumnIndex("nombreEmisor"));
                                        idUserino = c.getString(c.getColumnIndex("idReceptorFK"));


                                        fotosPerfil.add("http://www.teamchaterinos.com/images/" + idUserino + ".png");

                                        t = t.replace("-", "/");
                                        t = t.replace("//", "  ");
                                        t = t.substring(0, t.lastIndexOf(":"));

                                        String[] hora = t.split("  ");
                                        hora[0] = hora[0].substring(0, t.lastIndexOf("/"));

                                        idUsuarios.add(idUserino);
                                        nombres.add(nom);
                                        messages.add(m);
                                        marcasDeTiempo.add(hora[1]);
                                        marcasDeFecha.add(hora[0]);

                                        Log.println(Log.ASSERT, "SQLITE-->", m + " - - - - " + t + " - - - - - " + nom);


                                    } while (c.moveToNext());
                                } catch (Exception e) {

                                }
                            }


                            gridView = root.findViewById(R.id.gridviewCv);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CustomAdapterConver customAdapter = new CustomAdapterConver();
                                    gridView.setAdapter(customAdapter);

                                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                            Intent intent = new Intent(getActivity(), Chat.class);
                                            intent.putExtra("idReceptor", idUsuarios.get(i));
                                            intent.putExtra("nombre", nombres.get(i));
                                            intent.putExtra("urlImagen", fotosPerfil.get(i));
                                            //intent.putExtra("edad", edadesArray.get(i));

                                            startActivity(intent);
                                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                        }
                                    });
                                }
                            });

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run()
                                {
                                    try {
                                        URL url = new URL("http://www.teamchaterinos.com/pruebachat.php?idReceptorFK=" + idUsuario);

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



                            Log.println(Log.ASSERT, "NOMBREDELUSER-->", marcaDeTiempo + "");

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
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), m,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getActivity().getApplicationContext(),CHANNEL_ID)
                .setContentTitle("DE: " + nombreEmisor)
                .setContentText("Mensaje: " +  mensaje)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setStyle(new Notification.BigTextStyle().bigText(mensaje))
                .setSmallIcon((R.drawable.chaterinoslogo))
                .build();

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(m, notification);
    }

    public class CustomAdapterConver extends BaseAdapter {

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

            final View view1 = getLayoutInflater().inflate(R.layout.row_data_listachats, null);

            final TextView nombre = view1.findViewById(R.id.lblNombreCv);
            final TextView mensaje = view1.findViewById(R.id.lblMensajeCv);
            final TextView lblHora = view1.findViewById(R.id.lblHora);
            final TextView lblFecha = view1.findViewById(R.id.lblFecha);

            final ImageView image = view1.findViewById(R.id.imgTablaInicioCv);
            final ImageView imgNotificado = view1.findViewById(R.id.imgNotificadoCv);

            nombre.setText(nombres.get(i));
            mensaje.setText(messages.get(i));
            lblFecha.setText(marcasDeFecha.get(i));
            lblHora.setText(marcasDeTiempo.get(i));

            FileUtils.deleteQuietly(getActivity().getCacheDir());
            Picasso.with(getActivity().getApplicationContext()).invalidate(fotosPerfil.get(i));
            Picasso.with(getActivity().getApplicationContext()).load(fotosPerfil.get(i)).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(image);

            return view1;
        }
    }
}