package com.clinique.lasagesse;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.List;
public class GestionRdvFragment extends Fragment {
    private EditText editSearch;
    private TabLayout tabLayout;
    private RecyclerView rvRdv;
    private DatabaseHelper dbHelper;
    private RendezVousAdapter adapter;
    public GestionRdvFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_rdv, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        editSearch = view.findViewById(R.id.edit_search);
        tabLayout = view.findViewById(R.id.tab_layout);
        rvRdv = view.findViewById(R.id.rv_rdv);
        rvRdv.setLayoutManager(new LinearLayoutManager(requireContext()));
        // Charger tous les RDV au départ
        chargerRdv("Tous");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String filtre = tab.getText().toString();
                chargerRdv(filtre);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
        return view;
    }
    private void chargerRdv(String filtre) {
        List<RendezVous> rdvList;
        switch (filtre) {
            case "Aujourd'hui":
                rdvList = dbHelper.getRdvAujourdHui();
                break;
            case "À confirmer":
                rdvList = dbHelper.getRdvAConfirmer();
                break;
            case "Urgences":
                rdvList = dbHelper.getUrgencesSecretaire();
                break;
            default:
                rdvList = dbHelper.getAllRendezVous();
                break;
        }
        adapter = new RendezVousAdapter(rdvList);
        rvRdv.setAdapter(adapter);
    }
}