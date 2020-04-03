package com.example.proyectointegradojmjq.ui.Inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectointegradojmjq.R;

public class FragmentoInicio extends Fragment implements View.OnClickListener {

    private InicioViewModel inicioViewModel;

    Button btnActualizarInicio;
    ConstraintLayout contendor1;
    ConstraintLayout contendor2;
    ConstraintLayout contendor3;
    ConstraintLayout contendor4;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
/*
        inicioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        */

        btnActualizarInicio = root.findViewById(R.id.btnActualizarInicio);
        contendor1 = root.findViewById(R.id.cont1Inicio);
        contendor2 = root.findViewById(R.id.cont2Inicio);
        contendor3 = root.findViewById(R.id.cont3Inicio);
        contendor4 = root.findViewById(R.id.cont4Inicio);

        btnActualizarInicio.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnActualizarInicio:
                contendor1.setBackgroundResource(R.drawable.senora);
                contendor2.setBackgroundResource(R.drawable.senora);
                contendor3.setBackgroundResource(R.drawable.senora);
                contendor4.setBackgroundResource(R.drawable.senora);

                break;


        }
    }
}