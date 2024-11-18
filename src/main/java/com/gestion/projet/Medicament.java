package com.gestion.projet;

import javafx.beans.property.SimpleStringProperty;

public class Medicament {
    private final SimpleStringProperty ref;
    private final SimpleStringProperty libelle;
    private final SimpleStringProperty prix;

    public Medicament(String ref, String libelle, String prix) {
        this.ref = new SimpleStringProperty(ref);
        this.libelle = new SimpleStringProperty(libelle);
        this.prix = new SimpleStringProperty(prix);
    }

    // Getters et setters pour ref, libelle et prix
    public String getRef() { return ref.get(); }
    public void setRef(String value) { ref.set(value); }

    public String getLibelle() { return libelle.get(); }
    public void setLibelle(String value) { libelle.set(value); }

    public String getPrix() { return prix.get(); }
    public void setPrix(String value) { prix.set(value); }
}