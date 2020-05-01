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
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

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

    Bitmap bitmapFoto;
    String encoded;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = null;

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

                CropImage.startPickImageActivity(getActivity());
                break;

            case R.id.btnSiguienteWelcomeF4:

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

                encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Log.println(Log.ASSERT,"Bitmap", encoded);

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
                                    .appendQueryParameter("fotoPerfilusuario", encoded)
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

    public String encondeandoFoto (Bitmap bitmapImagen)
    {




        return "";
    }


/*
    private void showFileChooser()
    {


        final int RESULT_GALLERY = 0;

        /*Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent , RESULT_GALLERY );

         */
/*
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try
        {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK)
                {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    imgPerfil.setImageURI(uri);
                    Log.d(TAG, "File Uri: " + uri.toString());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/

}
