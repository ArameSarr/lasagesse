package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

/**
 * Fragment de connexion pour l'administrateur
 * Authentification sécurisée avec email et mot de passe
 */
public class LoginAdministrateurFragment extends Fragment {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;

    public LoginAdministrateurFragment() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_administrateur, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        btnLogin = view.findViewById(R.id.btn_login);

        // Pré-remplir avec données de test (à supprimer en production)
        editEmail.setText("admin@clinique.com");
        editPassword.setText("admin123");

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Veuillez remplir tous les champs",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérification des identifiants administrateur
            if (verifierLoginAdmin(email, password)) {
                // Connexion réussie - Charger le dashboard
                AdministrateurDashboardFragment fragment =
                        AdministrateurDashboardFragment.newInstance(1);
                ((MainActivity) requireActivity()).loadFragment(fragment, true);
            } else {
                Toast.makeText(requireContext(),
                        "Email ou mot de passe incorrect",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    /**
     * Vérifie les identifiants de l'administrateur
     * TODO: Implémenter dans DatabaseHelper
     */
    private boolean verifierLoginAdmin(String email, String password) {
        // Pour le moment, authentification simple
        // À remplacer par une vérification en base de données
        return email.equals("admin@clinique.com") && password.equals("admin123");
    }
}