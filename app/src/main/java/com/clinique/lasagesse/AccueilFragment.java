package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccueilFragment extends Fragment {

    public AccueilFragment() {
        // Constructeur vide requis
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accueil, container, false);

        Button btnPatient = view.findViewById(R.id.btn_patient);
        Button btnMedecin = view.findViewById(R.id.btn_medecin);
        Button btnSecretaire = view.findViewById(R.id.btn_secretaire);
        Button btnInformations = view.findViewById(R.id.btn_informations);

        btnPatient.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new LoginPatientFragment(), true);
        });

        btnMedecin.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new LoginMedecinFragment(), true);
        });

        btnSecretaire.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new LoginSecretaireFragment(), true);
        });

        btnInformations.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new InformationsFragment(), true);
        });

        return view;
    }
}