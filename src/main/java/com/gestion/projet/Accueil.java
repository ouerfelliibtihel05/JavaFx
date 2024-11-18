package com.gestion.projet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class Accueil {

    @FXML
    private ImageView imagePatients;

    @FXML
    private ImageView imageMedecins;

    @FXML
    private ImageView imageMedicaments;

    @FXML
    private ImageView imageChambre;

    @FXML
    private ImageView logout;

    @FXML
    public void initialize() {
        imagePatients.setOnMouseClicked(event -> ouvrirPageGestionPatients());
        imageMedecins.setOnMouseClicked(event -> ouvrirPageGestionMedecins());
        imageMedicaments.setOnMouseClicked(event -> ouvrirPageGestionMedicaments());
        imageChambre.setOnMouseClicked(event -> ouvrirPageGestionChambre());
        logout.setOnMouseClicked(event -> ouvrirPageLogout());
    }

    private void ouvrirPageLogout() {
        try {
            Main.changeScene("login-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ouvrirPageGestionPatients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestionPatient.fxml"));
            Parent gestionPatientParent = loader.load();
            Scene gestionPatientScene = new Scene(gestionPatientParent);
            Stage stage = new Stage();
            stage.setScene(gestionPatientScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ouvrirPageGestionMedecins() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Doctor-view.fxml"));
            Parent gestionPatientParent = loader.load();
            Scene gestionPatientScene = new Scene(gestionPatientParent);
            Stage stage = new Stage();
            stage.setScene(gestionPatientScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ouvrirPageGestionMedicaments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("medicaments.fxml"));
            Parent gestionPatientParent = loader.load();
            Scene gestionPatientScene = new Scene(gestionPatientParent);
            Stage stage = new Stage();
            stage.setScene(gestionPatientScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ouvrirPageGestionChambre() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestionPatientMedicament.fxml"));
            Parent gestionPatientParent = loader.load();
            Scene gestionPatientScene = new Scene(gestionPatientParent);
            Stage stage = new Stage();
            stage.setScene(gestionPatientScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
