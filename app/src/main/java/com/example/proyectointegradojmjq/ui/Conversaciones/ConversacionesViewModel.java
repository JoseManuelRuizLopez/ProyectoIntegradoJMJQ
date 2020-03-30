package com.example.proyectointegradojmjq.ui.Conversaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConversacionesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConversacionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}