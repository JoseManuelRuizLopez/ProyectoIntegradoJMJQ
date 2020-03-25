package com.example.proyectointegradojmjq;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecordarClave extends AppCompatActivity implements View.OnClickListener
{

    Button btnCancelarRC;
    Button btnEnviarRC;

    TextView txtCorreo;

    String correoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordar_clave);

        btnEnviarRC = findViewById(R.id.btnEnviarRC);
        btnCancelarRC = findViewById(R.id.btnCancelarRC);

        txtCorreo = findViewById(R.id.txtCorreoRC);

        btnEnviarRC.setOnClickListener(this);
        btnCancelarRC.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnEnviarRC:

                correoUsuario = txtCorreo.getText().toString();

                Toast.makeText(this, R.string.tsCorreoEnviadoRC, Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.btnCancelarRC:
                finish();
                break;
        }
    }
}
