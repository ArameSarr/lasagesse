package com.clinique.lasagesse;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class GestionUtilisateursFragment extends Fragment {

    private static final String ARG_TYPE = "type";

    private String typeUtilisateur; // "patients", "medecins", "secretaires"
    private DatabaseHelper dbHelper;

    private TextView tvTitle, tvCount;
    private RecyclerView recyclerView;
    private MaterialButton btnAjouter;

    public GestionUtilisateursFragment() {}

    public static GestionUtilisateursFragment newInstance(String type) {
        GestionUtilisateursFragment fragment = new GestionUtilisateursFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typeUtilisateur = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_utilisateurs, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        tvTitle = view.findViewById(R.id.tv_title);
        tvCount = view.findViewById(R.id.tv_count);
        recyclerView = view.findViewById(R.id.rv_utilisateurs);
        btnAjouter = view.findViewById(R.id.btn_ajouter);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Configurer selon le type
        configurerInterface();
        chargerUtilisateurs();

        btnAjouter.setOnClickListener(v -> afficherDialogueAjout());

        return view;
    }

    private void configurerInterface() {
        switch (typeUtilisateur) {
            case "patients":
                tvTitle.setText("👥 Gestion des Patients");
                break;
            case "medecins":
                tvTitle.setText("👨‍⚕️ Gestion des Médecins");
                break;
            case "secretaires":
                tvTitle.setText("👩‍💼 Gestion des Secrétaires");
                break;
        }
    }

    private void chargerUtilisateurs() {
        switch (typeUtilisateur) {
            case "patients":
                List<Patient> patients = dbHelper.getAllPatients();
                tvCount.setText(patients.size() + " patient(s)");
                UtilisateursAdapter<Patient> adapterP = new UtilisateursAdapter<>(
                        patients,
                        this::modifierPatient,
                        this::supprimerPatient
                );
                recyclerView.setAdapter(adapterP);
                break;

            case "medecins":
                List<Medecin> medecins = dbHelper.getAllMedecins();
                tvCount.setText(medecins.size() + " médecin(s)");
                UtilisateursAdapter<Medecin> adapterM = new UtilisateursAdapter<>(
                        medecins,
                        this::modifierMedecin,
                        this::supprimerMedecin
                );
                recyclerView.setAdapter(adapterM);
                break;

            case "secretaires":
                List<Secretaire> secretaires = dbHelper.getAllSecretaires();
                tvCount.setText(secretaires.size() + " secrétaire(s)");
                UtilisateursAdapter<Secretaire> adapterS = new UtilisateursAdapter<>(
                        secretaires,
                        this::modifierSecretaire,
                        this::supprimerSecretaire
                );
                recyclerView.setAdapter(adapterS);
                break;
        }
    }

    private void afficherDialogueAjout() {
        switch (typeUtilisateur) {
            case "patients":
                ((MainActivity) requireActivity()).loadFragment(new RegisterPatientFragment(), true);
                break;
            case "medecins":
                ((MainActivity) requireActivity()).loadFragment(new RegisterMedecinFragment(), true);
                break;
            case "secretaires":
                ((MainActivity) requireActivity()).loadFragment(new RegisterSecretaireFragment(), true);
                break;
        }
    }

    // Méthodes de modification
    private void modifierPatient(Patient patient) {
        ProfilPatientFragment fragment = ProfilPatientFragment.newInstance(patient.getId());
        ((MainActivity) requireActivity()).loadFragment(fragment, true);
    }

    private void modifierMedecin(Medecin medecin) {
        // Implémenter l'édition du médecin
        Toast.makeText(requireContext(), "Modification : " + medecin.getNom(), Toast.LENGTH_SHORT).show();
    }

    private void modifierSecretaire(Secretaire secretaire) {
        // Implémenter l'édition de la secrétaire
        Toast.makeText(requireContext(), "Modification : " + secretaire.getNom(), Toast.LENGTH_SHORT).show();
    }

    // Méthodes de suppression
    private void supprimerPatient(Patient patient) {
        confirmerSuppression("patient", patient.getNom() + " " + patient.getPrenom(),
                () -> {
                    if (dbHelper.supprimerPatient(patient.getId())) {
                        Toast.makeText(requireContext(), "✅ Patient supprimé", Toast.LENGTH_SHORT).show();
                        chargerUtilisateurs();
                    } else {
                        Toast.makeText(requireContext(), "❌ Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void supprimerMedecin(Medecin medecin) {
        confirmerSuppression("médecin", medecin.getNom() + " " + medecin.getPrenom(),
                () -> {
                    if (dbHelper.supprimerMedecin(medecin.getId())) {
                        Toast.makeText(requireContext(), "✅ Médecin supprimé", Toast.LENGTH_SHORT).show();
                        chargerUtilisateurs();
                    } else {
                        Toast.makeText(requireContext(), "❌ Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void supprimerSecretaire(Secretaire secretaire) {
        confirmerSuppression("secrétaire", secretaire.getNom() + " " + secretaire.getPrenom(),
                () -> {
                    if (dbHelper.supprimerSecretaire(secretaire.getId())) {
                        Toast.makeText(requireContext(), "✅ Secrétaire supprimée", Toast.LENGTH_SHORT).show();
                        chargerUtilisateurs();
                    } else {
                        Toast.makeText(requireContext(), "❌ Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void confirmerSuppression(String type, String nom, Runnable onConfirm) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmer la suppression")
                .setMessage("Voulez-vous vraiment supprimer ce " + type + " ?\n\n" + nom +
                        "\n\n⚠️ Cette action est irréversible et supprimera également tous les rendez-vous associés.")
                .setPositiveButton("Supprimer", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Annuler", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        chargerUtilisateurs();
    }
}