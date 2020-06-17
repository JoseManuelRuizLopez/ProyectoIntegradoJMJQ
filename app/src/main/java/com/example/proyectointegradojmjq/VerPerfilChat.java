package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;


public class VerPerfilChat extends AppCompatActivity implements KeyListener {

    String nombre;
    String urlImagen;
    int edad;
    String genero;
    String estadoCivil;
    String altura;
    String descripcion;

    TextView lblNombreVPC;
    TextView lblEdadVPC;
    TextView lblGeneroVPC;
    TextView lblEstadoCivilVPC;
    TextView lblAlturaVPC;
    TextView lblDescripcionVPC;

    ImageView imgPerfilVPC;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil_chat);

        Intent intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        urlImagen = intent.getStringExtra("urlImagen");
        edad = intent.getIntExtra("edad", 8);
        genero = intent.getStringExtra("genero");
        altura = intent.getStringExtra("altura");
        estadoCivil = intent.getStringExtra("estadoCivil");
        descripcion = intent.getStringExtra("descripcion");

        lblNombreVPC = findViewById(R.id.lblNombreVPC);
        lblEdadVPC = findViewById(R.id.lblEdadVPC);
        lblEstadoCivilVPC = findViewById(R.id.lblEstadoCivilVPC);
        lblGeneroVPC = findViewById(R.id.lblGeneroVPC);
        lblAlturaVPC = findViewById(R.id.lblAlturaVPC);
        lblDescripcionVPC = findViewById(R.id.lblDescripcionVPC);

        imgPerfilVPC = findViewById(R.id.imgPerfilVPC);

        FileUtils.deleteQuietly(getApplicationContext().getCacheDir());
        Picasso.with(this).invalidate(urlImagen);
        Picasso.with(this).load(urlImagen).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(imgPerfilVPC);

        lblNombreVPC.setText(nombre);
        lblEdadVPC.setText(edad + "");
        lblAlturaVPC.setText(altura);
        lblEstadoCivilVPC.setText(estadoCivil);
        lblGeneroVPC.setText(genero);
        lblDescripcionVPC.setText(descripcion);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public int getInputType() {
        return 0;
    }

    @Override
    public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyOther(View view, Editable text, KeyEvent event) {
        return false;
    }

    @Override
    public void clearMetaKeyState(View view, Editable content, int states) {

    }
}