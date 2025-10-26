package com.clinique.lasagesse;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment pour gérer les disponibilités du médecin
 * Permet de définir les jours et horaires de consultation
 */
public class DisponibilitesFragment extends Fragment {

    private static final String ARG_MEDECIN_ID = "medecin_id";
    private static final String PREFS_NAME = "DisponibilitesPrefs";

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

    public DisponibilitesFragment() {
        // Constructeur vide requis
    }

    /**
     * Créer une nouvelle instance du fragment
     */
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

        // Initialiser toutes les vues
        initialiserVues(view);

        // Configurer les listeners
        configurerListeners();

        // Charger les disponibilités existantes
        chargerDisponibilites();

        return view;
    }

    /**
     * Initialise toutes les vues du fragment
     */
    private void initialiserVues(View view) {
        // Jours de la semaine
        cbLundi = view.findViewById(R.id.cb_lundi);
        cbMardi = view.findViewById(R.id.cb_mardi);
        cbMercredi = view.findViewById(R.id.cb_mercredi);
        cbJeudi = view.findViewById(R.id.cb_jeudi);
        cbVendredi = view.findViewById(R.id.cb_vendredi);
        cbSamedi = view.findViewById(R.id.cb_samedi);
        cbDimanche = view.findViewById(R.id.cb_dimanche);

        // Horaires de consultation
        etHeureDebut = view.findViewById(R.id.et_heure_debut);
        etHeureFin = view.findViewById(R.id.et_heure_fin);
        etDureeConsultation = view.findViewById(R.id.et_duree_consultation);

        // Pause déjeuner
        cbPauseDejeune = view.findViewById(R.id.cb_pause_dejeuner);
        etPauseDebut = view.findViewById(R.id.et_pause_debut);
        etPauseFin = view.findViewById(R.id.et_pause_fin);

        // Boutons d'action
        btnSauvegarder = view.findViewById(R.id.btn_sauvegarder);
        btnReinitialiser = view.findViewById(R.id.btn_reinitialiser);
    }

    /**
     * Configure tous les listeners
     */
    private void configurerListeners() {
        // TimePickerDialog pour heure de début
        etHeureDebut.setOnClickListener(v -> afficherTimePicker(etHeureDebut, "Heure de début"));

        // TimePickerDialog pour heure de fin
        etHeureFin.setOnClickListener(v -> afficherTimePicker(etHeureFin, "Heure de fin"));

        // TimePickerDialog pour pause début
        etPauseDebut.setOnClickListener(v -> afficherTimePicker(etPauseDebut, "Début pause"));

        // TimePickerDialog pour pause fin
        etPauseFin.setOnClickListener(v -> afficherTimePicker(etPauseFin, "Fin pause"));

        // Activer/désactiver les champs de pause selon la checkbox
        cbPauseDejeune.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etPauseDebut.setEnabled(isChecked);
            etPauseFin.setEnabled(isChecked);

            if (!isChecked) {
                etPauseDebut.setText("");
                etPauseFin.setText("");
            }
        });

        // Bouton sauvegarder
        btnSauvegarder.setOnClickListener(v -> sauvegarderDisponibilites());

        // Bouton réinitialiser
        btnReinitialiser.setOnClickListener(v -> {
            // Demander confirmation
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Réinitialiser")
                    .setMessage("Voulez-vous vraiment réinitialiser toutes les disponibilités ?")
                    .setPositiveButton("Oui", (dialog, which) -> reinitialiserFormulaire())
                    .setNegativeButton("Non", null)
                    .show();
        });
    }

    /**
     * Affiche un TimePicker pour sélectionner une heure
     */
    private void afficherTimePicker(EditText editText, String titre) {
        Calendar calendar = Calendar.getInstance();

        // Si l'EditText contient déjà une heure, l'utiliser comme valeur par défaut
        String currentText = editText.getText().toString();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (!currentText.isEmpty() && currentText.contains(":")) {
            try {
                String[] parts = currentText.split(":");
                hour = Integer.parseInt(parts[0]);
                minute = Integer.parseInt(parts[1]);
            } catch (Exception e) {
                // Utiliser l'heure actuelle en cas d'erreur
            }
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, minuteOfHour);
                    editText.setText(time);
                },
                hour,
                minute,
                true // Format 24h
        );

        timePickerDialog.setTitle(titre);
        timePickerDialog.show();
    }

    /**
     * Charge les disponibilités sauvegardées
     */
    private void chargerDisponibilites() {
        SharedPreferences prefs = requireContext().getSharedPreferences(
                PREFS_NAME + "_" + medecinId, Context.MODE_PRIVATE);

        // Charger les jours (par défaut: Lundi à Vendredi)
        cbLundi.setChecked(prefs.getBoolean("lundi", true));
        cbMardi.setChecked(prefs.getBoolean("mardi", true));
        cbMercredi.setChecked(prefs.getBoolean("mercredi", true));
        cbJeudi.setChecked(prefs.getBoolean("jeudi", true));
        cbVendredi.setChecked(prefs.getBoolean("vendredi", true));
        cbSamedi.setChecked(prefs.getBoolean("samedi", false));
        cbDimanche.setChecked(prefs.getBoolean("dimanche", false));

        // Charger les horaires (par défaut: 8h-18h, 30min)
        etHeureDebut.setText(prefs.getString("heure_debut", "08:00"));
        etHeureFin.setText(prefs.getString("heure_fin", "18:00"));
        etDureeConsultation.setText(prefs.getString("duree_consultation", "30"));

        // Charger la pause déjeuner (par défaut: 12h-14h)
        boolean pauseActive = prefs.getBoolean("pause_active", true);
        cbPauseDejeune.setChecked(pauseActive);
        etPauseDebut.setText(prefs.getString("pause_debut", "12:00"));
        etPauseFin.setText(prefs.getString("pause_fin", "14:00"));

        // Activer/désactiver les champs de pause
        etPauseDebut.setEnabled(pauseActive);
        etPauseFin.setEnabled(pauseActive);
    }

    /**
     * Sauvegarde les disponibilités dans SharedPreferences
     */
    private void sauvegarderDisponibilites() {
        // Valider le formulaire
        if (!validerFormulaire()) {
            return;
        }

        // Obtenir les SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences(
                PREFS_NAME + "_" + medecinId, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Sauvegarder les jours sélectionnés
        editor.putBoolean("lundi", cbLundi.isChecked());
        editor.putBoolean("mardi", cbMardi.isChecked());
        editor.putBoolean("mercredi", cbMercredi.isChecked());
        editor.putBoolean("jeudi", cbJeudi.isChecked());
        editor.putBoolean("vendredi", cbVendredi.isChecked());
        editor.putBoolean("samedi", cbSamedi.isChecked());
        editor.putBoolean("dimanche", cbDimanche.isChecked());

        // Sauvegarder les horaires
        editor.putString("heure_debut", etHeureDebut.getText().toString().trim());
        editor.putString("heure_fin", etHeureFin.getText().toString().trim());
        editor.putString("duree_consultation", etDureeConsultation.getText().toString().trim());

        // Sauvegarder la pause déjeuner
        boolean pauseActive = cbPauseDejeune.isChecked();
        editor.putBoolean("pause_active", pauseActive);

        if (pauseActive) {
            editor.putString("pause_debut", etPauseDebut.getText().toString().trim());
            editor.putString("pause_fin", etPauseFin.getText().toString().trim());
        } else {
            editor.remove("pause_debut");
            editor.remove("pause_fin");
        }

        // Appliquer les modifications
        boolean success = editor.commit();

        if (success) {
            Toast.makeText(requireContext(),
                    "✅ Disponibilités sauvegardées avec succès !",
                    Toast.LENGTH_SHORT).show();

            // Retourner au dashboard
            requireActivity().onBackPressed();
        } else {
            Toast.makeText(requireContext(),
                    "❌ Erreur lors de la sauvegarde",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Valide le formulaire avant la sauvegarde
     */
    private boolean validerFormulaire() {
        // Vérifier l'heure de début
        if (etHeureDebut.getText().toString().trim().isEmpty()) {
            etHeureDebut.setError("Heure de début requise");
            etHeureDebut.requestFocus();
            return false;
        }

        // Vérifier l'heure de fin
        if (etHeureFin.getText().toString().trim().isEmpty()) {
            etHeureFin.setError("Heure de fin requise");
            etHeureFin.requestFocus();
            return false;
        }

        // Vérifier la durée de consultation
        String dureeStr = etDureeConsultation.getText().toString().trim();
        if (dureeStr.isEmpty()) {
            etDureeConsultation.setError("Durée de consultation requise");
            etDureeConsultation.requestFocus();
            return false;
        }

        // Vérifier que la durée est un nombre valide
        try {
            int duree = Integer.parseInt(dureeStr);
            if (duree <= 0 || duree > 120) {
                etDureeConsultation.setError("Durée invalide (entre 1 et 120 minutes)");
                etDureeConsultation.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etDureeConsultation.setError("Durée invalide");
            etDureeConsultation.requestFocus();
            return false;
        }

        // Vérifier qu'au moins un jour est sélectionné
        if (!cbLundi.isChecked() && !cbMardi.isChecked() && !cbMercredi.isChecked() &&
                !cbJeudi.isChecked() && !cbVendredi.isChecked() && !cbSamedi.isChecked() &&
                !cbDimanche.isChecked()) {
            Toast.makeText(requireContext(),
                    "⚠️ Veuillez sélectionner au moins un jour de travail",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // Si la pause est activée, vérifier ses horaires
        if (cbPauseDejeune.isChecked()) {
            if (etPauseDebut.getText().toString().trim().isEmpty()) {
                etPauseDebut.setError("Heure de début de pause requise");
                etPauseDebut.requestFocus();
                return false;
            }
            if (etPauseFin.getText().toString().trim().isEmpty()) {
                etPauseFin.setError("Heure de fin de pause requise");
                etPauseFin.requestFocus();
                return false;
            }
        }

        return true;
    }

    /**
     * Réinitialise le formulaire aux valeurs par défaut
     */
    private void reinitialiserFormulaire() {
        // Décocher tous les jours
        cbLundi.setChecked(false);
        cbMardi.setChecked(false);
        cbMercredi.setChecked(false);
        cbJeudi.setChecked(false);
        cbVendredi.setChecked(false);
        cbSamedi.setChecked(false);
        cbDimanche.setChecked(false);

        // Réinitialiser les horaires
        etHeureDebut.setText("");
        etHeureFin.setText("");
        etDureeConsultation.setText("");

        // Réinitialiser la pause
        cbPauseDejeune.setChecked(false);
        etPauseDebut.setText("");
        etPauseFin.setText("");
        etPauseDebut.setEnabled(false);
        etPauseFin.setEnabled(false);

        Toast.makeText(requireContext(),
                "📝 Formulaire réinitialisé",
                Toast.LENGTH_SHORT).show();
    }
}