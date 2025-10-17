package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class LoginPatientFragment extends Fragment {

    private EditText editEmail, editPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    public LoginPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_patient, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Correction des IDs pour correspondre au XML
        editEmail = view.findViewById(R.id.edit_identifiant_patient);
        editPassword = view.findViewById(R.id.edit_password_patient);
        btnLogin = view.findViewById(R.id.btn_login_patient);
        TextView tvRegisterLink = view.findViewById(R.id.tv_register_patient_link);

        // Pré-remplir avec des données de test
        editEmail.setText("fatou.fall@email.com");
        editPassword.setText("patient123");

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.verifierLoginPatient(email, password)) {
                // Récupérer les informations du patient
                Patient patient = dbHelper.getPatientByEmail(email);

                // Charger le dashboard patient
                PatientDashboardFragment fragment = PatientDashboardFragment.newInstance(patient.getId(), patient.getPrenom());
                ((MainActivity) requireActivity()).loadFragment(fragment, true);
            } else {
                Toast.makeText(requireContext(), "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegisterLink.setOnClickListener(v -> {
            // Charger le fragment d'inscription
            RegisterPatientFragment fragment = new RegisterPatientFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        return view;
    }
}