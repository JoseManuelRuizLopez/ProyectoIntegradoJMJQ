package com.example.proyectointegradojmjq.ui.Busqueda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.proyectointegradojmjq.R;

public class FragmentoBusqueda extends Fragment {

    private BusquedaViewModel busquedaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        busquedaViewModel =
                ViewModelProviders.of(this).get(BusquedaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_busqueda, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        busquedaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}