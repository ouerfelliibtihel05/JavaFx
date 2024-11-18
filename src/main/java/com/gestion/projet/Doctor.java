package com.gestion.projet;

public class Doctor {
    private String brandName;
    private String productName;
    private String phoneNumber; // Remplace l'attribut image

    public Doctor(String brandName, String productName, String phoneNumber) {
        this.brandName = brandName;
        this.productName = productName;
        this.phoneNumber = phoneNumber;
    }

    // Getters et setters
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
