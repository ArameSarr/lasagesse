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
public class ProfilSecretaireFragment extends Fragment {
    private EditText editNom, editPrenom, editEmail;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int secretaireId = 1; // À remplacer par ID connecté
    public ProfilSecretaireFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_secretaire, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        editNom = view.findViewById(R.id.edit_nom);
        editPrenom = view.findViewById(R.id.edit_prenom);
        editEmail = view.findViewById(R.id.edit_email);
        btnSave = view.findViewById(R.id.btn_save);
        chargerProfil();
        btnSave.setOnClickListener(v -> {
            if (validerFormulaire()) {
                boolean success = dbHelper.mettreAJourSecretaire(
                        secretaireId,
                        editNom.getText().toString().trim(),
                        editPrenom.getText().toString().trim(),
                        editEmail.getText().toString().trim()
                );
                if (success) {
                    Toast.makeText(requireContext(), "Profil mis à jour", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private void chargerProfil() {
        Secretaire secretaire = dbHelper.getSecretaireById(secretaireId);
        if (secretaire != null) {
            editNom.setText(secretaire.getNom());
            editPrenom.setText(secretaire.getPrenom());
            editEmail.setText(secretaire.getEmail());
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
        return true;
    }
}
