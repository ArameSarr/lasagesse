package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Fragment pour gérer tous les utilisateurs de la clinique
 * Permet de voir, ajouter, modifier et supprimer patients, médecins et secrétaires
 */
public class GestionUtilisateursFragment extends Fragment {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAjouter;
    private DatabaseHelper dbHelper;
    private String typeUtilisateurActuel = "Patients";

    public GestionUtilisateursFragment() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_utilisateurs,
                container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Initialiser les vues
        tabLayout = view.findViewById(R.id.tab_layout_utilisateurs);
        recyclerView = view.findViewById(R.id.rv_utilisateurs);
        fabAjouter = view.findViewById(R.id.fab_ajouter_utilisateur);

        // Configurer le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Charger les patients par défaut
        chargerUtilisateurs("Patients");

        // Gérer les changements d'onglets
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String type = tab.getText().toString();
                typeUtilisateurActuel = type;
                chargerUtilisateurs(type);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Bouton d'ajout
        fabAjouter.setOnClickListener(v -> {
            ouvrirFormulaireAjout(typeUtilisateurActuel);
        });

        return view;
    }

    /**
     * Charge la liste des utilisateurs selon le type sélectionné
     */
    private void chargerUtilisateurs(String type) {
        switch (type) {
            case "Patients":
                // Charger la liste des patients
                // TODO: Implémenter UtilisateurAdapter
                Toast.makeText(requireContext(),
                        "Chargement des patients...",
                        Toast.LENGTH_SHORT).show();
                break;

            case "Médecins":
                // Charger la liste des médecins
                Toast.makeText(requireContext(),
                        "Chargement des médecins...",
                        Toast.LENGTH_SHORT).show();
                break;

            case "Secrétaires":
                // Charger la liste des secrétaires
                Toast.makeText(requireContext(),
                        "Chargement des secrétaires...",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Ouvre le formulaire d'ajout d'utilisateur
     */
    private void ouvrirFormulaireAjout(String type) {
        Fragment fragment;
        switch (type) {
            case "Patients":
                fragment = new RegisterPatientFragment();
                break;
            case "Médecins":
                fragment = new RegisterMedecinFragment();
                break;
            case "Secrétaires":
                fragment = new RegisterSecretaireFragment();
                break;
            default:
                return;
        }
        ((MainActivity) requireActivity()).loadFragment(fragment, true);
    }
}