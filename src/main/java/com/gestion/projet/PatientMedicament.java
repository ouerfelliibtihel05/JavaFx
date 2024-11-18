package com.gestion.projet;

public class PatientMedicament {
    private int id;
    private String patientCin;
    private String medicamentRef;

    public PatientMedicament(int id, String patientCin, String medicamentRef) {
        this.id = id;
        this.patientCin = patientCin;
        this.medicamentRef = medicamentRef;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientCin() {
        return patientCin;
    }

    public void setPatientCin(String patientCin) {
        this.patientCin = patientCin;
    }

    public String getMedicamentRef() {
        return medicamentRef;
    }

    public void setMedicamentRef(String medicamentRef) {
        this.medicamentRef = medicamentRef;
    }
}

