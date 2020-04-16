package com.example.proyectointegradojmjq;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Bienvenida_fragment3 extends Fragment implements View.OnClickListener
{


    Button btnSiguienteF3;
    Button btnAtrasF3;

    Spinner spnGeneroF3;
    SeekBar seekBar;

    RadioGroup rGroup;
    RadioButton rButtonSoltero;
    RadioButton rButtonCasado;
    RadioButton rButtonViudo;

    TextView txtAlturaF3;

    String generoUsuario;
    String estadoCivilUsuario;
    String alturaUsuario;


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
        seekBar = view.findViewById(R.id.seekBarMiPerfil);
        txtAlturaF3 = view.findViewById(R.id.txtSeekBarAlturaMiP);

        spnGeneroF3 = view.findViewById(R.id.spnGeneroMiP);

        ArrayList<String> arraySpinner = new ArrayList<String>();

        arraySpinner.add(getResources().getString(R.string.genero1B));
        arraySpinner.add(getResources().getString(R.string.genero2B));
        arraySpinner.add(getResources().getString(R.string.genero3B));
        arraySpinner.add(getResources().getString(R.string.genero4B));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnGeneroF3.setAdapter(spinnerArrayAdapter);


        rGroup = view.findViewById(R.id.rGroupWelcomeF3);
        rButtonSoltero = view.findViewById(R.id.rBoton1WelcomeF3);
        rButtonCasado = view.findViewById(R.id.rBoton2WelcomeF3);
        rButtonViudo = view.findViewById(R.id.rBoton3WelcomeF3);


        seekBar.setMax(VALOR_MAXIMO);

        btnSiguienteF3.setOnClickListener(this);
        btnAtrasF3.setOnClickListener(this);

        txtAlturaF3.setText("140cm");

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

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnSiguienteWelcomeF3:


                if (spnGeneroF3.getSelectedItem().toString().equals(getResources().getString(R.string.genero1B)))
                {
                    Toast.makeText(getContext(), R.string.seleccioneGeneroError, Toast.LENGTH_SHORT).show();
                }
                else if (!rButtonSoltero.isChecked() && !rButtonCasado.isChecked() && !rButtonViudo.isChecked())
                {
                    Toast.makeText(getContext(), R.string.seleccioneEstadoCivilError, Toast.LENGTH_SHORT).show();
                }
                else {

                        if (rButtonSoltero.isChecked())
                        {
                            estadoCivilUsuario = getResources().getString(R.string.rBtnSolteroB);
                        }
                        else if (rButtonCasado.isChecked())
                        {
                            estadoCivilUsuario = getResources().getString(R.string.rBtnCasadoaB);
                        }
                        else if (rButtonViudo.isChecked())
                        {
                            estadoCivilUsuario = getResources().getString(R.string.rBtnViudoaB);
                        }

                        generoUsuario = spnGeneroF3.getSelectedItem().toString();
                        alturaUsuario = String.valueOf(seekBar.getProgress() + 140);

                        Log.println(Log.ASSERT,"Resultado", "EstadoCivil: " + estadoCivilUsuario + " // Genero: " + generoUsuario + " // Altura: " + alturaUsuario);


                        getActivity().getIntent().putExtra("generoUsuario", generoUsuario);
                        getActivity().getIntent().putExtra("estadoCivilUsuario", estadoCivilUsuario);
                        getActivity().getIntent().putExtra("alturaUsuario", alturaUsuario);

                        ((BienvenidaUsuario)getActivity()).selectTab(3);
                        break;
                    }

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
