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

public class SecretaireDashboardFragment extends Fragment {

    private static final String ARG_SECRETAIRE_ID = "secretaire_id";

    private int secretaireId;
    private TextView tvRdvJour, tvEnAttente, tvUrgences;
    private RecyclerView rvActionsUrgentes;
    private MaterialCardView cardGestionRdv;
    private DatabaseHelper dbHelper;

    public SecretaireDashboardFragment() {}

    public static SecretaireDashboardFragment newInstance(int secretaireId) {
        SecretaireDashboardFragment fragment = new SecretaireDashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECRETAIRE_ID, secretaireId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            secretaireId = getArguments().getInt(ARG_SECRETAIRE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secretaire_dashboard, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        tvRdvJour = view.findViewById(R.id.tv_rdv_jour);
        tvEnAttente = view.findViewById(R.id.tv_en_attente);
        tvUrgences = view.findViewById(R.id.tv_urgences);
        rvActionsUrgentes = view.findViewById(R.id.rv_actions_urgentes);

        chargerDonnees();

        return view;
    }

    private void chargerDonnees() {
        // Charger les statistiques
        List<RendezVous> rdvJour = dbHelper.getRdvAujourdHui();
        List<RendezVous> rdvAConfirmer = dbHelper.getRdvAConfirmer();
        List<RendezVous> urgences = dbHelper.getUrgencesSecretaire();

        tvRdvJour.setText(String.valueOf(rdvJour.size()));
        tvEnAttente.setText(String.valueOf(rdvAConfirmer.size()));
        tvUrgences.setText(String.valueOf(urgences.size()));

        // Afficher les urgences
        RendezVousAdapter adapter = new RendezVousAdapter(urgences);
        rvActionsUrgentes.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvActionsUrgentes.setAdapter(adapter);
    }
}