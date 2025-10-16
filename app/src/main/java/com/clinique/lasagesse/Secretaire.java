package com.clinique.lasagesse;

public class Secretaire {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String password;

    // Constructeur vide
    public Secretaire() {
    }

    // Constructeur avec paramètres
    public Secretaire(int id, String nom, String prenom, String email, String password) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return nom + " " + prenom + " (Secrétaire)";
    }
}