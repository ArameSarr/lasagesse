package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class PatientDashboardFragment extends Fragment {

    private static final String ARG_PATIENT_ID = "patient_id";
    private static final String ARG_PATIENT_NAME = "patient_name";

    private int patientId;
    private String patientName;
    private TextView tvWelcome, tvRdvCount, tvConfirmCount;
    private RecyclerView rvRendezVous;
    private DatabaseHelper dbHelper;

    public PatientDashboardFragment() {
        // Required empty public constructor
    }

    public static PatientDashboardFragment newInstance(int patientId, String patientName) {
        PatientDashboardFragment fragment = new PatientDashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PATIENT_ID, patientId);
        args.putString(ARG_PATIENT_NAME, patientName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getInt(ARG_PATIENT_ID);
            patientName = getArguments().getString(ARG_PATIENT_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_dashboard, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Initialiser les vues
        tvWelcome = view.findViewById(R.id.tv_welcome);
        tvRdvCount = view.findViewById(R.id.tv_rdv_count);
        tvConfirmCount = view.findViewById(R.id.tv_confirm_count);
        rvRendezVous = view.findViewById(R.id.rv_rendezvous);

        // Configurer les cartes d'action
        MaterialCardView cardPrendreRdv = view.findViewById(R.id.card_prendre_rdv);
        MaterialCardView cardMesRdv = view.findViewById(R.id.card_mes_rdv);
        MaterialCardView cardDossier = view.findViewById(R.id.card_dossier);
        MaterialCardView cardProfil = view.findViewById(R.id.card_profil);

        cardPrendreRdv.setOnClickListener(v -> {
            PrendreRdvFragment fragment = PrendreRdvFragment.newInstance(patientId);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        cardMesRdv.setOnClickListener(v -> {
            MesRdvFragment fragment = MesRdvFragment.newInstance(patientId);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        cardProfil.setOnClickListener(v -> {
            ProfilPatientFragment fragment = ProfilPatientFragment.newInstance(patientId);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        // Charger les données
        chargerDonnees();

        return view;
    }

    private void chargerDonnees() {
        // Afficher le nom du patient
        tvWelcome.setText("Bonjour " + patientName + "!");

        // Charger les rendez-vous du patient
        List<RendezVous> rdvList = dbHelper.getRendezVousPatient(patientId);

        // Calculer les statistiques
        int totalRdv = rdvList.size();
        int rdvAConfirmer = 0;
        for (RendezVous rdv : rdvList) {
            if ("À confirmer".equalsIgnoreCase(rdv.getStatut())) {
                rdvAConfirmer++;
            }
        }

        tvRdvCount.setText(String.valueOf(totalRdv));
        tvConfirmCount.setText(String.valueOf(rdvAConfirmer));

        // Configurer le RecyclerView
        RendezVousAdapter adapter = new RendezVousAdapter(rdvList);
        rvRendezVous.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRendezVous.setAdapter(adapter);
    }
}