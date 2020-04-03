package com.example.proyectointegradojmjq;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Bienvenida_fragment1 extends Fragment implements View.OnClickListener
{

    Button btnSiguienteF1;

    public Bienvenida_fragment1()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bienvenida_fragment1, container, false);

        btnSiguienteF1 = view.findViewById(R.id.btnSiguienteWelcomeF1);
        btnSiguienteF1.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {

        switch(v.getId())
        {

            case R.id.btnSiguienteWelcomeF1:
                ((BienvenidaUsuario)getActivity()).selectTab(1);
        }

    }
}
