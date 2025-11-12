package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class StatistiquesFragment extends Fragment {

    private DatabaseHelper dbHelper;

    private TextView tvTotalPatients, tvTotalMedecins, tvTotalSecretaires;
    private TextView tvTotalRdv, tvRdvConfirmes, tvRdvAnnules;
    private TextView tvRdvMois, tvTauxOccupation;

    public StatistiquesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistiques, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Initialiser les vues
        tvTotalPatients = view.findViewById(R.id.tv_total_patients);
        tvTotalMedecins = view.findViewById(R.id.tv_total_medecins);
        tvTotalSecretaires = view.findViewById(R.id.tv_total_secretaires);
        tvTotalRdv = view.findViewById(R.id.tv_total_rdv);
        tvRdvConfirmes = view.findViewById(R.id.tv_rdv_confirmes);
        tvRdvAnnules = view.findViewById(R.id.tv_rdv_annules);
        tvRdvMois = view.findViewById(R.id.tv_rdv_mois);
        tvTauxOccupation = view.findViewById(R.id.tv_taux_occupation);

        chargerStatistiques();

        return view;
    }

    private void chargerStatistiques() {
        // Statistiques générales
        int totalPatients = dbHelper.getTotalPatients();
        int totalMedecins = dbHelper.getTotalMedecins();
        int totalSecretaires = dbHelper.getTotalSecretaires();
        int totalRdv = dbHelper.getTotalRendezVous();
        int rdvMois = dbHelper.getRendezVousMoisActuel();

        tvTotalPatients.setText(String.valueOf(totalPatients));
        tvTotalMedecins.setText(String.valueOf(totalMedecins));
        tvTotalSecretaires.setText(String.valueOf(totalSecretaires));
        tvTotalRdv.setText(String.valueOf(totalRdv));
        tvRdvMois.setText(String.valueOf(rdvMois));

        // Statistiques calculées
        // (À améliorer avec de vraies requêtes SQL)
        tvRdvConfirmes.setText("--");
        tvRdvAnnules.setText("--");
        tvTauxOccupation.setText("--");
    }
}