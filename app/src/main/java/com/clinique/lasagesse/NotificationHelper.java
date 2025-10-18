package com.clinique.lasagesse;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Classe utilitaire pour gérer les notifications de rappel de rendez-vous
 * Supporte : Notifications Android, SMS et Email
 */
public class NotificationHelper {

    private static final String TAG = "NotificationHelper";
    private static final String CHANNEL_ID = "rdv_reminders";
    private static final String CHANNEL_NAME = "Rappels Rendez-vous";
    private static final int NOTIFICATION_ID_BASE = 1000;

    private Context context;
    private DatabaseHelper dbHelper;

    public NotificationHelper(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        createNotificationChannel();
    }

    /**
     * Créer le canal de notification (Android 8.0+)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Rappels de rendez-vous médicaux");
            channel.enableVibration(true);
            channel.enableLights(true);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Planifier un rappel de rendez-vous
     * @param rdvId ID du rendez-vous
     * @param dateRdv Date du RDV (format: dd/MM/yyyy)
     * @param heureRdv Heure du RDV (format: HH:mm)
     * @param hoursBeforeRdv Nombre d'heures avant le RDV pour le rappel (défaut: 24h)
     */
    public void planifierRappelRdv(int rdvId, String dateRdv, String heureRdv, int hoursBeforeRdv) {
        try {
            // Parser la date et l'heure du RDV
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
            Date rdvDateTime = sdf.parse(dateRdv + " " + heureRdv);

            if (rdvDateTime == null) {
                Log.e(TAG, "Impossible de parser la date: " + dateRdv + " " + heureRdv);
                return;
            }

            // Calculer le moment du rappel (X heures avant)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rdvDateTime);
            calendar.add(Calendar.HOUR_OF_DAY, -hoursBeforeRdv);

            // Vérifier si le rappel est dans le futur
            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                // Créer l'intent pour l'alarme
                Intent intent = new Intent(context, RdvReminderReceiver.class);
                intent.putExtra("rdv_id", rdvId);
                intent.putExtra("date_rdv", dateRdv);
                intent.putExtra("heure_rdv", heureRdv);

                int flags = PendingIntent.FLAG_UPDATE_CURRENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    flags |= PendingIntent.FLAG_IMMUTABLE;
                }

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        NOTIFICATION_ID_BASE + rdvId,
                        intent,
                        flags
                );

                // Planifier l'alarme
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    } else {
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                    Log.d(TAG, "Rappel planifié pour le RDV ID: " + rdvId + " à " + calendar.getTime());
                }
            } else {
                Log.w(TAG, "La date du rappel est dans le passé, pas de planification");
            }
        } catch (ParseException e) {
            Log.e(TAG, "Erreur lors du parsing de la date: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Afficher une notification locale
     */
    public void afficherNotification(int rdvId, String titre, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.clinique_logo)
                .setContentTitle(titre)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 200, 500});

        // Intent pour ouvrir l'app au clic
        Intent intent = new Intent(context, MainActivity.class);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                rdvId,
                intent,
                flags
        );
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID_BASE + rdvId, builder.build());
            Log.d(TAG, "Notification affichée pour RDV ID: " + rdvId);
        }
    }

    /**
     * Envoyer un SMS de rappel
     * IMPORTANT: Nécessite la permission SEND_SMS dans le Manifest
     */
    public void envoyerSmsRappel(String numeroTelephone, String nomPatient, String dateRdv,
                                 String heureRdv, String nomMedecin) {
        try {
            String message = String.format(
                    "🏥 CLINIQUE LA SAGESSE\n\n" +
                            "Bonjour %s,\n\n" +
                            "Rappel de votre rendez-vous:\n" +
                            "📅 Date: %s\n" +
                            "🕐 Heure: %s\n" +
                            "👨‍⚕️ Avec: Dr. %s\n\n" +
                            "Merci de confirmer ou annuler si nécessaire.\n" +
                            "☎️ (+221) 33 951 45 45",
                    nomPatient, dateRdv, heureRdv, nomMedecin
            );

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numeroTelephone, null, message, null, null);
            Log.d(TAG, "SMS envoyé au numéro: " + numeroTelephone);
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'envoi du SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envoyer un email de rappel (ouvre le client email)
     */
    public void envoyerEmailRappel(String emailPatient, String nomPatient, String dateRdv,
                                   String heureRdv, String nomMedecin, String specialite) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailPatient});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rappel - Rendez-vous Clinique La Sagesse");

        String messageEmail = String.format(
                "Bonjour %s,\n\n" +
                        "Ceci est un rappel concernant votre rendez-vous à la Clinique La Sagesse:\n\n" +
                        "📅 Date: %s\n" +
                        "🕐 Heure: %s\n" +
                        "👨‍⚕️ Médecin: Dr. %s\n" +
                        "🏥 Spécialité: %s\n\n" +
                        "📍 Adresse: Avenue Felix Boigny, BP 282 Thiès - Sénégal\n" +
                        "☎️ Téléphone: (+221) 33 951 45 45\n\n" +
                        "Merci de confirmer votre présence ou de nous contacter pour toute modification.\n\n" +
                        "Cordialement,\n" +
                        "L'équipe de la Clinique La Sagesse\n" +
                        "Excellence Médicale & Innovation",
                nomPatient, dateRdv, heureRdv, nomMedecin, specialite
        );

        emailIntent.putExtra(Intent.EXTRA_TEXT, messageEmail);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Envoyer l'email via..."));
            Log.d(TAG, "Intention d'email créée pour: " + emailPatient);
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e(TAG, "Aucune application email disponible");
        }
    }

    /**
     * Annuler un rappel planifié
     */
    public void annulerRappel(int rdvId) {
        Intent intent = new Intent(context, RdvReminderReceiver.class);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID_BASE + rdvId,
                intent,
                flags
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "Rappel annulé pour RDV ID: " + rdvId);
        }
    }
}