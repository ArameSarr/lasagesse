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

public class HistoriqueCompletFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private TextView tvCount;

    public HistoriqueCompletFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historique_complet, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        recyclerView = view.findViewById(R.id.rv_historique);
        tvCount = view.findViewById(R.id.tv_count);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        chargerHistorique();

        return view;
    }

    private void chargerHistorique() {
        List<RendezVous> rdvList = dbHelper.getAllRendezVous();
        tvCount.setText(rdvList.size() + " rendez-vous au total");

        RendezVousAdapter adapter = new RendezVousAdapter(rdvList);
        recyclerView.setAdapter(adapter);
    }
}