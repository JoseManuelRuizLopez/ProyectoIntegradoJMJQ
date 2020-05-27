package com.example.proyectointegradojmjq.ui.Conversaciones;

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

public class FragmentoConversaciones extends Fragment {

    private ConversacionesViewModel conversacionesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        conversacionesViewModel =
                ViewModelProviders.of(this).get(ConversacionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_conversaciones, container, false);


        return root;
    }
}