package com.clinique.lasagesse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver pour gérer les rappels de rendez-vous planifiés
 * Déclenché automatiquement par AlarmManager 24h avant le RDV
 */
public class RdvReminderReceiver extends BroadcastReceiver {

    private static final String TAG = "RdvReminderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Rappel de rendez-vous déclenché");

        // Récupérer les informations du rendez-vous depuis l'Intent
        int rdvId = intent.getIntExtra("rdv_id", -1);
        String dateRdv = intent.getStringExtra("date_rdv");
        String heureRdv = intent.getStringExtra("heure_rdv");

        if (rdvId == -1) {
            Log.e(TAG, "ID de rendez-vous invalide");
            return;
        }

        try {
            // Récupérer les détails du RDV depuis la base de données
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            RendezVous rdv = dbHelper.getRendezVousById(rdvId);

            if (rdv != null) {
                // Créer le message de notification
                String titre = "🏥 Rappel de Rendez-vous";
                String message = String.format(
                        "Vous avez un rendez-vous demain le %s à %s avec %s",
                        dateRdv,
                        heureRdv,
                        rdv.getMedecinNom()
                );

                // Afficher la notification
                NotificationHelper notificationHelper = new NotificationHelper(context);
                notificationHelper.afficherNotification(rdvId, titre, message);

                Log.d(TAG, "Notification envoyée pour RDV ID: " + rdvId);

                // Optionnel: Envoyer aussi un SMS si le patient a un numéro
                // Décommenter les lignes ci-dessous si vous voulez activer l'envoi SMS
                /*
                Patient patient = dbHelper.getPatientById(rdv.getPatientId());
                if (patient != null && patient.getTelephone() != null && !patient.getTelephone().isEmpty()) {
                    notificationHelper.envoyerSmsRappel(
                        patient.getTelephone(),
                        patient.getPrenom() + " " + patient.getNom(),
                        dateRdv,
                        heureRdv,
                        rdv.getMedecinNom()
                    );
                    Log.d(TAG, "SMS envoyé au: " + patient.getTelephone());
                }
                */

            } else {
                Log.e(TAG, "Rendez-vous introuvable pour ID: " + rdvId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors du traitement du rappel: " + e.getMessage());
            e.printStackTrace();
        }
    }
}