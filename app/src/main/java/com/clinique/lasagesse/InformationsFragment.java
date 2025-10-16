package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class InformationsFragment extends Fragment {

    public InformationsFragment() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Pour l'instant, vous pouvez créer une vue simple
        // ou remplacez par votre layout si vous l'avez
        return inflater.inflate(android.R.layout.simple_list_item_1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialiser vos composants ici
    }
}