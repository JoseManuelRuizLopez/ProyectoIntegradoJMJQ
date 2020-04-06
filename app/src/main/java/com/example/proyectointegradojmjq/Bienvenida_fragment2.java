package com.example.proyectointegradojmjq;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


public class Bienvenida_fragment2 extends Fragment implements View.OnClickListener
{

    DatePickerDialog dialogoCalendario;
    DatePicker datePicker;
    Calendar c;

    String nombreReal;
    String fechaEspañola;
    String fechaAmericana;
    String descripcion;

    EditText txtNombreRealB;
    EditText txtFechaNacimientoB;
    EditText txtDescripcionB;

    Button btnSiguienteF2;

    public Bienvenida_fragment2()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bienvenida_fragment2, container, false);;

        txtNombreRealB = view.findViewById(R.id.txtNombreRealB);
        txtFechaNacimientoB = view.findViewById(R.id.txtFechaNacimientoB);
        txtDescripcionB = view.findViewById(R.id.txtDescripcionB);
        btnSiguienteF2 = view.findViewById(R.id.btnSiguienteWelcomeF2);

        txtFechaNacimientoB.setOnClickListener(this);
        btnSiguienteF2.setOnClickListener(this);

        c = Calendar.getInstance();
        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH);
        int dayOfMonth = c.get(c.DAY_OF_MONTH);


        dialogoCalendario = new DatePickerDialog(getContext(), android.R.style.Theme_Material_Dialog, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {

            }

        }, year, month, dayOfMonth);

        dialogoCalendario.getDatePicker().init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener()
        {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3)
            {
                fechaEspañola = i3+ "/" + (i2 + 1) + "/" + i;
                fechaAmericana = i+ "/" + (i2 + 1) + "/" + i3;
            }
        });

        dialogoCalendario.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnCalendarioDialogoAceptarB), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {
                    txtFechaNacimientoB.setText(fechaEspañola);
                }
            }
        });

        dialogoCalendario.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btnCalendarioDialogoCancelarB), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int cualBoton)
            {
                if (cualBoton == DialogInterface.BUTTON_NEGATIVE)
                {

                }
            }
        });

        dialogoCalendario.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoCalendario.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePicker = view.findViewById(R.id.dp);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.txtFechaNacimientoB:
                dialogoCalendario.show();
                break;

            case R.id.btnSiguienteWelcomeF2:

                nombreReal = txtNombreRealB.getText().toString();
                descripcion = txtDescripcionB.getText().toString();

                getActivity().getIntent().putExtra("nombreReal", nombreReal);
                getActivity().getIntent().putExtra("fechaAmericana", fechaAmericana);
                getActivity().getIntent().putExtra("descripcion", descripcion);

                ((BienvenidaUsuario)getActivity()).selectTab(2);

                break;
        }
    }
}
