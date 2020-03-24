package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CrearUsuario extends AppCompatActivity implements View.OnClickListener
{

    EditText txtNombreUsuario;
    EditText txtClaveUsuario;
    EditText txtEmailUsuario;
    EditText txtRepetirEmailUsuario;

    Button btnCrearUsuario;
    Button btnLimpiar;
    Button btnCancelar;

    String nombreUsuario;
    String claveUsuario;
    String emailUsuario;
    String repetirEmailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        txtNombreUsuario = findViewById(R.id.txtNombreUsuarioCU);
        txtClaveUsuario = findViewById(R.id.txtClaveUsuarioCU);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuarioCU);
        txtRepetirEmailUsuario = findViewById(R.id.txtEmailCU);

        btnCrearUsuario = findViewById(R.id.btnCrearUsuarioCU);
        btnLimpiar = findViewById(R.id.btnLimpiarCU);
        btnCancelar = findViewById(R.id.btnCancelarCU);

        btnCrearUsuario.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {

            case R.id.btnCrearUsuarioCU:

                nombreUsuario = txtNombreUsuario.getText().toString();
                claveUsuario =  txtClaveUsuario.getText().toString();
                emailUsuario = txtEmailUsuario.getText().toString();
                repetirEmailUsuario = txtRepetirEmailUsuario.getText().toString();

                txtNombreUsuario.setText(claveUsuario);
                break;

            case R.id.btnLimpiarCU:

                txtNombreUsuario.setText("");
                txtClaveUsuario.setText("");
                txtEmailUsuario.setText("");
                txtRepetirEmailUsuario.setText("");
                break;

            case R.id.btnCancelarCU:

                Intent intencionCancelar = new Intent(CrearUsuario.this, Login.class);
                startActivity(intencionCancelar);
                finish();
                break;

        }
    }
}