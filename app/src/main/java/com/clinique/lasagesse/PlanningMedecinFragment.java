package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlanningMedecinFragment extends Fragment {

    private static final String ARG_MEDECIN_ID = "medecin_id";

    private int medecinId;
    private RecyclerView rvPlanning;
    private DatabaseHelper dbHelper;

    public PlanningMedecinFragment() {}

    public static PlanningMedecinFragment newInstance(int medecinId) {
        PlanningMedecinFragment fragment = new PlanningMedecinFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MEDECIN_ID, medecinId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            medecinId = getArguments().getInt(ARG_MEDECIN_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planning_medecin, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        rvPlanning = view.findViewById(R.id.rv_planning);
        rvPlanning.setLayoutManager(new LinearLayoutManager(requireContext()));

        chargerPlanning();

        return view;
    }

    private void chargerPlanning() {
        List<RendezVous> rdvList = dbHelper.getRendezVousMedecin(medecinId);
        RendezVousAdapter adapter = new RendezVousAdapter(rdvList);
        rvPlanning.setAdapter(adapter);
    }
}