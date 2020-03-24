package com.example.proyectointegradojmjq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView logoApp;
    TextView lblLogoApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoApp = findViewById(R.id.logoApp);
        lblLogoApp = findViewById(R.id.lblLogoApp);

        logoApp.setOnClickListener(this);
        lblLogoApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.logoApp:
                Intent intentLogin = new Intent(MainActivity.this, Login.class);
                startActivity(intentLogin);
                finish();
                break;

            case R.id.lblLogoApp:
                Intent intentCrearUsuario = new Intent(MainActivity.this, CrearUsuario.class);
                startActivity(intentCrearUsuario);
                finish();
                break;
        }
    }
}
