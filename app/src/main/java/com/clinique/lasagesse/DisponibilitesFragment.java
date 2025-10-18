package com.clinique.lasagesse;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment pour gérer les disponibilités du médecin
 */
public class DisponibilitesFragment extends Fragment {

    private static final String ARG_MEDECIN_ID = "medecin_id";

    private int medecinId;
    private DatabaseHelper dbHelper;

    // Jours de la semaine
    private CheckBox cbLundi, cbMardi, cbMercredi, cbJeudi, cbVendredi, cbSamedi, cbDimanche;

    // Horaires
    private EditText etHeureDebut, etHeureFin;
    private EditText etDureeConsultation;

    // Pause déjeuner
    private CheckBox cbPauseDejeune;
    private EditText etPauseDebut, etPauseFin;

    // Boutons
    private Button btnSauvegarder, btnReinitialiser;

    public DisponibilitesFragment() {}

    public static DisponibilitesFragment newInstance(int medecinId) {
        DisponibilitesFragment fragment = new DisponibilitesFragment();
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
        View view = inflater.inflate(R.layout.fragment_disponibilites, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        initialiserVues(view);
        configurerListeners();
        chargerDisponibilites();

        return view;
    }

    private void initialiserVues(View view) {
        // Jours de la semaine
        cbLundi = view.findViewById(R.id.cb_lundi);
        cbMardi = view.findViewById(R.id.cb_mardi);
        cbMercredi = view.findViewById(R.id.cb_mercredi);
        cbJeudi = view.findViewById(R.id.cb_jeudi);
        cbVendredi = view.findViewById(R.id.cb_vendredi);
        cbSamedi = view.findViewById(R.id.cb_samedi);
        cbDimanche = view.findViewById(R.id.cb_dimanche);

        // Horaires
        etHeureDebut = view.findViewById(R.id.et_heure_debut);
        etHeureFin = view.findViewById(R.id.et_heure_fin);
        etDureeConsultation = view.findViewById(R.id.et_duree_consultation);

        // Pause
        cbPauseDejeune = view.findViewById(R.id.cb_pause_dejeuner);
        etPauseDebut = view.findViewById(R.id.et_pause_debut);
        etPauseFin = view.findViewById(R.id.et_pause_fin);

        // Boutons
        btnSauvegarder = view.findViewById(R.id.btn_sauvegarder);
        btnReinitialiser = view.findViewById(R.id.btn_reinitialiser);
    }

    private void configurerListeners() {
        // TimePickerDialog pour heure de début
        etHeureDebut.setOnClickListener(v -> afficherTimePicker(etHeureDebut));

        // TimePickerDialog pour heure de fin
        etHeureFin.setOnClickListener(v -> afficherTimePicker(etHeureFin));

        // TimePickerDialog pour pause début
        etPauseDebut.setOnClickListener(v -> afficherTimePicker(etPauseDebut));

        // TimePickerDialog pour pause fin
        etPauseFin.setOnClickListener(v -> afficherTimePicker(etPauseFin));

        // Activer/désactiver les champs de pause
        cbPauseDejeune.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etPauseDebut.setEnabled(isChecked);
            etPauseFin.setEnabled(isChecked);
        });

        // Bouton sauvegarder
        btnSauvegarder.setOnClickListener(v -> sauvegarderDisponibilites());

        // Bouton réinitialiser
        btnReinitialiser.setOnClickListener(v -> reinitialiserFormulaire());
    }

    private void afficherTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, minuteOfHour);
                    editText.setText(time);
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    private void chargerDisponibilites() {
        // Charger les disponibilités existantes depuis la base de données
        // Pour l'instant, on met des valeurs par défaut

        // Jours par défaut (Lundi à Vendredi)
        cbLundi.setChecked(true);
        cbMardi.setChecked(true);
        cbMercredi.setChecked(true);
        cbJeudi.setChecked(true);
        cbVendredi.setChecked(true);

        // Horaires par défaut
        etHeureDebut.setText("08:00");
        etHeureFin.setText("18:00");
        etDureeConsultation.setText("30");

        // Pause déjeuner par défaut
        cbPauseDejeune.setChecked(true);
        etPauseDebut.setText("12:00");
        etPauseFin.setText("14:00");
    }

    private void sauvegarderDisponibilites() {
        if (!validerFormulaire()) {
            return;
        }

        // Récupérer les données
        String heureDebut = etHeureDebut.getText().toString();
        String heureFin = etHeureFin.getText().toString();
        String duree = etDureeConsultation.getText().toString();

        boolean pauseActive = cbPauseDejeune.isChecked();
        String pauseDebut = pauseActive ? etPauseDebut.getText().toString() : "";
        String pauseFin = pauseActive ? etPauseFin.getText().toString() : "";

        // Créer un objet Disponibilite et sauvegarder
        // TODO: Implémenter la sauvegarde dans la base de données

        Toast.makeText(requireContext(),
                "Disponibilités sauvegardées avec succès !",
                Toast.LENGTH_SHORT).show();

        requireActivity().onBackPressed();
    }

    private boolean validerFormulaire() {
        if (etHeureDebut.getText().toString().isEmpty()) {
            etHeureDebut.setError("Heure de début requise");
            return false;
        }

        if (etHeureFin.getText().toString().isEmpty()) {
            etHeureFin.setError("Heure de fin requise");
            return false;
        }

        if (etDureeConsultation.getText().toString().isEmpty()) {
            etDureeConsultation.setError("Durée requise");
            return false;
        }

        if (!cbLundi.isChecked() && !cbMardi.isChecked() && !cbMercredi.isChecked() &&
                !cbJeudi.isChecked() && !cbVendredi.isChecked() && !cbSamedi.isChecked() &&
                !cbDimanche.isChecked()) {
            Toast.makeText(requireContext(),
                    "Veuillez sélectionner au moins un jour",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cbPauseDejeune.isChecked()) {
            if (etPauseDebut.getText().toString().isEmpty()) {
                etPauseDebut.setError("Heure de début de pause requise");
                return false;
            }
            if (etPauseFin.getText().toString().isEmpty()) {
                etPauseFin.setError("Heure de fin de pause requise");
                return false;
            }
        }

        return true;
    }

    private void reinitialiserFormulaire() {
        cbLundi.setChecked(false);
        cbMardi.setChecked(false);
        cbMercredi.setChecked(false);
        cbJeudi.setChecked(false);
        cbVendredi.setChecked(false);
        cbSamedi.setChecked(false);
        cbDimanche.setChecked(false);

        etHeureDebut.setText("");
        etHeureFin.setText("");
        etDureeConsultation.setText("");

        cbPauseDejeune.setChecked(false);
        etPauseDebut.setText("");
        etPauseFin.setText("");
    }
}