package com.clinique.lasagesse;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
public class PrendreRdvFragment extends Fragment {

    private Spinner spinnerMedecins;
    private EditText editDate, editHeure, editMotif, editNotes;
    private CheckBox checkUrgent;
    private Button btnConfirm;
    private DatabaseHelper dbHelper;

    public PrendreRdvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prendre_rdv, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        // Initialiser les vues
        spinnerMedecins = view.findViewById(R.id.spinner_medecins);
        editDate = view.findViewById(R.id.edit_date);
        editHeure = view.findViewById(R.id.edit_heure);
        editMotif = view.findViewById(R.id.edit_motif);
        editNotes = view.findViewById(R.id.edit_notes);
        checkUrgent = view.findViewById(R.id.check_urgent);
        btnConfirm = view.findViewById(R.id.btn_confirm);

        // Configurer le spinner des médecins
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.medecins_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedecins.setAdapter(adapter);
        // Pré-remplir les champs avec des données de test
        editDate.setText("15/01/2024");
        editHeure.setText("14:30");
        editMotif.setText("Consultation de routine");

        btnConfirm.setOnClickListener(v -> {
            if (validerFormulaire()) {
                // Logique de confirmation du rendez-vous
                Toast.makeText(requireContext(), "Rendez-vous confirmé !", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });

        return view;
    }

    private boolean validerFormulaire() {
        if (spinnerMedecins.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Veuillez sélectionner un médecin", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez saisir une date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editHeure.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez saisir une heure", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editMotif.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez saisir le motif", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}