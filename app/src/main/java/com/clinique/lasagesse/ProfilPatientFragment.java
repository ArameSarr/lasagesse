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

public class ProfilPatientFragment extends Fragment {

    private static final String ARG_PATIENT_ID = "patient_id";

    private int patientId;
    private EditText editNom, editPrenom, editEmail, editTelephone, editDateNaissance, editAdresse;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    public ProfilPatientFragment() {}

    public static ProfilPatientFragment newInstance(int patientId) {
        ProfilPatientFragment fragment = new ProfilPatientFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PATIENT_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getInt(ARG_PATIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_patient, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        editNom = view.findViewById(R.id.edit_nom);
        editPrenom = view.findViewById(R.id.edit_prenom);
        editEmail = view.findViewById(R.id.edit_email);
        editTelephone = view.findViewById(R.id.edit_telephone);
        editDateNaissance = view.findViewById(R.id.edit_date_naissance);
        editAdresse = view.findViewById(R.id.edit_adresse);
        btnSave = view.findViewById(R.id.btn_save);

        chargerProfil();

        btnSave.setOnClickListener(v -> {
            if (validerFormulaire()) {
                boolean success = dbHelper.mettreAJourPatient(
                        patientId,
                        editNom.getText().toString().trim(),
                        editPrenom.getText().toString().trim(),
                        editEmail.getText().toString().trim(),
                        editTelephone.getText().toString().trim(),
                        editDateNaissance.getText().toString().trim(),
                        editAdresse.getText().toString().trim()
                );
                if (success) {
                    Toast.makeText(requireContext(), "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void chargerProfil() {
        Patient patient = dbHelper.getPatientById(patientId);
        if (patient != null) {
            editNom.setText(patient.getNom());
            editPrenom.setText(patient.getPrenom());
            editEmail.setText(patient.getEmail());
            editTelephone.setText(patient.getTelephone());
            editDateNaissance.setText(patient.getDateNaissance());
            editAdresse.setText(patient.getAdresse());
        }
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