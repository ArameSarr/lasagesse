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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedecinDashboardFragment extends Fragment {

    private static final String ARG_MEDECIN_ID = "medecin_id";
    private static final String ARG_MEDECIN_NAME = "medecin_name";

    private int medecinId;
    private String medecinName;
    private TextView tvPlanningDate, tvConsultationsCount;
    private RecyclerView rvConsultations;
    private DatabaseHelper dbHelper;

    public MedecinDashboardFragment() {}

    public static MedecinDashboardFragment newInstance(int medecinId, String medecinName) {
        MedecinDashboardFragment fragment = new MedecinDashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MEDECIN_ID, medecinId);
        args.putString(ARG_MEDECIN_NAME, medecinName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            medecinId = getArguments().getInt(ARG_MEDECIN_ID);
            medecinName = getArguments().getString(ARG_MEDECIN_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medecin_dashboard, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        tvPlanningDate = view.findViewById(R.id.tv_planning_date);
        tvConsultationsCount = view.findViewById(R.id.tv_consultations_count);
        rvConsultations = view.findViewById(R.id.rv_consultations);

        // ✅ CORRECTION : Ajouter les listeners pour toutes les cartes
        MaterialCardView cardPlanning = view.findViewById(R.id.card_planning);
        MaterialCardView cardDisponibilites = view.findViewById(R.id.card_disponibilites);
        MaterialCardView cardPatients = view.findViewById(R.id.card_patients);
        MaterialCardView cardProfil = view.findViewById(R.id.card_profil);

        cardPlanning.setOnClickListener(v -> {
            PlanningMedecinFragment fragment = PlanningMedecinFragment.newInstance(medecinId);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        // ✅ NOUVEAU : Bouton Disponibilités
        cardDisponibilites.setOnClickListener(v -> {
            DisponibilitesFragment fragment = DisponibilitesFragment.newInstance(medecinId);
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        // ✅ NOUVEAU : Bouton Patients (pour l'instant, affiche un message)
        cardPatients.setOnClickListener(v -> {
            android.widget.Toast.makeText(requireContext(),
                    "Liste des patients - En développement",
                    android.widget.Toast.LENGTH_SHORT).show();
        });

        // ✅ NOUVEAU : Bouton Profil
        cardProfil.setOnClickListener(v -> {
            ProfilMedecinFragment fragment = new ProfilMedecinFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        chargerDonnees();

        return view;
    }

    private void chargerDonnees() {
        // Afficher la date actuelle
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRENCH);
        String dateString = sdf.format(new Date());
        tvPlanningDate.setText(dateString);

        // Charger les consultations du médecin
        List<RendezVous> consultations = dbHelper.getRendezVousMedecin(medecinId);
        tvConsultationsCount.setText(consultations.size() + " consultation(s) prévue(s)");

        // Configurer le RecyclerView
        RendezVousAdapter adapter = new RendezVousAdapter(consultations);
        rvConsultations.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvConsultations.setAdapter(adapter);
    }
}