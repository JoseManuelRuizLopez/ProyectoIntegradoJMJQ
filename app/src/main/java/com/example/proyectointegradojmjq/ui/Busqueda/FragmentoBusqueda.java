package com.example.proyectointegradojmjq.ui.Busqueda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.proyectointegradojmjq.R;
import com.example.proyectointegradojmjq.ui.Inicio.FragmentoInicio;

import java.util.ArrayList;

public class FragmentoBusqueda extends Fragment implements View.OnClickListener {

    private BusquedaViewModel busquedaViewModel;

    Button btnBuscar;
    TextView lblEdadMin;
    TextView lblEdadMax;
    CrystalRangeSeekbar rangeSeekbar;
    Spinner spnGeneroBusqueda;
    RadioButton rb1, rb2, rb3;


    SharedPreferences sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        busquedaViewModel = ViewModelProviders.of(this).get(BusquedaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_busqueda, container, false);

        rangeSeekbar = (CrystalRangeSeekbar) root.findViewById(R.id.rangeSeekbar1);
        lblEdadMax = root.findViewById(R.id.lblEdadMax);
        lblEdadMin = root.findViewById(R.id.lblEdadMin);
        btnBuscar = root.findViewById(R.id.btnBuscarBusqueda);
        spnGeneroBusqueda = root.findViewById(R.id.spnGeneroBusqueda);
        rb1 = root.findViewById(R.id.rBoton1Busqueda);
        rb2 = root.findViewById(R.id.rBoton2Busqueda);
        rb3 = root.findViewById(R.id.rBoton3Busqueda);

        btnBuscar.setOnClickListener(this);

        sharedPref = getActivity().getSharedPreferences("prefBusqueda", Context.MODE_PRIVATE);

        ArrayList<String> arraySpinner = new ArrayList<String>();

        arraySpinner.add(getResources().getString(R.string.genero1Busqueda));
        arraySpinner.add(getResources().getString(R.string.genero2Busqueda));
        arraySpinner.add(getResources().getString(R.string.genero3Busqueda));
        arraySpinner.add(getResources().getString(R.string.genero4Busqueda));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGeneroBusqueda.setAdapter(spinnerArrayAdapter);

        rangeSeekbar.setMaxValue(95);
        rangeSeekbar.setMinValue(18);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                lblEdadMin.setText(String.valueOf(minValue));
                lblEdadMax.setText(String.valueOf(maxValue));
            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscarBusqueda:
                String genero;
                String estadoCivil = "";
                int edadMax;
                int edadMin;

                boolean fallo = false;

                if (spnGeneroBusqueda.getSelectedItem().toString().equals(getString(R.string.genero1Busqueda))) {
                    Toast.makeText(getContext(), getString(R.string.toastGenroBusqueda), Toast.LENGTH_SHORT).show();
                    fallo = true;
                }

                if (!rb1.isChecked() && !rb2.isChecked() && !rb3.isChecked()) {
                    Toast.makeText(getContext(), getString(R.string.toastNoEstadoCivilBusqueda), Toast.LENGTH_SHORT).show();
                    fallo = true;
                }

                if (fallo == false) {
                    if (rb1.isChecked()) {
                        estadoCivil = getString(R.string.rBtnSolteroBusqueda);
                    }
                    if (rb2.isChecked()) {
                        estadoCivil = getString(R.string.rBtnCasadoaBusqueda);

                    }
                    if (rb3.isChecked()) {
                        estadoCivil = getString(R.string.rBtnViudoaBusqueda);
                    }

                    genero = spnGeneroBusqueda.getSelectedItem().toString();
                    edadMin = Integer.parseInt(lblEdadMin.getText().toString());
                    edadMax = Integer.parseInt(rangeSeekbar.getSelectedMaxValue().toString());

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("genero", genero);
                    editor.putString("estadoCivil", estadoCivil);
                    editor.putInt("edadMin", edadMin);
                    editor.putInt("edadMax", edadMax);

                    editor.commit();

                    Toast.makeText(getContext(), getString(R.string.toastPrefCambiadas), Toast.LENGTH_SHORT).show();

                }


                fallo = false;

                break;
        }
    }
}