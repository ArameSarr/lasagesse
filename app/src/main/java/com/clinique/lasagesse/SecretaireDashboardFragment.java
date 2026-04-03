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

/**
 * Dashboard de la secrétaire médicale
 * Permet de gérer les RDV et voir les statistiques
 */
public class SecretaireDashboardFragment extends Fragment {

    private static final String ARG_SECRETAIRE_ID = "secretaire_id";

    private int secretaireId;
    private TextView tvRdvJour, tvEnAttente, tvUrgences;
    private RecyclerView rvActionsUrgentes;
    private DatabaseHelper dbHelper;

    public SecretaireDashboardFragment() {
        // Constructeur vide requis
    }

    /**
     * Créer une nouvelle instance du fragment
     */
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

        // Initialiser la base de données
        dbHelper = new DatabaseHelper(requireContext());

        // Initialiser les vues
        tvRdvJour = view.findViewById(R.id.tv_rdv_jour);
        tvEnAttente = view.findViewById(R.id.tv_en_attente);
        tvUrgences = view.findViewById(R.id.tv_urgences);
        rvActionsUrgentes = view.findViewById(R.id.rv_actions_urgentes);

        // Récupérer les cartes d'action
        MaterialCardView cardGestionRdv = view.findViewById(R.id.card_gestion_rdv);
        // ❌ SUPPRIMÉ : cardNouveauRdv, cardPatients, cardStatistiques

        // ✅ Configuration du seul listener actif : Gestion des RDV
        configurerListeners(cardGestionRdv);

        // Charger les données
        chargerDonnees();

        return view;
    }

    /**
     * Configure le listener du bouton Gestion des RDV uniquement
     */
    private void configurerListeners(MaterialCardView cardGestionRdv) {
        // ✅ Bouton Gestion des RDV (SEUL ACTIF)
        cardGestionRdv.setOnClickListener(v -> {
            GestionRdvFragment fragment = new GestionRdvFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        // ❌ SUPPRIMÉ : Bouton Nouveau RDV
        // ❌ SUPPRIMÉ : Bouton Gestion des Patients
        // ❌ SUPPRIMÉ : Bouton Statistiques
    }

    /**
     * Charge toutes les données depuis la base de données
     */
    private void chargerDonnees() {
        // Charger les statistiques du jour
        List<RendezVous> rdvJour = dbHelper.getRdvAujourdHui();
        List<RendezVous> rdvAConfirmer = dbHelper.getRdvAConfirmer();
        List<RendezVous> urgences = dbHelper.getUrgencesSecretaire();

        // Afficher les compteurs
        tvRdvJour.setText(String.valueOf(rdvJour.size()));
        tvEnAttente.setText(String.valueOf(rdvAConfirmer.size()));
        tvUrgences.setText(String.valueOf(urgences.size()));

        // Afficher la liste des urgences dans le RecyclerView
        RendezVousAdapter adapter = new RendezVousAdapter(urgences);
        rvActionsUrgentes.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvActionsUrgentes.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recharger les données quand on revient sur ce fragment
        if (dbHelper != null) {
            chargerDonnees();
        }
    }
}