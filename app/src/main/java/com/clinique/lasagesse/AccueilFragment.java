package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

        // Utiliser LinearLayout au lieu de Button
        LinearLayout btnPatient = view.findViewById(R.id.btn_patient);
        LinearLayout btnMedecin = view.findViewById(R.id.btn_medecin);
        LinearLayout btnSecretaire = view.findViewById(R.id.btn_secretaire);
        LinearLayout btnAdmin = view.findViewById(R.id.btn_admin);

        btnAdmin.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new LoginAdminFragment(), true);
        });
        View btnInformations = view.findViewById(R.id.btn_informations);

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