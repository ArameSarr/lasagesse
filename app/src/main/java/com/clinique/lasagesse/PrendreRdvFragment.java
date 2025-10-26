package com.clinique.lasagesse;

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

public class PrendreRdvFragment extends Fragment {

    private static final String ARG_PATIENT_ID = "patient_id";

    private int patientId;
    private Spinner spinnerMedecins;
    private EditText editDate, editHeure, editMotif, editNotes;
    private CheckBox checkUrgent;
    private Button btnConfirm;
    private DatabaseHelper dbHelper;
    private Calendar calendar;
    private List<Medecin> medecinsList;

    public PrendreRdvFragment() {}

    public static PrendreRdvFragment newInstance(int patientId) {
        PrendreRdvFragment fragment = new PrendreRdvFragment();
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
        calendar = Calendar.getInstance();
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

        // ✅ CORRECTION : Charger les médecins depuis la base de données
        chargerMedecins();

        // Date picker
        editDate.setOnClickListener(v -> showDatePicker());

        // Time picker
        editHeure.setOnClickListener(v -> showTimePicker());

        btnConfirm.setOnClickListener(v -> {
            if (validerFormulaire()) {
                enregistrerRendezVous();
            }
        });

        return view;
    }

    private void chargerMedecins() {
        // ✅ CORRECTION : Charger la liste des médecins depuis la DB
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

    private void showDatePicker() {
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

    private void enregistrerRendezVous() {
        // ✅ CORRECTION : Enregistrer réellement le rendez-vous
        int medecinPosition = spinnerMedecins.getSelectedItemPosition();
        int medecinId = medecinsList.get(medecinPosition - 1).getId();

        String date = editDate.getText().toString().trim();
        String heure = editHeure.getText().toString().trim();
        String motif = editMotif.getText().toString().trim();
        String notes = editNotes.getText().toString().trim();
        boolean urgent = checkUrgent.isChecked();
        String statut = urgent ? "Urgent" : "À confirmer";

        boolean success = dbHelper.ajouterRendezVous(
                patientId,
                medecinId,
                date,
                heure,
                motif,
                notes,
                statut,
                urgent
        );

        if (success) {
            Toast.makeText(requireContext(),
                    "✅ Rendez-vous confirmé !",
                    Toast.LENGTH_LONG).show();

            // ✅ NOUVEAU : Planifier un rappel 24h avant
            RendezVous rdv = dbHelper.getRendezVousById(
                    dbHelper.getAllRendezVous().get(0).getId()
            );

            if (rdv != null) {
                NotificationHelper notificationHelper = new NotificationHelper(requireContext());
                notificationHelper.planifierRappelRdv(rdv.getId(), date, heure, 24);
            }

            requireActivity().onBackPressed();
        } else {
            Toast.makeText(requireContext(),
                    "❌ Erreur lors de la création du rendez-vous",
                    Toast.LENGTH_SHORT).show();
        }
    }
}