package com.clinique.lasagesse;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String telephone;
    private String dateNaissance;
    private String adresse;

    // Constructeur vide
    public Patient() {
    }

    // Constructeur avec paramètres
    public Patient(int id, String nom, String prenom, String email, String password,
                   String telephone, String dateNaissance, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getTelephone() { return telephone; }
    public String getDateNaissance() { return dateNaissance; }
    public String getAdresse() { return adresse; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}