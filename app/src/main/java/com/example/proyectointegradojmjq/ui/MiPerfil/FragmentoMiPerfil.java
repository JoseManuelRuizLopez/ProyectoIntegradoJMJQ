package com.example.proyectointegradojmjq.ui.MiPerfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectointegradojmjq.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FragmentoMiPerfil extends Fragment implements View.OnClickListener
{

    private MiPerfilViewModel miPerfilViewModel;



    Spinner spnMiPerfil;
    SeekBar seekBarMiPerfil;

    TextView txtAlturaMiPerfil;
    TextView txtNombrePerfilMiP;
    TextView txtDescripcionMiP;

    Button btnEditarGaleria;

    final int VALOR_MAXIMO = 220;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        miPerfilViewModel = ViewModelProviders.of(this).get(MiPerfilViewModel.class);
        View view = inflater.inflate(R.layout.fragment_miperfil, container, false);

        btnEditarGaleria = view.findViewById(R.id.btnEditarGaleriaMiP);
        btnEditarGaleria.setOnClickListener(this);

        txtDescripcionMiP = view.findViewById(R.id.txtDescripcionMiP);

        txtNombrePerfilMiP = view.findViewById(R.id.txtNombreMiP);
        txtNombrePerfilMiP.setText("Joseter aND GETTERS");

        txtAlturaMiPerfil = view.findViewById(R.id.txtSeekBarAlturaMiP);
        txtAlturaMiPerfil.setText("140cm");

        spnMiPerfil = view.findViewById(R.id.spnGeneroMiP);

        ArrayList<String> arraySpinner = new ArrayList<String>();
        arraySpinner.add(getResources().getString(R.string.seleccioneEstadoCivil));
        arraySpinner.add(getResources().getString(R.string.rBtnSolteroB));
        arraySpinner.add(getResources().getString(R.string.rBtnCasadoaB));
        arraySpinner.add(getResources().getString(R.string.rBtnViudoaB));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnMiPerfil.setAdapter(spinnerArrayAdapter);

        seekBarMiPerfil = view.findViewById(R.id.seekBarMiPerfil);
        seekBarMiPerfil.setMax(VALOR_MAXIMO);

        seekBarMiPerfil.setMax(220-140);
        seekBarMiPerfil.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                progress += 140;
                txtAlturaMiPerfil.setText(progress + "");

                int x = seekBar.getThumb().getBounds().left;

                txtDescripcionMiP.clearFocus();

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
            case R.id.btnEditarGaleriaMiP:

        }
    }

    private void centimetrosSeekbar(int cuanto)
    {
        String progreso = String.valueOf(cuanto);
        txtAlturaMiPerfil.setText(progreso + "cm");
        int posicionSeekbarLabel = (((seekBarMiPerfil.getRight() - seekBarMiPerfil.getLeft()) * seekBarMiPerfil.getProgress()) / seekBarMiPerfil.getMax()) + seekBarMiPerfil.getLeft();

        if (cuanto <=9)
        {
            txtAlturaMiPerfil.setX(posicionSeekbarLabel - 6);
        }
        else
        {
            txtAlturaMiPerfil.setX(posicionSeekbarLabel - 11);
        }
    }
}