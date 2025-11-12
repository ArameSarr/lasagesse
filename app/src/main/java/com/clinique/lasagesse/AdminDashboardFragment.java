package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;

public class AdminDashboardFragment extends Fragment {

    private static final String ARG_ADMIN_ID = "admin_id";
    private static final String ARG_ADMIN_NAME = "admin_name";
    private static final String ARG_ADMIN_ROLE = "admin_role";

    private int adminId;
    private String adminName;
    private String adminRole;

    private DatabaseHelper dbHelper;

    private TextView tvWelcome;
    private TextView tvTotalPatients, tvTotalMedecins, tvTotalSecretaires;
    private TextView tvTotalRdv, tvRdvMois;

    private MaterialCardView cardGestionPatients, cardGestionMedecins;
    private MaterialCardView cardGestionSecretaires, cardHistorique;
    private MaterialCardView cardParametres, cardStatistiques;

    public AdminDashboardFragment() {}

    public static AdminDashboardFragment newInstance(int adminId, String adminName, String adminRole) {
        AdminDashboardFragment fragment = new AdminDashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ADMIN_ID, adminId);
        args.putString(ARG_ADMIN_NAME, adminName);
        args.putString(ARG_ADMIN_ROLE, adminRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            adminId = getArguments().getInt(ARG_ADMIN_ID);
            adminName = getArguments().getString(ARG_ADMIN_NAME);
            adminRole = getArguments().getString(ARG_ADMIN_ROLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Initialiser les vues
        tvWelcome = view.findViewById(R.id.tv_welcome);
        tvTotalPatients = view.findViewById(R.id.tv_total_patients);
        tvTotalMedecins = view.findViewById(R.id.tv_total_medecins);
        tvTotalSecretaires = view.findViewById(R.id.tv_total_secretaires);
        tvTotalRdv = view.findViewById(R.id.tv_total_rdv);
        tvRdvMois = view.findViewById(R.id.tv_rdv_mois);

        cardGestionPatients = view.findViewById(R.id.card_gestion_patients);
        cardGestionMedecins = view.findViewById(R.id.card_gestion_medecins);
        cardGestionSecretaires = view.findViewById(R.id.card_gestion_secretaires);
        cardHistorique = view.findViewById(R.id.card_historique);
        cardParametres = view.findViewById(R.id.card_parametres);
        cardStatistiques = view.findViewById(R.id.card_statistiques);

        // Afficher le nom de l'admin
        tvWelcome.setText("Bonjour " + adminName + " !");

        // Charger les statistiques
        chargerStatistiques();

        // Configurer les listeners
        configurerListeners();

        return view;
    }

    private void chargerStatistiques() {
        tvTotalPatients.setText(String.valueOf(dbHelper.getTotalPatients()));
        tvTotalMedecins.setText(String.valueOf(dbHelper.getTotalMedecins()));
        tvTotalSecretaires.setText(String.valueOf(dbHelper.getTotalSecretaires()));
        tvTotalRdv.setText(String.valueOf(dbHelper.getTotalRendezVous()));
        tvRdvMois.setText(String.valueOf(dbHelper.getRendezVousMoisActuel()));
    }

    private void configurerListeners() {
        cardGestionPatients.setOnClickListener(v -> {
            GestionUtilisateursFragment fragment = GestionUtilisateursFragment.newInstance("patients");
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        cardGestionMedecins.setOnClickListener(v -> {
            GestionUtilisateursFragment fragment = GestionUtilisateursFragment.newInstance("medecins");
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        cardGestionSecretaires.setOnClickListener(v -> {
            GestionUtilisateursFragment fragment = GestionUtilisateursFragment.newInstance("secretaires");
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        cardHistorique.setOnClickListener(v -> {
            HistoriqueCompletFragment fragment = new HistoriqueCompletFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        cardStatistiques.setOnClickListener(v -> {
            StatistiquesFragment fragment = new StatistiquesFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        cardParametres.setOnClickListener(v -> {
            ParametresFragment fragment = new ParametresFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });
    }
}