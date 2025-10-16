package com.clinique.lasagesse;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "clinique_db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_PATIENTS = "patients";
    private static final String TABLE_MEDECINS = "medecins";
    private static final String TABLE_SECRETAIRES = "secretaires";
    private static final String TABLE_RENDEZVOUS = "rendezvous";

    // Colonnes communes
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_TELEPHONE = "telephone";
    //Colonnes patients
private  static final String KEY_DATE_NAISSANCE ="date_naissance";
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

        // Insertion de données de test
        insererDonneesTest(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENDEZVOUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDECINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECRETAIRES);
        onCreate(db);
    }

    private void insererDonneesTest(SQLiteDatabase db) {
        // Insertion de patients de test
        ContentValues patient1 = new ContentValues();
        patient1.put(KEY_NOM, "Fall");
        patient1.put(KEY_PRENOM, "Fatou");
        patient1.put(KEY_EMAIL, "fatou.fall@email.com");
        patient1.put(KEY_PASSWORD, "patient123");
        patient1.put(KEY_TELEPHONE, "771234567");
        patient1.put(KEY_DATE_NAISSANCE, "15/03/1992");
        patient1.put(KEY_ADRESSE, "Dakar, Sénégal");
        db.insert(TABLE_PATIENTS, null, patient1);

        // Insertion de médecins de test
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

        // Insertion de secrétaire de test
        ContentValues secretaire = new ContentValues();
        secretaire.put(KEY_NOM, "Diallo");
        secretaire.put(KEY_PRENOM, "Marie");
        secretaire.put(KEY_EMAIL, "marie.diallo@clinique.com");
        secretaire.put(KEY_PASSWORD, "secretaire123");
        db.insert(TABLE_SECRETAIRES, null, secretaire);
        // Insertion de rendez-vous de test
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
    // Méthodes pour la gestion des patients
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
    // Vérifier login médecin
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
    // Vérifier login secrétaire
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
    // Obtenir rendez-vous d’un médecin
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
                rdv.setMedecinNom(patientNom); // Ici on met nom patient dans medecinNom pour affichage
                rdvList.add(rdv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rdvList;
    }
    // Obtenir urgences pour secrétaire (statut "urgent" = 1)
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
                rdv.setSpecialite(patientNom); // Utilisé pour afficher patient ici
                rdvList.add(rdv);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rdvList;
    }
    // Obtenir RDV d’aujourd’hui (exemple simple, filtre par date)
    public List<RendezVous> getRdvAujourdHui() {
        List<RendezVous> rdvList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = "14/01/2024"; // À remplacer par date dynamique
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
    // Obtenir RDV à confirmer (statut = "À confirmer")
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
    // Obtenir tous les rendez-vous
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
    // Ajouter un patient
    public boolean ajouterPatient(String nom, String prenom, String email, String password,
                                  String telephone, String dateNaissance, String adresse) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Vérifier si email existe déjà
        Cursor cursor = db.query(TABLE_PATIENTS, new String[]{KEY_ID},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Email déjà utilisé
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
    // Récupérer patient par ID
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
    // Mettre à jour patient
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
    // Ajouter un médecin
    public boolean ajouterMedecin(String nom, String prenom, String email, String password,
                                  String specialite, String cabinet) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MEDECINS, new String[]{KEY_ID},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Email déjà utilisé
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
    // Récupérer médecin par ID
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
    // Mettre à jour médecin
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
    // Ajouter une secrétaire
    public boolean ajouterSecretaire(String nom, String prenom, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SECRETAIRES, new String[]{KEY_ID},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Email déjà utilisé
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
    // Récupérer secrétaire par ID
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
    // Mettre à jour secrétaire
    public boolean mettreAJourSecretaire(int id, String nom, String prenom, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_EMAIL, email);
        int rows = db.update(TABLE_SECRETAIRES, values, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }
    // Récupérer patient par email
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

    // Récupérer médecin par email
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

    // Récupérer secrétaire par email
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
}
