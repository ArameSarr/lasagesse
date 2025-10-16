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
public class ProfilMedecinFragment extends Fragment {
    private EditText editNom, editPrenom, editEmail, editSpecialite, editCabinet;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int medecinId = 1; // À remplacer par ID connecté
    public ProfilMedecinFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_medecin, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        editNom = view.findViewById(R.id.edit_nom);
        editPrenom = view.findViewById(R.id.edit_prenom);
        editEmail = view.findViewById(R.id.edit_email);
        editSpecialite = view.findViewById(R.id.edit_specialite);
        editCabinet = view.findViewById(R.id.edit_cabinet);
        btnSave = view.findViewById(R.id.btn_save);
        chargerProfil();
        btnSave.setOnClickListener(v -> {
            if (validerFormulaire()) {
                boolean success = dbHelper.mettreAJourMedecin(
                        medecinId,
                        editNom.getText().toString().trim(),
                        editPrenom.getText().toString().trim(),
                        editEmail.getText().toString().trim(),
                        editSpecialite.getText().toString().trim(),
                        editCabinet.getText().toString().trim()
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
        Medecin medecin = dbHelper.getMedecinById(medecinId);
        if (medecin != null) {
            editNom.setText(medecin.getNom());
            editPrenom.setText(medecin.getPrenom());
            editEmail.setText(medecin.getEmail());
            editSpecialite.setText(medecin.getSpecialite());
            editCabinet.setText(medecin.getCabinet());
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
        if (TextUtils.isEmpty(editSpecialite.getText())) {
            editSpecialite.setError("Spécialité requise");
            return false;
        }
        if (TextUtils.isEmpty(editCabinet.getText())) {
            editCabinet.setError("Cabinet requis");
            return false;
        }
        return true;
    }
}