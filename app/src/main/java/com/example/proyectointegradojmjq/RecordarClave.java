package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RecordarClave extends AppCompatActivity implements View.OnClickListener
{

    Button btnCancelarRC, btnEnviarRC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordar_clave);

        btnEnviarRC = findViewById(R.id.btnEnviarRC);
        btnCancelarRC = findViewById(R.id.btnCancelarRC);

        btnEnviarRC.setOnClickListener(this);
        btnCancelarRC.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnviarRC:
                Toast.makeText(this, R.string.tsCorreoEnviadoRC, Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.btnCancelarRC:
                finish();
                break;
        }
    }
}
