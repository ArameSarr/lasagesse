package com.clinique.lasagesse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "clinique_db";
    private static final int DATABASE_VERSION = 2;

    // Tables
    private static final String TABLE_PATIENTS = "patients";
    private static final String TABLE_MEDECINS = "medecins";
    private static final String TABLE_SECRETAIRES = "secretaires";
    private static final String TABLE_RENDEZVOUS = "rendezvous";
    private static final String TABLE_ADMINS = "admins";

    // Colonnes communes
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_TELEPHONE = "telephone";
    private static final String KEY_ROLE = "role";

    // Colonnes patients
    private static final String KEY_DATE_NAISSANCE = "date_naissance";
    private static final String KEY_ADRESSE = "adresse";

    // Colonnes médecins
    private static final String KEY_SPECIALITE = "specialite";
    private static final String KEY_CABINET = "cabinet";

    // Colonnes rendez-vous
    private static final String KEY_PATIENT_ID = "patient_id";
    private static final String KEY_MEDECIN_ID = "medecin_id";
    private static final String KEY_DATE_RDV = "date_rdv";
    private static final String KEY_HEURE = "heure";
    private static final String KEY_MOTIF = "motif";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_STATUT = "statut";
    private static final String KEY_URGENT = "urgent";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Création de la table patients
        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOM + " TEXT,"
                + KEY_PRENOM + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_TELEPHONE + " TEXT,"
                + KEY_DATE_NAISSANCE + " TEXT,"
                + KEY_ADRESSE + " TEXT" + ")";
        db.execSQL(CREATE_PATIENTS_TABLE);

        // Création de la table médecins
        String CREATE_MEDECINS_TABLE = "CREATE TABLE " + TABLE_MEDECINS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOM + " TEXT,"
                + KEY_PRENOM + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_SPECIALITE + " TEXT,"
                + KEY_CABINET + " TEXT" + ")";
        db.execSQL(CREATE_MEDECINS_TABLE);

        // Création de la table secrétaires
        String CREATE_SECRETAIRES_TABLE = "CREATE TABLE " + TABLE_SECRETAIRES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOM + " TEXT,"
                + KEY_PRENOM + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_SECRETAIRES_TABLE);

        // Création de la table rendez-vous
        String CREATE_RENDEZVOUS_TABLE = "CREATE TABLE " + TABLE_RENDEZVOUS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PATIENT_ID + " INTEGER,"
                + KEY_MEDECIN_ID + " INTEGER,"
                + KEY_DATE_RDV + " TEXT,"
                + KEY_HEURE + " TEXT,"
                + KEY_MOTIF + " TEXT,"
                + KEY_NOTES + " TEXT,"
                + KEY_STATUT + " TEXT,"
                + KEY_URGENT + " INTEGER,"
                + "FOREIGN KEY(" + KEY_PATIENT_ID + ") REFERENCES " + TABLE_PATIENTS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_MEDECIN_ID + ") REFERENCES " + TABLE_MEDECINS + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_RENDEZVOUS_TABLE);

        // ✅ Création de la table admins AVANT l'insertion des données
        String CREATE_ADMINS_TABLE = "CREATE TABLE " + TABLE_ADMINS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NOM + " TEXT,"
                + KEY_PRENOM + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_ROLE + " TEXT" + ")";
        db.execSQL(CREATE_ADMINS_TABLE);

        // Insertion de données de test
        insererDonneesTest(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENDEZVOUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDECINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECRETAIRES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINS);
        onCreate(db);
    }

    private void insererDonneesTest(SQLiteDatabase db) {
        // ========== INSERTION ADMIN ==========
        ContentValues admin = new ContentValues();
        admin.put(KEY_NOM, "Admin");
        admin.put(KEY_PRENOM, "Principal");
        admin.put(KEY_EMAIL, "admin@clinique.com");
        admin.put(KEY_PASSWORD, "admin123");
        admin.put(KEY_ROLE, "super_admin");
        db.insert(TABLE_ADMINS, null, admin);

        // ========== INSERTION PATIENTS ==========
        ContentValues patient1 = new ContentValues();
        patient1.put(KEY_NOM, "Fall");
        patient1.put(KEY_PRENOM, "Fatou");
        patient1.put(KEY_EMAIL, "fatou.fall@email.com");
        patient1.put(KEY_PASSWORD, "patient123");
        patient1.put(KEY_TELEPHONE, "771234567");
        patient1.put(KEY_DATE_NAISSANCE, "15/03/1992");
        patient1.put(KEY_ADRESSE, "Dakar, Sénégal");
        db.insert(TABLE_PATIENTS, null, patient1);

        // ========== INSERTION MÉDECINS ==========
        ContentValues medecin1 = new ContentValues();
        medecin1.put(KEY_NOM, "Diallo");
        medecin1.put(KEY_PRENOM, "Amadou");
        medecin1.put(KEY_EMAIL, "dr.diallo@clinique.com");
        medecin1.put(KEY_PASSWORD, "medecin123");
        medecin1.put(KEY_SPECIALITE, "Médecine Générale");
        medecin1.put(KEY_CABINET, "Cabinet 12");
        db.insert(TABLE_MEDECINS, null, medecin1);

        ContentValues medecin2 = new ContentValues();
        medecin2.put(KEY_NOM, "Sow");
        medecin2.put(KEY_PRENOM, "Aissatou");
        medecin2.put(KEY_EMAIL, "dr.sow@clinique.com");
        medecin2.put(KEY_PASSWORD, "medecin123");
        medecin2.put(KEY_SPECIALITE, "Cardiologie");
        medecin2.put(KEY_CABINET, "Cabinet 8");
        db.insert(TABLE_MEDECINS, null, medecin2);

        // ========== INSERTION SECRÉTAIRE ==========
        ContentValues secretaire = new ContentValues();
        secretaire.put(KEY_NOM, "Diallo");
        secretaire.put(KEY_PRENOM, "Marie");
        secretaire.put(KEY_EMAIL, "marie.diallo@clinique.com");
        secretaire.put(KEY_PASSWORD, "secretaire123");
        db.insert(TABLE_SECRETAIRES, null, secretaire);

        // ========== INSERTION RENDEZ-VOUS ==========
        ContentValues rdv1 = new ContentValues();
        rdv1.put(KEY_PATIENT_ID, 1);
        rdv1.put(KEY_MEDECIN_ID, 1);
        rdv1.put(KEY_DATE_RDV, "15/01/2024");
        rdv1.put(KEY_HEURE, "14:30");
        rdv1.put(KEY_MOTIF, "Consultation de routine");
        rdv1.put(KEY_STATUT, "confirmé");
        rdv1.put(KEY_URGENT, 0);
        db.insert(TABLE_RENDEZVOUS, null, rdv1);
    }

    // ===================== MÉTHODES ADMIN =====================

    public boolean verifierLoginAdmin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,
                new String[]{KEY_ID},
                KEY_EMAIL + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Admin getAdminByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMINS,
                null,
                KEY_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Admin admin = new Admin();
            admin.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            admin.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
            admin.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
            admin.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            admin.setRole(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE)));
            cursor.close();
            return admin;
        }
        return null;
    }

    // ===================== MÉTHODES PATIENTS =====================

    public boolean verifierLoginPatient(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS,
                new String[]{KEY_ID, KEY_NOM, KEY_PRENOM},
                KEY_EMAIL + " = ? AND " + KEY_PASSWORD + " = ?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Patient getPatientByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS,
                null,
                KEY_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Patient patient = new Patient();
            patient.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            patient.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
            patient.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
            patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            patient.setTelephone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TELEPHONE)));
            patient.setDateNaissance(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_NAISSANCE)));
            patient.setAdresse(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADRESSE)));
            cursor.close();
            return patient;
        }
        return null;
    }

    public Patient getPatientById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS,
                null,
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Patient patient = new Patient();
            patient.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            patient.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
            patient.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
            patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            patient.setTelephone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TELEPHONE)));
            patient.setDateNaissance(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_NAISSANCE)));
            patient.setAdresse(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADRESSE)));
            cursor.close();
            return patient;
        }
        return null;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patientList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, null, null, null, null, null, KEY_NOM);

        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                patient.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
                patient.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
                patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
                patient.setTelephone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TELEPHONE)));
                patient.setDateNaissance(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_NAISSANCE)));
                patient.setAdresse(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ADRESSE)));
                patientList.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return patientList;
    }

    public boolean ajouterPatient(String nom, String prenom, String email, String password,
                                  String telephone, String dateNaissance, String adresse) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{KEY_ID},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_TELEPHONE, telephone);
        values.put(KEY_DATE_NAISSANCE, dateNaissance);
        values.put(KEY_ADRESSE, adresse);
        long result = db.insert(TABLE_PATIENTS, null, values);
        return result != -1;
    }

    public boolean mettreAJourPatient(int id, String nom, String prenom, String email,
                                      String telephone, String dateNaissance, String adresse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_EMAIL, email);
        values.put(KEY_TELEPHONE, telephone);
        values.put(KEY_DATE_NAISSANCE, dateNaissance);
        values.put(KEY_ADRESSE, adresse);
        int rows = db.update(TABLE_PATIENTS, values, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean supprimerPatient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RENDEZVOUS, KEY_PATIENT_ID + "=?", new String[]{String.valueOf(id)});
        int rows = db.delete(TABLE_PATIENTS, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // ===================== MÉTHODES MÉDECINS =====================

    public boolean verifierLoginMedecin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDECINS,
                new String[]{KEY_ID},
                KEY_EMAIL + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Medecin getMedecinByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDECINS,
                null,
                KEY_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Medecin medecin = new Medecin();
            medecin.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            medecin.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
            medecin.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
            medecin.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            medecin.setSpecialite(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SPECIALITE)));
            medecin.setCabinet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CABINET)));
            cursor.close();
            return medecin;
        }
        return null;
    }

    public Medecin getMedecinById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDECINS,
                null,
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Medecin medecin = new Medecin();
            medecin.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            medecin.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
            medecin.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
            medecin.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            medecin.setSpecialite(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SPECIALITE)));
            medecin.setCabinet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CABINET)));
            cursor.close();
            return medecin;
        }
        return null;
    }

    public List<Medecin> getAllMedecins() {
        List<Medecin> medecinList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDECINS, null, null, null, null, null, KEY_NOM);

        if (cursor.moveToFirst()) {
            do {
                Medecin medecin = new Medecin();
                medecin.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                medecin.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
                medecin.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
                medecin.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
                medecin.setSpecialite(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SPECIALITE)));
                medecin.setCabinet(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CABINET)));
                medecinList.add(medecin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return medecinList;
    }

    public boolean ajouterMedecin(String nom, String prenom, String email, String password,
                                  String specialite, String cabinet) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MEDECINS, new String[]{KEY_ID},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_SPECIALITE, specialite);
        values.put(KEY_CABINET, cabinet);
        long result = db.insert(TABLE_MEDECINS, null, values);
        return result != -1;
    }

    public boolean mettreAJourMedecin(int id, String nom, String prenom, String email,
                                      String specialite, String cabinet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_EMAIL, email);
        values.put(KEY_SPECIALITE, specialite);
        values.put(KEY_CABINET, cabinet);
        int rows = db.update(TABLE_MEDECINS, values, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean supprimerMedecin(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RENDEZVOUS, KEY_MEDECIN_ID + "=?", new String[]{String.valueOf(id)});
        int rows = db.delete(TABLE_MEDECINS, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // ===================== MÉTHODES SECRÉTAIRES =====================

    public boolean verifierLoginSecretaire(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SECRETAIRES,
                new String[]{KEY_ID},
                KEY_EMAIL + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Secretaire getSecretaireByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SECRETAIRES,
                null,
                KEY_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Secretaire secretaire = new Secretaire();
            secretaire.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            secretaire.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
            secretaire.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
            secretaire.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            cursor.close();
            return secretaire;
        }
        return null;
    }

    public Secretaire getSecretaireById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SECRETAIRES,
                null,
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Secretaire secretaire = new Secretaire();
            secretaire.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            secretaire.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
            secretaire.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
            secretaire.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            cursor.close();
            return secretaire;
        }
        return null;
    }

    public List<Secretaire> getAllSecretaires() {
        List<Secretaire> secretaireList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SECRETAIRES, null, null, null, null, null, KEY_NOM);

        if (cursor.moveToFirst()) {
            do {
                Secretaire secretaire = new Secretaire();
                secretaire.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                secretaire.setNom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOM)));
                secretaire.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRENOM)));
                secretaire.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
                secretaireList.add(secretaire);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return secretaireList;
    }

    public boolean ajouterSecretaire(String nom, String prenom, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SECRETAIRES, new String[]{KEY_ID},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        long result = db.insert(TABLE_SECRETAIRES, null, values);
        return result != -1;
    }

    public boolean mettreAJourSecretaire(int id, String nom, String prenom, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_EMAIL, email);
        int rows = db.update(TABLE_SECRETAIRES, values, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean supprimerSecretaire(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_SECRETAIRES, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // ===================== MÉTHODES RENDEZ-VOUS =====================

    public boolean ajouterRendezVous(int patientId, int medecinId, String date,
                                     String heure, String motif, String notes,
                                     String statut, boolean urgent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PATIENT_ID, patientId);
        values.put(KEY_MEDECIN_ID, medecinId);
        values.put(KEY_DATE_RDV, date);
        values.put(KEY_HEURE, heure);
        values.put(KEY_MOTIF, motif);
        values.put(KEY_NOTES, notes);
        values.put(KEY_STATUT, statut);
        values.put(KEY_URGENT, urgent ? 1 : 0);
        long result = db.insert(TABLE_RENDEZVOUS, null, values);
        return result != -1;
    }

    public boolean modifierRendezVous(int rdvId, int medecinId, String date,
                                      String heure, String motif, String notes, boolean urgent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MEDECIN_ID, medecinId);
        values.put(KEY_DATE_RDV, date);
        values.put(KEY_HEURE, heure);
        values.put(KEY_MOTIF, motif);
        values.put(KEY_NOTES, notes);
        values.put(KEY_URGENT, urgent ? 1 : 0);
        values.put(KEY_STATUT, urgent ? "Urgent" : "À confirmer");
        int rows = db.update(TABLE_RENDEZVOUS, values, KEY_ID + "=?",
                new String[]{String.valueOf(rdvId)});
        return rows > 0;
    }

    public boolean supprimerRendezVous(int rdvId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_RENDEZVOUS, KEY_ID + "=?",
                new String[]{String.valueOf(rdvId)});
        return rows > 0;
    }

    public boolean mettreAJourStatutRdv(int rdvId, String nouveauStatut) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUT, nouveauStatut);
        int rows = db.update(TABLE_RENDEZVOUS, values, KEY_ID + "=?",
                new String[]{String.valueOf(rdvId)});
        return rows > 0;
    }

    public boolean annulerRendezVous(int rdvId) {
        return mettreAJourStatutRdv(rdvId, "Annulé");
    }

    public boolean confirmerRendezVous(int rdvId) {
        return mettreAJourStatutRdv(rdvId, "Confirmé");
    }

    public RendezVous getRendezVousById(int rdvId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r.*, p." + KEY_NOM + " as patient_nom, p." + KEY_PRENOM + " as patient_prenom, " +
                "m." + KEY_NOM + " as medecin_nom, m." + KEY_PRENOM + " as medecin_prenom, " +
                "m." + KEY_SPECIALITE + " as specialite " +
                "FROM " + TABLE_RENDEZVOUS + " r " +
                "INNER JOIN " + TABLE_PATIENTS + " p ON r." + KEY_PATIENT_ID + " = p." + KEY_ID + " " +
                "INNER JOIN " + TABLE_MEDECINS + " m ON r." + KEY_MEDECIN_ID + " = m." + KEY_ID + " " +
                "WHERE r." + KEY_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(rdvId)});
        if (cursor != null && cursor.moveToFirst()) {
            RendezVous rdv = new RendezVous();
            rdv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
            rdv.setPatientId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PATIENT_ID)));
            rdv.setMedecinId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_MEDECIN_ID)));
            rdv.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_RDV)));
            rdv.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(KEY_HEURE)));
            rdv.setMotif(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MOTIF)));
            rdv.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTES)));
            rdv.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUT)));
            rdv.setUrgent(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_URGENT)) == 1);
            String medecinNom = cursor.getString(cursor.getColumnIndexOrThrow("medecin_nom")) + " " +
                    cursor.getString(cursor.getColumnIndexOrThrow("medecin_prenom"));
            rdv.setMedecinNom(medecinNom);
            rdv.setSpecialite(cursor.getString(cursor.getColumnIndexOrThrow("specialite")));
            cursor.close();
            return rdv;
        }
        return null;
    }

    public List<RendezVous> getRendezVousPatient(int patientId) {
        List<RendezVous> rdvList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r.*, m." + KEY_NOM + " as medecin_nom, m." + KEY_PRENOM + " as medecin_prenom, " +
                "m." + KEY_SPECIALITE + " as specialite " +
                "FROM " + TABLE_RENDEZVOUS + " r " +
                "INNER JOIN " + TABLE_MEDECINS + " m ON r." + KEY_MEDECIN_ID + " = m." + KEY_ID + " " +
                "WHERE r." + KEY_PATIENT_ID + " = ? " +
                "ORDER BY r." + KEY_DATE_RDV + ", r." + KEY_HEURE;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(patientId)});
        if (cursor.moveToFirst()) {
            do {
                RendezVous rdv = new RendezVous();
                rdv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                rdv.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_RDV)));
                rdv.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(KEY_HEURE)));
                rdv.setMotif(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MOTIF)));
                rdv.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUT)));
                rdv.setMedecinNom(cursor.getString(cursor.getColumnIndexOrThrow("medecin_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("medecin_prenom")));
                rdv.setSpecialite(cursor.getString(cursor.getColumnIndexOrThrow("specialite")));
                rdvList.add(rdv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rdvList;
    }

    public List<RendezVous> getRendezVousMedecin(int medecinId) {
        List<RendezVous> rdvList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r.*, p." + KEY_NOM + " as patient_nom, p." + KEY_PRENOM + " as patient_prenom " +
                "FROM " + TABLE_RENDEZVOUS + " r " +
                "INNER JOIN " + TABLE_PATIENTS + " p ON r." + KEY_PATIENT_ID + " = p." + KEY_ID + " " +
                "WHERE r." + KEY_MEDECIN_ID + " = ? " +
                "ORDER BY r." + KEY_DATE_RDV + ", r." + KEY_HEURE;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(medecinId)});
        if (cursor.moveToFirst()) {
            do {
                RendezVous rdv = new RendezVous();
                rdv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                rdv.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_RDV)));
                rdv.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(KEY_HEURE)));
                rdv.setMotif(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MOTIF)));
                rdv.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUT)));
                String patientNom = cursor.getString(cursor.getColumnIndexOrThrow("patient_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("patient_prenom"));
                rdv.setMedecinNom(patientNom);
                rdvList.add(rdv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rdvList;
    }

    public List<RendezVous> getUrgencesSecretaire() {
        List<RendezVous> rdvList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r.*, p." + KEY_NOM + " as patient_nom, p." + KEY_PRENOM + " as patient_prenom, " +
                "m." + KEY_NOM + " as medecin_nom, m." + KEY_PRENOM + " as medecin_prenom " +
                "FROM " + TABLE_RENDEZVOUS + " r " +
                "INNER JOIN " + TABLE_PATIENTS + " p ON r." + KEY_PATIENT_ID + " = p." + KEY_ID + " " +
                "INNER JOIN " + TABLE_MEDECINS + " m ON r." + KEY_MEDECIN_ID + " = m." + KEY_ID + " " +
                "WHERE r." + KEY_URGENT + " = 1 " +
                "ORDER BY r." + KEY_DATE_RDV + ", r." + KEY_HEURE;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                RendezVous rdv = new RendezVous();
                rdv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                rdv.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_RDV)));
                rdv.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(KEY_HEURE)));
                rdv.setMotif(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MOTIF)));
                rdv.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUT)));
                String patientNom = cursor.getString(cursor.getColumnIndexOrThrow("patient_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("patient_prenom"));
                String medecinNom = cursor.getString(cursor.getColumnIndexOrThrow("medecin_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("medecin_prenom"));
                rdv.setMedecinNom(medecinNom);
                rdv.setSpecialite(patientNom);
                rdvList.add(rdv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rdvList;
    }

    public List<RendezVous> getRdvAujourdHui() {
        List<RendezVous> rdvList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        String today = sdf.format(new Date());

        String query = "SELECT r.*, p." + KEY_NOM + " as patient_nom, p." + KEY_PRENOM + " as patient_prenom, " +
                "m." + KEY_NOM + " as medecin_nom, m." + KEY_PRENOM + " as medecin_prenom " +
                "FROM " + TABLE_RENDEZVOUS + " r " +
                "INNER JOIN " + TABLE_PATIENTS + " p ON r." + KEY_PATIENT_ID + " = p." + KEY_ID + " " +
                "INNER JOIN " + TABLE_MEDECINS + " m ON r." + KEY_MEDECIN_ID + " = m." + KEY_ID + " " +
                "WHERE r." + KEY_DATE_RDV + " = ? " +
                "ORDER BY r." + KEY_HEURE;

        Cursor cursor = db.rawQuery(query, new String[]{today});
        if (cursor.moveToFirst()) {
            do {
                RendezVous rdv = new RendezVous();
                rdv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                rdv.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_RDV)));
                rdv.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(KEY_HEURE)));
                rdv.setMotif(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MOTIF)));
                rdv.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUT)));
                String patientNom = cursor.getString(cursor.getColumnIndexOrThrow("patient_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("patient_prenom"));
                String medecinNom = cursor.getString(cursor.getColumnIndexOrThrow("medecin_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("medecin_prenom"));
                rdv.setMedecinNom(medecinNom);
                rdv.setSpecialite(patientNom);
                rdvList.add(rdv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rdvList;
    }

    public List<RendezVous> getRdvAConfirmer() {
        List<RendezVous> rdvList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r.*, p." + KEY_NOM + " as patient_nom, p." + KEY_PRENOM + " as patient_prenom, " +
                "m." + KEY_NOM + " as medecin_nom, m." + KEY_PRENOM + " as medecin_prenom " +
                "FROM " + TABLE_RENDEZVOUS + " r " +
                "INNER JOIN " + TABLE_PATIENTS + " p ON r." + KEY_PATIENT_ID + " = p." + KEY_ID + " " +
                "INNER JOIN " + TABLE_MEDECINS + " m ON r." + KEY_MEDECIN_ID + " = m." + KEY_ID + " " +
                "WHERE r." + KEY_STATUT + " = ? " +
                "ORDER BY r." + KEY_DATE_RDV + ", r." + KEY_HEURE;

        Cursor cursor = db.rawQuery(query, new String[]{"À confirmer"});
        if (cursor.moveToFirst()) {
            do {
                RendezVous rdv = new RendezVous();
                rdv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                rdv.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_RDV)));
                rdv.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(KEY_HEURE)));
                rdv.setMotif(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MOTIF)));
                rdv.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUT)));
                String patientNom = cursor.getString(cursor.getColumnIndexOrThrow("patient_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("patient_prenom"));
                String medecinNom = cursor.getString(cursor.getColumnIndexOrThrow("medecin_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("medecin_prenom"));
                rdv.setMedecinNom(medecinNom);
                rdv.setSpecialite(patientNom);
                rdvList.add(rdv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rdvList;
    }

    public List<RendezVous> getAllRendezVous() {
        List<RendezVous> rdvList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r.*, p." + KEY_NOM + " as patient_nom, p." + KEY_PRENOM + " as patient_prenom, " +
                "m." + KEY_NOM + " as medecin_nom, m." + KEY_PRENOM + " as medecin_prenom " +
                "FROM " + TABLE_RENDEZVOUS + " r " +
                "INNER JOIN " + TABLE_PATIENTS + " p ON r." + KEY_PATIENT_ID + " = p." + KEY_ID + " " +
                "INNER JOIN " + TABLE_MEDECINS + " m ON r." + KEY_MEDECIN_ID + " = m." + KEY_ID + " " +
                "ORDER BY r." + KEY_DATE_RDV + ", r." + KEY_HEURE;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                RendezVous rdv = new RendezVous();
                rdv.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                rdv.setDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE_RDV)));
                rdv.setHeure(cursor.getString(cursor.getColumnIndexOrThrow(KEY_HEURE)));
                rdv.setMotif(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MOTIF)));
                rdv.setStatut(cursor.getString(cursor.getColumnIndexOrThrow(KEY_STATUT)));
                String patientNom = cursor.getString(cursor.getColumnIndexOrThrow("patient_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("patient_prenom"));
                String medecinNom = cursor.getString(cursor.getColumnIndexOrThrow("medecin_nom")) + " " +
                        cursor.getString(cursor.getColumnIndexOrThrow("medecin_prenom"));
                rdv.setMedecinNom(medecinNom);
                rdv.setSpecialite(patientNom);
                rdvList.add(rdv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rdvList;
    }

    // ===================== MÉTHODES STATISTIQUES =====================

    public int getTotalPatients() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PATIENTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getTotalMedecins() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_MEDECINS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getTotalSecretaires() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_SECRETAIRES, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getTotalRendezVous() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RENDEZVOUS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getRendezVousMoisActuel() {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.FRANCE);
        String moisActuel = sdf.format(new Date());

        String query = "SELECT COUNT(*) FROM " + TABLE_RENDEZVOUS +
                " WHERE substr(" + KEY_DATE_RDV + ", 4) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{moisActuel});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
}