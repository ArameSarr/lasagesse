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
    private TextView tvRdvJour, tvEnAttente, tvUrgences;
    private RecyclerView rvActionsUrgentes;
    private DatabaseHelper dbHelper;
    public SecretaireDashboardFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secretaire_dashboard, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        tvRdvJour = view.findViewById(R.id.tv_rdv_jour);
        tvEnAttente = view.findViewById(R.id.tv_en_attente);
        tvUrgences = view.findViewById(R.id.tv_urgences);
        rvActionsUrgentes = view.findViewById(R.id.rv_actions_urgentes);
        // Simuler données
        tvRdvJour.setText("28");
        tvEnAttente.setText("5");
        tvUrgences.setText("3");
        // Charger actions urgentes (exemple statique)
        List<RendezVous> urgences = dbHelper.getUrgencesSecretaire();
        RendezVousAdapter adapter = new RendezVousAdapter(urgences);
        rvActionsUrgentes.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvActionsUrgentes.setAdapter(adapter);
        return view;
    }
}
