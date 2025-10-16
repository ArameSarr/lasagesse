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
public class MedecinDashboardFragment extends Fragment {
    private TextView tvPlanningDate, tvConsultationsCount;
    private RecyclerView rvConsultations;
    private DatabaseHelper dbHelper;
    public MedecinDashboardFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medecin_dashboard, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        tvPlanningDate = view.findViewById(R.id.tv_planning_date);
        tvConsultationsCount = view.findViewById(R.id.tv_consultations_count);
        rvConsultations = view.findViewById(R.id.rv_consultations);
        MaterialCardView cardPlanning = view.findViewById(R.id.card_planning);
        cardPlanning.setOnClickListener(v -> {
            PlanningMedecinFragment fragment = new PlanningMedecinFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });
        // Simuler données
        tvPlanningDate.setText("Lundi 14 Janvier 2024");
        tvConsultationsCount.setText("5 consultations prévues");
        // Charger liste consultations (exemple statique)
        List<RendezVous> consultations = dbHelper.getRendezVousMedecin(1); // ID médecin test
        RendezVousAdapter adapter = new RendezVousAdapter(consultations);
        rvConsultations.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvConsultations.setAdapter(adapter);
        return view;
    }
}