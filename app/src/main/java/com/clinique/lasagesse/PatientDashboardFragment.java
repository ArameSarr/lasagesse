package com.clinique.lasagesse;
import android.annotation.SuppressLint;
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

    private TextView tvWelcome, tvRdvCount, tvConfirmCount;
    private RecyclerView rvRendezVous;
    private DatabaseHelper dbHelper;

    public PatientDashboardFragment() {
        // Required empty public constructor
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
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialCardView cardPrendreRdv = view.findViewById(R.id.card_prendre_rdv);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialCardView cardMesRdv = view.findViewById(R.id.card_mes_rdv);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialCardView cardDossier = view.findViewById(R.id.card_dossier);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialCardView cardProfil = view.findViewById(R.id.card_profil);
        cardPrendreRdv.setOnClickListener(v -> {
            PrendreRdvFragment fragment = new PrendreRdvFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        cardMesRdv.setOnClickListener(v -> {
            MesRdvFragment fragment = new MesRdvFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        // Charger les données
        chargerDonnees();

        return view;
    }
    private void chargerDonnees() {
        // Simuler les données de l'utilisateur
        tvWelcome.setText("Bonjour Fatou!");

        // Statistiques (simulées pour l'exemple)
        tvRdvCount.setText("3");
        tvConfirmCount.setText("1");

        // Charger les rendez-vous
        List<RendezVous> rdvList = dbHelper.getRendezVousPatient(1); // ID 1 pour le patient de test
        RendezVousAdapter adapter = new RendezVousAdapter(rdvList);
        rvRendezVous.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRendezVous.setAdapter(adapter);
    }
}