package com.gestion.projet;

import javafx.beans.property.SimpleStringProperty;

public class Patient {
    private final SimpleStringProperty nom;
    private final SimpleStringProperty prenom;
    private final SimpleStringProperty telephone;
    private final SimpleStringProperty sexe;
    private final SimpleStringProperty cin;

    public Patient(String nom, String prenom, String telephone) {
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.telephone = new SimpleStringProperty(telephone);
        this.sexe = new SimpleStringProperty(""); // Initialisez sexe avec une chaîne vide
        this.cin = new SimpleStringProperty(""); // Initialisez cin avec une chaîne vide
    }

    // Getters et setters pour nom, prenom, telephone, sexe et cin
    public String getNom() { return nom.get(); }
    public void setNom(String value) { nom.set(value); }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String value) { prenom.set(value); }

    public String getTelephone() { return telephone.get(); }
    public void setTelephone(String value) { telephone.set(value); }

    public String getSexe() { return sexe.get(); }
    public void setSexe(String value) { sexe.set(value); }

    public String getCin() { return cin.get(); }
    public void setCin(String value) { cin.set(value); }
}
