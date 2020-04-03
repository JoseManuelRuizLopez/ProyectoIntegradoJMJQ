package com.example.proyectointegradojmjq;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class Bienvenida_fragment3 extends Fragment implements View.OnClickListener
{


    Button btnSiguienteF3;
    Button btnAtrasF3;

    Spinner spnGeneroF3;
    SeekBar seekBar;

    TextView txtAlturaF3;


    final int VALOR_MAXIMO = 220;

    public Bienvenida_fragment3()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bienvenida_fragment3, container, false);

        btnSiguienteF3 = view.findViewById(R.id.btnSiguienteWelcomeF3);
        btnAtrasF3 = view.findViewById(R.id.btnAtrasWelcomeF4);
        seekBar = view.findViewById(R.id.seekBarWelcomeB);
        txtAlturaF3 = view.findViewById(R.id.txtSeekBarAlturaB);

        spnGeneroF3 = view.findViewById(R.id.spnGeneroF3);

        ArrayList<String> arraySpinner = new ArrayList<String>();

        arraySpinner.add(getResources().getString(R.string.genero1B));
        arraySpinner.add(getResources().getString(R.string.genero2B));
        arraySpinner.add(getResources().getString(R.string.genero3B));
        arraySpinner.add(getResources().getString(R.string.genero4B));
        arraySpinner.add(getResources().getString(R.string.genero5B));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spnGeneroF3.setAdapter(spinnerArrayAdapter);


        seekBar.setMax(VALOR_MAXIMO);

        btnSiguienteF3.setOnClickListener(this);
        btnAtrasF3.setOnClickListener(this);

        seekBar.setMax(220-140);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progress += 140;
                txtAlturaF3.setText(progress + "");

                int x = seekBar.getThumb().getBounds().left;

                centimetrosSeekbar(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        spnGeneroF3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnSiguienteWelcomeF3:

                String value1 = getActivity().getIntent().getExtras().getString("nombreReal");
                String value2 = getActivity().getIntent().getExtras().getString("fechaAmericana");
                String value3 = getActivity().getIntent().getExtras().getString("descripcion");

                ((BienvenidaUsuario)getActivity()).selectTab(3);
                break;

            case R.id.btnAtrasWelcomeF4:

                ((BienvenidaUsuario)getActivity()).selectTab(1);
                break;

        }
    }


    private void centimetrosSeekbar(int cuanto)
    {
        String progreso = String.valueOf(cuanto);
        txtAlturaF3.setText(progreso + "cm");
        int posicionSeekbarLabel = (((seekBar.getRight() - seekBar.getLeft()) * seekBar.getProgress()) / seekBar.getMax()) + seekBar.getLeft();

        if (cuanto <=9)
        {
            txtAlturaF3.setX(posicionSeekbarLabel - 6);
        }
        else
        {
            txtAlturaF3.setX(posicionSeekbarLabel - 11);
        }
    }
}
