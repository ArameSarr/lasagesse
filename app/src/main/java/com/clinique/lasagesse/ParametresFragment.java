package com.clinique.lasagesse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ParametresFragment extends Fragment {

    private static final String PREFS_NAME = "AppSettings";

    private SharedPreferences prefs;
    private DatabaseHelper dbHelper;

    private Switch switchNotifications, switchRappelsAuto, switchModeSombre;
    private TextInputEditText etDelaiRappel, etNomClinique, etTelephone, etEmail;
    private MaterialButton btnSauvegarder, btnReinitialiser;

    public ParametresFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parametres, container, false);

        prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        dbHelper = new DatabaseHelper(requireContext());

        // Initialiser les vues
        switchNotifications = view.findViewById(R.id.switch_notifications);
        switchRappelsAuto = view.findViewById(R.id.switch_rappels_auto);
        switchModeSombre = view.findViewById(R.id.switch_mode_sombre);

        etDelaiRappel = view.findViewById(R.id.et_delai_rappel);
        etNomClinique = view.findViewById(R.id.et_nom_clinique);
        etTelephone = view.findViewById(R.id.et_telephone);
        etEmail = view.findViewById(R.id.et_email);

        btnSauvegarder = view.findViewById(R.id.btn_sauvegarder);
        btnReinitialiser = view.findViewById(R.id.btn_reinitialiser);

        chargerParametres();

        btnSauvegarder.setOnClickListener(v -> sauvegarderParametres());
        btnReinitialiser.setOnClickListener(v -> confirmerReinitialisation());

        return view;
    }

    private void chargerParametres() {
        // Charger les paramètres sauvegardés
        switchNotifications.setChecked(prefs.getBoolean("notifications_enabled", true));
        switchRappelsAuto.setChecked(prefs.getBoolean("rappels_auto", true));
        switchModeSombre.setChecked(prefs.getBoolean("mode_sombre", false));

        etDelaiRappel.setText(prefs.getString("delai_rappel", "24"));
        etNomClinique.setText(prefs.getString("nom_clinique", "Clinique La Sagesse"));
        etTelephone.setText(prefs.getString("telephone_clinique", "(+221) 33 951 45 45"));
        etEmail.setText(prefs.getString("email_clinique", "contact@cliniquelasagesse.sn"));
    }

    private void sauvegarderParametres() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("notifications_enabled", switchNotifications.isChecked());
        editor.putBoolean("rappels_auto", switchRappelsAuto.isChecked());
        editor.putBoolean("mode_sombre", switchModeSombre.isChecked());

        editor.putString("delai_rappel", etDelaiRappel.getText().toString());
        editor.putString("nom_clinique", etNomClinique.getText().toString());
        editor.putString("telephone_clinique", etTelephone.getText().toString());
        editor.putString("email_clinique", etEmail.getText().toString());

        boolean success = editor.commit();

        if (success) {
            Toast.makeText(requireContext(), "✅ Paramètres sauvegardés", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "❌ Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmerReinitialisation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Réinitialiser l'application")
                .setMessage("⚠️ ATTENTION : Cette action va supprimer TOUTES les données de l'application (patients, médecins, rendez-vous, etc.).\n\nÊtes-vous absolument sûr ?")
                .setPositiveButton("OUI, TOUT SUPPRIMER", (dialog, which) -> reinitialiserApplication())
                .setNegativeButton("Annuler", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void reinitialiserApplication() {
        // Supprimer toutes les données
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 1);

        // Réinitialiser les préférences
        prefs.edit().clear().apply();

        Toast.makeText(requireContext(), "✅ Application réinitialisée", Toast.LENGTH_LONG).show();

        // Retourner à l'écran d'accueil
        requireActivity().getSupportFragmentManager().popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ((MainActivity) requireActivity()).loadFragment(new AccueilFragment(), false);
    }
}