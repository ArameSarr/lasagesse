package com.clinique.lasagesse;

public class Medecin {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String specialite;
    private String cabinet;

    // Constructeur vide
    public Medecin() {
    }

    // Constructeur avec paramètres
    public Medecin(int id, String nom, String prenom, String email, String password,
                   String specialite, String cabinet) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.specialite = specialite;
        this.cabinet = cabinet;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getSpecialite() { return specialite; }
    public String getCabinet() { return cabinet; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public void setCabinet(String cabinet) { this.cabinet = cabinet; }

    @Override
    public String toString() {
        return "Dr. " + nom + " " + prenom + " (" + specialite + ")";
    }
}