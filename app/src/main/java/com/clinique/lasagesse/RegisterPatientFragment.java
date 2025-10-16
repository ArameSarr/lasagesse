package com.clinique.lasagesse;
import android.annotation.SuppressLint;
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
    @SuppressLint("MissingInflatedId")
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
                    Toast.makeText(requireContext(), "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de l'inscription, email déjà utilisé.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private boolean validerFormulaire() {
        if (TextUtils.isEmpty(editNom.getText())) {
            editNom.setError("Nom requis");
            return false;
        }
        if (TextUtils.isEmpty(editPrenom.getText())) {
            editPrenom.setError("Prénom requis");
            return false;
        }
        if (TextUtils.isEmpty(editEmail.getText())) {
            editEmail.setError("Email requis");
            return false;
        }
        if (TextUtils.isEmpty(editPassword.getText())) {
            editPassword.setError("Mot de passe requis");
            return false;
        }
        if (TextUtils.isEmpty(editTelephone.getText())) {
            editTelephone.setError("Téléphone requis");
            return false;
        }
        if (TextUtils.isEmpty(editDateNaissance.getText())) {
            editDateNaissance.setError("Date de naissance requise");
            return false;
        }
        if (TextUtils.isEmpty(editAdresse.getText())) {
            editAdresse.setError("Adresse requise");
            return false;
        }
        return true;
    }
}