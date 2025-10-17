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

public class MesRdvFragment extends Fragment {

    private static final String ARG_PATIENT_ID = "patient_id";

    private int patientId;
    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private DatabaseHelper dbHelper;

    public MesRdvFragment() {
        // Constructeur vide requis
    }

    public static MesRdvFragment newInstance(int patientId) {
        MesRdvFragment fragment = new MesRdvFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PATIENT_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getInt(ARG_PATIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mes_rdv, container, false);

        recyclerView = view.findViewById(R.id.rv_mes_rdv);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        chargerRendezVous();

        return view;
    }

    private void chargerRendezVous() {
        dbHelper = new DatabaseHelper(requireContext());
        List<RendezVous> rdvList = dbHelper.getRendezVousPatient(patientId);

        if (rdvList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
            tvEmptyState.setText("Vous n'avez pas encore de rendez-vous");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);

            RendezVousAdapter adapter = new RendezVousAdapter(rdvList);
            recyclerView.setAdapter(adapter);
        }
    }
}