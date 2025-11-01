package com.clinique.lasagesse;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModifierRdvFragment extends Fragment {

    private static final String ARG_RDV_ID = "rdv_id";
    private static final String ARG_PATIENT_ID = "patient_id";

    private int rdvId;
    private int patientId;
    private Spinner spinnerMedecins;
    private EditText editDate, editHeure, editMotif, editNotes;
    private CheckBox checkUrgent;
    private Button btnModifier, btnAnnuler;
    private DatabaseHelper dbHelper;
    private Calendar calendar;
    private List<Medecin> medecinsList;
    private RendezVous rdvActuel;

    public ModifierRdvFragment() {}

    public static ModifierRdvFragment newInstance(int rdvId, int patientId) {
        ModifierRdvFragment fragment = new ModifierRdvFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RDV_ID, rdvId);
        args.putInt(ARG_PATIENT_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rdvId = getArguments().getInt(ARG_RDV_ID);
            patientId = getArguments().getInt(ARG_PATIENT_ID);
        }
        calendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modifier_rdv, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Initialiser les vues
        spinnerMedecins = view.findViewById(R.id.spinner_medecins);
        editDate = view.findViewById(R.id.edit_date);
        editHeure = view.findViewById(R.id.edit_heure);
        editMotif = view.findViewById(R.id.edit_motif);
        editNotes = view.findViewById(R.id.edit_notes);
        checkUrgent = view.findViewById(R.id.check_urgent);
        btnModifier = view.findViewById(R.id.btn_modifier);
        btnAnnuler = view.findViewById(R.id.btn_annuler);

        // Charger les données
        chargerMedecins();
        chargerRendezVous();

        // Date picker
        editDate.setOnClickListener(v -> showDatePicker());

        // Time picker
        editHeure.setOnClickListener(v -> showTimePicker());

        // Bouton Modifier
        btnModifier.setOnClickListener(v -> {
            if (validerFormulaire()) {
                modifierRendezVous();
            }
        });

        // Bouton Annuler
        btnAnnuler.setOnClickListener(v -> confirmerAnnulation());

        return view;
    }

    private void chargerMedecins() {
        medecinsList = dbHelper.getAllMedecins();

        String[] medecinsArray = new String[medecinsList.size() + 1];
        medecinsArray[0] = "Sélectionner un médecin";

        for (int i = 0; i < medecinsList.size(); i++) {
            Medecin m = medecinsList.get(i);
            medecinsArray[i + 1] = "Dr. " + m.getNom() + " " + m.getPrenom() +
                    " (" + m.getSpecialite() + ")";
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                medecinsArray
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedecins.setAdapter(adapter);
    }

    private void chargerRendezVous() {
        rdvActuel = dbHelper.getRendezVousById(rdvId);

        if (rdvActuel != null) {
            // Pré-remplir les champs
            editDate.setText(rdvActuel.getDate());
            editHeure.setText(rdvActuel.getHeure());
            editMotif.setText(rdvActuel.getMotif());
            editNotes.setText(rdvActuel.getNotes());
            checkUrgent.setChecked(rdvActuel.isUrgent());

            // Sélectionner le médecin dans le spinner
            for (int i = 0; i < medecinsList.size(); i++) {
                if (medecinsList.get(i).getId() == rdvActuel.getMedecinId()) {
                    spinnerMedecins.setSelection(i + 1);
                    break;
                }
            }
        }
    }

    private void showDatePicker() {
        // Parser la date actuelle si elle existe
        if (!editDate.getText().toString().isEmpty()) {
            try {
                String[] parts = editDate.getText().toString().split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1;
                int year = Integer.parseInt(parts[2]);
                calendar.set(year, month, day);
            } catch (Exception e) {
                // Utiliser la date actuelle
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                    editDate.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Parser l'heure actuelle si elle existe
        if (!editHeure.getText().toString().isEmpty()) {
            try {
                String[] parts = editHeure.getText().toString().split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
            } catch (Exception e) {
                // Utiliser l'heure actuelle
            }
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    String heure = String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, minute);
                    editHeure.setText(heure);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private boolean validerFormulaire() {
        if (spinnerMedecins.getSelectedItemPosition() == 0) {
            Toast.makeText(requireContext(), "Veuillez sélectionner un médecin", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editHeure.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez sélectionner une heure", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editMotif.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Veuillez saisir le motif", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void modifierRendezVous() {
        int medecinPosition = spinnerMedecins.getSelectedItemPosition();
        int medecinId = medecinsList.get(medecinPosition - 1).getId();

        String date = editDate.getText().toString().trim();
        String heure = editHeure.getText().toString().trim();
        String motif = editMotif.getText().toString().trim();
        String notes = editNotes.getText().toString().trim();
        boolean urgent = checkUrgent.isChecked();

        boolean success = dbHelper.modifierRendezVous(
                rdvId,
                medecinId,
                date,
                heure,
                motif,
                notes,
                urgent
        );

        if (success) {
            // Annuler l'ancien rappel et en créer un nouveau
            NotificationHelper notificationHelper = new NotificationHelper(requireContext());
            notificationHelper.annulerRappel(rdvId);
            notificationHelper.planifierRappelRdv(rdvId, date, heure, 24);

            Toast.makeText(requireContext(),
                    "✅ Rendez-vous modifié avec succès !",
                    Toast.LENGTH_LONG).show();

            requireActivity().onBackPressed();
        } else {
            Toast.makeText(requireContext(),
                    "❌ Erreur lors de la modification du rendez-vous",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmerAnnulation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Annuler le rendez-vous")
                .setMessage("Êtes-vous sûr de vouloir annuler ce rendez-vous ? Cette action est irréversible.")
                .setPositiveButton("Oui, annuler", (dialog, which) -> annulerRendezVous())
                .setNegativeButton("Non", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void annulerRendezVous() {
        boolean success = dbHelper.supprimerRendezVous(rdvId);

        if (success) {
            // Annuler le rappel de notification
            NotificationHelper notificationHelper = new NotificationHelper(requireContext());
            notificationHelper.annulerRappel(rdvId);

            Toast.makeText(requireContext(),
                    "✅ Rendez-vous annulé avec succès",
                    Toast.LENGTH_LONG).show();

            requireActivity().onBackPressed();
        } else {
            Toast.makeText(requireContext(),
                    "❌ Erreur lors de l'annulation du rendez-vous",
                    Toast.LENGTH_SHORT).show();
        }
    }
}