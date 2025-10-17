package com.clinique.lasagesse;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class RegisterPatientFragment extends Fragment {

    private EditText editNom, editPrenom, editEmail, editPassword, editTelephone, editDateNaissance, editAdresse;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    public RegisterPatientFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_patient, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        editNom = view.findViewById(R.id.edit_nom);
        editPrenom = view.findViewById(R.id.edit_prenom);
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        editTelephone = view.findViewById(R.id.edit_telephone);
        editDateNaissance = view.findViewById(R.id.edit_date_naissance);
        editAdresse = view.findViewById(R.id.edit_adresse);
        btnRegister = view.findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(v -> {
            if (validerFormulaire()) {
                boolean success = dbHelper.ajouterPatient(
                        editNom.getText().toString().trim(),
                        editPrenom.getText().toString().trim(),
                        editEmail.getText().toString().trim(),
                        editPassword.getText().toString().trim(),
                        editTelephone.getText().toString().trim(),
                        editDateNaissance.getText().toString().trim(),
                        editAdresse.getText().toString().trim()
                );
                if (success) {
                    Toast.makeText(requireContext(), "Inscription réussie ! Vous pouvez maintenant vous connecter.", Toast.LENGTH_LONG).show();
                    requireActivity().onBackPressed();
                } else {
                    Toast.makeText(requireContext(), "Erreur : cet email est déjà utilisé.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean validerFormulaire() {
        if (TextUtils.isEmpty(editNom.getText())) {
            editNom.setError("Nom requis");
            editNom.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(editPrenom.getText())) {
            editPrenom.setError("Prénom requis");
            editPrenom.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(editEmail.getText())) {
            editEmail.setError("Email requis");
            editEmail.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()) {
            editEmail.setError("Email invalide");
            editEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(editPassword.getText())) {
            editPassword.setError("Mot de passe requis");
            editPassword.requestFocus();
            return false;
        }
        if (editPassword.getText().length() < 6) {
            editPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
            editPassword.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(editTelephone.getText())) {
            editTelephone.setError("Téléphone requis");
            editTelephone.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(editDateNaissance.getText())) {
            editDateNaissance.setError("Date de naissance requise");
            editDateNaissance.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(editAdresse.getText())) {
            editAdresse.setError("Adresse requise");
            editAdresse.requestFocus();
            return false;
        }
        return true;
    }
}