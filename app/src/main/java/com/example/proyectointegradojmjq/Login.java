package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button btnRegistrarLogin, btnEntrarLogin;
    TextView lblRecordarClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegistrarLogin = findViewById(R.id.btnRegistrarLogin);
        lblRecordarClave = findViewById(R.id.lblRecordarClave);

        btnRegistrarLogin.setOnClickListener(this);
        lblRecordarClave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnRegistrarLogin:
                Intent intentRegistrar = new Intent(Login.this, CrearUsuario.class);
                startActivity(intentRegistrar);
                break;

            case R.id.lblRecordarClave:
                Intent intentRecordarClave = new Intent(Login.this, RecordarClave.class);
                startActivity(intentRecordarClave);
                break;
        }
    }
}
